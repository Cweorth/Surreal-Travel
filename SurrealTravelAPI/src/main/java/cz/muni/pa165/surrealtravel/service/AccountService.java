package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import java.util.List;

/**
 * Interface to provide Account service.
 * @author Jan Klimeš [374259]
 */
public interface AccountService {
    
    /**
     * Store new account.
     * @param account   the account to be stored.
     */
    public void addAccount(AccountDTO account);
    
    /**
     * Get the account with the given id.
     * @param id        the id of an account to find.
     * @return the account
     */
    public AccountDTO getAccountById(long id);
    
    /**
     * Get the account with the given username.
     * @param username  the username of an account to find.
     * @return the account (username is unique, only one result at most)
     */
    public AccountDTO getAccountByUsername(String username);
    
    /**
     * Get the account with the given Customer attached. There may be more Accounts with the same customer.
     * @param customer  the list of accounts with the given {@code customer} attached.
     * @return customer collection
     */
    public List<AccountDTO> getAccountByCustomer(CustomerDTO customer);

    /**
     * Get all accounts.
     * @return the list of accounts
     */
    public List<AccountDTO> getAllAccounts();
    
    /**
     * Modify the account.
     * @param account   the account to be updated.
     */
    public void updateAccount(AccountDTO account);
    
    /**
     * Remove account by id.
     * @param id        the id of an account to be deleted.
     */
    public void deleteAccountById(long id);
    
}
