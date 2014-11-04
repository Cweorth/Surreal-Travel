package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for the {@link JPAReservationDAO} class.
 * @author Roman Lacko [396157]
 */
public class JPAExcursionDAOTest extends AbstractTest {
    
    @Autowired
    private ExcursionDAO    dao;
    
    private List<Excursion> createTestExcursions() {
        return Arrays.asList(
            mkexcursion(mkdate(20, 10, 2941), 2, "Battle of Five Armies",   "Erebor",      new BigDecimal(500)),
            mkexcursion(mkdate(25, 10, 3018), 1, "Council of Elrond",       "Rivendell",   new BigDecimal(150)),
            mkexcursion(mkdate(02, 03, 3019), 1, "Destruction of Isengard", "Isengard",    new BigDecimal(400)),
            mkexcursion(mkdate(03, 03, 3019), 1, "Battle of Hornburg",      "Helm's Deep", new BigDecimal(350)),
            mkexcursion(mkdate(14, 03, 3019), 3, "Mt Doom Excursion",       "Mordor",      new BigDecimal(200)),
            mkexcursion(mkdate(25, 03, 3019), 2, "Downfall of Barad-dûr",   "Mordor",      new BigDecimal(300))
        );
    }
    
    private void insertTestExcursions() {
        logger.info("adding test excursions");
        
        for(Excursion excursion : createTestExcursions()) {
            dao.addExcursion(excursion);
        }
    }
    
    //<editor-fold desc="[  Functional extensions  ]" defaultstate="collapsed">
    
    interface Function<T,U> {
        public U apply(T x);
    }
    
    interface Predicate<T> extends Function<T, Boolean> 
    { }
    
    private <T> List<T> filter(Predicate<T> p, List<T> source) {
        List<T> result = new ArrayList<>();
        for(T x : source) { if (p.apply(x)) { result.add(x); } }
        return result;
    }
    
    //</editor-fold>
    
    class DestEquals implements Predicate<Excursion> {
        private final String dest;        
        
        public DestEquals(String dest) 
        { this.dest = Objects.requireNonNull(dest, "elem"); }
        
        @Override
        public Boolean apply(Excursion x) 
        { return (x != null) && Objects.equals(x.getDestination(), dest); }
    }
   
    @Test(expected = NullPointerException.class)
    @Transactional
    public void testAddNullExcursionToDAO() {
        dao.addExcursion(null);
    }
    
    @Test
    @Transactional
    public void testInvalidExcursions() {
        List<Excursion> invalidExcursions = Arrays.asList(
            new Excursion(),
            mkexcursion(null,                  3, "Battle of Hornburg",      "Helm's Deep",  new BigDecimal(350)),
            mkexcursion(mkdate(25, 10, 3018), -1, "Council of Elrond",       "Rivendell",    new BigDecimal(150)),
            mkexcursion(mkdate(15, 03, 3019),  1, null,                      "Minas Tirith", new BigDecimal(700)),
            mkexcursion(mkdate(15, 03, 3019),  1, "",                        "Minas Tirith", new BigDecimal(750)),
            mkexcursion(mkdate(27, 03, 3019),  2, "The Siege of Erebor",     null,           new BigDecimal(600)),
            mkexcursion(mkdate(27, 03, 3019),  2, "The Siege of Erebor",     "",             new BigDecimal(600)),
            mkexcursion(mkdate(02, 03, 3019),  1, "Destruction of Isengard", "Isengard",     null)
        );
        
        for(int ix = 0; ix < invalidExcursions.size(); ++ix) {
            try {
                dao.addExcursion(invalidExcursions.get(ix));
            } catch (NullPointerException | IllegalArgumentException ex) {
                // OK
                continue;
            }
            
            Assert.fail(String.format("An exception should have been thrown for index %d [%s]", ix, invalidExcursions.get(ix)));
        }
    }
    
    @Test
    @Transactional
    public void testExcursionPersistence() {
        insertTestExcursions();
        
        long result = em.createQuery("SELECT COUNT(e) FROM Excursion e", Long.class)
                        .getSingleResult();
        
        Assert.assertEquals(createTestExcursions().size(), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void testGetExcursionByIdInvalid() {
        dao.getExcursionById(-1);
    }
    
    @Test
    @Transactional
    public void testGetExcursionById() {
        insertTestExcursions();
        
        List<Excursion> managedExcursions = 
            em.createQuery("SELECT e FROM Excursion e", Excursion.class)
              .getResultList();
        
        for(Excursion expected : managedExcursions) {
            Excursion excursion = dao.getExcursionById(expected.getId());
            Assert.assertEquals(expected, excursion);
        }
    }
    
    @Test(expected = NullPointerException.class)
    @Transactional
    public void testGetExcursionsByNullDestination() {
        dao.getExcursionsByDestination(null);
    }
    
    @Test
    @Transactional
    public void testGetExcursionByDestinationEmpty() {
        List<Excursion> expected = new ArrayList<>();
        List<Excursion> actual   = dao.getExcursionsByDestination("Edoras");
        
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    @Transactional
    public void testGetExcursionsByDestination() {
        insertTestExcursions();
        String[] destinations = new String[] { "Erebor", "Mordor", "Esgaroth" };
        
        for(String destination : destinations) {
            logger.info(String.format("destination = %s", destination));
            List<Excursion> expected = filter(new DestEquals(destination), createTestExcursions());
            List<Excursion> actual   = dao.getExcursionsByDestination(destination);
            
            Assert.assertEquals(expected.size(), actual.size());
            
            for(int ix = 0; ix < actual.size(); ++ix) {
                expected.get(ix).setId(actual.get(ix).getId());
                Assert.assertTrue(actual.contains(expected.get(ix)));
            }
        }
    }
    
    @Test
    @Transactional
    public void testGetAllExcursionsEmpty() {
        Assert.assertEquals(new ArrayList<Excursion>(), dao.getAllExcursions());
    }
    
    @Test
    @Transactional
    public void testGetAllExcursions() {
        insertTestExcursions();
        
        List<Excursion> expl = createTestExcursions();
        List<Excursion> actl = dao.getAllExcursions();
        Assert.assertEquals(expl.size(), actl.size());
        
        for(int ix = 0; ix < actl.size(); ++ix) {
            expl.get(ix).setId(actl.get(ix).getId());
        }
        
        Assert.assertEquals(new HashSet(expl), new HashSet(actl));
    }
    
    @Test(expected = NullPointerException.class)
    @Transactional
    public void testUpdateExcursionNull() {
        dao.updateExcursion(null);
    }
    
    @Test
    @Transactional
    public void testUpdateExcursionInvalid() {
        insertTestExcursions();
        
        List<Excursion> excursions = createTestExcursions();
        
        Assert.assertTrue("Checking excursions count", excursions.size() >= 6);
        
        excursions.get(0).setDescription(null);
        excursions.get(1).setDestination(null);
        excursions.get(2).setDuration(-1);
        excursions.get(3).setExcursionDate(null);
        excursions.get(4).setPrice(null);
        excursions.get(5).setPrice(new BigDecimal(-200));
        
        for(int ix = 0; ix < excursions.size(); ++ix) {
            try {
                dao.updateExcursion(excursions.get(ix));
            } catch (NullPointerException | IllegalArgumentException ex) {
                // OK
                continue;
            }
            
            Assert.fail(String.format("An exception should have been thrown for index %d  [%s]", ix, excursions.get(ix)));
        }
    }
    
    @Test
    @Transactional
    public void testUpdateExcursion() {
        insertTestExcursions();
        
        List<Excursion> original = filter(new DestEquals("Mordor"), dao.getAllExcursions());
        
        for(Excursion excursion : original) {
            excursion.setDuration(20);
            dao.updateExcursion(excursion);
        }
        
        List<Excursion> updated = filter(new DestEquals("Mordor"), dao.getAllExcursions());
        
        for(Excursion excursion : updated) {
            Assert.assertEquals(20, (int) excursion.getDuration());
        }
    }
    
    @Test(expected = NullPointerException.class)
    @Transactional
    public void testDeleteExcursionNull() {
        dao.deleteExcursion(null);
    }
    
    @Test
    @Transactional
    public void testDeleteExcursion() {
        insertTestExcursions();
        Excursion excursion = createTestExcursions().get(0);

        dao.deleteExcursion(excursion);
       
        Assert.assertFalse(dao.getAllExcursions().contains(excursion));
    }
    
    @Test
    @Transactional
    public void testDeleteExcursionById() {
        insertTestExcursions();
        Excursion excursion = createTestExcursions().get(1);
        
        dao.deleteExcursionById(excursion.getId());
       
        Assert.assertFalse(dao.getAllExcursions().contains(excursion));
    }
}
