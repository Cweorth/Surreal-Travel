package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Account;
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
     * @return the account (username is unique, only one result at most)
     */
    public Account getAccountByUsername(String username);

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
