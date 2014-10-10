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
 * @author Kiclus
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


    public void addReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        if(reservation.getId() < 0) throw new IllegalArgumentException("reservation id must be positiv number.");
        if(reservation.getCustomer() == null) throw new NullPointerException("customer in reservation is null.");
        if(reservation.getCustomer().getClass() != Customer.class ) throw new IllegalArgumentException("customer is not customer is empty string.");
        if(reservation.getExcursions()==null) throw new NullPointerException("no list of excursion");
        
        //EntityManager em = emf.createEntityManager();
        
        entityManager.persist(reservation);
        entityManager.close();
        
        
    }

    public Reservation getReservationById(long id) {
        
        if(id < 0) throw new IllegalArgumentException("problem in id- Id < 0");
        
        //EntityManager em = emf.createEntityManager();
        Reservation result = entityManager.find(Reservation.class, id);
        entityManager.close();
        return result;
        }

    public List<Reservation> getAllReservations() {
        //EntityManager em = emf.createEntityManager();
        List<Reservation> reservation = entityManager.createNamedQuery("Reservation.getAll", Reservation.class).getResultList();
        entityManager.close();
        return reservation;
    }

    public List<Reservation> getAllReservationsByCustomer(Customer customer) {
        //EntityManager em = emf.createEntityManager();
        List<Reservation> reserves = entityManager.createQuery("SELECT r FROM Reservation r JOIN FETCH r.customer WHERE r.customer.id= :d", Reservation.class).setParameter("d", customer.getId()).getResultList();
	entityManager.close();
        return reserves;
       
        
        
    }

    public List<Reservation> getAllReservationsByExcursion(Excursion excursion) {
        //EntityManager em = emf.createEntityManager();
        List<Reservation> reserves = entityManager.createQuery("SELECT r FROM Reservation r JOIN FETCH r.excursions WHERE r.excursions.id= :d", Reservation.class).setParameter("d", excursion.getId()).getResultList();
        entityManager.close();
        return reserves;
    }

    public void updateReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        if(reservation.getId() < 0) throw new IllegalArgumentException("reservation id must be positiv number.");
        if(reservation.getCustomer() == null) throw new NullPointerException("customer in reservation is null.");
        if(reservation.getCustomer().getClass() != Customer.class ) throw new IllegalArgumentException("customer is not customer is empty string.");
        if(reservation.getExcursions()==null) throw new NullPointerException("no list of excursion");
        
        //EntityManager em = emf.createEntityManager();
        entityManager.merge(reservation);
        entityManager.close();
        
    }

    public void deleteReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        
        
        //EntityManager em = emf.createEntityManager();
        entityManager.remove(reservation);
        entityManager.close();
    }

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
