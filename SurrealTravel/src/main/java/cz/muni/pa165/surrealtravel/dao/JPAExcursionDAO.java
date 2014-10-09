/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;

/**
 *
 * @author Petr
 */
public class JPAExcursionDAO {
    
    private EntityManager entityManager;

    public JPAExcursionDAO(EntityManager em) {
        Objects.requireNonNull(em);
        this.entityManager = em;
    }

    public void addExcursion(Excursion excursion){
        if(excursion == null) throw new NullPointerException("Excursion object is null.");
        if(excursion.getId() < 0) throw new IllegalArgumentException("Excursion object is not valid - id < 0");
        if(excursion.getDescription().isEmpty()) throw new IllegalArgumentException("Description of excursion is empty string.");
        if(excursion.getDestination().isEmpty()) throw new IllegalArgumentException("Destination of excursion is empty string.");
        if(excursion.getPrice() == null) throw new NullPointerException("Excursion object is not valid - price is null");
        if(excursion.getExcursionDate() == null) throw new NullPointerException("Excursion object is not valid - excursionDate is null");
        
        entityManager.persist(excursion);
        
    }
    

    public Excursion getExcursionById(long id){
        if (id < 0) throw new IllegalArgumentException("The id has a negative value");
                
        return entityManager.find(Excursion.class, id);
    }
    
    public List<Excursion> getExcursionByDestination(String destination){
       Objects.requireNonNull(destination, "destination");
        
        return entityManager.createQuery("SELECT e FROM Trip e WHERE e.destination = :d", Excursion.class)
            .setParameter("d", destination)
            .getResultList();
    }
     
    public List<Excursion> getAllExcursion(){
      return entityManager.createNamedQuery("Excursion.getAll", Excursion.class)
            .getResultList();
    }
    
    
    public void updateExcursion(Excursion excursion){
        if(excursion == null) throw new NullPointerException("Excursion object is null.");
        if(excursion.getId() < 0) throw new IllegalArgumentException("Excursion object is not valid - id < 0");
        if(excursion.getDescription().isEmpty()) throw new IllegalArgumentException("Description of excursion is empty string.");
        if(excursion.getDestination().isEmpty()) throw new IllegalArgumentException("Destination of excursion is empty string.");
        
        entityManager.merge(excursion);
    }
    
   
    public void deleteExcursion(Excursion excursion){
        if(excursion == null) throw new NullPointerException("Excursion object is null.");
        
        entityManager.remove(excursion);
    }
    
    public void deleteExcursionById(long id){
        if (id < 0) throw new IllegalArgumentException("The id has a negative value");
        
        entityManager.createNamedQuery("DELETE FROM Excursion e WHERE e.id = :id", Excursion.class)
             .setParameter("id", id)
             .executeUpdate();
        
        
        
    }
}
