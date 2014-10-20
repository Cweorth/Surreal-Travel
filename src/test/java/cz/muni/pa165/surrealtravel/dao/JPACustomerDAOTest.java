/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 *
 * @author Petr Dvořák [359819]
 */
public class JPACustomerDAOTest extends AbstractTest {
    
     private List<Customer> customers;
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
    
        @Test (expected = IllegalArgumentException.class)
        public void getCustomerByEmptyName(){
        dao.getCustomerByName("");
        }
        
        @Test //(expected = IllegalArgumentException.class)
        public void getCustomerByName(){
        Customer customer1= mkcustomer("Josh","Brno");
        Customer customer2= mkcustomer("Nick","Bratislava");
        Customer customer3= mkcustomer("Josh","Praha");
        customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer3);
        em.getTransaction().begin();
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        for(Customer customer : customers) {        
            dao.addCustomer(customer);
        }
        em.getTransaction().commit();
        
        String name = customer1.getName();
        List<Customer> actual = dao.getCustomerByName(name);
        
//        List<Customer> actual = em.createQuery("SELECT c FROM Customer c WHERE c.name = :name", Customer.class)
//            .setParameter("name", name)
//            .getResultList();
        
        Assert.assertEquals(customers.size(),actual.size());
       
        em.getTransaction().begin();
        em.remove(customer1);
        em.remove(customer2);
        em.remove(customer3);
        em.getTransaction().commit();
    }
    
    @Test
    public void testUpdateCustomer() {
        Customer customer1= mkcustomer("Josh","Brno");
        Customer customer2= mkcustomer("Nick","Bratislava");
        Customer customer3= mkcustomer("Jack","Praha");
        
        em.getTransaction().begin();
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        em.getTransaction().commit();
        
        String name = customer1.getName();
        String address = customer2.getAddress();
        
        em.getTransaction().begin();
        customer3.setName(name);
        customer3.setAddress(address);
        em.persist(customer3);
        em.getTransaction().commit();
               
        assertEquals(customer3.getName(),name);
        assertEquals(customer3.getAddress(),address);
        
        em.getTransaction().begin();
        em.remove(customer1);
        em.remove(customer2);
        em.remove(customer3);
        em.getTransaction().commit();
    }    
        
    @Test
    public void testDeleteCustomer() {
        Customer customer1= mkcustomer("Josh","Brno");
        Customer customer2= mkcustomer("Josh","Bratislava");
        Customer customer3= mkcustomer("Josh","Praha");
        
        em.getTransaction().begin();
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        dao.deleteCustomer(customer2);
        em.getTransaction().commit();
       
        String name = customer1.getName();
        List<Customer> actual = dao.getCustomerByName(name);        
        
        Assert.assertEquals(dao.getAllCustomers(),actual);
    }
    
    
 }


