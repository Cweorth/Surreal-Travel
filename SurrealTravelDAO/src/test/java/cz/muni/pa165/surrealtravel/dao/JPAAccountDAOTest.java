/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractPersistenceTest;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.entity.Account;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;

import org.junit.Test;

import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Tomáš Kácel [359965]
 */
@Transactional
public class JPAAccountDAOTest  extends AbstractPersistenceTest {
    
    public JPAAccountDAOTest() {
    }
    
  @Autowired
  private AccountDAO accountDao;
  
  @Autowired
  private CustomerDAO customerDao;
  
 
    
    
     @Test(expected = NullPointerException.class)
     public void testAddAccountNull(){
         accountDao.addAccount(null);
     }
     
     @Test
     public void addAccountNullCustomer(){
        Account act= new Account();
        act.setCustomer(null);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Karel");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        
        accountDao.addAccount(act);
     }
     
    @Test
    public void testAddAccount() {
        Customer customer = mkcustomer("Some girls", "there");
        Account act= new Account();
        act.setCustomer(customer);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Karel");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        
        accountDao.addAccount(act);
        Account retrieved = em.find(Account.class, act.getId());
        assertEquals(act, retrieved);
    }
    
    @Test
    public void getAccountById() {
        Customer customer = mkcustomer("Some dude", "not here");
        Account act= new Account();
        act.setCustomer(customer);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Karel");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        accountDao.addAccount(act);
        long id = act.getId();
        Account retriew = accountDao.getAccountById(id);
        assertEquals(act, retriew);
    }
    
    @Test(expected = NullPointerException.class)
    public void getAccountByUsernameNull(){
       accountDao.getAccountByUsername(null);
    }
    
    @Test
    public void getAccountByUsername() {
        Customer customer = mkcustomer("Some dudes", "here");
        Account act= new Account();
        act.setCustomer(customer);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Karel");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        customerDao.addCustomer(customer);
        accountDao.addAccount(act);
        
        Account retrieved=accountDao.getAccountByUsername(act.getUsername());
        assertEquals(act, retrieved);
        
        
    }
    
    @Test(expected = NullPointerException.class)
    public void getAccountByCustomerNull(){
        accountDao.getAccountByCustomer(null);
    }
    
    @Test
    public void getAccountByCustomer() {
        Customer customer = mkcustomer("Peter", "Brno");
        Account act= new Account();
        act.setCustomer(customer);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Petr");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        customerDao.addCustomer(customer);
        accountDao.addAccount(act);
        List<Account> retrieved= accountDao.getAccountByCustomer(customer);
        if(retrieved.size() !=1){
        fail();
        }
        else{
            assertEquals(act, retrieved.get(0));
        }
    }
       
    @Test
    public void getAllAccounts() {
      List<Account> list= prepareAccountBatch();
      for(Account act:list){
          customerDao.addCustomer(act.getCustomer());
          accountDao.addAccount(act);
      }
      List<Account> retrieved= accountDao.getAllAccounts();
        //assertEquals(list.size(), retrieved.size());
      for(Account account:list){
          Assert.assertTrue(retrieved.contains(account));
      }
        
    }  
    
    @Test
     public void deleteAccountById() {
         List<Account> acts= prepareAccountBatch();
         customerDao.addCustomer(acts.get(0).getCustomer());
         accountDao.addAccount(acts.get(0));
         Long id= acts.get(0).getId();
         accountDao.deleteAccountById(id);
         Assert.assertFalse(accountDao.getAllAccounts().contains(acts.get(0)));    
    }
    
    @Test(expected = NullPointerException.class)
    public void updateAccountNull(){
        accountDao.updateAccount(null);
    }
     
    @Test 
    public void updateAccount() {
        List<Account> acts= prepareAccountBatch();
        for(Account act:acts){
          customerDao.addCustomer(act.getCustomer());
          accountDao.addAccount(act);
      }
      List<Account> actsBeforeUpdate= accountDao.getAllAccounts();
      Set<UserRole> r= new HashSet<>();
      r.add(UserRole.ROLE_ROOT);
      for(Account account: actsBeforeUpdate){
          
          account.setRoles(r);
          accountDao.updateAccount(account);
      }
      List<Account> actsAfterUpdate= accountDao.getAllAccounts();
      
      for(Account a : actsAfterUpdate) {
            Assert.assertEquals(r, a.getRoles());
        }
        
    } 
     
     
    /*
    * prepare list of Account for some testing
    * @return list of account
    */
    private List<Account> prepareAccountBatch(){
        List<Account> list= new ArrayList<>();
        Customer customer = mkcustomer("Peters", "Brno");
        Account act= new Account();
        act.setCustomer(customer);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Petr3");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        
        Customer customer2 = mkcustomer("Karel", "Praha");
        Account act2= new Account();
        act2.setCustomer(customer);
        act2.setPassword("pass");
        act2.setPlainPassword("pass");
        act2.setUsername("Karel5");
        Set<UserRole> r2= new HashSet<>();
        r2.add(UserRole.ROLE_STAFF);
        act2.setRoles(r2);
        
        Customer customer3 = mkcustomer("Karel", "Prerov");
        Account act3= new Account();
        act3.setCustomer(customer);
        act3.setPassword("pass");
        act3.setPlainPassword("pass");
        act3.setUsername("Karel12");
        Set<UserRole> r3= new HashSet<>();
        r3.add(UserRole.ROLE_ADMIN);
        act3.setRoles(r3);
        list.add(act);
        list.add(act2);
        list.add(act3);
        return list;
        
    }

    

   
    
    
    
    

  
    
    
    

    
    
}
