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
     * @param excursion      the excursion to add
     */
    void addExcursion(ExcursionDTO excursion);

    /**
     * Get excursion DTO by id.
     * @param id        the id of an excursion to find.
     * @return the excursion
     */
    ExcursionDTO getExcursionById(long id);

    /**
     * Get list of all excursion DTOs.
     * @return the list of excursions
     */
    List<ExcursionDTO> getAllExcursions();

    /**
     * Update excursion entry for the given DTO.
     * @param excursion      the excursion to update.
     */
    void updateExcursion(ExcursionDTO excursion);

    /**
     * Delete excursion entry for the given id.
     * @param id        the id of an excursion to delete.
     */
    void deleteExcursionById(long id);
}
