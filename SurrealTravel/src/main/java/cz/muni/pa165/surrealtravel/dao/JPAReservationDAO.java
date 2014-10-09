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
    
    EntityManagerFactory emf;
    
    public JPAReservationDAO(){
        emf = Persistence.createEntityManagerFactory("Surreal-Travel");
    }

    public void addReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        if(reservation.getId() < 0) throw new IllegalArgumentException("reservation id must be positiv number.");
        if(reservation.getCustomer() == null) throw new NullPointerException("customer in reservation is null.");
        if(reservation.getCustomer().getClass() != Customer.class ) throw new IllegalArgumentException("customer is not customer is empty string.");
        if(reservation.getExcursions()==null) throw new NullPointerException("no list of excursion");
        
        EntityManager em = emf.createEntityManager();
        em.persist(reservation);
        em.close();
        
        
    }

    public Reservation getReservationById(long id) {
        
        if(id < 0) throw new IllegalArgumentException("problem in id- Id < 0");
        
        EntityManager em = emf.createEntityManager();
        Reservation result = em.find(Reservation.class, id);
        em.close();
        return result;
        }

    public List<Reservation> getAllReservations() {
        EntityManager em = emf.createEntityManager();
        List<Reservation> reservation = em.createNamedQuery("Reservation.getAll", Reservation.class).getResultList();
        em.close();
        return reservation;
    }

    public List<Reservation> getAllReservationsByCustomer(Customer customer) {
        EntityManager em = emf.createEntityManager();
        List<Reservation> reserves = em.createQuery("SELECT r FROM Reservation r JOIN FETCH r.customer WHERE r.customer.id= :d", Reservation.class).setParameter("d", customer.getId()).getResultList();
	em.close();
        return reserves;
        /*
        for (Reservation reservation : reserves){
            if(reservation)
            
        }*/
        
        
    }

    public List<Reservation> getAllReservationsByExcursion(Excursion excursion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Reservation> updateReservation(Reservation reservacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteReservation(Reservation reservacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BigDecimal getFullPriceByCustomer(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeExcursionFromAllReservations(Excursion excursion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
