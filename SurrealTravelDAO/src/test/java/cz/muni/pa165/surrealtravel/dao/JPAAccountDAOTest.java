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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author Kiclus
 */
public class JPAAccountDAOTest extends AbstractPersistenceTest  {
    
    public JPAAccountDAOTest() {
    }
    
  @Autowired
  private AccountDAO accountDao;
    
    
    /**
     * Test of addAccount method, of class JPAAccountDAO.
     */
    @Test
    public void testAddAccount() {
        Customer customer = mkcustomer("Some dude", "not here");
        Account act= new Account();
        act.setCustomer(customer);
        //act.setId(id);
        act.setPassword("pass");
        act.setPlainPassword("pass");
        act.setUsername("Karel");
        Set<UserRole> r= new HashSet<>();
        r.add(UserRole.ROLE_USER);
        act.setRoles(r);
        
        accountDao.addAccount(act);
        assertTrue(act.getId() > 0);
        //Account retrieved = em.find(Account.class, act.getId());
        //assertEquals(act, retrieved);
    }

    
    
}
