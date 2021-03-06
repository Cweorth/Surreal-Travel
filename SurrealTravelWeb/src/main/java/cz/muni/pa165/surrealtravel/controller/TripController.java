package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.service.TripService;
import cz.muni.pa165.surrealtravel.utils.AuthCommons;
import cz.muni.pa165.surrealtravel.utils.TripModelData;
import cz.muni.pa165.surrealtravel.validator.TripValidator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The Trip controller class
 * @author Roman Lacko [396157]
 */
@Controller
@RequestMapping("/trips")
public class TripController {

    final static Logger logger = LoggerFactory.getLogger(TripController.class);

    @Autowired
    private TripService tripService;

    @Autowired
    private ExcursionService excursionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MessageSource messageSource;

    @InitBinder(value = {"tripdata"})
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new TripValidator());

        CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * Default page that lists all customers
     * @param model
     * @param eid
     * @return redirect
     */
    @RequestMapping(method = RequestMethod.GET)
    public String listTrips(ModelMap model, @RequestParam(value = "eid", required = false) String eid) {
        List<TripDTO> allTrips = new ArrayList<>();
        List<TripDTO> reserved = new ArrayList<>();

        // if filtered by excursions
        if (eid != null) try {
            ExcursionDTO excursion = excursionService.getExcursionById(Long.valueOf(eid));
            if (excursion != null) {
                allTrips.addAll(tripService.getTripsWithExcursion(excursion));
                model.addAttribute("excursion", excursion);
            }
        } catch (NumberFormatException ex) {
            // leave it be
        } else {
            allTrips.addAll(tripService.getAllTrips());
        }

        model.addAttribute("trips", allTrips);

        // STAFF can delete accounts, so let's list reserved trips also
        if (AuthCommons.hasRole(UserRole.ROLE_STAFF)) {
            Set<TripDTO>         reservedSet  = new HashSet<>();
            List<ReservationDTO> reservations = reservationService.getAllReservations();

            for (ReservationDTO reservation : reservations) {
                reservedSet.add(reservation.getTrip());
            }

            reserved.addAll(reservedSet);
        }

        model.addAttribute("reserved", reserved);
        return "trip/list";
    }

    /**
     * Redirects to a NewTrip form
     * @param model
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newTripForm(ModelMap model) {
        model.addAttribute("tripdata", new TripModelData(new TripDTO(), excursionService.getAllExcursions()));
        return "trip/new";
    }

    /**
     * Processes the POST request for creating a new trip
     * @param data
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newTrip(@Validated @ModelAttribute("tripdata") TripModelData data,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriBuilder,
            Locale locale) {

        List<ExcursionDTO> excursions = excursionService.getAllExcursions();
        data.setAllExcursions(excursions);

        if (bindingResult.hasErrors() || !checkExcursions(data, excursions, bindingResult, locale)) {
            logFormErrors(bindingResult);
            return "trip/new";
        }

        String resultStatus = "success";

        try {
            tripService.addTrip(data.unwrap());
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "trip.message.add" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{data.getDestination()}, locale));
        return "redirect:" + uriBuilder.path("/trips").queryParam("notification", resultStatus).build();
    }

    /**
     * Redirects to a EditTrip form
     * @param id
     * @param model
     * @param redirectAttributes
     * @param locale
     * @param uriBuilder
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editTripForm(@PathVariable long id, ModelMap model, RedirectAttributes redirectAttributes, Locale locale, UriComponentsBuilder uriBuilder) {
        TripDTO trip = null;

        try {
            trip = tripService.getTripById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        if(trip == null) {
            return "trip/list";
        }

        TripModelData tripdata = new TripModelData(trip, excursionService.getAllExcursions());
        for(ExcursionDTO excursion : trip.getExcursions()) {
            tripdata.getExcursionIDs().add(excursion.getId());
        }

        model.addAttribute("tripdata", tripdata);
        return "trip/edit";
    }

    /**
     * Processes the POST request for updating a trip
     * @param data
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editTrip(@Validated @ModelAttribute("tripdata") TripModelData data, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {

        List<ExcursionDTO> excursions = excursionService.getAllExcursions();
        data.setAllExcursions(excursions);

        if (bindingResult.hasErrors() || !checkExcursions(data, excursions, bindingResult, locale)) {
            logFormErrors(bindingResult);
            return "trip/edit";
        }

        String resultStatus = "success";

        try {
            tripService.updateTrip(data.unwrap());
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "trip.message.edit" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{data.getDestination()}, locale));
        return "redirect:" + uriBuilder.path("/trips").queryParam("notification", resultStatus).build();
    }

    /**
     * Removes the requested trip
     * @param id
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteTrip(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String destination  = null;
        String resultStatus = "success";

        try {
            TripDTO trip = tripService.getTripById(id);
            destination  = trip.getDestination();
            tripService.deleteTripById(id);
        } catch(Exception e) {
            logger.error(e.getClass().getName() + " :: " + e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "trip.message.delete" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{ destination }, locale));
        return "redirect:" + uriBuilder.path("/trips").queryParam("notification", resultStatus).build();
    }

    //--[  Excursion date constraint check  ]-----------------------------------

    private boolean checkExcursions(TripModelData data, List<ExcursionDTO> excursions,
            BindingResult bindingResult, Locale locale) {
        if ((data.getExcursionIDs() != null) && !data.getExcursionIDs().isEmpty()) {
            Map<Long, ExcursionDTO> exmap = new HashMap<>(excursions.size());
            for(ExcursionDTO ex : excursions) {
                exmap.put(ex.getId(), ex);
            }

            for(long id : data.getExcursionIDs()) {
                ExcursionDTO ex = exmap.get(id);
                exmap.remove(id);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ex.getExcursionDate());
                calendar.add(Calendar.DATE, ex.getDuration());

                Date start = ex.getExcursionDate();
                Date end   = calendar.getTime();

                if (start.before(data.getDateFrom()) || end.after(data.getDateTo())) {
                    String message = messageSource.getMessage("trip.validator.invalidExcursion", new Object[]{ ex.getDestination() }, locale);
                    bindingResult.addError(new FieldError("data", "excursions", message));
                }

                data.addExcursion(ex);
            }
        }

        return !bindingResult.hasErrors();
    }

    private void logFormErrors(BindingResult bindingResult) {
        logger.debug("Encountered following errors when validating form.");
        for(ObjectError ge : bindingResult.getGlobalErrors()) {
            logger.debug("ObjectError: {}", ge);
        }
        for(FieldError fe : bindingResult.getFieldErrors()) {
            logger.debug("FieldError: {}", fe);
        }
    }
}
