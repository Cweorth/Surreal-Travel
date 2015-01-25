package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dao.AccountDAO;
import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.entity.Account;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    
    @Override
    @Transactional
    public void addAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account data transfer object is null.");
        
        // there can only be one root, and his account should already be
        // created at this point
        if (account.getRoles().contains(UserRole.ROLE_ROOT)) {
            throw new IllegalArgumentException("Cannot add another root account!");
        }
        
        checkRoles(account);
        Account entity = mapper.map(account, Account.class);
        accountDao.addAccount(entity);
        account.setId(entity.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDTO getAccountById(long id) {
        Account account = accountDao.getAccountById(id);
        return account == null ? null : mapper.map(account, AccountDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AccountDTO getAccountByUsername(String username) {
        Account account = accountDao.getAccountByUsername(username);
        return account == null ? null : mapper.map(account, AccountDTO.class);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountByCustomer(CustomerDTO customer) {
        Objects.requireNonNull(customer, "Customer data transfer object is null.");
        List<AccountDTO> accounts = new ArrayList<>();
        for(Account a : accountDao.getAccountByCustomer(mapper.map(customer, Customer.class)))
            accounts.add(mapper.map(a, AccountDTO.class));
        return accounts;
    }

    @Override
    @Secured("ROLE_USER")
    @Transactional(readOnly = true)
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accountsDTO = new ArrayList<>();
        for(Account a : accountDao.getAllAccounts())
            accountsDTO.add(mapper.map(a, AccountDTO.class));
        return accountsDTO;
    }

    @Override
    @Transactional
    @Secured("ROLE_USER")
    public void updateAccount(AccountDTO account) {
        Objects.requireNonNull(account, "Account data transfer object is null.");
        checkRoles(account);
        
        // user can edit only his own account
        if (   !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
            && !SecurityContextHolder.getContext().getAuthentication().getName().equals(account.getUsername())) {
            throw new IllegalArgumentException("Insufficient rights to edit the given account");
        }        
        
        // check that username was not changed
        AccountDTO original = getAccountById(account.getId());
        if (!original.getUsername().equals(account.getUsername())) {
            throw new IllegalArgumentException("Username cannot be changed!");
        }
        
        //    the account has ROLE_ROOT <=> it is the "root"
        AccountDTO root = getAccountByUsername("root");
        
        // 1) the account has ROLE_ROOT ==> it is the "root"
        if (account.getRoles().contains(UserRole.ROLE_ROOT) && (root.getId() != account.getId())) {
            throw new IllegalArgumentException("Cannot grant ROLE_ROOT to another account");
        }
        
        // 2) the account has ROLE_ROOT <== it is the "root"
        if ("root".equals(account.getUsername()) && !root.getRoles().equals(account.getRoles())) {
            throw new IllegalArgumentException("Cannot change root's permissions!");
        }
        
        accountDao.updateAccount(mapper.map(account, Account.class));
    }
    
    @Override
    @Transactional
    @Secured("ROLE_USER")
    public void deleteAccountById(long id) {
        AccountDTO account = getAccountById(id);
        if (account == null) {
            throw new IllegalArgumentException("Entity with id " + id + " not found");
        }
        
        // root account cannot not be deleted
        if (account.getRoles().contains(UserRole.ROLE_ROOT)) {
            throw new IllegalArgumentException("Cannot delete root account");
        }
        
        // user can delete only his own account
        if (   !SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
            && !SecurityContextHolder.getContext().getAuthentication().getName().equals(account.getUsername())) {
            throw new IllegalArgumentException("Insufficient rights to delete the given account");
        }

        accountDao.deleteAccountById(id);
    }
    
    /**
     * Check if Account's roles are set in a correct fashion.
     * That is, ROOT implies ADMIN, ADMIN implies STAFF and STAFF implies USER
     * @param account 
     */
    private void checkRoles(AccountDTO account) {
        if(account.getRoles().contains(UserRole.ROLE_ROOT))
            account.getRoles().add(UserRole.ROLE_ADMIN);
        
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
