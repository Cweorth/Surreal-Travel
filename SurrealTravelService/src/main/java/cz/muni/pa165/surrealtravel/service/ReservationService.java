/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.ReservationDAO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.entity.Reservation;
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
  

    private void validateReservation(ReservationDTO reservationDTO) {
        if (reservationDTO.getCustomer() == null) {
            throw new IllegalArgumentException("Customer must exist");
        }
        
    }
}
