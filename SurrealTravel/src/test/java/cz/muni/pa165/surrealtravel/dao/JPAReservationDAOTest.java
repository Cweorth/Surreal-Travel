package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Jan Klimeš [374259]
 */
public class JPAReservationDAOTest extends AbstractTest {
    
    private ReservationDAO dao;

    @Override
    public void setUp() {
        super.setUp();
        dao = new JPAReservationDAO(em);
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullExcursion() {
        Reservation reservation = new Reservation();
        reservation.addExcursion(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddNullReservation() {
        dao.addReservation(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddReservationCustomerNull() {
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(null, trip);
        
        dao.addReservation(reservation);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddReservationTripNull() {
        Customer customer = mkcustomer("Some dude", "not here");
        Reservation reservation = mkreservation(customer, null);
        
        dao.addReservation(reservation);
    }
    
    @Test
    public void testAddReservation() {
        Customer customer = mkcustomer("Some dude", "not here");
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(customer, trip);
        em.getTransaction().begin();
        dao.addReservation(reservation);
        em.getTransaction().commit();
        
        assertTrue(reservation.getId() > 0);

        em.getTransaction().begin();
        Reservation retrieved = em.find(Reservation.class, reservation.getId());
        em.getTransaction().commit();
        
        assertEquals(retrieved, reservation);
        truncateDb();
    }
    
    @Test
    public void testGetReservationById() {
        Customer customer = mkcustomer("Some dude", "not here");
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(customer, trip);
        
        em.getTransaction().begin();
        em.persist(customer);
        em.persist(trip);
        em.persist(reservation);
        em.getTransaction().commit();
        
        long id = reservation.getId();
        Reservation retrieved = dao.getReservationById(id);
        
        assertEquals(reservation, retrieved);
        truncateDb();
    }
    
    @Test
    public void testGetAllReservationsEmptyTable() {
        
        truncateDb();
        
        List<Reservation> expected = new ArrayList<>();
        em.getTransaction().begin();
        List<Reservation> retrieved = dao.getAllReservations();
        em.getTransaction().commit();
        
        assertEquals(expected, retrieved);
        assertTrue(expected.isEmpty() && retrieved.isEmpty());
        
    }
    
    @Test
    public void testGetAllReservations() {
        
        List<Reservation> expected = prepareReservationBatch();
        storeReservations(expected);
        
        em.getTransaction().begin();
        List<Reservation> retrieved = dao.getAllReservations();
        em.getTransaction().commit();
        
        assertDeepEquals(expected, retrieved);
        truncateDb();
        
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetAllReservationsByCustomerNull() {
        dao.getAllReservationsByCustomer(null);
    }
    
    @Test
    public void testGetAllReservationsByCustomer() {
        List<Reservation> res = prepareReservationBatch();
        Customer chosenCustomer = res.get(0).getCustomer();
        List<Reservation> expected = getReservationsForCustomer(res, chosenCustomer);
        
        storeReservations(res);

        em.getTransaction().begin();
        List<Reservation> retrieved = dao.getAllReservationsByCustomer(chosenCustomer);
        em.getTransaction().commit();
        
        assertDeepEquals(expected, retrieved);
        truncateDb();
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetAllReservationsByExcursionNull() {
        dao.getAllReservationsByExcursion(null);
    }
    
    @Test
    public void testGetAllReservationsByExcursion() {
        List<Reservation> res = prepareReservationBatch();
        Excursion chosenExcursion = res.get(0).getTrip().getExcursions().get(0); // every trip has at least 1 excursion, therefore this is a valid selection
        List<Reservation> expected = getReservationsForExcursion(res, chosenExcursion);
        
        storeReservations(res);

        em.getTransaction().begin();
        List<Reservation> retrieved = dao.getAllReservationsByExcursion(chosenExcursion);
        em.getTransaction().commit();

        assertDeepEquals(expected, retrieved);
        truncateDb();
    }
    
    /**
     * Generate 10 random Reservation objects.
     * @return 
     */
    private List<Reservation> prepareReservationBatch() {
        
        Random rand = new Random();  
        
        Customer[] customers = {
            mkcustomer("John Doe", "Six feet under"),
            mkcustomer("Jimbo Pennyless", "No-money-ville"),
            mkcustomer("Scrooge McDuck", "Duckburg"),
            mkcustomer("Weirdo", "hell-if-i-know")
        };
        
        Excursion[] excursions = {
            mkexcursion(mkdate(8, 11, 2014), 4, "description of excursion", "someplace", new BigDecimal(99)),
            mkexcursion(mkdate(10, 11, 2014), 2, "test text", "someplace else", new BigDecimal(199))
        };

        Trip[] trips = {
            mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99)),
            mktrip(mkdate(1, 12, 2014), mkdate(8, 12, 2014), "Someplace no one will find you", 1, new BigDecimal(199)),
            mktrip(mkdate(5, 11, 2014), mkdate(7, 11, 2014), "wherever", 60, new BigDecimal(1500))
        };
        
        for(int i = 0; i < rand.nextInt(3) + 1; i++)
            for(Trip t : trips)
                t.addExcursion(excursions[rand.nextInt(excursions.length)]);
   
        List<Reservation> res = new ArrayList<>();
        
        for(int i = 0; i < 10; i++)
            res.add(mkreservation(customers[rand.nextInt(customers.length)], trips[rand.nextInt(trips.length)]));
        
        return res;
      
    }
    
    /**
     * Store Reservation objects in the database.
     * @param res 
     */
    private void storeReservations(List<Reservation> res) {
        em.getTransaction().begin();
        for(Reservation r : res) {
            if(em.find(Customer.class, r.getCustomer().getId()) == null) em.persist(r.getCustomer());
            if(em.find(Trip.class, r.getTrip().getId()) == null) em.persist(r.getTrip());
//            for(Excursion e : r.getTrip().getExcursions())
//                if(em.find(Excursion.class, e.getId()) == null) em.persist(e);
            em.persist(r);
        }
        em.getTransaction().commit();
    }
    
    /**
     * Perform search and return Reservation objects only for the specified customer.
     * @param res
     * @param customer
     * @return 
     */
    private List<Reservation> getReservationsForCustomer(List<Reservation> res, Customer customer) {
        List<Reservation> subList = new ArrayList<>();
        for(Reservation r : res)
            if(r.getCustomer().equals(customer)) subList.add(r);
        return subList;
    }
    
    /**
     * Perform search and return Reservation objects only for the trips containing specified excursison.
     * @param res
     * @param excursion
     * @return 
     */
    private List<Reservation> getReservationsForExcursion(List<Reservation> res, Excursion excursion) {
        List<Reservation> subList = new ArrayList<>();
        for(Reservation r : res)
            if(r.getTrip().getExcursions().contains(excursion)) subList.add(r);
        return subList;
    }
    
    /**
     * Remove data from tables this test is working with.
     */
    private void truncateDb() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Reservation").executeUpdate();
        em.createQuery("DELETE FROM Trip").executeUpdate();
        em.createQuery("DELETE FROM Excursion").executeUpdate();
        em.createQuery("DELETE FROM Customer").executeUpdate();
        em.getTransaction().commit();
    }
    
    /**
     * Do deep equals (assertion) of two reservation collections.
     * @param expected
     * @param retrieved 
     */
    private void assertDeepEquals(List<Reservation> expected, List<Reservation> retrieved) {
        assertEquals(expected.size(), retrieved.size());
        if(expected.size() == retrieved.size())
            for(int i = 0; i < expected.size(); i++)
                assertEquals(retrieved.get(i), expected.get(i));
    }
}