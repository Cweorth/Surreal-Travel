package cz.muni.pa165.surrealtravel.service;

import java.util.List;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;

/**
 * Excursion service implementation
 * @author Petr Dvořák [359819]
 */
public interface ExcursionService {
    
    /**
     * Create new excursion.
     * @param excursionDTO
     */
    void addExcursion(ExcursionDTO excursionDTO);
    
    /**
     * Get excursion DTO by id.
     * @param id
     * @return 
     */
    ExcursionDTO getExcursionById(long id);
 
    /**
     * Get list of excursion DTOs with the given destination.
     * @param destination
     * @return 
     */
    List<ExcursionDTO> getExcursionsByDestination(String destination);
    
    /**
     * Get list of all excursion DTOs.
     * @return 
     */
    List<ExcursionDTO> getAllExcursions();
    
    /**
     * Update excursion entry for the given DTO.
     * @param excursionDTO 
     */
    void updateExcursion(ExcursionDTO excursionDTO);
   
     /**
     * Delete excursion entry for the given DTO.
     * @param excursionDTO
     */
    void deleteExcursion(ExcursionDTO excursionDTO);
    
    /**
     * Delete excursion entry for the given id.
     * @param id 
     */
    void deleteExcursionById(long id);
}
