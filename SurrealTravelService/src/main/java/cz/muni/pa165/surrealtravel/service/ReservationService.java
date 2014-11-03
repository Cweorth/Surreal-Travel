/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.ReservationDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Tomáš Kácel [359965]
 */
@Service
@Transactional
public class ReservationService {
  @Autowired
  private ReservationDAO reservationDAO;
  @Autowired
  private DozerBeanMapper mapper;
  
  @Transactional
  public void addReservation(ReservationDTO reservationDTO){
      if (reservationDTO == null) {
            throw new IllegalArgumentException("reservation can't be null");
        }
      validateReservation(reservationDTO );
      Reservation reservation=mapper.map(reservationDTO, Reservation.class);
      reservationDAO.addReservation(reservation);
      reservationDTO.setId(reservation.getId());
  }
  
  public ReservationDTO getReservationById(long id){
      Reservation reservation = reservationDAO.getReservationById(id);
      ReservationDTO result = mapper.map(reservation,ReservationDTO.class);
      return result;
  }
  
  public List<ReservationDTO> getAllReservations(){
      List<ReservationDTO> result = new ArrayList<>();
      List<Reservation> reservations = reservationDAO.getAllReservations();
      for (Reservation reservation : reservations) {
            result.add(mapper.map(reservation,ReservationDTO.class));
        }
      return result;
  }
  
  public List<ReservationDTO> getAllReservationsByCustomer(CustomerDTO customerDTO){
      List<ReservationDTO> result = new ArrayList<>();
      //Reservation reservation=mapper.map(reservationDTO, Reservation.class);
      Customer customer=mapper.map(customerDTO, Customer.class);
      List<Reservation> reservations = reservationDAO.getAllReservationsByCustomer(customer);
      for (Reservation reservation : reservations) {
            result.add(mapper.map(reservation,ReservationDTO.class));
        }
      return result;
  }
  
  public List<ReservationDTO> getAllReservationsByExcursion(ExcursionDTO excursionDTO){
      List<ReservationDTO> result = new ArrayList<>();
      Excursion excursion=mapper.map(excursionDTO, Excursion.class);
      List<Reservation> reservations = reservationDAO.getAllReservationsByExcursion(excursion);
      for (Reservation reservation : reservations) {
            result.add(mapper.map(reservation,ReservationDTO.class));
        }
      return result;
  }
  
  @Transactional
  public void updateReservation(ReservationDTO reservationDTO){
      if (reservationDTO == null) {
            throw new IllegalArgumentException("reservationDTO to update can't be null");
        }
     validateReservation(reservationDTO);
     Reservation reservation=mapper.map(reservationDTO, Reservation.class);
     reservationDAO.updateReservation(reservation);
      
  }
  
  @Transactional
  public void deleteReservation(ReservationDTO reservationDTO){
      if (reservationDTO == null) {
            throw new IllegalArgumentException("reservationDTO to update can't be null");
        }
      Reservation reservation=mapper.map(reservationDTO, Reservation.class);
      reservationDAO.deleteReservation(reservation);
  }
  
  @Transactional
  public BigDecimal getFullPriceByCustomer(CustomerDTO customerDTO){
      Customer customer=mapper.map(customerDTO, Customer.class);
      BigDecimal result= reservationDAO.getFullPriceByCustomer(customer);
      return result;
  }
  
  @Transactional
  public void removeExcursionFromAllReservations(ExcursionDTO excursionDTO){
      if (excursionDTO == null) {
            throw new IllegalArgumentException("excursionDTO to update can't be null");
        }
      Excursion excursion=mapper.map(excursionDTO, Excursion.class);
      reservationDAO.removeExcursionFromAllReservations(excursion);
  }

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
