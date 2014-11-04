package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This class creates the in-memory database for testing.
 * @author Roman Lacko [396157]
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:persistence/testContext.xml"})
public abstract class AbstractTest { 
    
    @PersistenceContext
    protected EntityManager         em;
    
    protected static final Logger   logger = LogManager.getLogger("TestLogger");
    
    @Rule
    public final TestName             testName   = new TestName();
    @Rule
    public final ExceptionLoggingRule excpLogger = new ExceptionLoggingRule(logger);
    
    //--[  Methods  ]-----------------------------------------------------------
    
    @Before
    public void setUp() {
        logger.info(String.format("==[(  %s  )]==============================================", testName.getMethodName()));
        logger.debug("[Setup] Creating entity manager for the test");
    }
    
    @After
    public void tearDown() {
        logger.debug("[TearDown] test finished");
    }
    
    /**
     * Convenience method for creating a date.
     * @param  day           The day of the date.
     * @param  month         The month of the date.
     * @param  year          The year of the date.
     * @return The date.
     */
    protected static Date mkdate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return calendar.getTime();
    }
    
    /**
     * Convenience method for creating a customer entity.
     * @param  name          Customer's name.
     * @param  address       Customer's address.
     * @return A new customer.
     */
    protected static Customer mkcustomer(String name, String address) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setAddress(address);
        
        logger.info(String.format("[mk] Created %s", customer.toString()));
        return customer;
    }
    
    /**
     * Convenience method for creating an excursion entity.
     * @param  date          The date of the excursion.
     * @param  duration      Duration in days.
     * @param  description   Excursion's description.
     * @param  destination   Excursion's destination.
     * @param  price         The price of the excursion.
     * @return A new excursion.
     */
    protected static Excursion mkexcursion(Date date, int duration, String description, String destination, BigDecimal price) {
        Excursion excursion = new Excursion();
        excursion.setExcursionDate(date);
        excursion.setDuration(duration);
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setPrice(price);
        
        logger.info(String.format("[mk] Created %s", excursion.toString()));
        return excursion;
    }
    
    /**
     * Convenience method for creating a reservation.
     * @param  customer      A customer who makes this reservation.
     * @param  trip          A trip this customer books.
     * @return A new reservation.
     */
    protected static Reservation mkreservation(Customer customer, Trip trip) {
        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setTrip(trip);
        
        logger.info(String.format("[mk] Created %s", reservation.toString()));
        return reservation;
    }
    
    /**
     * Convenience method for creating a trip.
     * @param  from          The date the trip starts.
     * @param  to            The date the trip ends.
     * @param  destination   The destination.
     * @param  capacity      A Number of customers that can book this trip.
     * @param  price         The base price of this trip without excursions.
     * @return A new trip.
     */
    protected static Trip mktrip(Date from, Date to, String destination, int capacity, BigDecimal price) {
        Trip trip = new Trip();
        trip.setDateFrom(from);
        trip.setDateTo(to);
        trip.setDestination(destination);
        trip.setCapacity(capacity);
        trip.setBasePrice(price);
        
        logger.info(String.format("[mk] Created %s", trip.toString()));
        return trip;
    }
    
    //<editor-fold desc="[  Getters | Setters  ]" defaultstate="collapsed">

    /*
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = Objects.requireNonNull(emf);
    }
    
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
    */

    //</editor-fold>
    
}