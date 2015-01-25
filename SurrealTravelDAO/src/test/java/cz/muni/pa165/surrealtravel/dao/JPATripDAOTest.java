package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractPersistenceTest;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Testo for JPATripDAO
 *
 * @author Tomáš Kácel [359965]
 */
@Transactional
public class JPATripDAOTest extends AbstractPersistenceTest {

    @Autowired
    private JPATripDAO dao;

    @Test
    public void getTripById() {
        Trip trip1 = mktrip(mkdate(2015, 6, 15), mkdate(2015, 8, 13), "Trip to tramtaria", 15, new BigDecimal(1000));
        Excursion ext = mkexcursion(mkdate(2015, 6, 15), 21, "Tramtaria", "Tramtaria castle", new BigDecimal(2020));
        List<Excursion> extList = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);
        em.persist(trip1);
        Long id = trip1.getId();
        em.persist(ext);

        Trip newTrip = dao.getTripById(id);

        assertEquals(newTrip.getId(), trip1.getId());
    }

    @Test
    public void getAllTrips() {
        List<Trip> trp = new ArrayList<>();
        Trip trip1 = mktrip(mkdate(15, 6, 2015), mkdate(18, 6, 2018), "Trip to Transilvania", 15, new BigDecimal(1000));
        Excursion ext = mkexcursion(mkdate(2015, 6, 15), 21, "Transilvania", "Transilvani castle", new BigDecimal(2020));
        List<Excursion> extList = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);

        Trip trip2 = mktrip(mkdate(5, 2, 2018), mkdate(5, 1, 2020), "Trip to gogoland", 15, new BigDecimal(1000));
        Excursion ext2 = mkexcursion(mkdate(5, 2, 2018), 21, "gogoland", "gogoland is very goood", new BigDecimal(2020));
        List<Excursion> extList2 = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);
        trp.add(trip2);
        trp.add(trip1);

        em.persist(trip1);
        em.persist(trip2);
        em.persist(ext);
        em.persist(ext2);

        List<Trip> l = dao.getAllTrips();

        int a = l.size();

        assertEquals(a, trp.size());
        em.remove(trip1);
        em.remove(trip2);
    }

    @Test(expected = NullPointerException.class)
    public void testAddTripNull() {
        Trip trip1 = null;
        dao.addTrip(trip1);
    }

    @Test
    public void addTrip() {
        Trip trip1 = mktrip(mkdate(15, 6, 2015), mkdate(20, 7, 2015), "Trip to tramtaria", 15, new BigDecimal(1000));
        Excursion ext = mkexcursion(mkdate(16, 6, 2015), 21, "Tramtaria", "Tramtaria castle", new BigDecimal(2020));
        List<Excursion> extList = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);
        dao.addTrip(trip1);

        Long id = trip1.getId();

        Trip newTrip = dao.getTripById(id);
        assertEquals(newTrip.getBasePrice(), trip1.getBasePrice());
        assertEquals(newTrip.getDestination(), trip1.getDestination());
    }

    @Test
    public void deleteTripById() {
        Trip trip1 = mktrip(mkdate(15, 6, 2015), mkdate(20, 7, 2015), "Trip to prerov", 15, new BigDecimal(1000));
        Excursion ext = mkexcursion(mkdate(16, 6, 2015), 21, "Prerov", "Prerov castle", new BigDecimal(2020));
        List<Excursion> extList = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);
        dao.addTrip(trip1);

        Long id = trip1.getId();

        dao.deleteTripById(id);
        assertFalse(dao.getAllTrips().contains(trip1));
    }

    @Test(expected = NullPointerException.class)
    public void getTripByDestinationNull() {
        Trip trip1 = mktrip(mkdate(15, 6, 2015), mkdate(18, 6, 2018), null, 15, new BigDecimal(1000));
        dao.addTrip(trip1);
    }

    @Test
    public void getTripsWithExcursion() {
        Trip trip1 = mktrip(mkdate(15, 6, 2015), mkdate(18, 6, 2018), "Trip to Transilvania", 15, new BigDecimal(1000));
        Excursion ext = mkexcursion(mkdate(2015, 6, 15), 21, "Metro", "Metro castle", new BigDecimal(2020));
        List<Excursion> extList = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);

        Trip trip2 = mktrip(mkdate(5, 2, 2018), mkdate(5, 1, 2020), "Trip to gogoland", 15, new BigDecimal(1000));
        Excursion ext2 = mkexcursion(mkdate(5, 2, 2018), 21, "gogoland", "gogoland is very goood", new BigDecimal(2020));
        extList.add(ext);
        trip1.setExcursions(extList);

        em.persist(trip1);
        em.persist(trip2);
        em.persist(ext);
        em.persist(ext2);
        List<Trip> trips = dao.getTripsWithExcursion(ext);
        for (Trip newTrip : trips) {
            List<Excursion> extlist = newTrip.getExcursions();
            for (Excursion e : extlist) {
                assertEquals(e, ext);
            }
        }

    }

    @Test
    public void testUpdateTrip() {
        Trip trip1 = mktrip(mkdate(15, 6, 2015), mkdate(18, 6, 2018), "Trip to Transilvania", 15, new BigDecimal(1000));

        Excursion ext = mkexcursion(mkdate(15, 6, 2015), 21, "Metro", "Metro castle", new BigDecimal(2020));

        List<Excursion> extList = new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);

        dao.addTrip(trip1);
        em.detach(trip1);
        trip1.addExcursion(ext);

        trip1 = dao.updateTrip(trip1);

        Trip retrieved = em.find(Trip.class, trip1.getId());

        assertEquals(retrieved, trip1);
    }

}
