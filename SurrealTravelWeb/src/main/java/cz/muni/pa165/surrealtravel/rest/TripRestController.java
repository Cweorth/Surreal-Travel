package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.rest.exceptions.EntityNotFoundException;
import cz.muni.pa165.surrealtravel.rest.exceptions.EntityNotDeletedException;
import cz.muni.pa165.surrealtravel.rest.exceptions.InvalidEntityException;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.TripService;
import java.util.List;
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

    private final static Logger logger = LoggerFactory.getLogger(cz.muni.pa165.surrealtravel.rest.TripRestController.class);

    @Autowired
    private TripService tripService;

    @Autowired
    private ExcursionService excursionService;

    /**
     * Default page - list all trips.
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<TripDTO> listTrips() {
        logger.info("Retrieving a list of trips");
        return tripService.getAllTrips();
    }

    /**
     * Get trip by id.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public @ResponseBody
    TripDTO getTrip(@PathVariable long id) {
        logger.info("Retrieving a trip with id " + id);
        TripDTO trip = tripService.getTripById(id);

        if (trip == null) {
            throw new EntityNotFoundException("Trip", id);
        }

        return trip;
    }

    /**
     * Create new trip
     *
     * @param trip
     * @return
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    TripDTO newTrip(@RequestBody TripDTO trip) {
        logger.info("Creating a new trip");

        for (ExcursionDTO e : trip.getExcursions()) {
            if (!excursionService.getExcursionById(e.getId()).equals(e)) {
                throw new EntityNotFoundException("Excursion", e.getId());
            }
        }

        try {
            tripService.addTrip(trip);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new InvalidEntityException("The trip is not valid", ex);
        }

        return trip;
    }

    /**
     * update trip.
     *
     * @param trip
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    TripDTO updateTrip(@PathVariable long id, @RequestBody TripDTO trip) {
        logger.info("Updating a trip with id " + id);
        for (ExcursionDTO e : trip.getExcursions()) {
            if (!excursionService.getExcursionById(e.getId()).equals(e)) {
                throw new EntityNotFoundException("Excursion", e.getId());
            }
        }

        try {
            tripService.updateTrip(trip);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidEntityException("The trip is not valid", ex);
        }

        return trip;
    }

    /**
     * Delete excursion by id.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody TripDTO deleteExcursion(@PathVariable long id) {
        logger.info("Deleting a trip with id " + id);
        TripDTO trip = tripService.getTripById(id);

        if (trip == null) {
            throw new EntityNotFoundException("Trip", id);
        }

        try {
            tripService.deleteTripById(id);
        } catch (Exception ex) {
            logger.error("The trip with id " + id + " cannot be deleted");
            throw new EntityNotDeletedException("Trip", id, ex);
        }

        return trip;
    }

}
