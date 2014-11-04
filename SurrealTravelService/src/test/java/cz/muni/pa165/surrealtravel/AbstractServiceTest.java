package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.mockito.Spy;

/**
 * This class loads application context for service layer tests.
 * @author Roman Lacko [396157]
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractServiceTest {
    
    protected static final Logger     logger     = LogManager.getLogger("TestLogger");
    
    @Spy
    protected static DozerBeanMapper  mapper;
    @Rule
    public final TestName             testName   = new TestName();
    @Rule
    public final ExceptionLoggingRule excpLogger = new ExceptionLoggingRule(logger);
    
    //--[  Methods  ]-----------------------------------------------------------
    
    @BeforeClass
    public static void setUpClass() {
        ApplicationContext context = new ClassPathXmlApplicationContext("service/testContext.xml");
        mapper = context.getBean("mapper", DozerBeanMapper.class);
    }
    
    @Before
    public void setUp() {
        logger.info(String.format("==[(  %s  )]==============================================", testName.getMethodName()));
        logger.debug("[Setup] test started");
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
     * Convenience method for creating a customer DTO.
     * @param  name          Customer's name.
     * @param  address       Customer's address.
     * @return A new customer.
     */
    protected static CustomerDTO mkcustomer(String name, String address) {
        CustomerDTO customer = new CustomerDTO();
        customer.setName(name);
        customer.setAddress(address);
        
        logger.info(String.format("[mk] Created %s", customer.toString()));
        return customer;
    }
    
    /**
     * Convenience method for creating an excursion DTO.
     * @param  date          The date of the excursion.
     * @param  duration      Duration in days.
     * @param  description   Excursion's description.
     * @param  destination   Excursion's destination.
     * @param  price         The price of the excursion.
     * @return A new excursion.
     */
    protected static ExcursionDTO mkexcursion(Date date, int duration, String description, String destination, BigDecimal price) {
        ExcursionDTO excursion = new ExcursionDTO();
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
     * @param  trip          A trip the {@code customer} books.
     * @return A new reservation.
     */
    protected static ReservationDTO mkreservation(CustomerDTO customer, TripDTO trip) {
        ReservationDTO reservation = new ReservationDTO();
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
    protected static TripDTO mktrip(Date from, Date to, String destination, int capacity, BigDecimal price) {
        TripDTO trip = new TripDTO();
        trip.setDateFrom(from);
        trip.setDateTo(to);
        trip.setDestination(destination);
        trip.setCapacity(capacity);
        trip.setBasePrice(price);
        
        logger.info(String.format("[mk] Created %s", trip.toString()));
        return trip;
    }    
    
}
