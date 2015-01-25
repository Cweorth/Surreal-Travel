package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractPersistenceTest;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test for JPAReservationDAO class.
 * @author Jan Klimeš [374259]
 */
public class JPAReservationDAOTest extends AbstractPersistenceTest {

    @Autowired
    private ReservationDAO dao;

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testNullExcursion() {
        Reservation reservation = new Reservation();
        reservation.addExcursion(null);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testAddNullReservation() {
        dao.addReservation(null);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testAddReservationCustomerNull() {
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(null, trip);

        dao.addReservation(reservation);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testAddReservationTripNull() {
        Customer customer = mkcustomer("Some dude", "not here");
        Reservation reservation = mkreservation(customer, null);

        dao.addReservation(reservation);
    }

    @Test
    @Transactional
    public void testAddReservation() {
        Customer customer = mkcustomer("Some dude", "not here");
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(customer, trip);
        dao.addReservation(reservation);

        assertTrue(reservation.getId() > 0);

        Reservation retrieved = em.find(Reservation.class, reservation.getId());

        assertEquals(reservation, retrieved);
    }

    @Test
    @Transactional
    public void testGetReservationById() {
        Customer customer = mkcustomer("Some dude", "not here");
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(customer, trip);

        em.persist(reservation);

        long id = reservation.getId();
        Reservation retrieved = dao.getReservationById(id);

        assertEquals(reservation, retrieved);
    }

    @Test
    @Transactional
    public void testGetAllReservationsEmptyTable() {
        List<Reservation> expected  = new ArrayList<>();
        List<Reservation> retrieved = dao.getAllReservations();

        assertEquals(expected, retrieved);
        assertTrue(expected.isEmpty() && retrieved.isEmpty());
    }

    @Test
    @Transactional
    public void testGetAllReservations() {
        List<Reservation> expected = prepareReservationBatch();
        storeReservations(expected);

        List<Reservation> retrieved = dao.getAllReservations();

        assertDeepEquals(expected, retrieved);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testGetAllReservationsByCustomerNull() {
        dao.getAllReservationsByCustomer(null);
    }

    @Test
    @Transactional
    public void testGetAllReservationsByCustomer() {
        List<Reservation> res = prepareReservationBatch();
        Customer chosenCustomer = res.get(0).getCustomer();
        List<Reservation> expected = getReservationsForCustomer(res, chosenCustomer);

        storeReservations(res);

        List<Reservation> retrieved = dao.getAllReservationsByCustomer(chosenCustomer);

        assertDeepEquals(expected, retrieved);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testUpdateReservationNull() {
        dao.updateReservation(null);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testUpdateReservationCustomerNull() {
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(null, trip);

        dao.updateReservation(reservation);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testUpdateReservationTripNull() {
        Customer customer = mkcustomer("Some dude", "not here");
        Reservation reservation = mkreservation(customer, null);

        dao.updateReservation(reservation);
    }

    @Test
    @Transactional
    public void testUpdateReservationRemoveExcursions() {
        List<Reservation> res = prepareReservationBatch();
        storeReservations(res);
        Reservation chosenReservation = res.get(0);

        // we need to detach managed object or the update method below is redundant
        em.detach(chosenReservation);

        chosenReservation.setExcursions(null);

        chosenReservation = dao.updateReservation(chosenReservation);

        Reservation retrieved = em.find(Reservation.class, chosenReservation.getId());

        assertEquals(retrieved, chosenReservation);
    }

    @Test
    @Transactional
    public void testUpdateReservation() {
        Customer customer = mkcustomer("Some dude", "not here");
        Trip trip = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(99));
        Reservation reservation = mkreservation(customer, trip);
        Excursion excursion = mkexcursion(mkdate(8, 11, 2014), 4, "description of excursion", "someplace", new BigDecimal(99));

        em.persist(reservation);

        // we need to detach managed object or the update method below is redundant
        em.detach(reservation);

        reservation.addExcursion(excursion);

        reservation = dao.updateReservation(reservation);

        Reservation retrieved = em.find(Reservation.class, reservation.getId());

        assertEquals(retrieved, reservation);
    }

    @Test(expected = NullPointerException.class)
    @Transactional
    public void testGetFullPriceByCustomerNull() {
        dao.getFullPriceByCustomer(null);
    }

    @Test
    @Transactional
    public void testGetFullPriceByCustomer() {
        Customer customer = mkcustomer("Some dude", "not here");
        Trip trip1 = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(100));
        Trip trip2 = mktrip(mkdate(1, 11, 2014), mkdate(8, 11, 2014), "Iraq", 500, new BigDecimal(200));
        Reservation reservation1 = mkreservation(customer, trip1);
        Reservation reservation2 = mkreservation(customer, trip2);

        Excursion excursion1 = mkexcursion(mkdate(8, 11, 2014), 4, "description of excursion", "someplace", new BigDecimal(1000));
        Excursion excursion2 = mkexcursion(mkdate(8, 11, 2014), 4, "description of excursion", "someplace", new BigDecimal(2000));

        reservation1.addExcursion(excursion1);
        reservation1.addExcursion(excursion2);

        BigDecimal expected = sumReservation(reservation1);
        expected = expected.add(sumReservation(reservation2));

        storeReservation(reservation1);
        storeReservation(reservation2);

        BigDecimal retrieved = dao.getFullPriceByCustomer(customer);

        assertEquals(expected, retrieved);
    }
    //--[  Helper methods  ]---------------------------------------------------

    /**
     * Generate dummy Reservation objects.
     * @return
     */
    private List<Reservation> prepareReservationBatch() {

        Customer[] customers = {
            mkcustomer("John Doe", "Six feet under"),
            mkcustomer("Jimbo Pennyless", "No-money-ville"),
            mkcustomer("Scrooge McDuck", "Duckburg"),
            mkcustomer("Weirdo", "hell-if-i-know")
        };

        Trip[] trips = {
            mktrip(mkdate(1, 11, 2010), mkdate(8, 11, 2015), "Iraq", 500, new BigDecimal(99)),
            mktrip(mkdate(1, 12, 2010), mkdate(8, 12, 2015), "Someplace no one will find you", 1, new BigDecimal(199)),
            mktrip(mkdate(5, 11, 2010), mkdate(7, 11, 2015), "wherever", 60, new BigDecimal(1500)),
            mktrip(mkdate(8, 11, 2010), mkdate(15, 11, 2015), "No man's land", 120, new BigDecimal(9999))
        };

        Excursion[] excursions = {
            mkexcursion(mkdate(8, 11, 2014), 4, "Gunning with god", "Off the grid", new BigDecimal(99)),
            mkexcursion(mkdate(10, 11, 2014), 2, "Sightseeing", "City of history", new BigDecimal(199)),
            mkexcursion(mkdate(11, 11, 2014), 8, "Broken computers freaky exposition", "Dead circuit city", new BigDecimal(300)),
            mkexcursion(mkdate(11, 11, 2014), 8, "Boring expo", "Boredomville", new BigDecimal(300)),
            mkexcursion(mkdate(11, 11, 2014), 8, "Freaking lunapark", "Vienna", new BigDecimal(500))
        };

        trips[0].addExcursion(excursions[0]);
        trips[1].addExcursion(excursions[1]);
        trips[2].addExcursion(excursions[2]);
        trips[2].addExcursion(excursions[3]);
        trips[2].addExcursion(excursions[4]);

        List<Reservation> res = new ArrayList<>();
        res.add(createReservation(customers[0], trips[0]));
        res.add(createReservation(customers[0], trips[1]));
        res.add(createReservation(customers[1], trips[2]));
        res.add(createReservation(customers[2], trips[0]));
        res.add(createReservation(customers[2], trips[2]));
        res.add(createReservation(customers[3], trips[3]));

        return res;

    }

    /**
     * Create dummy reservation object and set all excursions from the trip as selected.
     * @param c
     * @param t
     * @return
     */
    private Reservation createReservation(Customer c, Trip t) {
        Reservation r = mkreservation(c, t);
        for(Excursion e : t.getExcursions())
            r.addExcursion(e);

        return r;
    }

    /**
     * Store Reservation objects in the database.
     * @param res
     */
    private void storeReservations(List<Reservation> res) {
        for(Reservation r : res) {
            storeReservation(r);
        }
    }

    /**
     * Store single reservation.
     * @param r
     */
    private void storeReservation(Reservation r) {
        em.persist(r.getCustomer());
        for(Excursion e : r.getExcursions())
            em.persist(e);
        em.persist(r.getTrip());
        em.persist(r);
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
     * Sum reservation price.
     * @param r
     * @return
     */
    private BigDecimal sumReservation(Reservation r) {
        BigDecimal price = r.getTrip().getBasePrice();

        for(Excursion e : r.getExcursions())
            price = price.add(e.getPrice());

        return price;
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
