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
     * @return the customer
     */
    public Customer getCustomerById(long id);

    /**
     * Get all customers.
     * @return the list of customers
     */
    public List<Customer> getAllCustomers();

    /**
     * Modify the customer.
     * @param customer
     * @return the modified customer
     */
    public Customer updateCustomer(Customer customer);

    /**
     * Remove customer by id.
     * @param id
     */
    public void deleteCustomerById(long id);

}
