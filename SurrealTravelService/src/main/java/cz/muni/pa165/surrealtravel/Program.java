package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.math.BigDecimal;
import org.dozer.DozerBeanMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {

    private DozerBeanMapper mapper;
    
    public void run() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        mapper = context.getBean("mapper", DozerBeanMapper.class);
        for(String file : mapper.getMappingFiles()) {
            System.out.println(file);
        }
        
        Trip classa = new Trip();
        classa.setBasePrice(new BigDecimal(500L));
        
        TripDTO classb = mapper.map(classa, TripDTO.class);
        Trip    classc = mapper.map(classb, Trip.class);
        
        System.out.println(classb.getBasePrice());
        System.out.println(classc.getBasePrice());
       
    }
    
    public static void main(String[] args) {
        Program program = new Program();
        program.run();        
    }
    
}
