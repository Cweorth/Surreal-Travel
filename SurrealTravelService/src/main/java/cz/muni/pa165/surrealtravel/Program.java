package cz.muni.pa165.surrealtravel;

import org.dozer.DozerBeanMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import cz.muni.pa165.surrealtravel.dao.CustomerDAO;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import org.junit.Assert;

public class Program {

    private DozerBeanMapper mapper;
    
    public void run() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CustomerDAO     dao = context.getBean("customerDao",     CustomerDAO.class);
        CustomerService svc = context.getBean("customerService", CustomerService.class);
        
        Assert.assertNotNull(dao);
        Assert.assertNotNull(svc);
    }
    
    public static void main(String[] args) {
        Program program = new Program();
        program.run();        
    }
    
}
