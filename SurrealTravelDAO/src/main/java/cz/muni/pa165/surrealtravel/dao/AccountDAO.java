package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Account;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.List;

/**
 * Interface to provide DAO functionality to Accounts.
 * @author Jan Klimeš [374259]
 */
public interface AccountDAO {
    
    /**
     * Store new account.
     * @param account 
     */
    public void addAccount(Account account);
    
    /**
     * Get the account with the given id.
     * @param id
     * @return the account
     */
    public Account getAccountById(long id);
    
    /**
     * Get the account with the given username.
     * @param username
     * @return the account (username is unique, only one result at most), null when not found
     */
    public Account getAccountByUsername(String username);
    
    /**
     * Get the account with the given Customer attached. There may be more Accounts with the same customer.
     * @param customer
     * @return account collection
     */
    public List<Account> getAccountByCustomer(Customer customer);

    /**
     * Get all accounts.
     * @return the list of accounts
     */
    public List<Account> getAllAccounts();
    
    /**
     * Modify the account.
     * @param account 
     * @return the modified account
     */
    public Account updateAccount(Account account);
    
    /**
     * Remove account by id.
     * @param id 
     */
    public void deleteAccountById(long id);
    
}
