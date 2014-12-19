package cz.muni.pa165.surrealtravel.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Customer entity.
 * @author Jan Klimeš [374259]
 */
@NamedQueries({
    @NamedQuery(name="Customer.getAll",query="SELECT c FROM Customer c"),
    @NamedQuery(name="Customer.removeById",query="DELETE FROM Customer c WHERE c.id = :id"),
})
@Entity
public class Customer implements Serializable {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String address;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.address != null ? this.address.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return !((this.address == null) ? (other.address != null) : !this.address.equals(other.address));
    }

    @Override
    public String toString() {
        return "Customer[" + "id=" + id + ", name=" + name + ", address=" + address + ']';
    } 
    
}
