package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.service.TripService;
import cz.muni.pa165.surrealtravel.utils.AuthCommons;
import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Reservation controller.
 * @author Tomáš Kácel [359965]
 */
@Controller
@RequestMapping("/reservations")
public class ReservationController {
    final static Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TripService tripService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ExcursionService excursionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource messageSource;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(CustomerDTO.class, "customer", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                CustomerDTO type = customerService.getCustomerById(Long.valueOf(text));
                setValue(type);
            }
        });

        binder.registerCustomEditor(TripDTO.class, "trip", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                TripDTO type = tripService.getTripById(Long.valueOf(text));
                setValue(type);
            }
        });

        binder.registerCustomEditor(List.class, "excursions", new CustomCollectionEditor(List.class) {
            @Override
            protected Object convertElement(Object element) {
                long id = -1;
                if(element instanceof String) {
                    try {
                        id = Long.parseLong((String) element);
                    } catch(NumberFormatException e) {
                        logger.debug(e.toString());
                    }
                    return excursionService.getExcursionById(id);
                }
                return null;
            }
        });
    }

    /**
     * List all excursions. Prepared to be called via ajax.
     * @param id
     * @param model
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/ajaxGetExcursions/{id}", method = RequestMethod.GET)
    public String listExcursionsAjax(@PathVariable long id, ModelMap model) {
        TripDTO trip = tripService.getTripById(id);
        model.addAttribute("excursions", trip.getExcursions());
        model.addAttribute("ajaxReload", true);
        return "reservation/excursionsAjax";
    }

    /**
     * Get all reservations. User must be logged in, different funtionality for (USER vs STAFF,ADMIN).
     * @param model
     * @param uriBuilder
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.GET)
    public String listReservations(ModelMap model, UriComponentsBuilder uriBuilder) {
        // If authenticated user is staff or higher, show all reservations. If
        // not, show only reservations for the authenticated user.
        List<ReservationDTO> reservations;
        if(AuthCommons.hasRole(UserRole.ROLE_STAFF)) {
            reservations = reservationService.getAllReservations();
        } else {
            AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
            if(account.getCustomer() != null)
                reservations = reservationService.getAllReservationsByCustomer(account.getCustomer());
            else
                return AuthCommons.forceDenied(uriBuilder);
        }

        model.addAttribute("reservations", reservations);
        return "reservation/list";
    }

    /**
     * Display a form for creating a new reservation.
     * @param model
     * @param uriBuilder
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newReservationForm(ModelMap model, UriComponentsBuilder uriBuilder) {
        List<TripDTO> allTrips = tripService.getAllTrips();

        // ROLE_USERs can create reservation only for themselves, others can
        // create reservations for any Customers.
        ReservationDTO reservation = new ReservationDTO();
        if(AuthCommons.hasRole(UserRole.ROLE_STAFF)) {
            model.addAttribute("customers", customerService.getAllCustomers());
        } else {
            AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
            if(account.getCustomer() != null)
                reservation.setCustomer(account.getCustomer());
            else
                return AuthCommons.forceDenied(uriBuilder);
        }
        model.addAttribute("reservationDTO", reservation);

        model.addAttribute("trips", allTrips);
        if(!allTrips.isEmpty()) model.addAttribute("excursions", allTrips.get(0).getExcursions());
        return "reservation/new";
    }

    /**
     * Process POST request for creating of a new reservation.
     * @param reservationDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newReservation(@ModelAttribute ReservationDTO reservationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        // check the form validator output
        if(bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "reservation/new";
        }

        String resultStatus = "success";

        // so far so good, try to save reservation
        try {
            reservationService.addReservation(reservationDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "reservation.message.new" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{reservationDTO.getCustomer().getName()}, locale));
        return "redirect:" + uriBuilder.path("/reservations").queryParam("notification", resultStatus).build();
    }

    /**
     * Display a form for editing of a reservation.
     * @param id
     * @param model
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editReservationForm(@PathVariable long id, ModelMap model) {
        ReservationDTO reservation = null;

        try {
            reservation = reservationService.getReservationById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        if(reservation == null) return "reservation/list";

        model.addAttribute("reservationDTO", reservation);
        model.addAttribute("customers", customerService.getAllCustomers());
        model.addAttribute("trips", tripService.getAllTrips());
        model.addAttribute("excursions", reservation.getTrip().getExcursions());
        return "reservation/edit";
    }

    /**
     * Process POST request for editing of a reservation.
     * @param reservationDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editReservation(@ModelAttribute ReservationDTO reservationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {

        // check the form validator output
        if(bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "reservation/edit";
        }

        String resultStatus = "success";
        // so far so good, try to save customer
        try {
            reservationService.updateReservation(reservationDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "reservation.message.edit" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{reservationDTO.getCustomer().getName()}, locale));
        return "redirect:" + uriBuilder.path("/reservations").queryParam("notification", resultStatus).build();
    }

    /**
     * Delete reservation with the given id.
     * @param id
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteReservation(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String name         = null;
        String resultStatus = "success";

        try {
            ReservationDTO reservation = reservationService.getReservationById(id);

            // If ROLE_USER is trying to delete reservation of someone other than himself, deny it.
            if(!AuthCommons.hasRole(UserRole.ROLE_STAFF)) {
                AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
                if(account.getCustomer() == null || account.getCustomer().getId() != reservation.getCustomer().getId())
                    return AuthCommons.forceDenied(uriBuilder, redirectAttributes, messageSource.getMessage("reservation.message.delete.403", null, locale));
            }

            name = reservation.getCustomer().getName();
            reservationService.deleteReservationById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "reservation.message.delete" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{ name }, locale));
        return "redirect:" + uriBuilder.path("/reservations").queryParam("notification", resultStatus).build();
    }

    /**
     * Check BindingResult of a request for errors found by a validator.
     * @param bindingResult
     * @param viewName
     * @return redirect
     */
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
