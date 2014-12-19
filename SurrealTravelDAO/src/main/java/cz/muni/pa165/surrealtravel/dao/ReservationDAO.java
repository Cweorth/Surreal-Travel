package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.List;

/**
 * Reservation access interface.
 *
 * @author Tomáš Kácel [359965]
 */
public interface ReservationDAO {

    /**
     * Store new reservation.
     * @param reservation to store in database
     */
    public void addReservation(Reservation reservation);

    /**
     * Get the reservation with the given id.
     * @param id the id in databaze
     * @return Reservation
     */
    public Reservation getReservationById(long id);

    /**
     * Get all reservations.
     * @return list of Resefvation
     */
    public List<Reservation> getAllReservations();

    /**
     * Get all reservations of the given customer.
     * @param customer customer to search for
     * @return the list of reservations
     */
    public List<Reservation> getAllReservationsByCustomer(Customer customer);

    /**
     * Update the reservation.
     * @param reservation reservation to update
     * @return updated reservation
     */
    public Reservation updateReservation(Reservation reservation);

    /**
     * Get the total price from all reservations by one customer
     * @param customer customer to search for
     * @return the total price
     */
    public BigDecimal getFullPriceByCustomer(Customer customer);

    /**
     * Removes the reservation with the given id.
     * @param id id of the reservation to remove
     */
    public void deleteReservationById(long id);
}
