/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
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
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import junit.framework.TestCase;
//import org.springframework.test.annotation.DirtiesContext;
//import org.testng.annotations.BeforeMethod;

/**
 *
 * @author Tomáš Kácel [359965]
 */
public class JPATripDAOTest extends AbstractTest {
    
    //@PersistenceUnit
    
    
    //public EntityManagerFactory emf;
    private JPATripDAO dao;
    private long tripId;
    
    @Override
    public void setUp() {
        super.setUp();
        dao = new JPATripDAO(em);
    }
    /*
    @Test
    public void addTrip(Trip trip){
        
    }
    */
    @Test
    public void getTripById(){
        Trip trip1= mktrip(mkdate(2015,6,15),mkdate(2015,8,13),"Trip to tramtaria",15,new BigDecimal(1000));
        Excursion ext= mkexcursion(mkdate(2015,6,15),21,"Tramtaria","Tramtaria castle",new BigDecimal(2020));
        List<Excursion> extList= new ArrayList<>();
        extList.add(ext);
        trip1.setExcursions(extList);
        em.getTransaction().begin();
        em.persist(trip1);
        Long id= trip1.getId();
        em.persist(ext);
        em.getTransaction().commit();
        
        
        Trip newTrip=dao.getTripById(id);
     
        assertEquals(newTrip.getId(),trip1.getId());
        em.getTransaction().begin();
        em.remove(trip1);
        em.getTransaction().commit();
        
        
    }
    
        @Test
    public void getAllTrips(){
      List<Trip> trp=new ArrayList<>();
      Trip trip1= mktrip(mkdate(15,6,2015),mkdate(18,6,2018),"Trip to Transilvania",15,new BigDecimal(1000));
      Excursion ext= mkexcursion(mkdate(2015,6,15),21,"Transilvania","Transilvani castle",new BigDecimal(2020));
      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      
      Trip trip2= mktrip(mkdate(5,2,2018),mkdate(5,1,2020),"Trip to gogoland",15,new BigDecimal(1000));
      Excursion ext2= mkexcursion(mkdate(5,2,2018),21,"gogoland","gogoland is very goood",new BigDecimal(2020));
      List<Excursion> extList2= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      trp.add(trip2);
      trp.add(trip1);
              
      
        em.getTransaction().begin();
        em.persist(trip1);
        em.persist(trip2);
        Long id= trip1.getId();
        Long id2= trip2.getId();
        em.persist(ext);
        em.persist(ext2);
        em.getTransaction().commit();
     
        
        
        List<Trip> l= dao.getAllTrips();
        
        int a=l.size();
        
        assertEquals(a,trp.size());
        em.getTransaction().begin();
        em.remove(trip1);
        em.remove(trip2);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void addTrip(){
      Trip trip1= mktrip(mkdate(15,6,2015),mkdate(20,7,2015),"Trip to tramtaria",15,new BigDecimal(1000));
      Excursion ext= mkexcursion(mkdate(16,6,2015),21,"Tramtaria","Tramtaria castle",new BigDecimal(2020));
      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      em.getTransaction().begin();
      dao.addTrip(trip1);
      
      
      
      Long id= trip1.getId();
      em.getTransaction().commit();
      
      Trip newTrip=dao.getTripById(id);
      assertEquals(newTrip.getBasePrice(),trip1.getBasePrice());
      assertEquals(newTrip.getDestination(),trip1.getDestination());
      em.getTransaction().begin();
      em.remove(trip1);
      em.getTransaction().commit();
    }
    
    @Test
    public void deleteTrip(){
      Trip trip1= mktrip(mkdate(15,6,2015),mkdate(20,7,2015),"Trip to tramtaria",15,new BigDecimal(1000));
      Excursion ext= mkexcursion(mkdate(16,6,2015),21,"Tramtaria","Tramtaria castle",new BigDecimal(2020));
      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      em.getTransaction().begin();
      dao.addTrip(trip1);
      
      Long id= trip1.getId();
      em.getTransaction().commit();
      
      
      Trip trp2= dao.getTripById(id);
      //assertNotNull(trp2);
      em.getTransaction().begin();
      dao.deleteTrip(trip1);
      em.getTransaction().commit();
      Trip trp3=dao.getTripById(id);
      //System.out.println(trp3);
      assertNull(trp3);
      
        
    }
    
    @Test
    public void deleteTripById(){
       Trip trip1= mktrip(mkdate(15,6,2015),mkdate(20,7,2015),"Trip to prerov",15,new BigDecimal(1000));
      Excursion ext= mkexcursion(mkdate(16,6,2015),21,"Prerov","Prerov castle",new BigDecimal(2020));
      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      em.getTransaction().begin();
      dao.addTrip(trip1);
      
      Long id= trip1.getId();
      em.getTransaction().commit();
      
      Trip trp2= dao.getTripById(id);
      em.getTransaction().begin();
      dao.deleteTrip(trip1);
      em.getTransaction().commit();
      Trip trp3=dao.getTripById(id);
      assertNull(trp3);
      
    }
    
    @Test
    public void getTripsByDestination(){
        
      List<Trip> trp=new ArrayList<>();
      Trip trip1= mktrip(mkdate(15,6,2015),mkdate(18,6,2018),"Trip to Transilvania",15,new BigDecimal(1000));
      Excursion ext= mkexcursion(mkdate(2015,6,15),21,"Transilvania","Transilvani castle",new BigDecimal(2020));
      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      
      Trip trip2= mktrip(mkdate(5,2,2018),mkdate(5,1,2020),"Trip to gogoland",15,new BigDecimal(1000));
      Excursion ext2= mkexcursion(mkdate(5,2,2018),21,"gogoland","gogoland is very goood",new BigDecimal(2020));
      List<Excursion> extList2= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      trp.add(trip2);
      trp.add(trip1);
              
      
      em.getTransaction().begin();
      em.persist(trip1);
      em.persist(trip2);
      Long id= trip1.getId();
      Long id2= trip2.getId();
      em.persist(ext);
      em.persist(ext2);
      em.getTransaction().commit();
      List<Trip> trips= dao.getTripsByDestination(trip1.getDestination());
      for(Trip newTrip:trips){
          assertEquals(newTrip.getDestination(),trip1.getDestination());
      //dao.getTripsWithExcursion(ext2)
      }
      
      
        
    }
    
    @Test
    public void getEntityManager(){
      EntityManager man= dao.getEntityManager();
      assertTrue(man instanceof EntityManager);
      }
    
    
    
    
    @Test
    public void getTripsWithExcursion(){
      List<Trip> trp=new ArrayList<>();
      Trip trip1= mktrip(mkdate(15,6,2015),mkdate(18,6,2018),"Trip to Transilvania",15,new BigDecimal(1000));
      Excursion ext= mkexcursion(mkdate(2015,6,15),21,"Metro","Metro castle",new BigDecimal(2020));
      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      
      Trip trip2= mktrip(mkdate(5,2,2018),mkdate(5,1,2020),"Trip to gogoland",15,new BigDecimal(1000));
      Excursion ext2= mkexcursion(mkdate(5,2,2018),21,"gogoland","gogoland is very goood",new BigDecimal(2020));
      List<Excursion> extList2= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      trp.add(trip2);
      trp.add(trip1);
      
      em.getTransaction().begin();
      em.persist(trip1);
      em.persist(trip2);
      Long id= trip1.getId();
      Long id2= trip2.getId();
      em.persist(ext);
      em.persist(ext2);
      em.getTransaction().commit();
      List<Trip> trips= dao.getTripsWithExcursion(ext);
      for(Trip newTrip:trips){
          List<Excursion> extlist=newTrip.getExcursions();
          for(Excursion e:extlist){
              
          
          assertEquals(e,ext);
          }
      }
      
     
    }
    
    @Test
    public void testUpdateTrip(){
        List<Trip> trp=new ArrayList<>();
      Trip trip1= mktrip(mkdate(15,6,2015),mkdate(18,6,2018),"Trip to Transilvania",15,new BigDecimal(1000));
      
      Excursion ext= mkexcursion(mkdate(15,6,2015),21,"Metro","Metro castle",new BigDecimal(2020));
      

      List<Excursion> extList= new ArrayList<>();
      extList.add(ext);
      trip1.setExcursions(extList);
      
      em.getTransaction().begin();
      dao.addTrip(trip1);
      em.getTransaction().commit();
      em.detach(trip1);
      trip1.addExcursion(ext);
      
      em.getTransaction().begin();
      trip1 = dao.updateTrip(trip1);
      em.getTransaction().commit();

      em.getTransaction().begin();
      Trip retrieved = em.find(Trip.class, trip1.getId());
      em.getTransaction().commit();

      assertEquals(retrieved, trip1);
      
    }
        
 }    