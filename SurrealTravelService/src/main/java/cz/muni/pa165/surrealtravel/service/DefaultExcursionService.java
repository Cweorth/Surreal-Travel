package cz.muni.pa165.surrealtravel.service;

import java.util.List;
import java.util.Objects;
import cz.muni.pa165.surrealtravel.dao.ExcursionDAO;
import cz.muni.pa165.surrealtravel.dao.TripDAO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.entity.Excursion;
import cz.muni.pa165.surrealtravel.entity.Trip;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * Excursion service implementation
 * @author Petr Dvořák [359819]
 */
@Service(value = "excursionService")
public class DefaultExcursionService implements ExcursionService{
    
    @Autowired
    private DozerBeanMapper mapper;
    
    @Autowired
    private ExcursionDAO excursionDAO;
    
    @Autowired
    private TripDAO tripDAO;
    
    /**
     * Create new excursion.
     * @param excursionDTO
     */
    @Override
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
    @Override
    public ExcursionDTO getExcursionById(long id){
      Excursion excursion = excursionDAO.getExcursionById(id);
      return excursion == null ? null : mapper.map(excursion,ExcursionDTO.class);
    }
 
    /**
     * Get list of all excursion DTOs.
     * @return 
     */
    @Override
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
    @Override
    @Transactional
    public void updateExcursion(ExcursionDTO excursionDTO){
        Objects.requireNonNull(excursionDTO);
        
        Excursion newExcursion = mapper.map(excursionDTO, Excursion.class);
        Excursion oldExcursion = excursionDAO.getExcursionById(excursionDTO.getId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newExcursion.getExcursionDate());
        calendar.add(Calendar.DATE, newExcursion.getDuration());

        Date start = newExcursion.getExcursionDate();
        Date end   = calendar.getTime();

        List<Trip> trips = tripDAO.getTripsWithExcursion(oldExcursion);
        for(Trip trip : trips) {
            if (start.before(trip.getDateFrom()) || end.after(trip.getDateTo())) {
                throw new IllegalArgumentException("The excursion breaks date constraint by a trip with ID " + String.valueOf(trip.getId()));
            }
        }
        
        excursionDAO.updateExcursion(newExcursion);
    }
   
    /**
     * Delete excursion entry for the given id.
     * @param id 
     */
    @Override
    @Transactional
    public void deleteExcursionById(long id){
        excursionDAO.deleteExcursionById(id);
    }
}
