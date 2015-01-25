package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import java.util.List;

/**
 * Customer service interface.
 * @author Jan Klimeš
 */
public interface CustomerService {

    /**
     * Create new customer entry.
     * @param customer  the customer to be added.
     */
    public void addCustomer(CustomerDTO customer);

    /**
     * Get customer DTO by id.
     * @param id        the id of an customer to find.
     * @return the customer
     */
    public CustomerDTO getCustomerById(long id);

    /**
     * Get list of all customer DTOs.
     * @return the list of customers
     */
    public List<CustomerDTO> getAllCustomers();

    /**
     * Update customer entry for the given DTO.
     * @param customer  the customer to update.
     * @return the updated customer
     */
    public CustomerDTO updateCustomer(CustomerDTO customer);

    /**
     * Delete customer entry for the given DTO.
     * @param id        the id of an customer to delete.
     */
    public void deleteCustomerById(long id);

}
