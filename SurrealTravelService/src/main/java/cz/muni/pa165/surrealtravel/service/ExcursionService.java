package cz.muni.pa165.surrealtravel.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import cz.muni.pa165.surrealtravel.dao.ExcursionDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Excursion service implementation
 * @author Petr Dvořák [359819]
 */
public class ExcursionService {
    
    @Autowired
    private DozerBeanMapper mapper;
    @Autowired
    private ExcursionDAO excursionDAO;
    
    @Transactional
    public void addExcursion(ExcursionDTO excursionDTO){
        if (excursionDTO == null) {
            throw new IllegalArgumentException("excursion can't be null");
        }
      Excursion excursion=mapper.map(excursionDTO, Excursion.class);
      excursionDAO.addExcursion(excursion);
      excursionDTO.setId(excursion.getId());
        
    }
    
    public ExcursionDTO getExcursionById(long id){
      Excursion excursion = excursionDAO.getExcursionById(id);
      ExcursionDTO result = mapper.map(excursion,ExcursionDTO.class);
      return result;
    }
 
    public List<ExcursionDTO> getExcursionsByDestination(String destination){
       Objects.requireNonNull(destination, "destination");
        
       Excursion excursion = excursionDAO.getExcursionByDestination(destination);
       List<ExcursionDTO> result = mapper.map(excursion,ExcursionDTO.class);
       return result;
    }
    
    public List<ExcursionDTO> getAllExcursions(){
      List<ExcursionDTO> result = new ArrayList<>();
      List<Excursion> excursions = excursionDAO.getAllReservations();
      for (Excursion excursion : excursions) {
            result.add(mapper.map(excursion,ExcursionDTO.class));
        }
      return result;
    }

    @Transactional
    public void updateExcursion(ExcursionDTO excursionDTO){
        Objects.requireNonNull(excursionDTO);
        
        Excursion excursion=mapper.map(excursionDTO, Excursion.class);
        excursionDAO.updateExcursion(excursion);
    }
   
    @Transactional
    public void deleteExcursion(ExcursionDTO excursionDTO){
        Objects.requireNonNull(excursionDTO);
        
        Excursion excursion=mapper.map(excursionDTO, Excursion.class);
        excursionDAO.getExcursion(excursion);
        ExcursionDAO.deleteExcursion(excursionDTO);
    }

    @Transactional
    public void deleteExcursionById(long id){
        excursionDAO.deleteExcursionById(id);
    }
}
