/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.service.TripService;
import java.util.List;
import javassist.tools.rmi.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Tomáš Kácel [359965]
 */

@RestController
@RequestMapping("/rest/trips")
public class TripRestController {
    final static Logger logger = LoggerFactory.getLogger(cz.muni.pa165.surrealtravel.rest.TripRestController.class);
    
    @Autowired
    private TripService tripService;
    
    
    /**
     * Default page - list all trips.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<TripDTO> listTrips() {
        logger.info("showing trips");
        return tripService.getAllTrips();
     }
    
    
     /**
     * Get trip by id.
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public @ResponseBody TripDTO getTrip(@PathVariable long id) {
        logger.info("trip by id");
        return tripService.getTripById(id);
    }
    
    
    /**
     * Create new trip
     * @param trip
     * @return 
     */
    @RequestMapping(value="/new", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody TripDTO newTrip(TripDTO trip){
        logger.info("making new trip");
        tripService.addTrip(trip);
        return trip;
    
}
    
/**
     * update trip.
     * @param trip
     * @param id
     * @return 
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT,  consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody TripDTO updateTrip(@PathVariable long id, @RequestBody TripDTO trip) {
        logger.info("Updating trip");
        tripService.updateTrip(trip);
        return trip;
    }
    
    /**
     * Delete excursion by id.
     * @param id
     * @return 
     * @throws javassist.tools.rmi.ObjectNotFoundException 
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody TripDTO deleteExcursion(@PathVariable long id) throws ObjectNotFoundException {
        logger.info("Deleting trip");
        TripDTO trip=new TripDTO();
        try {
        trip = tripService.getTripById(id);
        if(trip == null) throw new ObjectNotFoundException("no trip.");
        tripService.deleteTripById(id);
        }catch(Exception e){
            logger.error("the trip have got reservation canot delete");
            throw new IllegalArgumentException("this trip is part of reservation can be deleted");
        }
        return trip;
    }
    
    
}