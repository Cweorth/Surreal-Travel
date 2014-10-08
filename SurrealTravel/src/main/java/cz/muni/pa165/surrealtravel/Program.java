package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The class that contains the main method.
 * @author Roman Lacko [396157]
 */
public class Program {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Surreal-Travel");
        EntityManager        em  = emf.createEntityManager();
        
        Excursion e = new Excursion();
        e.setDescription("description");
        e.setDestination("kazachstan");
        e.setExcursionDate(new Date());
        e.setPrice(new BigDecimal(500));
        
        em.persist(e);
        em.close();
        
    }
}
