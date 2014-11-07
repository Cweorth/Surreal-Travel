package cz.muni.pa165.surrealtravel.service;

import java.util.List;
import java.util.Objects;
import cz.muni.pa165.surrealtravel.dao.ExcursionDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Excursion service implementation
 * @author Petr Dvořák [359819]
 */
@Service
@Transactional
public class ExcursionService {
    
    @Autowired
    private DozerBeanMapper mapper;
    @Autowired
    private ExcursionDAO excursionDAO;
    
    /**
     * Create new excursion.
     * @param excursionDTO
     */
    @Transactional
    public void addExcursion(ExcursionDTO excursionDTO){
      Objects.requireNonNull(excursionDTO);
      Excursion excursion=mapper.map(excursionDTO, Excursion.class);
      excursionDAO.addExcursion(excursion);
      excursionDTO.setId(excursion.getId());
    }
    
    /**
     * Get excursion DTO by id.
     * @param id
     * @return 
     */
    public ExcursionDTO getExcursionById(long id){
      Excursion excursion = excursionDAO.getExcursionById(id);
      ExcursionDTO result = mapper.map(excursion,ExcursionDTO.class);
      return result;
    }
 
    /**
     * Get list of excursion DTOs with the given destination.
     * @param destination
     * @return 
     */
    public List<ExcursionDTO> getExcursionsByDestination(String destination){
       Objects.requireNonNull(destination, "destination");
       
       List<ExcursionDTO> result = new ArrayList<>();
         for (Excursion excursion : excursionDAO.getExcursionsByDestination(destination)) 
             result.add(mapper.map(excursion,ExcursionDTO.class));
       return result;
    }
    
    /**
     * Get list of all excursion DTOs.
     * @return 
     */
    public List<ExcursionDTO> getAllExcursions(){
      List<ExcursionDTO> result = new ArrayList<>();
    
      for (Excursion excursion : excursionDAO.getAllExcursions()) {
            result.add(mapper.map(excursion,ExcursionDTO.class));
      }
      return result;
    }
    
    /**
     * Update excursion entry for the given DTO.
     * @param excursionDTO 
     */
    @Transactional
    public void updateExcursion(ExcursionDTO excursionDTO){
        Objects.requireNonNull(excursionDTO);
        
        Excursion excursion=mapper.map(excursionDTO, Excursion.class);
        excursionDAO.updateExcursion(excursion);
    }
   
     /**
     * Delete excursion entry for the given DTO.
     * @param excursionDTO
     */
    @Transactional
    public void deleteExcursion(ExcursionDTO excursionDTO){
        Objects.requireNonNull(excursionDTO, "Excursion is null");
        
        excursionDAO.deleteExcursion(mapper.map(excursionDTO, Excursion.class));
    }
    
    /**
     * Delete excursion entry for the given id.
     * @param id 
     */
    @Transactional
    public void deleteExcursionById(long id){
        excursionDAO.deleteExcursionById(id);
    }
}
