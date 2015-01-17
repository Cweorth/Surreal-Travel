package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.CustomerDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customer service class.
 * @author Jan Klimeš [374259]
 */
@Service(value = "customerService")
public class DefaultCustomerService implements CustomerService {
    
    @Autowired
    private CustomerDAO customerDao;

    @Autowired
    private DozerBeanMapper mapper;

    @Transactional
    @Override
    public void addCustomer(CustomerDTO customer) {
        Objects.requireNonNull(customer, "Customer data transfer object is null.");
        Customer entity = mapper.map(customer, Customer.class);
        customerDao.addCustomer(entity);
        customer.setId(entity.getId());
    }
    
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    @Override
    public CustomerDTO getCustomerById(long id) {
        Customer customer = customerDao.getCustomerById(id);
        return customer == null ? null : mapper.map(customer, CustomerDTO.class);
    }
    
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customersDTO = new ArrayList<>();
        for(Customer c : customerDao.getAllCustomers())
            customersDTO.add(mapper.map(c, CustomerDTO.class));
        return customersDTO;
    }
    
    @Secured("ROLE_USER")
    @Transactional
    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        Objects.requireNonNull(customer, "Customer data transfer object is null.");
        Customer updated = customerDao.updateCustomer(mapper.map(customer, Customer.class));
        return mapper.map(updated, CustomerDTO.class);
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    @Override
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
