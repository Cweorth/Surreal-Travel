package cz.muni.pa165.surrealtravel.dao;

import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.util.List;

/**
 *
 * @author Petr Dvořák [359819]
 */
public interface ExcursionDAO {
    /**
     * Store new excursion.
     * @param excursion 
     */
    public void addExcursion(Excursion excursion);
    
    /**
     * Get the excursion with the given id.
     * @param id
     * @return 
     */
    public Excursion getExcursionById(long id);
    
    /**
     * Get the excursion with the given destination. More excursion with the same destination can exist in the system.
     * @param destination
     * @return 
     */
    public List<Excursion> getExcursionByDestination(String destination);
    
    /**
     * Get all excursions.
     * @return 
     */
    public List<Excursion> getAllExcursion();
    
    /**
     * Modify the excursion.
     * @param excursion 
     */
    public void updateExcursion(Excursion excursion);
    
    /**
     * Remove excursion.
     * @param excursion 
     */
    public void deleteExcursion(Excursion excursion);
     
    /**
     * Remove excursion by ID.
     * @param id
     */   
    public void deleteExcursionById(long id);
}
