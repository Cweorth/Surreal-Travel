package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.util.List;

/**
 * The Trip Data Access Object interface.
 * @author Roman Lacko [396157]
 */
public interface TripDAO {
    
    /**
     * Persists a new trip.
     * @param  trip          The trip to persist.
     */
    public void addTrip(Trip trip);
    
    /**
     * Returns the trip with the specified {@code id}.
     * @param  id            The ID number of the trip to fetch.
     * @return               The trip.
     */
    public Trip getTripById(long id);
    
    /**
     * Returns all trips that have the specified {@code destination}.
     * @param  destination   The destination to look for in all trips.
     * @return               A list of trips.
     */
    public List<Trip> getTripsByDestination(String destination);
    
    /**
     * Returns all trips that contain a given {@code excursion}.
     * @param  excursion     The excursion to look for.
     * @return               A list of trips.
     */
    public List<Trip> getTripsWithExcursion(Excursion excursion);
    
    /**
     * Returns all trips.
     * @return               All trips.
     */
    public List<Trip> getAllTrips();
    
    /**
     * Updates the trip.
     * @param  trip          The trip to update.
     */
    public void updateTrip(Trip trip);
    
    /**
     * Removes the specified {@code trip}.
     * @param  trip          The trip to remove.
     */
    public void deleteTrip(Trip trip);

    /**
     * Removes the trip with the specified {@code id}.
     * @param  id            The ID of the trip to remove.
     */
    public void deleteTripById(long id);
}
