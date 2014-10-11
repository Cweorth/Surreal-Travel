/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Tomáš Kácel [359965]
 */
public class JPAReservationDAO implements ReservationDAO {
    
    //EntityManagerFactory emf;
    private EntityManager entityManager;
    
    
    public JPAReservationDAO(){
        //emf = Persistence.createEntityManagerFactory("Surreal-Travel");
    }

    public JPAReservationDAO(EntityManager em) {
         entityManager= em;
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public void addReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        if(reservation.getId() < 0) throw new IllegalArgumentException("reservation id must be positiv number.");
        if(reservation.getCustomer() == null) throw new NullPointerException("customer in reservation is null.");
        if(reservation.getCustomer().getClass() != Customer.class ) throw new IllegalArgumentException("customer is not customer is empty string.");
        if(reservation.getTrip()==null) throw new NullPointerException("trip in reservatio doesnt exist.");
        //if(reservation.getExcursions()==null) throw new NullPointerException("no list of excursion");
        
        //EntityManager em = emf.createEntityManager();
        entityManager.persist(reservation.getTrip());
        entityManager.persist(reservation.getCustomer());
        entityManager.persist(reservation);
        
        
        
    }
    
    @Override
    public Reservation getReservationById(long id) {
        
        if(id < 0) throw new IllegalArgumentException("problem in id- Id < 0");
        
        //EntityManager em = emf.createEntityManager();
        Reservation result = entityManager.find(Reservation.class, id);
        
        return result;
        }
    
    @Override
    public List<Reservation> getAllReservations() {
        //EntityManager em = emf.createEntityManager();
        List<Reservation> reservation = entityManager.createNamedQuery("Reservation.getAll", Reservation.class).getResultList();
        
        return reservation;
    }
    
    @Override
    public List<Reservation> getAllReservationsByCustomer(Customer customer) {
        //EntityManager em = emf.createEntityManager();
        List<Reservation> reserves = entityManager.createQuery("SELECT r FROM Reservation r JOIN FETCH r.customer WHERE r.customer.id= :d", Reservation.class).setParameter("d", customer.getId()).getResultList();
	
        return reserves;
       
        
        
    }
    @Override
    public List<Reservation> getAllReservationsByExcursion(Excursion excursion) {
        //EntityManager em = emf.createEntityManager();
        if(excursion == null) throw new NullPointerException("excursion doesnt exist.");
        List<Reservation> reserves = entityManager.createQuery("SELECT r FROM Reservation r JOIN r.excursions e WHERE e.id= :d", Reservation.class).setParameter("d", excursion.getId()).getResultList();
        
        return reserves;
    }
    
    @Override
    public Reservation updateReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        if(reservation.getId() < 0) throw new IllegalArgumentException("reservation id must be positiv number.");
        if(reservation.getCustomer() == null) throw new NullPointerException("customer in reservation is null.");
        if(reservation.getCustomer().getClass() != Customer.class ) throw new IllegalArgumentException("customer is not customer is empty string.");
        if(reservation.getExcursions()==null) throw new NullPointerException("no list of excursion");
        
        //EntityManager em = emf.createEntityManager();
        Reservation merge = entityManager.merge(reservation);
        return merge;
        
        
    }
    
    @Override
    public void deleteReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        
        
        //EntityManager em = emf.createEntityManager();
        entityManager.remove(reservation);
        
    }
    
    @Override
    public BigDecimal getFullPriceByCustomer(Customer customer) {
      if(customer==null) throw new NullPointerException("customer doesnt exist.");
      if(customer.getId() < 0) throw new IllegalArgumentException("customer id must be positiv number.");
        
      //EntityManager em = emf.createEntityManager();
      List<Reservation> reserv =getAllReservationsByCustomer(customer);
      BigDecimal dec= new BigDecimal(0);
      for(Reservation r: reserv){
          dec.add(r.getTotalPrice());
          
      }
      
      return dec;  
        
    }
    
    @Override
    public void removeExcursionFromAllReservations(Excursion excursion) {
        if(excursion==null)throw new NullPointerException("excursion doesnt exist.");
        if(excursion.getId() < 0) throw new IllegalArgumentException("excursion id must be positiv number.");
        
        List<Reservation> res= getAllReservations();
        for(Reservation r: res){
          if(r.getExcursions().contains(excursion)){
              r.removeExcursion(excursion);
              updateReservation(r);
          }
            
        }
    }
    
}
