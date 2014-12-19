package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.service.TripService;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.context.MessageSource;
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
    @RequestMapping(value = "/ajaxGetExcursions/{id}", method = RequestMethod.GET)
    public String listExcursionsAjax(@PathVariable long id, ModelMap model) {
        TripDTO trip = tripService.getTripById(id);
        model.addAttribute("excursions", trip.getExcursions());
        model.addAttribute("ajaxReload", true);
        return "reservation/excursionsAjax";
    }
    
    /**
     * Get all reservations.
     * @param model
     * @return redirect
     */
    @RequestMapping(method = RequestMethod.GET)
    public String listReservations(ModelMap model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservation/list";
    }
    
    /**
     * Display a form for creating a new reservation.
     * @param model
     * @return redirect
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newReservationForm(ModelMap model) {
        List<TripDTO> allTrips = tripService.getAllTrips();
        model.addAttribute("reservationDTO", new ReservationDTO());
        model.addAttribute("customers", customerService.getAllCustomers());
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
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteReservation(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String name         = null;
        String resultStatus = "success";
        
        try {
            ReservationDTO reservation = reservationService.getReservationById(id);
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

    @PostConstruct
    public void init() {
        
        CustomerDTO c1 = new CustomerDTO();
        c1.setName("Honza");
        c1.setAddress("Olomouc");
        customerService.addCustomer(c1);
        TripDTO trip= new TripDTO();
        
        Calendar calendar = new GregorianCalendar();
        calendar.set(2014, 6, 21);
        Date from= calendar.getTime();
        calendar.set(2014,7,3);
        Date to= calendar.getTime();
        
        trip.setDateFrom(from);
        trip.setDateTo(to);
        trip.setDestination("palava");
        trip.setCapacity(15);
        trip.setBasePrice(new BigDecimal(500));
        
        ExcursionDTO e1 = new ExcursionDTO();
        e1.setDestination("Afgánistán");
        e1.setDescription("Best of war");
        e1.setDuration(2);
        e1.setExcursionDate(from);
        e1.setPrice(new BigDecimal(0));
        
        excursionService.addExcursion(e1);
        List<ExcursionDTO> excursions= new ArrayList<>();
        excursions.add(e1);
        
        trip.setExcursions(excursions);
        tripService.addTrip(trip);
        
        ReservationDTO res1 = new ReservationDTO();
        res1.setTrip(trip);
        res1.addExcursion(e1);
        res1.setCustomer(c1);
        
        reservationService.addReservation(res1);
                
    }
}
