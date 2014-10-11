/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Customer;
import cz.muni.pa165.surrealtravel.entity.Reservation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Petr
 */
public class JPACustomerDAOTest extends AbstractTest {
    
     private JPACustomerDAO dao;
     
     @Override
     public void setUp() {
        super.setUp();
        dao = new JPACustomerDAO(em);
    }
    
     
    @Test(expected = NullPointerException.class)
    public void testAddNullCustomer() {
        dao.addCustomer(null);
    }
     
    @Test(expected = NullPointerException.class)
    public void testNullName() {
        Customer customer1= mkcustomer(null,"Brno");
        dao.addCustomer(customer1);
    }
    
    @Test
    public void getCustomerById(){
        Customer customer1= mkcustomer("Josh","Brno");
        em.getTransaction().begin();
        em.persist(customer1);
        Long id = customer1.getId();
        em.getTransaction().commit();
                
        Customer newCustomer=dao.getCustomerById(id);
     
        assertEquals(newCustomer.getId(),customer1.getId());
        em.getTransaction().begin();
        em.remove(customer1);
        em.getTransaction().commit();
        
        
    }
    
}
