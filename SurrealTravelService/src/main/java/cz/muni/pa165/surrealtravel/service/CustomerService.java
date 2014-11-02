package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.CustomerDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customer service class.
 * @author Jan Klimeš [374259]
 */
@Service
@Transactional
public class CustomerService {
    
    @Autowired
    private CustomerDAO customerDao;

    @Autowired
    private DozerBeanMapper mapper;

    /**
     * Create new customer entry.
     * @param customer 
     */
    @Transactional
    public void addCustomer(CustomerDTO customer) {
        Objects.requireNonNull(customer, "Customer data transfer object is null.");
        customerDao.addCustomer(mapper.map(customer, Customer.class));
    }
    
    /**
     * Get customer DTO by id.
     * @param id
     * @return 
     */
    public CustomerDTO getCustomerById(long id) {
        return mapper.map(customerDao.getCustomerById(id), CustomerDTO.class);
    }
    
    /**
     * Get list of customer DTOs with the given name.
     * @param name
     * @return 
     */
    public List<CustomerDTO> getCustomerByName(String name) {
        List<CustomerDTO> customersDTO = new ArrayList<>();
        for(Customer c : customerDao.getCustomerByName(name))
            customersDTO.add(mapper.map(c, CustomerDTO.class));
        return customersDTO;
    }
    
    /**
     * Get list of all customer DTOs.
     * @return 
     */
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customersDTO = new ArrayList<>();
        for(Customer c : customerDao.getAllCustomers())
            customersDTO.add(mapper.map(c, CustomerDTO.class));
        return customersDTO;
    }
    
    /**
     * Update customer entry for the given DTO.
     * @param customer
     * @return 
     */
    @Transactional
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        Objects.requireNonNull(customer, "Customer data transfer object is null.");
        Customer updated = customerDao.updateCustomer(mapper.map(customer, Customer.class));
        return mapper.map(updated, CustomerDTO.class);
    }
    
    /**
     * Delete customer entry for the given id.
     * @param customer 
     */
    @Transactional
    public void deleteCustomer(CustomerDTO customer) {
        Objects.requireNonNull(customer, "Customer data transfer object is null.");
        customerDao.deleteCustomer(mapper.map(customer, Customer.class));
    }
    
    /**
     * Delete customer entry for the given DTO.
     * @param id 
     */
    @Transactional
    public void deleteCustomerById(long id) {
        customerDao.deleteCustomerById(id);
    }

    public void setCustomerDao(CustomerDAO customerDao) {
        this.customerDao = customerDao;
    }
    
    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }
    
}
