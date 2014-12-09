package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET, produces="text/plain")
    public @ResponseBody ExcursionDTO listExcrusions() {
        ExcursionDTO e = excursionService.getAllExcursions().get(0);
        if(e==null) {
            logger.info("BLA BLA BLA");
            return null;
        }
        logger.info("TEST TEST TEST"+e.getDescription());
        
        return e;
        
    }
    
}
