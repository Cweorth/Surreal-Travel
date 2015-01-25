package cz.muni.pa165.surrealtravel.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Excursion data transfer object.
 * @author Petr Dvorak [359819]
 */

public class ExcursionDTO implements Serializable{

    private long id = 0;
    private Date excursionDate;
    private Integer duration;
    private String description;
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
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
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
        ExcursionDTO other = (ExcursionDTO) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.valueOf(id); // needs to be just ID in order to be reconginzed by spring for selecting of active items in forms
    }
}
