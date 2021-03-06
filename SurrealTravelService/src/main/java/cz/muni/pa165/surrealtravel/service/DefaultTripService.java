package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.TripDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Trip service implementation
 * @author Roman Lacko [396157]
 */
@Service(value = "tripService")
public class DefaultTripService implements TripService {

    //<editor-fold desc="Extensions" defaultstate="collapsed">

    private interface Function<T,U> {
        U apply(T x);
    }

    private <T,U> List<U> map(Function<T,U> f, List<T> s) {
        List<U> r = new ArrayList<>(s.size());
        for(T x : s) r.add(f.apply(x));
        return r;
    }

    //</editor-fold>

    //--[  Private  ]-----------------------------------------------------------

    @Autowired
    private DozerBeanMapper mapper;
    @Autowired
    private TripDAO tripDao;

    private final Function<Trip,        TripDTO>   toDTO;
    private final Function<TripDTO,     Trip>      toEntity;
    private final Function<ExcursionDTO,Excursion> excToEntity;

    //--[  Constructors  ]------------------------------------------------------

    public DefaultTripService() {
        toDTO = new Function<Trip,TripDTO>() {
            @Override public TripDTO apply(Trip x) { return mapper.map(x, TripDTO.class); }
        };

        toEntity = new Function<TripDTO,Trip>() {
            @Override public Trip apply(TripDTO x) { return mapper.map(x, Trip.class); }
        };

        excToEntity = new Function<ExcursionDTO, Excursion>() {
            @Override public Excursion apply(ExcursionDTO x) { return mapper.map(x, Excursion.class); }
        };
    }

    //--[  Methods  ]-----------------------------------------------------------

    /**
     * Saves the {@code trip}.
     * @param trip           The trip to save.
     */
    @Override
    @Transactional
    @Secured("ROLE_STAFF")
    public void addTrip(TripDTO trip) {
        Objects.requireNonNull(trip, "trip");
        Trip entity = toEntity.apply(trip);
        tripDao.addTrip(entity);
        trip.setId(entity.getId());
    }

    /**
     * Finds a trip with the specified {@code id}.
     * @param id             The ID to look for.
     * @return               A trip.
     */
    @Override
    @Transactional(readOnly = true)
    public TripDTO getTripById(long id) {
        Trip trip = tripDao.getTripById(id);
        return trip == null ? null : toDTO.apply(trip);
    }

    /**
     * Finds all trips that contain the specified {@code excursion}.
     * @param excursion      The excursion to look for.
     * @return               A list of matching trips.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TripDTO> getTripsWithExcursion(ExcursionDTO excursion) {
        Objects.requireNonNull(excursion, "exucrsion");
        return map(toDTO, tripDao.getTripsWithExcursion(excToEntity.apply(excursion)));
    }

    /**
     * Yields a list of all trips.
     * @return               A list of all trips.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TripDTO> getAllTrips() {
        return map(toDTO, tripDao.getAllTrips());
    }

    /**
     * Updates the {@code trip}.
     * @param trip           The trip to update.
     */
    @Override
    @Transactional
    @Secured("ROLE_STAFF")
    public void updateTrip(TripDTO trip) {
        Objects.requireNonNull(trip, "trip");
        tripDao.updateTrip(toEntity.apply(trip));
    }

    /**
     * Removes a trip by its {@code id}.
     * @param id             The id of a trip to remove.
     */
    @Override
    @Transactional
    @Secured("ROLE_STAFF")
    public void deleteTripById(long id) {
        tripDao.deleteTripById(id);
    }

    //<editor-fold defaultstate="collapsed" desc="[  Setters  ]">

    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

    public void setTripdao(TripDAO tripdao) {
        this.tripDao = tripdao;
    }

    //</editor-fold>

}
