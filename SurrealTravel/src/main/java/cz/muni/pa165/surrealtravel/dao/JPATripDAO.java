package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Roman Lacko [396157]
 */
public class JPATripDAO implements TripDAO {

    private final EntityManagerFactory emf;

    public JPATripDAO() {
        emf = Persistence.createEntityManagerFactory("Surreal-Travel");
    }
    
    @Override
    public void addTrip(Trip trip) {
        Objects.requireNonNull(trip, "trip");
        EntityManager em = emf.createEntityManager();
        em.persist(trip);
        em.close();
    }

    @Override
    public Trip getTripById(long id) {
        EntityManager em = emf.createEntityManager();        
        Trip result = em.find(Trip.class, id);
        em.close();
        return result;
    }

    @Override
    public List<Trip> getTripsByDestination(String destination) {
        EntityManager em = emf.createEntityManager();        
        List<Trip> result =
             em.createQuery("SELECT t FROM Trip t WHERE t.destination = :d", Trip.class)
               .setParameter("d", destination)
               .getResultList();
        em.close();
        return result;        
    }

    @Override
    public List<Trip> getAllTrips() {
        EntityManager em = emf.createEntityManager();        
        List<Trip> result =
             em.createNamedQuery("getAllTrips", Trip.class)
               .getResultList();
        em.close();
        return result;
    }

    @Override
    public void updateTrip(Trip trip) {
        EntityManager em = emf.createEntityManager();
        em.merge(trip); 
        em.close();
    }    

    @Override
    public void deleteTrip(Trip trip) {
        EntityManager em = emf.createEntityManager();
        em.remove(trip);
        em.close();
    }    

    @Override
    public List<Trip> getTripsWithExcursion(Excursion excursion) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
