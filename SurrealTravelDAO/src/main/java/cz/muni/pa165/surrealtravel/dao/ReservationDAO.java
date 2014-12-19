package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.List;

/**
 * Reservation acces interface
 * @author Tomáš Kácel [359965]
 */
public interface ReservationDAO {
    /**
     * Store new reservation.
     * @param  reservation to store in database
     */
    public void addReservation(Reservation reservation);
    
    /**
     * Get the reservation with the given id.
     * @param id the id in databaze
     * @return Reservation 
     */
    public Reservation getReservationById(long id);
    
    /**
     * get all reservations
     * @return list of Resefvation
     */
    public List<Reservation> getAllReservations();
    
    
    /**
     * get all reservations from one customer
     * @param customer
     * @return 
     */
    public List<Reservation> getAllReservationsByCustomer(Customer customer);
    
    /**
     * updatereservation
     * @param reservation
     * @return updated reservation
     */
    public Reservation updateReservation(Reservation reservation);
    
    
    /**
     * get end price from all ereservation by one customer
     * @param customer
     * @return decimal fullprice
     */
    public BigDecimal getFullPriceByCustomer(Customer customer);
      
    /**
     *
     * @param id
     */
    public void deleteReservationById(long id);
}
