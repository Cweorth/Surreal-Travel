package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.ReservationDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Tomáš Kácel [359965]
 */
@Service(value = "reservationService")
public class DefaultReservationService implements ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;
    @Autowired
    private DozerBeanMapper mapper;

    /**
     * Save the reservation.
     * @param reservationDTO the DTO of reservation
     */
    @Secured("ROLE_USER")
    @Transactional
    @Override
    public void addReservation(ReservationDTO reservationDTO) {
        validateReservation(reservationDTO);
        Reservation reservation = mapper.map(reservationDTO, Reservation.class);
        reservationDAO.addReservation(reservation);
        reservationDTO.setId(reservation.getId());
    }

    /**
     * Get all reservations with the given id.
     * @param id the id of the reservation
     * @return the reservation
     */
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    @Override
    public ReservationDTO getReservationById(long id) {
        Reservation reservation = reservationDAO.getReservationById(id);
        return reservation == null ? null : mapper.map(reservation, ReservationDTO.class);
    }

    /**
     * Return a list of all reservations.
     * @return list of reservations
     */
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    @Override
    public List<ReservationDTO> getAllReservations() {
        List<ReservationDTO> result = new ArrayList<>();
        List<Reservation> reservations = reservationDAO.getAllReservations();
        for (Reservation reservation : reservations) {
            result.add(mapper.map(reservation, ReservationDTO.class));
        }
        return result;
    }

    /**
     * Return a list of all reservations for the given customer.
     * @param customerDTO the customer to search for
     * @return the list of reservations
     */
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    @Override
    public List<ReservationDTO> getAllReservationsByCustomer(CustomerDTO customerDTO) {
        Objects.requireNonNull(customerDTO);
        List<ReservationDTO> result = new ArrayList<>();
        Customer customer = mapper.map(customerDTO, Customer.class);
        List<Reservation> reservations = reservationDAO.getAllReservationsByCustomer(customer);
        for (Reservation reservation : reservations) {
            result.add(mapper.map(reservation, ReservationDTO.class));
        }
        return result;
    }

    /**
     * Update the given reservation.
     * @param reservationDTO the reservation to update
     */
    @Secured("ROLE_STAFF")
    @Transactional
    @Override
    public void updateReservation(ReservationDTO reservationDTO) {
        validateReservation(reservationDTO);
        Reservation reservation = mapper.map(reservationDTO, Reservation.class);
        reservationDAO.updateReservation(reservation);

    }

    /**
     * Get the full price of all reservations of the given customer.
     * @param customerDTO the customer to search for
     * @return the full price
     */
    @Secured("ROLE_USER")
    @Transactional
    @Override
    public BigDecimal getFullPriceByCustomer(CustomerDTO customerDTO) {
        Objects.requireNonNull(customerDTO);
        Customer customer = mapper.map(customerDTO, Customer.class);
        BigDecimal result = reservationDAO.getFullPriceByCustomer(customer);
        return result;
    }

    /**
     * Delete reservation entry for the given id.
     * @param id the id of the reservation to delete
     */
    @Secured("ROLE_USER")
    @Transactional
    @Override
    public void deleteReservationById(long id) {
        reservationDAO.deleteReservationById(id);
    }    
    
    /**
     * Validate reservation DTO.
     * @param reservationDTO 
     */
    private void validateReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getCustomer() == null) {
            throw new IllegalArgumentException("Customer must exist");
        }
    }

    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

}
