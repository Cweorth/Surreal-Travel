package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.CustomerDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import org.dozer.DozerBeanMapper;
import org.dozer.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
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
    private PlatformTransactionManager txManager;
    @Autowired
    private DozerBeanMapper mapper;

    public CustomerService() {
    }
    
    @Transactional
    public void addCustomer(CustomerDTO customer) {
        TransactionStatus ts = null;
        try {
            ts = txManager.getTransaction(null);
            customerDao.addCustomer(mapper.map(customer, Customer.class));
            txManager.commit(ts);
        } catch(NullPointerException | IllegalArgumentException | MappingException e) {
            // TODO log
            if(ts != null) txManager.rollback(ts);
            throw new DataAccessException("Error when creating a new customer.", e) {};
        } catch(TransactionException e) {
            // TODO log
            throw new DataAccessException("Error when creating a new customer.", e) {};
        }
    }

    public void setCustomerDao(CustomerDAO customerDao) {
        this.customerDao = customerDao;
    }
    
    public void setTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }
    
}
