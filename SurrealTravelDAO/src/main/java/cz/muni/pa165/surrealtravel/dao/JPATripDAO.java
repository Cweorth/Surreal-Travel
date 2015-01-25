package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * The JPA implementation of the {@link TripDAO} class.
 * @author Roman Lacko [396157]
 */
@Repository(value = "tripDao")
public class JPATripDAO implements TripDAO {

    @PersistenceContext
    private EntityManager entityManager;

    //--[  Private methods  ]---------------------------------------------------

    private void validate(Trip trip) {
        Objects.requireNonNull(trip,                  "trip");
        Objects.requireNonNull(trip.getDateFrom(),    "trip.dateFrom");
        Objects.requireNonNull(trip.getDateTo(),      "trip.dateTo");
        Objects.requireNonNull(trip.getDestination(), "trip.destination");
        Objects.requireNonNull(trip.getBasePrice(),   "trip.basePrice");
        Objects.requireNonNull(trip.getExcursions(),  "trip.excursions");

        if (trip.getDateFrom().after(trip.getDateTo())) {
            throw new IllegalArgumentException("The trip requires a time machine for it ends before it starts");
        }

        if (trip.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("The trip has a negative base price");
        }

        if (trip.getDestination().isEmpty()) {
            throw new IllegalArgumentException("The trip has an empty destination");
        }

        for(Excursion excursion : trip.getExcursions()) {
            Date excursionStart = excursion.getExcursionDate();
            Calendar calendar   = Calendar.getInstance();

            calendar.setTime(excursionStart);
            calendar.add(Calendar.DATE, excursion.getDuration());

            Date excursionEnd   = calendar.getTime();

            if (excursionStart.before(trip.getDateFrom()) || excursionEnd.after(trip.getDateTo())) {
                throw new IllegalArgumentException(String.format("Date of excursion '%s' is outside of that of the trip", excursion.getDescription()));
            }
        }
    }

    //--[  Methods  ]-----------------------------------------------------------

    //<editor-fold desc="[  Getters | Setters  ]" defaultstate="collapsed">

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        Objects.requireNonNull(entityManager, "entityManager");
        this.entityManager = entityManager;
    }

    //</editor-fold>

    //--[  Overriden methods  ]-------------------------------------------------

    @Override
    public void addTrip(Trip trip) {
        validate(trip);

        entityManager.persist(trip);
    }

    @Override
    public Trip getTripById(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("The id has a negative value");
        }

        return entityManager.find(Trip.class, id);
    }

    @Override
    public List<Trip> getTripsWithExcursion(Excursion excursion) {
        Objects.requireNonNull(excursion, "excursion");

        return entityManager.createQuery("SELECT t FROM Trip t JOIN t.excursions e WHERE e.id = :eid", Trip.class)
            .setParameter("eid", excursion.getId())
            .getResultList();
    }

    @Override
    public List<Trip> getAllTrips() {
        return entityManager.createNamedQuery("Trip.getAll", Trip.class)
            .getResultList();
    }

    @Override
    public Trip updateTrip(Trip trip) {
        validate(trip);
        return entityManager.merge(trip);
    }

    @Override
    public void deleteTripById(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("The id has a negative value");
        }

        entityManager.createNamedQuery("Trip.removeById")
            .setParameter("id", id)
            .executeUpdate();
    }

}
