package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
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
     * get all reservations wich have got this excursion
     * @param excursion
     * @return list of reservation with excrsion
     */
    public List<Reservation> getAllReservationsByExcursion(Excursion excursion);
    
    /**
     * updatereservation
     * @param reservation
     * @return updated reservation
     */
    public Reservation updateReservation(Reservation reservation);
    
    /**
     * Deletereservation from databaze
     * @param reservation 
     */
    public void deleteReservation(Reservation reservation);
    
    /**
     * get end price from all ereservation by one customer
     * @param customer
     * @return decimal fullprice
     */
    public BigDecimal getFullPriceByCustomer(Customer customer);
    
    /**
     * remove one kind of excursion from all reservations
     * @param excursion 
     */
    public void removeExcursionFromAllReservations(Excursion excursion);
    
    /**
     *
     * @param id
     */
    public void deleteReservationById(long id);
}
