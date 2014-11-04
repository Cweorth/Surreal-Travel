package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.ReservationDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Roman Lacko [396157]
 */
public class ReservationServiceTest extends AbstractServiceTest {
    
    @Mock
    private ReservationDAO     dao;
    @InjectMocks
    private ReservationService service;
    
    private final List<CustomerDTO>    customers;
    private final List<ExcursionDTO>   excursions;
    private final List<TripDTO>        trips;
    private final List<ReservationDTO> reservations;
        
    public ReservationServiceTest() {
        super();
               
        //<editor-fold defaultstate="collapsed" desc="(  Data Initialization  )">
        
        customers = Arrays.asList(
            mkcustomer("Frodo Baggins",   "Hobbiton, The Shire"),
            mkcustomer("Suron The Great", "Barad-dûr, Mordor")
        );
        
        excursions = Arrays.asList(
            mkexcursion(mkdate(20, 10, 2941), 2, "Battle of Five Armies",   "Erebor",      new BigDecimal(500)),
            mkexcursion(mkdate(25, 10, 3018), 1, "Council of Elrond",       "Rivendell",   new BigDecimal(150)),
            mkexcursion(mkdate(02, 03, 3019), 1, "Destruction of Isengard", "Isengard",    new BigDecimal(400)),
            mkexcursion(mkdate(03, 03, 3019), 1, "Battle of Hornburg",      "Helm's Deep", new BigDecimal(350)),
            mkexcursion(mkdate(14, 03, 3019), 3, "Mt Doom Excursion",       "Mordor",      new BigDecimal(200)),
            mkexcursion(mkdate(25, 03, 3019), 2, "Downfall of Barad-dûr",   "Mordor",      new BigDecimal(300))
        );
        
        trips = Arrays.asList(
            mktrip(mkdate(19, 10, 2941), mkdate(27, 03, 3019), "Middle Earth",        20, new BigDecimal(1000)),
            mktrip(mkdate(19, 10, 2941), mkdate(05, 03, 3019), "Battles of the Ring", 15, new BigDecimal( 800)),
            mktrip(mkdate(13, 03, 3019), mkdate(27, 03, 3019), "Spring in Mordor",    10, new BigDecimal( 300))
        );
        
        trips.get(0).setExcursions(excursions);
        trips.get(1).setExcursions(Arrays.asList(excursions.get(0), excursions.get(2), excursions.get(3)));
        trips.get(2).setExcursions(excursions.subList(4, 5));
        
        reservations = Arrays.asList(
            mkreservation(customers.get(0), trips.get(0)),
            mkreservation(customers.get(1), trips.get(1)),
            mkreservation(customers.get(1), trips.get(2))
        );
        
        reservations.get(0).setExcursions(trips.get(0).getExcursions());
        reservations.get(1).setExcursions(trips.get(1).getExcursions());
        reservations.get(2).addExcursion (excursions.get(5));
        
        //</editor-fold>
        
    }
    
    //==================================================//
    //  REMOVE THE FOLLOWING BULLCRAP BEFORE DEPLOYING  //
    //==================================================//
    @Test
    public void sanityTest() {
        assertNotNull("dao",     dao);
        assertNotNull("service", service);
        assertNotNull("mapper",  mapper);
        
        java.lang.reflect.Field field;
        try {
            field = service.getClass().getDeclaredField("mapper");
        } catch (NoSuchFieldException | SecurityException ex) {
            fail("Goddammit f*ck!");
            return;
        }
        
        field.setAccessible(true);
        try {
            assertNotNull("service.mapper", field.get(service));
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            fail("A spinning black hole of Belzeebub");
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createNullReservationTest() {
        service.addReservation(null);
    }
    
    @Test
    public void createInvalidReservationTest() {
        try {
            service.addReservation(mkreservation(null, null));
            Assert.fail("No exception has been thrown (expected NullPointerException or IllegalArgumentException)");
        } catch (NullPointerException | IllegalArgumentException ex) {
            // OK
        }
    }
    
    @Test
    public void createReservations() {
        for(ReservationDTO reservation : reservations) {
            service.addReservation(reservation);
        }
        
        verify(dao, times(reservations.size())).addReservation(any(Reservation.class));
    }
    
}
