package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.rest.exceptions.EntityNotDeletedException;
import cz.muni.pa165.surrealtravel.rest.exceptions.EntityNotFoundException;
import cz.muni.pa165.surrealtravel.rest.exceptions.InvalidEntityException;
import cz.muni.pa165.surrealtravel.rest.exceptions.PermissionDeniedException;
import cz.muni.pa165.surrealtravel.rest.exceptions.RestAPIException;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
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
    
    private final static Logger logger = LoggerFactory.getLogger(cz.muni.pa165.surrealtravel.rest.ExcursionRestController.class);
    
    @Autowired
    private ExcursionService excursionService;
    
    @Autowired
    private RestAuthCommons restAuth;
    
    /**
     * Default page - list all excursions.
     * @return list of excursions
     */
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<ExcursionDTO> listExcursions() {
        logger.info("Retrieving a list of excursions");
        return excursionService.getAllExcursions();
    }
    
    /**
     * Get excursion by id.
     * @param id
     * @return the excursion
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public @ResponseBody ExcursionDTO getExcursion(@PathVariable long id) {
        logger.info("Retrieving an excursion with id " + id);
        ExcursionDTO excursion = excursionService.getExcursionById(id);
        
        if(excursion == null) {
            throw new EntityNotFoundException("Excursion", id);
        }
        
        return excursion;
    }
    
    /**
     * Create new excursion
     * @param excursion
     * @param request
     * @param response
     * @return the new excursion
     */
    @RequestMapping(value="/new", method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody ExcursionDTO addExcursion(@RequestBody ExcursionDTO excursion, HttpServletRequest request, HttpServletResponse response){
        logger.info("Creating a new excursion");

        try {
            restAuth.login(request);
            excursionService.addExcursion(excursion);
        } catch (AccessDeniedException ex) {
            // map to 403
            throw new PermissionDeniedException(ex);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidEntityException("The excursion is not valid", ex);
        } finally {
            restAuth.logout();
        }
        
        return excursion;
    }
    
    /**
     * Modify excursion.
     * @param excursion
     * @param id
     * @param request
     * @return the updated excursion
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT,  consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody ExcursionDTO updateExcursion(@PathVariable long id, @RequestBody ExcursionDTO excursion, HttpServletRequest request) {
        logger.info("Updating an excursion with id " + id);
        
        try {
            restAuth.login(request);
            excursionService.updateExcursion(excursion);
        } catch (AccessDeniedException ex) {
            // map to 403
            throw new PermissionDeniedException(ex);
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidEntityException("The excursion is not valid", ex);
        } finally {
            restAuth.logout();
        }
        
        return excursion;
    }
    
    /**
     * Delete excursion by id.
     * @param id
     * @param request
     * @return the deleted excursion
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody ExcursionDTO deleteExcursion(@PathVariable long id, HttpServletRequest request) {
        logger.info("Deleting an excursion with id " + id);
        ExcursionDTO excursion = excursionService.getExcursionById(id);
        
        if(excursion == null) {
            throw new EntityNotFoundException("Excursion", id);
        }
        
        try {
            restAuth.login(request);
            excursionService.deleteExcursionById(id);
        } catch (RestAPIException ex) {
            throw ex; // rethrow the exception
        } catch (AccessDeniedException ex) {
            // map to 403
            throw new PermissionDeniedException(ex);
        } catch (Exception ex) {
            logger.error("The excursion with id " + id + " cannot be deleted");
            logger.debug("Exception is ", ex);
            throw new EntityNotDeletedException("Excursion", id, ex);
        } finally {
            restAuth.logout();
        }
        
        return excursion;
    }
}
