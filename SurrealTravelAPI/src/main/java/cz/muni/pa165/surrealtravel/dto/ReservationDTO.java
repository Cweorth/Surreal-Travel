package cz.muni.pa165.surrealtravel.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * the DTO for reservation
 * @author Tomáš Kácel [359965]
 */
public class ReservationDTO {
    private long id=0;
    private CustomerDTO customer;
    private TripDTO trip;
    private List<ExcursionDTO> excursions;
    
    public ReservationDTO() {
        excursions = new ArrayList<>();
        
        
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public CustomerDTO getCustomer(){
        return customer;
    }
    
    public void setCustomer(CustomerDTO customer){
        this.customer= customer;
    }

    public TripDTO getTrip() {
        return trip;
    }

    public List<ExcursionDTO> getExcursions() {
        return excursions;
    }

    public void setTrip(TripDTO trip) {
        this.trip = trip;
    }

    public void setExcursions(List<ExcursionDTO> excursions) {
        this.excursions = excursions;
    }
    /**
     * add excursion to reservation
     * @param excursion to add
     */
    public void addExcursion(ExcursionDTO excursion){
        if(excursion==null)throw new NullPointerException("nejede");
        this.excursions.add(excursion);
    }
    /**
     * remove excursion form reservation
     * @param excursion to remove
     */
    public void removeExcursion(ExcursionDTO excursion){
        this.excursions.remove(excursion);
    }
    /**
     * method calculate total price for reservation
     * @return totalprice
     */
    public BigDecimal getTotalPrice(){
        BigDecimal base= this.getTrip().getBasePrice();
        BigDecimal dc=new BigDecimal(0);
        for(ExcursionDTO ext:getExcursions()){
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
        final ReservationDTO other = (ReservationDTO) obj;
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
