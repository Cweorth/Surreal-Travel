/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import junit.framework.TestCase;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Kiclus
 */
public class JPATripDAOTest extends TestCase {
    
    @PersistenceUnit
    public EntityManagerFactory emf;
    
    public JPATripDAOTest(String testName) {
        super(testName);
    }
    
    @DirtiesContext
    @BeforeMethod
    protected void setUp() throws Exception {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Date dat1= new Date(2014,6,22);
        Date dat2= new Date(2014,6,29);
        String destination="Tramtaria";
        int capacity=15;
        List<Excursion> ext= new ArrayList<Excursion>();
        BigDecimal price= new BigDecimal(1500);
        
        Excursion exc= new Excursion();
        exc.setDescription("Tramtaria Castle is one of the biggest...");
        exc.setDestination("Tramtaria Castle");
        exc.setDuration(10);
        exc.setExcursionDate(dat2);
        exc.setPrice(new BigDecimal(152));
        ext.add(exc);
        
        Trip trip1= new Trip();
        trip1.setBasePrice(price);
        trip1.setCapacity(capacity);
        trip1.setDateFrom(dat2);
        trip1.setDateTo(dat1);
        trip1.setDestination(destination);
        trip1.setExcursions(ext);
        
        
        
        
        
        
        
        
        
        
       
        
        
        
        
        
        
    }
    
    
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getEntityManager method, of class JPATripDAO.
     */
    public void testGetEntityManager() {
        System.out.println("getEntityManager");
        JPATripDAO instance = null;
        EntityManager expResult = null;
        EntityManager result = instance.getEntityManager();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEntityManager method, of class JPATripDAO.
     */
    public void testSetEntityManager() {
        System.out.println("setEntityManager");
        EntityManager entityManager = null;
        JPATripDAO instance = null;
        instance.setEntityManager(entityManager);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addTrip method, of class JPATripDAO.
     */
    public void testAddTrip() {
        System.out.println("addTrip");
        Trip trip = null;
        JPATripDAO instance = null;
        instance.addTrip(trip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTripById method, of class JPATripDAO.
     */
    public void testGetTripById() {
        System.out.println("getTripById");
        long id = 0L;
        JPATripDAO instance = null;
        Trip expResult = null;
        Trip result = instance.getTripById(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTripsByDestination method, of class JPATripDAO.
     */
    public void testGetTripsByDestination() {
        System.out.println("getTripsByDestination");
        String destination = "";
        JPATripDAO instance = null;
        List<Trip> expResult = null;
        List<Trip> result = instance.getTripsByDestination(destination);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTripsWithExcursion method, of class JPATripDAO.
     */
    public void testGetTripsWithExcursion() {
        System.out.println("getTripsWithExcursion");
        Excursion excursion = null;
        JPATripDAO instance = null;
        List<Trip> expResult = null;
        List<Trip> result = instance.getTripsWithExcursion(excursion);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllTrips method, of class JPATripDAO.
     */
    public void testGetAllTrips() {
        System.out.println("getAllTrips");
        JPATripDAO instance = null;
        List<Trip> expResult = null;
        List<Trip> result = instance.getAllTrips();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateTrip method, of class JPATripDAO.
     */
    public void testUpdateTrip() {
        System.out.println("updateTrip");
        Trip trip = null;
        JPATripDAO instance = null;
        instance.updateTrip(trip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteTrip method, of class JPATripDAO.
     */
    public void testDeleteTrip() {
        System.out.println("deleteTrip");
        Trip trip = null;
        JPATripDAO instance = null;
        instance.deleteTrip(trip);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteTripById method, of class JPATripDAO.
     */
    public void testDeleteTripById() {
        System.out.println("deleteTripById");
        long id = 0L;
        JPATripDAO instance = null;
        instance.deleteTripById(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
  
}
