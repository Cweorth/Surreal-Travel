package cz.muni.pa165.surrealtravel.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The trip entity.
 * @author Roman Lacko [396157]
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Trip.getAll",     query = "SELECT t FROM Trip t"),
    @NamedQuery(name = "Trip.getById",    query = "SELECT t FROM Trip t WHERE t.id = :id"),
    @NamedQuery(name = "Trip.removeById", query = "DELETE   FROM Trip t WHERE t.id = :id")
})
public class Trip implements Serializable {

    //--[  Private  ]-----------------------------------------------------------
    @Id
    @GeneratedValue
    private long id;

    @Temporal(TemporalType.DATE)
    private Date dateFrom;

    @Temporal(TemporalType.DATE)
    private Date dateTo;

    @Column(nullable = false)
    private String destination;

    @Column
    private int capacity;

    @Column
    private BigDecimal basePrice;

    @ManyToMany
    private List<Excursion> excursions;

    //--[  Constructors  ]------------------------------------------------------

    public Trip() {
        excursions = new ArrayList<>();
    }

    //--[  Methods  ]-----------------------------------------------------------

    /**
     * Adds an excursion to the trip.
     * @param  excursion     The excursion to add.
     */
    public void addExcursion(Excursion excursion) {
        excursions.add(Objects.requireNonNull(excursion, "excursion"));
    }

    /**
     * Removes an excursion from the trip.
     * @param  excursion     The excursion to remove.
     */
    public void removeExcursion(Excursion excursion) {
        excursions.remove(Objects.requireNonNull(excursion, "excursion"));
    }

    /**
     * Calculates the total price of the trip including all the excursions.
     * @return The total price of the trip.
     */
    public BigDecimal getFullPrice() {
        BigDecimal price = basePrice;

        for(Excursion e : excursions) {
            price = price.add(e.getPrice());
        }

        return price;
    }

    //<editor-fold desc="[  Getters | Setters  ]" defaultstate="collapsed">

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
    }

    //</editor-fold>

    //<editor-fold desc="[  Object methods     ]" defaultstate="collapsed">

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 17 * hash + (this.dateFrom    != null ? this.dateFrom.hashCode()    : 0);
        hash = 17 * hash + (this.dateTo      != null ? this.dateTo.hashCode()      : 0);
        hash = 17 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 17 * hash +  this.capacity;
        hash = 17 * hash + (this.basePrice   != null ? this.basePrice.hashCode()   : 0);
        hash = 17 * hash + (this.excursions  != null ? this.excursions.hashCode()  : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        final Trip other = (Trip) obj;

        return (id       == other.id)
            && (capacity == other.capacity)
            && (Objects.equals(basePrice,   other.basePrice))
            && (Objects.equals(dateFrom,    other.dateFrom))
            && (Objects.equals(dateTo,      other.dateTo))
            && (Objects.equals(destination, other.destination))
            && (Objects.equals(excursions,  other.excursions));
    }

    @Override
    public String toString() {
        return "Trip[id="      + id         + ", dateFrom="    + dateFrom
             + ", dateTo="     + dateTo     + ", destination=" + destination
             + ", capacity="   + capacity   + ", basePrice="   + basePrice
             + ", excursions=" + excursions + ']';
    }

    //</editor-fold>

}
