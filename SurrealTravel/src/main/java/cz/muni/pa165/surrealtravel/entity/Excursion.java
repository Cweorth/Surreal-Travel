package cz.muni.pa165.surrealtravel.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author Petr Dvořák [359819]
 */
@Entity
public class Excursion implements Serializable{

    @Id
    @GeneratedValue
    private long id = 0;
    
    @Temporal(TemporalType.DATE)
    private Date excursionDate;
    
    private Integer duration;
    
    @Enumerated(EnumType.STRING)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String destination;
    
    private BigDecimal price;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Date getExcursionDate() {
        return excursionDate;
    }
    public void setExcursionDate(Date excursionDate) {
        this.excursionDate = excursionDate;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setId(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String geDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Excursion other = (Excursion) obj;
        if (id != other.id) {
            return false;
        }
        return true;

    }

    @Override
    public String toString() {
        return "Excursion [id=" + id + ", date=" + excursionDate + ", duration="+ duration+", description="+description+", destination"+destination+", price="+price;

    }
}
