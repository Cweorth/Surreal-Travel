/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.entity.Trip;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.service.TripService;
import cz.muni.pa165.surrealtravel.validator.ReservationValidator;
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
import org.springframework.context.MessageSource;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
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
     private MessageSource messageSource;
     
      @Autowired
    private ExcursionService excursionService;
     
      @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ReservationValidator());
    }
    
     @RequestMapping(method = RequestMethod.GET)
    public String listCustomers(ModelMap model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservation/list";
    }
    
    /**
     * Display a form for creating a new reservation.
     * @param model
     * @return 
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newCustomerForm(ModelMap model) {
        model.addAttribute("reservationDTO", new ReservationDTO());
        model.addAttribute("customers", customerService.getAllCustomers());
        return "reservation/new";
    }
    
    /**
     * Process POST request for creating of a new reservation.
     * @param reservationDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return 
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newReservation(@Validated @ModelAttribute ReservationDTO reservationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {       
        
        // check the form validator output
        if(bindingResult.hasErrors()) {
            String error = checkFormErrors(bindingResult, "reservation/new");
            if(error != null) return error;
        }

        // so far so good, try to save reservation
        try {
            reservationService.addReservation(reservationDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "reservation/new";
        }
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("reservation.message.new", new Object[]{reservationDTO.toString()}, locale));
        
        // get back to customer list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/reservations").queryParam("notification", "success").build();
    }
    
    /**
     * Check BindingResult of a request for errors found by a validator.
     * @param bindingResult
     * @param viewName
     * @return 
     */
    private String checkFormErrors(BindingResult bindingResult, String viewName) {
        logger.debug("Encountered following errors when validating form.");
        for(ObjectError ge : bindingResult.getGlobalErrors()) {
            logger.debug("ObjectError: {}", ge);
        }
        for(FieldError fe : bindingResult.getFieldErrors()) {
            logger.debug("FieldError: {}", fe);
        }
        return viewName;
    }
    
    /**
     * Display a form for editing of a reservation.
     * @param id
     * @param model
     * @return 
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
        return "reservation/edit";
    }
    
    /**
     * Process POST request for editing of a reservation.
     * @param reservationDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return 
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editReservation(@Validated @ModelAttribute ReservationDTO reservationDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {       
        
        // check the form validator output
        if(bindingResult.hasErrors()) {
            String error = checkFormErrors(bindingResult, "reservation/edit");
            if(error != null) return error;
            
        }

        // so far so good, try to save customer
        try {
            reservationService.updateReservation(reservationDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "reservation/edit";
        }
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("reservation.message.edit", new Object[]{reservationDTO.toString(), reservationDTO.getId()}, locale));
        
        // get back to customer list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/reservations").queryParam("notification", "success").build();
    }
    
    /**
     * Delete reservation with the given id.
     * @param id
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return 
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteReservation(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        TripDTO name = null;
        
        try {
            ReservationDTO reservation = reservationService.getReservationById(id);
            name = reservation.getTrip();
            reservationService.deleteReservation(reservation);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        if(name == null) return "reservation/list";
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("customer.message.delete", new Object[]{name}, locale));
        
        // get back to customer list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/reservations").queryParam("notification", "success").build();
    }
    
    @PostConstruct
    public void init() {
        
        CustomerDTO c1 = new CustomerDTO();
        c1.setName("Honza");
        c1.setAddress("Olomouc");
        customerService.addCustomer(c1);
        //CustomerDTO c2 = new CustomerDTO();
        //c2.setName("Eva");
        //c2.setAddress("Olomouc");
        //customerService.addCustomer(c1);
        //customerService.addCustomer(c2);
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
        
        //ExcursionDTO e2 = new ExcursionDTO();
        //e2.setDestination("Afgánistán");
        //e2.setDescription("Best of army stock");
        //e2.setDuration(5);
        //e2.setExcursionDate(from);
        //e2.setPrice(new BigDecimal(10));
        excursionService.addExcursion(e1);
        //excursionService.addExcursion(e2);
        List<ExcursionDTO> excursions= new ArrayList<>();
        excursions.add(e1);
        
        //excursions.add(e2);
        
        trip.setExcursions(excursions);
        tripService.addTrip(trip);
        
        //customerService.addCustomer(c2);
        ReservationDTO res1 = new ReservationDTO();
        res1.setTrip(trip);
        res1.addExcursion(e1);
        res1.setCustomer(c1);
       
        
        //ReservationDTO res2= new ReservationDTO();
        //res2.setExcursions(excursions);
        //res2.setTrip(trip);
        //res2.setCustomer(c1);
        
        reservationService.addReservation(res1);
        //reservationService.addReservation(res2);
       
                
    }
}
