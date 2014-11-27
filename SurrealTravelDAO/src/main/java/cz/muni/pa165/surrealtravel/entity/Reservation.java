package cz.muni.pa165.surrealtravel.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Tomáš Kácel [359965]
 */
@NamedQueries({
    @NamedQuery(name="Reservation.getAll",query="SELECT r FROM Reservation r"),
    @NamedQuery(name="Reservation.getAllByCustomer",query="SELECT c FROM Customer c WHERE c.name = :name"),
    //@NamedQuery(name="Customer.removeById",query="DELETE FROM Customer c WHERE c.id = :id"),
})
@Entity
public class Reservation implements Serializable {
    //private methods
    @Id
    @GeneratedValue
    private long id = 0;
    
    //@Column(nullable = false)
    @ManyToOne
    private Customer customer;
    
    
    //@Column(nullable = false)
    @ManyToOne
    private Trip trip;
    
    
    
    @ManyToMany
    private List<Excursion> excursions;
    
    //methods
    
    public Reservation() {
        excursions = new ArrayList<>();
    }
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
        if(excursion==null)throw new NullPointerException("nejede");
        this.excursions.add(excursion);
    }
    
    public void removeExcursion(Excursion excursion){
        this.excursions.remove(excursion);
    }
    
    public BigDecimal getTotalPrice(){
        BigDecimal base= this.getTrip().getBasePrice();
        //List<Excursion> excursio=this.getExcursions();
        //if(excursions.isEmpty()){return base;}
        BigDecimal dc=new BigDecimal(0);
        for(Excursion ext:getExcursions()){
          dc= dc.add(ext.getPrice());
        }
        dc=dc.add(base);
        return dc;
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