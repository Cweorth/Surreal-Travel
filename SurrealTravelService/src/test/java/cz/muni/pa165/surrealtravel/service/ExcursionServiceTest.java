package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.ExcursionDAO;
import cz.muni.pa165.surrealtravel.dao.TripDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for Excursion service class.
 * @author Jan Klimeš [374259]
 */
public class ExcursionServiceTest extends AbstractServiceTest {
    
    @Mock
    private ExcursionDAO dao;
    
    @Mock
    private TripDAO tripDao;
    
    @InjectMocks
    private DefaultExcursionService service;
    
    public ExcursionServiceTest() {
        super();
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddExcursionNull() {
        service.addExcursion(null);
    }
    
    @Test
    public void testAddExcursion() { 
        List<ExcursionDTO> excursions = prepareDummyExcursions(3);
        
        for(ExcursionDTO excursion : excursions) {
            try {
                service.addExcursion(excursion);
            } catch(IllegalArgumentException e) {
                Assert.fail();
            }
        }
        
        verify(dao, times(excursions.size())).addExcursion(any(Excursion.class));
    }


    @Test
    public void testGetExcursionById() {
        long uniqueId = 3L;
        
        List<ExcursionDTO> excursions = prepareDummyExcursions(5);
        
        ExcursionDTO expected = excursions.get(3);
        Excursion entity = mapper.map(expected, Excursion.class);
       
        when(dao.getExcursionById(uniqueId)).thenReturn(entity);
        ExcursionDTO retrieved = service.getExcursionById(uniqueId);
        
        verify(dao, times(1)).getExcursionById(Matchers.eq(uniqueId));
        assertNotNull(retrieved);
        assertEquals(uniqueId, retrieved.getId());
        assertEquals(expected, retrieved);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetExcursionsByDestinationNull() {
        service.getExcursionsByDestination(null);
    }

    @Test
    public void testGetExcursionsByDestination() {
        String destination = "somewhereInTheJungle";
        
        List<ExcursionDTO> excursions = prepareDummyExcursions(5);
        ExcursionDTO selectedExcursion = excursions.get(1);
        
        List<ExcursionDTO> expected = Arrays.asList(new ExcursionDTO[] {selectedExcursion});
        Excursion entity = mapper.map(selectedExcursion, Excursion.class);
              
        when(dao.getExcursionsByDestination(destination)).thenReturn(Arrays.asList(new Excursion[] {entity}));
        List<ExcursionDTO> retrieved = service.getExcursionsByDestination(destination);
               
        verify(dao, times(1)).getExcursionsByDestination(Matchers.eq(destination));
        assertNotNull(retrieved);
        assertTrue(retrieved.size() > 0);
        for(ExcursionDTO e : retrieved)
            assertEquals(destination, e.getDestination());
        assertEquals(expected, retrieved);
    }

    @Test
    public void testGetAllExcursions() {
        List<ExcursionDTO> expected = prepareDummyExcursions(5);
        List<Excursion> entities = new ArrayList<>();
        for(ExcursionDTO e : expected)
            entities.add(mapper.map(e, Excursion.class));
        
        when(dao.getAllExcursions()).thenReturn(entities);
        List<ExcursionDTO> retrieved = service.getAllExcursions();
        
        verify(dao, times(1)).getAllExcursions();
        assertTrue(retrieved.size() == expected.size());
        assertEquals(expected, retrieved);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateExcursionNull() {
        service.updateExcursion(null);
    }
    
    @Test
    public void testUpdateExcursion() {
        ExcursionDTO toUpdate = prepareDummyExcursion();
        Excursion entity = mapper.map(toUpdate, Excursion.class);
        when(tripDao.getTripsWithExcursion(entity)).thenReturn(new ArrayList<Trip>());
        
        service.updateExcursion(toUpdate);
        verify(dao, times(1)).updateExcursion(entity);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteExcursionNull() {
        service.deleteExcursion(null);
    }
    
    @Test
    public void testDeleteExcursion() {
        ExcursionDTO toRemove = prepareDummyExcursion();
        Excursion entity = mapper.map(toRemove, Excursion.class);
        service.deleteExcursion(toRemove);
        verify(dao, times(1)).deleteExcursion(entity);
    }

    @Test
    public void testDeleteExcursionById() {
        ExcursionDTO toRemove = prepareDummyExcursion();
        long id = toRemove.getId();
        service.deleteExcursionById(id);
        verify(dao, times(1)).deleteExcursionById(id);
    }
    
    /**
     * Utility method. Create 1 ExcursionDTO object.
     * @return 
     */
    private ExcursionDTO prepareDummyExcursion() {
        return prepareDummyExcursions(1).get(0);
    }
    
    /**
     * Utility method. Create amount of ExcursionDTO objects.
     * @param amount
     * @return 
     */
    private List<ExcursionDTO> prepareDummyExcursions(int amount) {
        ExcursionDTO[] excursions = {
            mkexcursion(new Date(), 1, "whatever", "wherever", new BigDecimal(400)),
            mkexcursion(new Date(), 2, "whatever", "somewhereInTheJungle", new BigDecimal(100)),
            mkexcursion(new Date(), 2, "whatever", "someplace", new BigDecimal(50)),
            mkexcursion(new Date(), 1, "whatever", "hell", new BigDecimal(666)),
            mkexcursion(new Date(), 4, "whatever", "brno", new BigDecimal(500))
        };
        
        for(int i = 0; i < excursions.length - 1; i++)
            excursions[i].setId(i);
        
        if(amount > excursions.length) amount = excursions.length;
        
        List<ExcursionDTO> list = new ArrayList<>();
        for(int i = 0; i < amount; i++)
            list.add(excursions[i]);
        
        return list;
    }
    
}
