package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import javassist.tools.rmi.ObjectNotFoundException;


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
     */
    @RequestMapping(value="/create", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody void addExcursion(ExcursionDTO excursion){
        excursionService.addExcursion(excursion);
    }
    
    /**
     * Get excursion by id.
     * @param excursion
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT,  consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody void updateExcursion(@PathVariable("id") Long id, ExcursionDTO excursion) {
        excursionService.updateExcursion(excursion);
    }
    
    /**
     * Delete excursion by id.
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteExcursion(@PathVariable("id") Long id) throws ObjectNotFoundException {
         ExcursionDTO excursion = excursionService.getExcursionById(id);
           if (excursion == null) {
                throw new ObjectNotFoundException("Excursion not found.");
           }
         excursionService.deleteExcursion(excursion);
    }
}
