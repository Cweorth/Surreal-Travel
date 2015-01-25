package cz.muni.pa165.surrealtravel.utils;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A wrapper class for a trip and all possible excursions used
 * by NewTrip and EditTrip forms
 * @author Roman Lacko [396157]
 */
public class TripModelData extends TripDTO {

    private List<ExcursionDTO> allExcursions;
    private List<Long>         excursionIDs;

    /**
     * Default constructor
     */
    public TripModelData() {
        allExcursions = new ArrayList<>();
        excursionIDs  = new ArrayList<>();
    }

    /**
     * Creates a new TripModelData instance
     * @param trip           the trip to show
     * @param allExcursions  all excursions
     */
    public TripModelData(TripDTO trip, List<ExcursionDTO> allExcursions) {
        Objects.requireNonNull(trip, "trip");
        Objects.requireNonNull(allExcursions, "allExcursions");

        setId(trip.getId());
        setDestination(trip.getDestination());
        setDateFrom(trip.getDateFrom());
        setDateTo(trip.getDateTo());
        setCapacity(trip.getCapacity());
        setBasePrice(trip.getBasePrice());
        setExcursions(new ArrayList<>(trip.getExcursions()));

        this.allExcursions = new ArrayList<>(allExcursions);
        this.excursionIDs  = new ArrayList<>();
    }

    /**
     * Unwraps a TripDTO from this "unclean" instance.
     * @return  Returns a new instance of TripDTO with the same values.
     */
    public TripDTO unwrap() {
        TripDTO trip = new TripDTO();
        trip.setId(getId());
        trip.setDestination(getDestination());
        trip.setDateFrom(getDateFrom());
        trip.setDateTo(getDateTo());
        trip.setCapacity(getCapacity());
        trip.setBasePrice(getBasePrice());
        trip.setExcursions(getExcursions());

        return trip;
    }

    public List<ExcursionDTO> getAllExcursions() {
        return allExcursions;
    }

    public void setAllExcursions(List<ExcursionDTO> allExcursions) {
        this.allExcursions = allExcursions;
    }

    public List<Long> getExcursionIDs() {
        return excursionIDs;
    }

    public void setExcursionIDs(List<Long> excursionIDs) {
        this.excursionIDs = excursionIDs;
    }

}
