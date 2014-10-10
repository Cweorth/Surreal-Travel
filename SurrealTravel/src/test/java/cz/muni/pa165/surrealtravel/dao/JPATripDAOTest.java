/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import junit.framework.TestCase;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Tomáš Kácel [359965]
 */
public class JPATripDAOTest extends TestCase {
    
    @PersistenceUnit
    private JPAReservationDAO manager= new JPAReservationDAO();
    
    public EntityManagerFactory emf;
    private long tripId;
    
    public JPATripDAOTest(String testName) {
        super(testName);
    }
    
    @DirtiesContext
    @BeforeMethod
    protected void setUp() throws Exception {
        //emf = Persistence.createEntityManagerFactory("Surreal-Travel");
        EntityManager em = manager.emf.createEntityManager();
        //EntityManager em = emf.createEntityManager();
        //em.getTransaction().begin();
        
        Date dat1= new Date(2014,6,22);
        Date dat2= new Date(2014,6,29);
        String destination="Tramtaria";
        int capacity=15;
        List<Excursion> ext= new ArrayList<Excursion>();
        BigDecimal price= new BigDecimal(1500);
        
        Date dat3= new Date(2014,8,22);
        Date dat4= new Date(2014,9,14);
        String destionation2="Transilnavia";
        int capacity2=16;
        List<Excursion> ext2= new ArrayList<Excursion>();
        BigDecimal price2= new BigDecimal(1800);
        
        Excursion exc1= new Excursion();
        exc1.setDescription("Tramtaria Castle is one of the biggest...");
        exc1.setDestination("Tramtaria Castle");
        exc1.setDuration(10);
        exc1.setExcursionDate(dat2);
        exc1.setPrice(new BigDecimal(152));
        ext.add(exc1);
        
        Excursion exc2= new Excursion();
        exc2.setDescription("In transelvania lived DRACULA...");
        exc2.setDestination("Transilvania Castle");
        exc2.setDuration(16);
        exc2.setExcursionDate(dat3);
        exc2.setPrice(new BigDecimal(1600));
        ext2.add(exc2);
        
        Trip trip1= new Trip();
        trip1.setBasePrice(price);
        trip1.setCapacity(capacity);
        trip1.setDateFrom(dat2);
        trip1.setDateTo(dat1);
        trip1.setDestination(destination);
        trip1.setExcursions(ext);
        
        Trip trip2= new Trip();
        trip2.setBasePrice(price2);
        trip2.setCapacity(capacity2);
        trip2.setDateFrom(dat3);
        trip2.setDateTo(dat4);
        trip2.setDestination(destionation2);
        trip2.setExcursions(ext2);
        
        
        
        
        em.persist(trip1);
        em.persist(exc1);
        em.persist(trip1);
        em.persist(exc2);
        tripId=trip2.getId();
        //em.getTransaction().commit();
	em.close();
            
    }
    
    
    
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
}


