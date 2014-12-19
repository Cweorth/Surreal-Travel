package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.TripDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Petr Dvořák [359819]
 */
public class TripServiceTest extends AbstractServiceTest{
    
    @Mock
    private TripDAO     dao;
    @InjectMocks
    private DefaultTripService service;
    
    private final List<TripDTO>      trips;
    private final List<ExcursionDTO> excursions;
     
    public TripServiceTest() {
        super();
        
         excursions = Arrays.asList(
            mkexcursion(mkdate(02, 02, 1990), 1, "navsteva prezidentskeho sidla",   "Prazsky hrad",      new BigDecimal(100)),
            mkexcursion(mkdate(04, 02, 1991), 3, "navsteva korunovacnich klenotu",       "Prazsky hrad",   new BigDecimal(1000)),
            mkexcursion(mkdate(20, 03, 2000), 1, "navsteva Katakomb", "Spilberk",    new BigDecimal(200))
            );
         trips = Arrays.asList(
            mktrip(mkdate(01, 02, 1990), mkdate(10, 02, 1991), "Brno",  50, new BigDecimal(500)),
            mktrip(mkdate(11, 03, 2000), mkdate(31, 03, 2001), "Praha", 60, new BigDecimal(200))
            );
         trips.get(0).setExcursions(Arrays.asList(excursions.get(2)));
         trips.get(1).setExcursions(Arrays.asList(excursions.get(0), excursions.get(1)));
         trips.get(0).setId(1L);
         trips.get(1).setId(2L);
         
    }
    
    @Test(expected = NullPointerException.class)
    public void createNullTripTest() {
        service.addTrip(null);
    }
    
    @Test
    public void addTripTest(){
        for(TripDTO trip : trips) {
            service.addTrip(trip);
        }
                verify(dao, times(trips.size())).addTrip(any(Trip.class));
    }
    
    @Test
    public void getTripByIdTest(){
        long uniqueId = 2L;
        
        TripDTO expected = trips.get(1);
        Trip entity = mapper.map(expected, Trip.class);
       
        when(dao.getTripById(uniqueId)).thenReturn(entity);
        TripDTO retrieved = service.getTripById(uniqueId);
        
        verify(dao, times(1)).getTripById(Matchers.eq(uniqueId));
        assertNotNull(retrieved);
        assertEquals(uniqueId, retrieved.getId());
        assertEquals(expected, retrieved);
    }
    
    @Test
    public void getAllTripsTest(){
        List<Trip> entities = new ArrayList<>();
        for(TripDTO e : trips)
            entities.add(mapper.map(e, Trip.class));
        
        when(dao.getAllTrips()).thenReturn(entities);
        List<TripDTO> retrieved = service.getAllTrips();
        
        verify(dao, times(1)).getAllTrips();
        assertTrue(retrieved.size() == trips.size());
        assertEquals(trips, retrieved); 
    }
    
    @Test
    public void updateTripTest(){
        TripDTO tripToUpdate = trips.get(0);
        Trip entity = mapper.map(tripToUpdate, Trip.class);
        service.updateTrip(tripToUpdate);
        verify(dao, times(1)).updateTrip(entity);
        
    }
}
