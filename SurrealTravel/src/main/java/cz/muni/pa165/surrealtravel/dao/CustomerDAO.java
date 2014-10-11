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
    public void addCustomer(Customer customer);
    
    /**
     * Get the customer with the given id.
     * @param id
     * @return 
     */
    public Customer getCustomerById(long id);
    
    /**
     * Get the customer with the given name. All customers with the given name are returned.
     * @param name
     * @return 
     */
    public List<Customer> getCustomerByName(String name);
    
    /**
     * Get all customers.
     * @return 
     */
    public List<Customer> getAllCustomers();
    
    /**
     * Modify the customer.
     * @param customer 
     */
    public void updateCustomer(Customer customer);
    
    /**
     * Remove customer.
     * @param customer 
     */
    public void deleteCustomer(Customer customer);
    
    /**
     * Remove customer by id.
     * @param id 
     */
    public void deleteCustomerById(long id);
    
}
