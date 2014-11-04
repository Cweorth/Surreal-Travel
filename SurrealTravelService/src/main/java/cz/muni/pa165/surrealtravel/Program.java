package cz.muni.pa165.surrealtravel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cz.muni.pa165.surrealtravel.dao.CustomerDAO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import org.junit.Assert;

public class Program {
    
    public void run() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:service/applicationContext.xml");
        CustomerDAO     dao = context.getBean("customerDao",     CustomerDAO.class);
        CustomerService svc = context.getBean("customerService", CustomerService.class);
        
        Assert.assertNotNull(dao);
        Assert.assertNotNull(svc);
        
        CustomerDTO customer = new CustomerDTO();
        customer.setAddress("foo");
        customer.setName("bar");
        svc.addCustomer(customer);
        Assert.assertEquals(1, svc.getAllCustomers().size());
        System.out.println("OK");
    }
    
    public static void main(String[] args) {
        Program program = new Program();
        program.run();        
    }
    
}
