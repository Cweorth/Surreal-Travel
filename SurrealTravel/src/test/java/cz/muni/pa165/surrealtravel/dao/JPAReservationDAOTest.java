package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Roman Lacko [396157]
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
    
    @Test
    public void testSimpleReservationWithoutDao() {
        Customer gandalf = mkcustomer("Gandalf the Grey", "Valinor");
        Trip     mordor  = mktrip(mkdate(18, 12, 3018), mkdate(25, 03, 3019),
                                  "The journey to Mordor", 9, new BigDecimal(2500));
        Reservation reservation = mkreservation(gandalf, mordor);
        
        logger.info("persisting Gandalf and his reservation to mordor");
        em.getTransaction().begin();
        em.persist(gandalf);
        em.persist(mordor);
        em.persist(reservation);
        
        logger.info("commit");
        em.getTransaction().commit();
        
        long result = em.createQuery("SELECT COUNT(r) FROM Reservation r", Long.class)
                        .getSingleResult();
        
        try {
            Assert.assertEquals(1L, result);
        } catch (AssertionError ex) {
            logger.error(ex);
            throw ex;
        }
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddNullReservationToDAO() {
        dao.addReservation(null);
    }
    
}
