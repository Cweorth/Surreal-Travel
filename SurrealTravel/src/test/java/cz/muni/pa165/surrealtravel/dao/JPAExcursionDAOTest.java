/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author cweorth
 */
public class JPAExcursionDAOTest extends AbstractTest {
    
    private static List<Excursion> excursions;
    private ExcursionDAO dao;
    
    private void insertTestExcursions() {
        logger.info("adding test excursions");
        
        em.getTransaction().begin();
        for(Excursion excursion : excursions) {
            dao.addExcursion(excursion);
        }
        em.getTransaction().commit();
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
    
    @BeforeClass
    public static void createEntities() {
        excursions = new ArrayList<>();
        excursions.add(mkexcursion(mkdate(20, 10, 2941), 2, "Battle of Five Armies",   "Erebor",      new BigDecimal(500)));
        excursions.add(mkexcursion(mkdate(25, 10, 3018), 1, "Council of Elrond",       "Rivendell",   new BigDecimal(150)));
        excursions.add(mkexcursion(mkdate(02, 03, 3019), 1, "Destruction of Isengard", "Isengard",    new BigDecimal(400)));
        excursions.add(mkexcursion(mkdate(03, 03, 3019), 1, "Battle of Hornburg",      "Helm's Deep", new BigDecimal(350)));
        excursions.add(mkexcursion(mkdate(14, 03, 3019), 3, "Mt Doom Excursion",       "Mordor",      new BigDecimal(200)));
        excursions.add(mkexcursion(mkdate(25, 03, 3019), 2, "Downfall of Barad-dûr",   "Mordor",      new BigDecimal(300)));
    }
    
    @Before
    @Override
    public void setUp() {
        super.setUp();
        dao = new JPAExcursionDAO(em);
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddNullExcursionToDAO() {
        dao.addExcursion(null);
    }
    
    @Test
    public void testInvalidExcursions() {
        List<Excursion> invalidExcursions = Arrays.asList(
                new Excursion(),
                mkexcursion(null,                 3, "Battle of Hornburg",      "Helm's Deep",  new BigDecimal(350)),
                mkexcursion(mkdate(15, 03, 3019), 1, null,                      "Minas Tirith", new BigDecimal(700)),
                mkexcursion(mkdate(15, 03, 3019), 1, "",                        "Minas Tirith", new BigDecimal(750)),
                mkexcursion(mkdate(27, 03, 3019), 2, "The Siege of Erebor",     null,           new BigDecimal(600)),
                mkexcursion(mkdate(27, 03, 3019), 2, "The Siege of Erebor",     "",             new BigDecimal(600)),
                mkexcursion(mkdate(02, 03, 3019), 1, "Destruction of Isengard", "Isengard",     null)
        );
        
        for(int ix = 0; ix < invalidExcursions.size(); ++ix) {
            try {
                dao.addExcursion(invalidExcursions.get(ix));
            } catch (NullPointerException | IllegalArgumentException ex) {
                // OK
                continue;
            }
            
            String message = String.format("An exception should have been thrown for index %d", ix);
            logger.error(message);
            Assert.fail(message);
        }
    }
    
    @Test
    public void testExcursionPersistence() {
        insertTestExcursions();
        
        long result = em.createQuery("SELECT COUNT(e) FROM Excursion e", Long.class)
                        .getSingleResult();
        
        Assert.assertEquals(excursions.size(), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetExcursionByIdInvalid() {
        dao.getExcursionById(-1);
    }
    
    @Test
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
    
    @Test
    public void testGetExcursionsByDestination() {
        String[] destinations = new String[] { "Erebor", "Mordor", "Esgaroth" };
        
        for(String destination : destinations) {
            logger.info(String.format("destination = %s", destination));
            List<Excursion> expected = filter(new DestEquals(destination), excursions);
            List<Excursion> actual   = dao.getExcursionByDestination(destination);
            
            Assert.assertEquals(expected.size(), actual.size());
            
            for(Excursion excursion : expected) {
                Assert.assertTrue(actual.contains(excursion));
            }
        }
    }
    
    @Test
    public void testGetAllExcursions() {
        insertTestExcursions();
        
        Assert.assertEquals(excursions.size(), dao.getAllExcursion().size());
    }
    
    @Test
    public void testUpdateExcursion() {
        insertTestExcursions();
        
        List<Excursion> original = filter(new DestEquals("Mordor"), dao.getAllExcursion());
        
        em.getTransaction().begin();
        for(Excursion excursion : original) {
            excursion.setDuration(20);
            dao.updateExcursion(excursion);
        }
        em.getTransaction().commit();
        
        List<Excursion> updated = filter(new DestEquals("Mordor"), dao.getAllExcursion());
        
        for(Excursion excursion : updated) {
            Assert.assertEquals(20, (int) excursion.getDuration());
        }
    }
    
    @Test
    public void testDeleteExcursion() {
        insertTestExcursions();
        Excursion excursion = dao.getExcursionById(1L);
        
        em.getTransaction().begin();
        dao.deleteExcursion(excursion);
        em.getTransaction().commit();
       
        Assert.assertFalse(dao.getAllExcursion().contains(excursion));
    }
    
    @Test
    public void testDeleteExcursionById() {
        insertTestExcursions();
        Excursion excursion = dao.getExcursionById(2L);
        
        em.getTransaction().begin();
        dao.deleteExcursionById(2L);
        em.getTransaction().commit();
       
        Assert.assertFalse(dao.getAllExcursion().contains(excursion));
    }
}
