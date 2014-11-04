package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.AbstractTest;
import cz.muni.pa165.surrealtravel.entity.Customer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Petr Dvořák [359819]
 */
@Transactional
public class JPACustomerDAOTest extends AbstractTest {

    private List<Customer> customers;
    
    @Autowired
    private JPACustomerDAO dao;

    @Test(expected = NullPointerException.class)
    public void testAddNullCustomer() {
        dao.addCustomer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullName() {
        Customer customer1 = mkcustomer(null, "Brno");
        dao.addCustomer(customer1);
    }

    @Test
    public void getCustomerById() {
        Customer customer1 = mkcustomer("Josh", "Brno");
        em.persist(customer1);
        Long id = customer1.getId();

        Customer newCustomer = dao.getCustomerById(id);

        assertEquals(newCustomer.getId(), customer1.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCustomerByEmptyName() {
        dao.getCustomerByName("");
    }

    @Test
    public void getCustomerByName() {
        Customer customer1 = mkcustomer("Josh", "Brno");
        Customer customer2 = mkcustomer("Nick", "Bratislava");
        Customer customer3 = mkcustomer("Josh", "Praha");
        customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer3);
        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);
        for (Customer customer : customers) {
            dao.addCustomer(customer);
        }

        String name = customer1.getName();
        List<Customer> actual = dao.getCustomerByName(name);

        Assert.assertEquals(customers.size(), actual.size());
    }

    @Test
    public void testUpdateCustomer() {
        Customer customer1 = mkcustomer("Josh", "Brno");
        Customer customer2 = mkcustomer("Nick", "Bratislava");
        Customer customer3 = mkcustomer("Jack", "Praha");

        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);

        String name = customer1.getName();
        String address = customer2.getAddress();

        customer3.setName(name);
        customer3.setAddress(address);
        em.persist(customer3);

        assertEquals(customer3.getName(), name);
        assertEquals(customer3.getAddress(), address);
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer1 = mkcustomer("Josh", "Brno");
        Customer customer2 = mkcustomer("Josh", "Bratislava");
        Customer customer3 = mkcustomer("Josh", "Praha");

        em.persist(customer1);
        em.persist(customer2);
        em.persist(customer3);

        dao.deleteCustomer(customer2);

        String name = customer1.getName();
        List<Customer> actual = dao.getCustomerByName(name);

        Assert.assertEquals(dao.getAllCustomers(), actual);
    }

}
