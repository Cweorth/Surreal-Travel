package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import java.util.List;

/**
 * Interface to provide Account service.
 * @author Jan Klimeš [374259]
 */
public interface AccountService {
    
    /**
     * Store new account.
     * @param account 
     */
    public void addAccount(AccountDTO account);
    
    /**
     * Get the account with the given id.
     * @param id
     * @return the account
     */
    public AccountDTO getAccountById(long id);

    /**
     * Get all accounts.
     * @return the list of accounts
     */
    public List<AccountDTO> getAllAccounts();
    
    /**
     * Modify the account.
     * @param account 
     * @return the modified account
     */
    public AccountDTO updateAccount(AccountDTO account);
    
    /**
     * Remove account by id.
     * @param id 
     */
    public void deleteAccountById(long id);
    
}
