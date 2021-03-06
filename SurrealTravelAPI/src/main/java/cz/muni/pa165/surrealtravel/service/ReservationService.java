package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * The Reservation Service.
 * @author Tomáš Kácel
 */
public interface ReservationService {

    /**
     * Save the reservation.
     * @param reservationDTO the reservation to add.
     */

    void addReservation(ReservationDTO reservationDTO);

    /**
     * Get a list of all reservations.
     * @return list of all reservations
     */
    List<ReservationDTO> getAllReservations();

    /**
     * Get a list of all reservations for the given {@code customer}
     * @param customerDTO the customer we need reservations for.
     * @return the list of reservations
     */
    List<ReservationDTO> getAllReservationsByCustomer(CustomerDTO customerDTO);

    /**
     * Get full price of all reservations for the given {@code customer}.
     * @param customerDTO the customer
     * @return the price
     */
    BigDecimal getFullPriceByCustomer(CustomerDTO customerDTO);

    /**
     * Get the reservation with the given {@code id}.
     * @param id the id to search for
     * @return the reservation
     */
    ReservationDTO getReservationById(long id);

    /**
     * Update the reservation.
     * @param reservationDTO the reservation to update.
     */
    void updateReservation(ReservationDTO reservationDTO);

    /**
     * Remove the reservation with the given {@code id}.
     * @param id the id to search for.
     */
    void deleteReservationById(long id);
}
