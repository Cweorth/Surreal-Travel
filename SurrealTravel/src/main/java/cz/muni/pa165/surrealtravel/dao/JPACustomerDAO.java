package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Implementation of DAO for Customer entity.
 * @author Jan Klimeš [374259]
 */
public class JPACustomerDAO implements CustomerDAO {
    
    public static final String PERSISTENCE_NAME = "Surreal-Travel";
    
    EntityManagerFactory emf;
    
    public JPACustomerDAO() {
        emf = Persistence.createEntityManagerFactory(JPACustomerDAO.PERSISTENCE_NAME);
    }

    public void addCustomer(Customer customer) {
        
        if(customer == null) throw new NullPointerException("Customer object is null.");
        if(customer.getId() < 0) throw new IllegalArgumentException("Customer object is not valid - id < 0");
        if(customer.getName() == null) throw new NullPointerException("Name of customer object is null.");
        if(customer.getName().isEmpty()) throw new IllegalArgumentException("Name of customer is empty string.");
        
        EntityManager em = emf.createEntityManager();
        em.persist(customer);
        em.close();
        
    }

    public Customer getCustomerById(long id) {
       
        if(id < 0) throw new IllegalArgumentException("Id < 0");
        
        EntityManager em = emf.createEntityManager();
        
        Customer customer = em.find(Customer.class, id);
        em.close();
        
        return customer;
        
    }

    public List<Customer> getCustomerByName(String name) {
        
        if(name == null) throw new NullPointerException("Name of customer object is null.");
        if(name.isEmpty()) throw new IllegalArgumentException("Name of customer is empty string.");
        
        List<Customer> customer;
        
        EntityManager em = emf.createEntityManager();
        
        customer = em.createNamedQuery("Customer.getByName", Customer.class)
                .setParameter(0, name)
                .getResultList();
        
        em.close();
        
        return customer;
        
    }

    public List<Customer> getAllCustomers() {
        
        EntityManager em = emf.createEntityManager();
        
        List<Customer> customer = em.createNamedQuery("Customer.getAll", Customer.class).getResultList();
        em.close();

        return customer;
        
    }

    public void updateCustomer(Customer customer) {
        
        if(customer == null) throw new NullPointerException("Customer object is null.");
        if(customer.getId() < 0) throw new IllegalArgumentException("Customer object is not valid - id < 0");
        if(customer.getName() == null) throw new NullPointerException("Name of customer object is null.");
        if(customer.getName().isEmpty()) throw new IllegalArgumentException("Name of customer is empty string.");
        
        EntityManager em = emf.createEntityManager();
        em.merge(customer);
        em.close();
        
    }

    public void deleteCustomer(Customer customer) {
        
        if(customer == null) throw new NullPointerException("Customer object is null.");
        if(customer.getId() < 0) throw new IllegalArgumentException("Customer object is not valid - id < 0");
        
        EntityManager em = emf.createEntityManager();
        em.remove(customer);
        em.close();
        
    }

    public void deleteCustomerById(long id) {
        
        if(id < 0) throw new IllegalArgumentException("Customer id < 0");
        
        EntityManager em = emf.createEntityManager();
        em.createNamedQuery("Customer.removeById")
                .setParameter(0, id)
                .executeUpdate();
        em.close();
        
    }
    
}
