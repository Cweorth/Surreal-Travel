/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.service;

import cz.muni.pa165.surrealtravel.AbstractServiceTest;
import cz.muni.pa165.surrealtravel.dao.CustomerDAO;
import cz.muni.pa165.surrealtravel.dao.JPACustomerDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import org.mockito.MockitoAnnotations.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests for customer service class
 * @author Tomáš Kácel
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest extends AbstractServiceTest {
    @Mock
    private CustomerDAO dao;
    @InjectMocks
    private CustomerService service;
    
    public CustomerServiceTest() {
        super();
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddCustomerNull() {
        service.addCustomer(null);
    }
    
    @Test
    public void testAddCustomer() {
        //System.out.println("addCustomer");
        List<CustomerDTO> customers = makeCustomerDTO();
        for(CustomerDTO customer : customers) {
            try {
                service.addCustomer(customer);
            } catch(IllegalArgumentException e) {
                Assert.fail();
            }
        }
        verify(dao, times(customers.size())).addCustomer(any(Customer.class));
      
    }
    @Test
    public void getCustomerById(){
        Customer cus= makeCustomer();
        cus.setId(1L);
        List<CustomerDTO> customers = makeCustomerDTO();
        CustomerDTO exp= customers.get(0);
        
        when(dao.getCustomerById(1L)).thenReturn(cus);
        CustomerDTO ret=service.getCustomerById(1L);
        verify(dao, times(1)).getCustomerById(Matchers.eq(1L));
        assertNotNull(ret);
        assertEquals(1L, ret.getId());
        assertEquals(exp, ret);
    }
    @Test
    public void getCustomerByName(){
        Customer cus= makeCustomer();
        cus.setId(1L);
        List<CustomerDTO> customers = makeCustomerDTO();
        CustomerDTO exp= customers.get(0);
        List<CustomerDTO> expected = Arrays.asList(new CustomerDTO[] {exp});
        when(dao.getCustomerByName(cus.getName())).thenReturn(Arrays.asList(new Customer[] {cus})); 
        List<CustomerDTO> retrieved = service.getCustomerByName(cus.getName());
        verify(dao, times(1)).getCustomerByName(Matchers.eq(cus.getName()));
        assertNotNull(retrieved);
        for(CustomerDTO e : retrieved) assertEquals(cus.getName(), e.getName());
        assertEquals(expected, retrieved);
    }
    
    @Test
    public void getAllCustomers(){
        List<CustomerDTO> customers = makeCustomerDTO();
        List<Customer> exp= new ArrayList<>();
        Customer customer1=makeCustomer();
        customer1.setId(1L);
        exp.add(customer1);
        Customer customer2=makeCustomer();
        customer2.setName("Novotny");
        customer2.setAddress("Transilvania 58");
        customer2.setId(2L);
        exp.add(customer2);
        Customer customer3=makeCustomer();
        customer3.setName("polski");
        customer3.setAddress("Budapest");
        customer3.setId(3L);
        exp.add(customer3);
        when(dao.getAllCustomers()).thenReturn(exp);
        List<CustomerDTO> expected =service.getAllCustomers();
        verify(dao, times(1)).getAllCustomers();
        assertTrue(customers.size() == expected.size());
        assertEquals(expected, customers);
    }
    @Test
    public void deleteCustomerNull(){
        CustomerDTO customerDTO = null;
        try {
            service.deleteCustomer(customerDTO);
            fail();
        } catch (NullPointerException ex) {
            // OK
        }
        //List<CustomerDTO> customers = makeCustomerDTO();
        //customerDTO = customers.get(0);
       
    }
    
    @Test
    public void deleteCustomer(){
        //CustomerDTO customerDTO = null;
        
        List<CustomerDTO> customers = makeCustomerDTO();
        CustomerDTO c = customers.get(0);
        service.deleteCustomer(c);
        Customer entity=makeCustomer();
        entity.setId(1L);
        verify(dao, times(1)).deleteCustomer(entity);
       
    }
    @Test
    public void updateCustomer() {
      List<CustomerDTO> customers = makeCustomerDTO();
        CustomerDTO c = customers.get(1);
        CustomerDTO a= service.updateCustomer(c);
        Customer entity=makeCustomer();
        entity.setId(2L);
        verify(dao, times(1)).updateCustomer(entity);
    }


    @Test
    public void deleteCustomerById(){
     List<CustomerDTO> customers = makeCustomerDTO();
     CustomerDTO c = customers.get(0);
     long id = c.getId();
     service.deleteCustomerById(id);
     verify(dao, times(1)).deleteCustomerById(id);
    }

    /**
     * Test of getCustomerById method, of class CustomerService.
     */
    

    private List<CustomerDTO> makeCustomerDTO() {
        List<CustomerDTO> list = new ArrayList<>();
        
        CustomerDTO a=    mkcustomer("Novak","Tramtaria 15");
        CustomerDTO b=    mkcustomer("Novotny","Transilvania 58");
        CustomerDTO c =    mkcustomer("polski","Budapest");
        a.setId(1L);
        b.setId(2L);
        c.setId(3L);
        list.add(a);
        list.add(b);
        list.add(c);
       
        return list;
    }

    private Customer makeCustomer() {
        Customer customer = new Customer();
        customer.setName("Novak");
        customer.setAddress("Tramtaria 15");
        return customer;
    }
    
}
