package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Trip;
import java.util.List;

/**
 * 
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
     * Returns all the trips that have the specified {@code destination}
     * @param  destination   The destination to look for in all trips.
     * @return               A list of trips.
     */
    public List<Trip> getTripsByDestination(String destination);
        
    /**
     * @return  Returns all trips.
     */
    public List<Trip> getAllTrips();
    
    public void updateTrip(Trip trip);
    
    /**
     * Removes the specified {@code trip}.
     * @param  trip          The trip to remove.
     */
    public void deleteTrip(Trip trip);
    
}
