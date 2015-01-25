package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.rest.exceptions.EntityNotFoundException;
import cz.muni.pa165.surrealtravel.rest.exceptions.EntityNotDeletedException;
import cz.muni.pa165.surrealtravel.rest.exceptions.InvalidEntityException;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.rest.exceptions.PermissionDeniedException;
import cz.muni.pa165.surrealtravel.rest.exceptions.RestAPIException;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.TripService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
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
    
    @Autowired
    private RestAuthCommons restAuth;

    /**
     * Default page - list all trips.
     *
     * @return the list of trips
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
     * @return the trip
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
     * @param request
     * @return the new trip
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    TripDTO newTrip(@RequestBody TripDTO trip, HttpServletRequest request) {
        logger.info("Creating a new trip");

        for (ExcursionDTO e : trip.getExcursions()) {
            if (!excursionService.getExcursionById(e.getId()).equals(e)) {
                throw new EntityNotFoundException("Excursion", e.getId());
            }
        }

        try {
            restAuth.login(request);
            tripService.addTrip(trip);
        } catch (AccessDeniedException ex) {
            // map to 403
            throw new PermissionDeniedException(ex);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new InvalidEntityException("The trip is not valid", ex);
        } finally {
            restAuth.logout();
        }

        return trip;
    }

    /**
     * Update trip.
     *
     * @param trip
     * @param id
     * @param request
     * @return the updated trip
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody
    TripDTO updateTrip(@PathVariable long id, @RequestBody TripDTO trip, HttpServletRequest request) {
        logger.info("Updating a trip with id " + id);
        for (ExcursionDTO e : trip.getExcursions()) {
            if (!excursionService.getExcursionById(e.getId()).equals(e)) {
                throw new EntityNotFoundException("Excursion", e.getId());
            }
        }

        try {
            restAuth.login(request);
            tripService.updateTrip(trip);
        } catch (AccessDeniedException ex) {
            throw new PermissionDeniedException(ex);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidEntityException("The trip is not valid", ex);
        } finally {
            restAuth.logout();
        }

        return trip;
    }

    /**
     * Delete trip by id.
     *
     * @param id
     * @param request
     * @return the deleted trip
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody TripDTO deleteTrip(@PathVariable long id, HttpServletRequest request) {
        logger.info("Deleting a trip with id " + id);
        TripDTO trip = tripService.getTripById(id);

        if (trip == null) {
            throw new EntityNotFoundException("Trip", id);
        }

        try {
            restAuth.login(request);
            tripService.deleteTripById(id);
        } catch (RestAPIException ex) {
            throw ex;
        } catch (AccessDeniedException ex) {
            throw new PermissionDeniedException(ex);
        } catch (Exception ex) {
            logger.error("The trip with id " + id + " cannot be deleted");
            throw new EntityNotDeletedException("Trip", id, ex);
        } finally {
            restAuth.logout();
        }

        return trip;
    }

}
