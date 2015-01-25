package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Implementation of DAO for Customer entity.
 * @author Jan Klimeš [374259]
 */
@Repository(value = "customerDao")
public class JPACustomerDAO implements CustomerDAO {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addCustomer(Customer customer) {

        if(customer == null) throw new NullPointerException("Customer object is null.");
        if(customer.getId() < 0) throw new IllegalArgumentException("Customer object is not valid - id < 0");
        if(customer.getName() == null) throw new NullPointerException("Name of customer object is null.");
        if(customer.getName().isEmpty()) throw new IllegalArgumentException("Name of customer is empty string.");

        em.persist(customer);

    }

    @Override
    public Customer getCustomerById(long id) {

        if(id < 0) throw new IllegalArgumentException("Id < 0");

        return em.find(Customer.class, id);

    }

    @Override
    public List<Customer> getAllCustomers() {

        return em.createNamedQuery("Customer.getAll", Customer.class).getResultList();

    }

    @Override
    public Customer updateCustomer(Customer customer) {

        if(customer == null) throw new NullPointerException("Customer object is null.");
        if(customer.getId() < 0) throw new IllegalArgumentException("Customer object is not valid - id < 0");
        if(customer.getName() == null) throw new NullPointerException("Name of customer object is null.");
        if(customer.getName().isEmpty()) throw new IllegalArgumentException("Name of customer is empty string.");

        return em.merge(customer);

    }

    @Override
    public void deleteCustomerById(long id) {

        if(id < 0) throw new IllegalArgumentException("Customer id < 0");

        em.createNamedQuery("Customer.removeById")
                .setParameter("id", id)
                .executeUpdate();

    }

}
