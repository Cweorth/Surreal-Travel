package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


/**
 * Implementacion of DAO for Reservation entity
 * @author Tomáš Kácel [359965]
 */
@Repository(value = "reservationDao")
public class JPAReservationDAO implements ReservationDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
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
        
        entityManager.persist(reservation);
        
    }
    
    @Override
    public Reservation getReservationById(long id) {
        
        if(id < 0) throw new IllegalArgumentException("problem in id- Id < 0");
        
        Reservation result = entityManager.find(Reservation.class, id);
        
        return result;
    }
    
    @Override
    public List<Reservation> getAllReservations() {
        
        List<Reservation> reservation = entityManager.createNamedQuery("Reservation.getAll", Reservation.class).getResultList();
        
        return reservation;
    }
    
    @Override
    public List<Reservation> getAllReservationsByCustomer(Customer customer) {
        
        List<Reservation> reserves = entityManager.createQuery("SELECT r FROM Reservation r JOIN FETCH r.customer WHERE r.customer.id= :d", Reservation.class).setParameter("d", customer.getId()).getResultList();
	
        return reserves;    
        
    }
    
    @Override
    public List<Reservation> getAllReservationsByExcursion(Excursion excursion) {
        
        if(excursion == null) throw new NullPointerException("excursion doesnt exist.");
        List<Reservation> reserves = entityManager.createQuery("SELECT r FROM Reservation r JOIN r.excursions e WHERE e.id= :d", Reservation.class)
                .setParameter("d", excursion.getId())
                .getResultList();
        
        return reserves;
    }
    
    @Override
    public Reservation updateReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        if(reservation.getId() < 0) throw new IllegalArgumentException("reservation id must be positiv number.");
        if(reservation.getCustomer() == null) throw new NullPointerException("customer in reservation is null.");
        if(reservation.getCustomer().getClass() != Customer.class ) throw new IllegalArgumentException("customer is not customer is empty string.");
        if(reservation.getTrip()==null) throw new NullPointerException("No trip added to reservatio");        
        
        Reservation merge = entityManager.merge(reservation);
        return merge;
        
    }
    
    @Override
    public void deleteReservation(Reservation reservation) {
        if(reservation == null) throw new NullPointerException("reservation doesnt exist.");
        
        entityManager.remove(reservation);
        
    }
    
    @Override
    public BigDecimal getFullPriceByCustomer(Customer customer) {
      if(customer==null) throw new NullPointerException("customer doesnt exist.");
      if(customer.getId() < 0) throw new IllegalArgumentException("customer id must be positiv number.");
      
      List<Reservation> reserv =getAllReservationsByCustomer(customer);
      BigDecimal dec= new BigDecimal(0);
      for(Reservation r: reserv){
          dec= dec.add(r.getTotalPrice());
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
    
    @Override
    public void deleteReservationById(long id){
        if (id < 0) throw new IllegalArgumentException("The id has a negative value");
        
        entityManager.createQuery("DELETE FROM Reservation e WHERE e.id = :id")
             .setParameter("id", id)
             .executeUpdate();
    }
    
}
