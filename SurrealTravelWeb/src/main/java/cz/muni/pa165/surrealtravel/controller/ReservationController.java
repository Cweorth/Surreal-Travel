/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.validator.ReservationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Tomáš Kácel [359965]
 */
@Controller
@RequestMapping("/reservation")
public class ReservationController {
     final static Logger logger = LoggerFactory.getLogger(ReservationController.class);
     
     @Autowired
     private ReservationService reservationService;
     
     @Autowired
     private MessageSource messageSource;
     
      @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ReservationValidator());
    }
    
     @RequestMapping(method = RequestMethod.GET)
    public String listCustomers(ModelMap model) {
        model.addAttribute("reservation", reservationService.getAllReservations());
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
        return "reservation/new";
    }
    
    
}
