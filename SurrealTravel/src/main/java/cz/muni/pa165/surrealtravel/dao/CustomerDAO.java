package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.List;

/**
 *
 * @author Jan Klimeš [374259]
 */
public interface CustomerDAO {
    
    /**
     * Store new customer.
     * @param customer 
     */
    public void addCustomer(Customer customer) throws NullPointerException;
    
    /**
     * Get the customer with the given id.
     * @param id
     * @return 
     */
    public Customer getCustomerById(long id) throws IllegalArgumentException;
    
    /**
     * Get the customer with the given name. More customer with the same name can
     * exist in the system.
     * @param name
     * @return 
     */
    public List<Customer> getCustomerByName(String name) throws NullPointerException, IllegalArgumentException;
    
    /**
     * Get all customers.
     * @return 
     */
    public List<Customer> getAllCustomers();
    
    /**
     * Modify the customer.
     * @param customer 
     */
    public void updateCustomer(Customer customer) throws NullPointerException;
    
    /**
     * Remove customer.
     * @param customer 
     */
    public void deleteCustomer(Customer customer) throws NullPointerException;
    
}
