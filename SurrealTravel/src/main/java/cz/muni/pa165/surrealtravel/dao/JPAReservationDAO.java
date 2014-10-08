/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Kiclus
 */
public class JPAReservationDAO implements ReservationDAO {

    public void addReservation(Reservation reservation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Reservation getReservationById(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Reservation> getAllReservations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Reservation> getAllReservationsByCustomer(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Reservation> getAllReservationsByExcursion(Excursion excursion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Reservation> updateReservation(Reservation reservacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void deleteReservation(Reservation reservacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BigDecimal getFullPriceByCustomer(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void removeExcursionFromAllReservations(Excursion excursion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
