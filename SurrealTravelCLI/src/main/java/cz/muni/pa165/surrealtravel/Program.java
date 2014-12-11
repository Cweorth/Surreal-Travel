package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {
    
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(RestExcursionClient.class);
    
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("surrealtravel.xml");
        RestExcursionClient excursionClient = context.getBean(RestExcursionClient.class);
        
        for (ExcursionDTO excursion : excursionClient.getAllExcursions()) {
            System.out.println(excursion.toString());
        }
    }
}
