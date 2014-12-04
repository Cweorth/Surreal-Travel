package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Tomáš Kácel
 */
public interface ReservationService {

    /**
     * save trip
     * @param reservationDTO  the DTO of reservation
     */
    
    void addReservation(ReservationDTO reservationDTO);

    /**
     * delete reservation from databaze
     * @param reservationDTO the reservation to delete
     */
    
    void deleteReservation(ReservationDTO reservationDTO);

    /**
     * return list of al reservation
     * @return list of reservation
     */
    List<ReservationDTO> getAllReservations();

    /**
     * return list of all reservation for selected customer
     * @param customerDTO the customer for we need the reservations
     * @return
     */
    List<ReservationDTO> getAllReservationsByCustomer(CustomerDTO customerDTO);

    /**
     * get list with all reservation with selected excursion
     * @param excursionDTO the selected excursion we want all reservation from there
     * @return
     */
    List<ReservationDTO> getAllReservationsByExcursion(ExcursionDTO excursionDTO);

    /**
     * get full price to pay for one customer
     * @param customerDTO the customer, for him we calculate the price
     * @return
     */
    BigDecimal getFullPriceByCustomer(CustomerDTO customerDTO);

    /**
     * get all reservation with selected id
     * @param id the id of reservation
     * @return result reservation with selected id
     */
    ReservationDTO getReservationById(long id);

    /**
     * remove excursion from all reservations
     * @param excursionDTO the reservation which must be remove
     */
    void removeExcursionFromAllReservations(ExcursionDTO excursionDTO);

    /**
     * update reservation data
     * @param reservationDTO the reservation with data to update
     */
    void updateReservation(ReservationDTO reservationDTO);
    /**
     * 
     * @param id 
     */
    void deleteReservationById(long id);
}
