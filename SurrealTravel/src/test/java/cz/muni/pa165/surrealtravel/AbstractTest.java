package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class creates the in-memory database for testing.
 * @author Roman Lacko [396157]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testContext.xml"})
public abstract class AbstractTest { 
    
    @Autowired
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager        em;
    
    protected static final Logger logger = LogManager.getLogger(AbstractTest.class.getName());
    
    @Before
    public void setUp() {
        em = entityManagerFactory.createEntityManager();
    }
    
    @After
    public void tearDown() {
        em.close();
    }
    
    //--[  Methods  ]-----------------------------------------------------------
    
    protected Date mkdate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
    
    protected Customer mkcustomer(String name, String address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAddress(address);
        
        logger.info(String.format("Created %s", customer.toString()));
        return customer;
    }
    
    protected Excursion mkexcursion(Date date, int duration, String description, String destination, BigDecimal price) {
        Excursion excursion = new Excursion();
        excursion.setExcursionDate(date);
        excursion.setDuration(duration);
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setPrice(price);
        
        logger.info(String.format("Created %s", excursion.toString()));
        return excursion;
    }
    
    protected Reservation mkreservation(Customer customer, Trip trip) {
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setTrip(trip);
        
        logger.info(String.format("Created %s", reservation.toString()));
        return reservation;
    }
    
    protected Trip mktrip(Date from, Date to, String destination, int capacity, BigDecimal price) {
        Trip trip = new Trip();
        trip.setDateFrom(from);
        trip.setDateTo(to);
        trip.setDestination(destination);
        trip.setCapacity(capacity);
        trip.setBasePrice(price);
        
        logger.info(String.format("Created %s", trip.toString()));
        return trip;
    }
    
    //<editor-fold desc="[  Getters | Setters  ]" defaultstate="collapsed">
    
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = Objects.requireNonNull(emf);
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    //</editor-fold>
    
}