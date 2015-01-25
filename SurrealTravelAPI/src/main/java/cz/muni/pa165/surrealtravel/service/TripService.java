package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.util.List;

/**
 * The Trip Service interface
 * @author Roman Lacko [396157]
 */
public interface TripService {

    //--[  Methods  ]-----------------------------------------------------------
    /**
     * Saves the {@code trip}.
     * @param trip           The trip to save.
     */
    void addTrip(TripDTO trip);

    /**
     * Removes a trip by its {@code id}.
     * @param id             The id of a trip to remove.
     */
    void deleteTripById(long id);

    /**
     * Yields a list of all trips.
     * @return               A list of all trips.
     */
    List<TripDTO> getAllTrips();

    /**
     * Finds a trip with the specified {@code id}.
     * @param id             The ID to look for.
     * @return               A trip.
     */
    TripDTO getTripById(long id);

    /**
     * Finds all trips that contain the specified {@code excursion}.
     * @param excursion      The excursion to look for.
     * @return               A list of matching trips.
     */
    List<TripDTO> getTripsWithExcursion(ExcursionDTO excursion);

    /**
     * Updates the {@code trip}.
     * @param trip           The trip to update.
     */
    void updateTrip(TripDTO trip);

}
