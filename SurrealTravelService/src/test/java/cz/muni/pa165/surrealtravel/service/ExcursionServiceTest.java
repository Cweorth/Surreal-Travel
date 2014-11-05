package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.ExcursionDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test for Excursion service class.
 * @author Jan Klimeš [374259]
 */
public class ExcursionServiceTest extends AbstractServiceTest {
    
    @Mock
    private ExcursionDAO dao;
    
    @InjectMocks
    private ExcursionService service;
    
    public ExcursionServiceTest() {
        super();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddExcursionNull() {
        service.addExcursion(null);
    }
    
    @Test
    public void testAddExcursion() {
        ExcursionDTO[] list = {
            mkexcursion(new Date(), 2, "whatever", "dest", new BigDecimal(400)),
            mkexcursion(new Date(), 5, "desc", "abc", new BigDecimal(20)),
            mkexcursion(new Date(), 7, "not empty description", "not empty destination", new BigDecimal(100))
        };
        
        for(ExcursionDTO excursion : list) {
            try {
                service.addExcursion(excursion);
            } catch(IllegalArgumentException e) {
                Assert.fail();
            }
        }
        
        verify(dao, times(list.length)).addExcursion(any(Excursion.class));
    }


    @Test
    public void testGetExcursionById() {

    }

    /**
     * Test of getExcursionsByDestination method, of class ExcursionService.
     */
    @Test
    public void testGetExcursionsByDestination() {

    }

    /**
     * Test of getAllExcursions method, of class ExcursionService.
     */
    @Test
    public void testGetAllExcursions() {

    }

    /**
     * Test of updateExcursion method, of class ExcursionService.
     */
    @Test
    public void testUpdateExcursion() {

    }

    /**
     * Test of deleteExcursion method, of class ExcursionService.
     */
    @Test
    public void testDeleteExcursion() {

    }

    /**
     * Test of deleteExcursionById method, of class ExcursionService.
     */
    @Test
    public void testDeleteExcursionById() {

    }
    
}
