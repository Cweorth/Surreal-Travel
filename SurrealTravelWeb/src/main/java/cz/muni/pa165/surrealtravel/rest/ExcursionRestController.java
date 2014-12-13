package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import java.util.List;
import javassist.tools.rmi.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author Petr Dvořák [359819]
 */
@RestController
@RequestMapping("/rest/excursions")
public class ExcursionRestController {
    
    final static Logger logger = LoggerFactory.getLogger(cz.muni.pa165.surrealtravel.rest.ExcursionRestController.class);
    
    @Autowired
    private ExcursionService excursionService;
    
    /**
     * Default page - list all excursions.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<ExcursionDTO> listExcursions() {
        return excursionService.getAllExcursions();
    }
    
    /**
     * Get excursion by id.
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public @ResponseBody ExcursionDTO getExcursion(@PathVariable long id) {
        return excursionService.getExcursionById(id);
    }
    
    /**
     * Create new excursion
     * @param excursion
     * @return 
     */
    @RequestMapping(value="/new", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ExcursionDTO addExcursion(@RequestBody ExcursionDTO excursion){
        logger.info("Creating a new excursion");
        excursionService.addExcursion(excursion);
        return excursion;
    }
    
    /**
     * Modify excursion.
     * @param excursion
     * @param id
     * @return 
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT,  consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody ExcursionDTO updateExcursion(@PathVariable long id, @RequestBody ExcursionDTO excursion) {
        logger.info("Updating a new excursion");
        excursionService.updateExcursion(excursion);
        return excursion;
    }
    
    /**
     * Delete excursion by id.
     * @param id
     * @return 
     * @throws javassist.tools.rmi.ObjectNotFoundException 
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody ExcursionDTO deleteExcursion(@PathVariable long id) throws ObjectNotFoundException {
        logger.info("Deleting a new excursion");
        ExcursionDTO excursion = excursionService.getExcursionById(id);
        if(excursion == null) throw new ObjectNotFoundException("Excursion not found.");
        excursionService.deleteExcursionById(id);
        return excursion;
    }
}