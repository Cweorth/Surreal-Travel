package cz.muni.pa165.surrealtravel.cli.rest;

import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * The REST client providing operations for {@link cz.muni.pa165.surrealtravel.dto.ExcursionDTO}.
 * @author Jan Klimeš [374259]
 */
@Component
public class RestExcursionClient {

    final static Logger logger = LoggerFactory.getLogger(RestExcursionClient.class);
    
    @Autowired
    private RestTemplate template;
    
    public RestExcursionClient()
    { }
    
    /**
     * Returns the list of all excursions
     * @return the list of all excursions
     */
    public List<ExcursionDTO> getAllExcursions() {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), "excursions");
        } catch (MalformedURLException ex) {
            logger.error("URL format exception", ex);
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("Retrieving from " + address.toString());
        
        ResponseEntity<ExcursionDTO[]> response;
        try {
            response = template.getForEntity(address.toString(), ExcursionDTO[].class);
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex.getMessage(), ex);
        }
        
        logger.info(response.toString());
        
        if (!response.hasBody()) {
            throw new RESTAccessException(response.getStatusCode().toString());
        }
        
        return Arrays.asList(response.getBody());
    }
    
    /**
     * Get a single excursion by the id.
     * @param id
     * @return 
     */
    public ExcursionDTO getExcursion(long id) {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), "excursions/get/" + id);
        } catch (MalformedURLException ex) {
            logger.error("URL format exception", ex);
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("Retrieving from " + address.toString());
        
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.getForEntity(address.toString(), ExcursionDTO.class);
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex.getMessage(), ex);
        }
        
        logger.info(response.toString());
        
        if (!response.hasBody()) {
            throw new RESTAccessException(response.getStatusCode().toString());
        }
        
        return response.getBody();
    }
    
    /**
     * Create new excursion.
     * @param excursion
     * @return 
     */
    public ExcursionDTO addExcursion(ExcursionDTO excursion) {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), "excursions/new");
        } catch (MalformedURLException ex) {
            logger.error("URL format exception", ex);
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("Adding excursion via " + address.toString());
        
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.postForEntity(address.toString(), excursion, ExcursionDTO.class);
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex.getMessage(), ex);
        }

        logger.info(response.toString());
        
        if (!response.hasBody()) {
            throw new RESTAccessException(response.getStatusCode().toString());
        }
        
        return response.getBody();
    }
    
    /**
     * Modify existing excursion.
     * @param excursion
     * @return 
     */
    public ExcursionDTO editExcursion(ExcursionDTO excursion) {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), "excursions/edit/" + excursion.getId());
        } catch (MalformedURLException ex) {
            logger.error("URL format exception", ex);
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("Modifying excursion via " + address.toString());
        
        final HttpEntity<ExcursionDTO> request = new HttpEntity<>(excursion, new HttpHeaders());
        
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.exchange(address.toString(), HttpMethod.PUT, request, ExcursionDTO.class);
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex.getMessage(), ex);
        }
        
        logger.info(response.toString());
        
        if(!response.hasBody()) {
            throw new RESTAccessException(response.getStatusCode().toString());
        }
        
        return response.getBody();
        
    }
    
    /**
     * Delete excursion by id.
     * @param id
     * @return 
     */
    public ExcursionDTO deleteExcursion(long id) {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), "excursions/delete/" + id);
        } catch (MalformedURLException ex) {
            logger.error("URL format exception", ex);
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("Deleting by calling " + address.toString());
               
        // TODO tomorow
//        ResponseEntity<ExcursionDTO> response;
//        try {
////            response = template.
//        } catch (RestClientException ex) {
//            throw new RESTAccessException(ex.getMessage(), ex);
//        }
//        
//        logger.info(response.toString());
//        
//        if (!response.hasBody()) {
//            throw new RESTAccessException(response.getStatusCode().toString());
//        }
//        
//        return response.getBody();
        return null;
    }

    public RestTemplate getTemplate() {
        return template;
    }

    public void setTemplate(RestTemplate template) {
        this.template = template;
    }
     
}
