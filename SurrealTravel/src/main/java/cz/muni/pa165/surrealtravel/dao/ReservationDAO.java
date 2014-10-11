package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tomáš Kácel [359965]
 */
public interface ReservationDAO {
    /**
     * Store new reservation.
     * @param  reservation
     */
    public void addReservation(Reservation reservation);
    
    /**
     * Get the reservation with the given id.
     * @param id 
     * @return reservation 
     */
    public Reservation getReservationById(long id);
    
    /**
     * get all reservations
     * @return 
     */
    public List<Reservation> getAllReservations();
    /**
     * find all reservation by customer
     * @param customer
     * @return 
     */
    
    /**
     * get all reservations from one customer
     * @param customer
     * @return 
     */
    public List<Reservation> getAllReservationsByCustomer(Customer customer);
    /**
     * 
     * @param excursion
     * @return 
     */
    public List<Reservation> getAllReservationsByExcursion(Excursion excursion);
    
    /**
     * updatereservation
     * @param reservacion
     * @return 
     */
    public Reservation updateReservation(Reservation reservation);
    
    /**
     * 
     * @param reservacion 
     */
    public void deleteReservation(Reservation reservation);
    
    /**
     * get end price from all ereservation by one customer
     * @param customer
     * @return 
     */
    public BigDecimal getFullPriceByCustomer(Customer customer);
    
    /**
     * remove one kind of excursion from all reservations
     * @param excursion 
     */
    public void removeExcursionFromAllReservations(Excursion excursion);
}
