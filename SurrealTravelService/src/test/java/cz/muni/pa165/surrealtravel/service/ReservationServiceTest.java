package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.ReservationDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link ReservationService} class.
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
    
    //<editor-fold defaultstate="collapsed" desc="[  Extensions  ]">
    
    private interface Function<T,U> {
        U apply (T x);
    }
    
    private <T,U> List<U> map(Function<T,U> f, List<T> s) {
        List<U> r = new ArrayList<>(s.size());
        for(T x : s) r.add(f.apply(x));
        return r;
    }
    
    private static final Function<Reservation, ReservationDTO> toDTO
        = new Function<Reservation, ReservationDTO>() {
            @Override public ReservationDTO apply(Reservation x) { return mapper.map(x, ReservationDTO.class); }
        };
    
    private static final Function<ReservationDTO, Reservation> toEntity
        = new Function<ReservationDTO, Reservation>() {
            @Override public Reservation apply(ReservationDTO x) { return mapper.map(x, Reservation.class); }
        };
    
    //</editor-fold>
    
    private Customer customerDTOToEntity(CustomerDTO customer) {
        return mapper.map(customer, Customer.class);
    }
    
    private Excursion excursionDTOToEntity(ExcursionDTO excursion) {
        return mapper.map(excursion, Excursion.class);
    }
    
    public ReservationServiceTest() {
        super();
               
        //<editor-fold defaultstate="collapsed" desc="(  Data Initialization  )">
        
        customers = Arrays.asList(
            mkcustomer("Frodo Baggins",    "Hobbiton, The Shire"),
            mkcustomer("Sauron The Great", "Barad-dûr, Mordor")
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
       
    @Test(expected = NullPointerException.class)
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
    public void createReservationsTest() {
        doAnswer(new Answer() {
            private int counter = 0;
            
            @Override 
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Reservation arg = invocation.getArgumentAt(0, Reservation.class);
                arg.setId(counter++);
                return arg;
            }
        }).when(dao).addReservation(any(Reservation.class));
        
        long prevkey = Long.MIN_VALUE;
        for(ReservationDTO reservation : reservations) {
            service.addReservation(reservation);
            assertTrue("Expected increasing order of keys", prevkey < reservation.getId());
            prevkey = reservation.getId();
        }
        
        verify(dao, times(reservations.size())).addReservation(any(Reservation.class));
    }

    @Test
    public void getReservationByIdTest() {
        for(int ix = 0; ix < reservations.size(); ++ix) {
            Reservation r = toEntity.apply(reservations.get(ix));
            when(dao.getReservationById(ix)).thenReturn(r);
            assertEquals(reservations.get(ix), service.getReservationById(ix));
        }
        
        verify(dao, times(reservations.size())).getReservationById(any(Long.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void getReservationsByCustomerNullTest() {
        service.getAllReservationsByCustomer(null);
    }
    
    @Test
    public void getReservationsByCustomerTest() {
        Customer customer0 = customerDTOToEntity(customers.get(0));
        Customer customer1 = customerDTOToEntity(customers.get(1));
        List<Reservation> r0 = Arrays.asList(toEntity.apply(reservations.get(0)));
        List<Reservation> r1 = map(toEntity, reservations.subList(1, 2));
        
        when(dao.getAllReservationsByCustomer(customer0)).thenReturn(r0);
        when(dao.getAllReservationsByCustomer(customer1)).thenReturn(r1);
        
        List<ReservationDTO> act0 = service.getAllReservationsByCustomer(customers.get(0));
        List<ReservationDTO> act1 = service.getAllReservationsByCustomer(customers.get(1));
        
        assertEquals(Arrays.asList(reservations.get(0)),       act0);
        assertEquals(              reservations.subList(1, 2), act1);
        
        verify(dao, times(2)).getAllReservationsByCustomer(any(Customer.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void getReservationsByExcursionNullTest() {
        service.getAllReservationsByExcursion(null);
    }
    
    @Test
    public void getReservationsByExcursionTest() {
        Excursion excursion = excursionDTOToEntity(excursions.get(0));
        List<Reservation> result = map(toEntity, reservations.subList(0, 1));
        
        when(dao.getAllReservationsByExcursion(excursion)).thenReturn(result);
        
        List<ReservationDTO> actual = service.getAllReservationsByExcursion(excursions.get(0));
        assertEquals(              reservations.subList(0, 1), actual);
        verify(dao, times(1)).getAllReservationsByExcursion(any(Excursion.class));
    }

    @Test(expected = NullPointerException.class)
    public void updateReservationNullTest() {
        service.updateReservation(null);
    }
    
    @Test
    public void updateReservationTest() {
        service.updateReservation(reservations.get(0));
        verify(dao, times(1)).updateReservation(any(Reservation.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void deleteReservationNullTest() {
        service.deleteReservation(null);
    }
    
    @Test
    public void deleteReservationTest() {
        service.deleteReservation(reservations.get(0));
        verify(dao, times(1)).deleteReservation(any(Reservation.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void getFullPriceNullTest() {
        service.getFullPriceByCustomer(null);
    }
    
    @Test
    public void getFullPriceTest() {
        service.getFullPriceByCustomer(customers.get(0));
        verify(dao, times(1)).getFullPriceByCustomer(any(Customer.class));
    }
    
    @Test(expected = NullPointerException.class)
    public void removeExcursionNullTest() {
        service.removeExcursionFromAllReservations(null);
    }
    
    @Test
    public void removeExcursionTest() {
        service.removeExcursionFromAllReservations(excursions.get(0));
        verify(dao, times(1)).removeExcursionFromAllReservations(any(Excursion.class));
    }
    
}
