package cz.muni.pa165.surrealtravel.entity;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author Tomáš Kácel [359965]
 */
public class Reservation {
    //private methods
    @Id
    @GeneratedValue
    private long id = 0;
    
    @Column(nullable = false)
    private Customer customer;
    
    @Column(nullable = false)
    private Trip trip;
    
    @Column(nullable = false)//empty list means no excursion but must be list
    private List<Excursion> excursions;
    
    //methods
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public Customer getCustomer(){
        return customer;
    }
    
    public void setCustomer(Customer customer){
        this.customer= customer;
    }

    public Trip getTrip() {
        return trip;
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
    }
    
    public void addExcursion(Excursion excursion){
        this.excursions.add(excursion);
    }
    
    public void removeExcursion(Excursion excursion){
        this.excursions.remove(excursion);
    }
    
    public BigDecimal getTotalPrice(){
        BigDecimal base= this.getTrip().getBasePrice();
        List<Excursion> excursio=this.getExcursions();
        //if(excursions.isEmpty()){return base;}
        for(Excursion ext:excursio){
          base.add(ext.getPrice());
        }
        return base;
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reservation other = (Reservation) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.trip != other.trip && (this.trip == null || !this.trip.equals(other.trip))) {
            return false;
        }
        if (this.excursions != other.excursions && (this.excursions == null || !this.excursions.equals(other.excursions))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Reservation{" + "id=" + id + ", customer=" + customer + ", trip=" + trip + ", excursions=" + excursions + '}';
    }
    
    
    
    
    
}