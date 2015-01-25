package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.AccountDAO;
import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.entity.Account;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author Petr Dvořák [359819]
 */
public class AccountServiceTest extends AbstractServiceTest{
    
    @Mock
    private AccountDAO     dao;
    @InjectMocks
    private DefaultAccountService service;
    
    private final List<AccountDTO>      accounts;
        
    /**
     * Constructor.
     */
    public AccountServiceTest() {
        super();
        
        String password = "pa165";
        AccountDTO a1 = new AccountDTO();
        AccountDTO a2 = new AccountDTO();
        a1.setId(2L);
        a1.setUsername("pa165");
        a1.setPlainPassword(password);
        a1.setRoles(EnumSet.of(UserRole.ROLE_ADMIN));
        password = "password";
        a2.setId(3L);
        a2.setUsername("username");
        a2.setPlainPassword(password);
        a2.setRoles(EnumSet.of(UserRole.ROLE_USER));
        
        accounts = Arrays.asList(a1,a2);
         
    }
    
    @Test(expected = NullPointerException.class)
    public void createNullAccountTest() {
        service.addAccount(null);
    }
    
    @Test
    public void addAccountTest(){
         for(AccountDTO account : accounts) {
            service.addAccount(account);
        }
         verify(dao, times(accounts.size())).addAccount(any(Account.class));
    }
    
    @Test
    public void getAccountByIdTest(){
        long uniqueId = 2L;
        
        AccountDTO expected = accounts.get(0);
        Account entity = mapper.map(expected, Account.class);
       
        when(dao.getAccountById(uniqueId)).thenReturn(entity);
        AccountDTO retrieved = service.getAccountById(uniqueId);
        
        verify(dao, times(1)).getAccountById(Matchers.eq(uniqueId));
        assertNotNull(retrieved);
        assertEquals(uniqueId, retrieved.getId());
        assertEquals(expected, retrieved);
        
    }
    
    @Test
    public void getAccountByUsernameTest(){
        String uniqueName = "pa165";
        
        AccountDTO expected = accounts.get(0);
        Account entity = mapper.map(expected, Account.class);
       
        when(dao.getAccountByUsername(uniqueName)).thenReturn(entity);
        AccountDTO retrieved = service.getAccountByUsername(uniqueName);
        
        verify(dao, times(1)).getAccountByUsername(Matchers.eq(uniqueName));
        assertNotNull(retrieved);
        assertEquals(uniqueName, retrieved.getUsername());
        assertEquals(expected, retrieved);
    }
    
    @Test
    public void getAllAccountsTest(){
        List<Account> entities = new ArrayList<>();
        for(AccountDTO e : accounts)
            entities.add(mapper.map(e, Account.class));
        
        when(dao.getAllAccounts()).thenReturn(entities);
        List<AccountDTO> retrieved = service.getAllAccounts();
        
        verify(dao, times(1)).getAllAccounts();
        assertTrue(retrieved.size() == accounts.size());
        assertEquals(accounts, retrieved); 
    }

}
