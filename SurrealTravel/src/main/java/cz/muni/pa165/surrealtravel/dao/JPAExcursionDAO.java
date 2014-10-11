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
 * Implementation of DAO for Excursion entity.
 * @author Petr Dvořák [359819]
 */
public class JPAExcursionDAO implements ExcursionDAO {
    
    private EntityManager entityManager;

    public JPAExcursionDAO(EntityManager em) {
        Objects.requireNonNull(em);
        this.entityManager = em;
    }
    
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }


    @Override
    public void addExcursion(Excursion excursion){
        if(excursion == null) throw new NullPointerException("Excursion object is null.");
        if(excursion.getId() < 0) throw new IllegalArgumentException("Excursion object is not valid - id < 0");
        if(excursion.getDescription().isEmpty()) throw new IllegalArgumentException("Description of excursion is empty string.");
        if(excursion.getDestination().isEmpty()) throw new IllegalArgumentException("Destination of excursion is empty string.");
        if(excursion.getPrice() == null) throw new NullPointerException("Excursion object is not valid - price is null");
        if(excursion.getExcursionDate() == null) throw new NullPointerException("Excursion object is not valid - excursionDate is null");
        
        entityManager.persist(excursion);
        
    }
    
    @Override
    public Excursion getExcursionById(long id){
        if (id < 0) throw new IllegalArgumentException("The id has a negative value");
                
        return entityManager.find(Excursion.class, id);
    }
    
    @Override
    public List<Excursion> getExcursionsByDestination(String destination){
       Objects.requireNonNull(destination, "destination");
        
        return entityManager.createQuery("SELECT e FROM Trip e WHERE e.destination = :d", Excursion.class)
            .setParameter("d", destination)
            .getResultList();
    }
    
    @Override 
    public List<Excursion> getAllExcursions(){
      return entityManager.createNamedQuery("Excursion.getAll", Excursion.class)
            .getResultList();
    }
    
    @Override
    public void updateExcursion(Excursion excursion){
        if(excursion == null) throw new NullPointerException("Excursion object is null.");
        if(excursion.getId() < 0) throw new IllegalArgumentException("Excursion object is not valid - id < 0");
        if(excursion.getDescription().isEmpty()) throw new IllegalArgumentException("Description of excursion is empty string.");
        if(excursion.getDestination().isEmpty()) throw new IllegalArgumentException("Destination of excursion is empty string.");
        
        entityManager.merge(excursion);
    }
    
   @Override
    public void deleteExcursion(Excursion excursion){
        if(excursion == null) throw new NullPointerException("Excursion object is null.");
        
        entityManager.remove(excursion);
    }
    @Override
    public void deleteExcursionById(long id){
        if (id < 0) throw new IllegalArgumentException("The id has a negative value");
        
        entityManager.createNamedQuery("DELETE FROM Excursion e WHERE e.id = :id", Excursion.class)
             .setParameter("id", id)
             .executeUpdate();
        
        
        
    }
}
