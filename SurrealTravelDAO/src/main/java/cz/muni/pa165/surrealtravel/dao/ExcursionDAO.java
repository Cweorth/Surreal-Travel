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
     * @return the excursion
     */
    public Excursion getExcursionById(long id);
    
    /**
     * Get all excursions.
     * @return the excursion
     */
    public List<Excursion> getAllExcursions();
    
    /**
     * Modify the excursion.
     * @param excursion 
     * @return the modified excursion
     */
    public Excursion updateExcursion(Excursion excursion);
     
    /**
     * Remove excursion by ID.
     * @param id
     */   
    public void deleteExcursionById(long id);
}
