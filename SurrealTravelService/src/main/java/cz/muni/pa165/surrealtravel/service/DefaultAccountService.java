package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.AccountDAO;
import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.entity.Account;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default service for handling accounts.
 * @author Jan Klimeš [374259]
 */
@Service(value = "accountService")
public class DefaultAccountService implements AccountService {

    @Autowired
    private AccountDAO accountDao;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private DozerBeanMapper mapper;
    
    @Transactional
    @Override
    public void addAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account data transfer object is null.");
        checkRoles(account);
        Account entity = mapper.map(account, Account.class);
        accountDao.addAccount(entity);
        account.setId(entity.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public AccountDTO getAccountById(long id) {
        Account account = accountDao.getAccountById(id);
        return account == null ? null : mapper.map(account, AccountDTO.class);
    }
    
    @Transactional(readOnly = true)
    @Override
    public AccountDTO getAccountByUsername(String username) {
        Account account = accountDao.getAccountByUsername(username);
        return account == null ? null : mapper.map(account, AccountDTO.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accountsDTO = new ArrayList<>();
        for(Account a : accountDao.getAllAccounts())
            accountsDTO.add(mapper.map(a, AccountDTO.class));
        return accountsDTO;
    }

    @Transactional
    @Override
    public AccountDTO updateAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account data transfer object is null.");
        checkRoles(account);
        Account updated = accountDao.updateAccount(mapper.map(account, Account.class));
        return mapper.map(updated, AccountDTO.class);
    }

    @Transactional
    @Override
    public void deleteAccountById(long id) {
        AccountDTO account = getAccountById(id);
        if (account == null) {
            throw new IllegalArgumentException("Entity with id " + id + " not found");
        }
        
        // customer shall be removed as well if this account is the only one
        // that references it        
        if (account.getCustomer() != null) {
            boolean rmcustomer = true;
            List<Account> accounts = accountDao.getAllAccounts();
            
            Account other;
            for(Iterator<Account> it = accounts.iterator(); rmcustomer && it.hasNext(); ) {
                other = it.next();
                
                rmcustomer &= (other.getCustomer()         == null)
                           || (other.getId()               != account.getId())
                           || (other.getCustomer().getId() != account.getCustomer().getId());
            }
            
            
            if (rmcustomer) {
                // the customer is referenced only by this account
                CustomerDTO customer = account.getCustomer();
                for(ReservationDTO reservation : reservationService.getAllReservationsByCustomer(customer)) {
                    reservationService.deleteReservationById(reservation.getId());
                }
                
                customerService.deleteCustomerById(customer.getId());
            }
        }
        
        accountDao.deleteAccountById(id);
    }
    
    /**
     * Check if Account's roles are set in a correct fashion.
     * @param account 
     */
    private void checkRoles(AccountDTO account) {
        if(account.getRoles().contains(UserRole.ROLE_ADMIN))
            account.getRoles().add(UserRole.ROLE_STAFF);
        
        if(account.getRoles().contains(UserRole.ROLE_STAFF))
            account.getRoles().add(UserRole.ROLE_USER);
    }

    public void setAccountDao(AccountDAO accountDao) {
        this.accountDao = accountDao;
    }

    public void setMapper(DozerBeanMapper mapper) {
        this.mapper = mapper;
    }

}
