package cz.muni.pa165.surrealtravel.entity;

import cz.muni.pa165.surrealtravel.dto.UserRole;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Account entity.
 * @author Jan Klimeš [374259]
 */
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    @Size(min = 4, max = 32)
    @Pattern(regexp = "[a-z0-9]+")
    private String username;

    @Column(nullable = false)
    private String password; // hash of the password

    @Transient
    @Size(min = 4, max = 32)
    private String plainPassword; // not persisted, used only for purposes of password length

    @OneToOne
    private Customer customer;

    @ElementCollection(targetClass = UserRole.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ACCOUNT_ROLES")
    @Column(name = "AUTHORITY", nullable = false)
    private Set<UserRole> roles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.username);
        hash = 59 * hash + Objects.hashCode(this.password);
        hash = 59 * hash + Objects.hashCode(this.customer);
        hash = 59 * hash + Objects.hashCode(this.roles);
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
        final Account other = (Account) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.customer, other.customer)) {
            return false;
        }
        return Objects.equals(this.roles, other.roles);
    }

    @Override
    public String toString() {
        return "Account[" + "id=" + id + ", username=" + username + ", password=" + password + ", plainPassword=" + plainPassword + ", customer=" + customer + ", roles=" + roles + ']';
    }

}
