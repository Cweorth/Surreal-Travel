package cz.muni.pa165.surrealtravel.cli.rest;

import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * The REST client providing operations for {@link cz.muni.pa165.surrealtravel.dto.ExcursionDTO}.
 * @author Jan Klimeš [374259]
 */
public class RestExcursionClient {

    private final static Logger logger = LoggerFactory.getLogger(RestExcursionClient.class);
    
    private final RestTemplate template;
    
    /**
     * Constructor.
     * @param template 
     */
    public RestExcursionClient(RestTemplate template) {
        this.template = Objects.requireNonNull(template, "template");
    }
    
    /**
     * Returns the list of all excursions
     * @return the list of all excursions
     */
    public List<ExcursionDTO> getAllExcursions() {
        ResponseEntity<ExcursionDTO[]> response;
        try {
            response = template.getForEntity(getAddress("excursions"), ExcursionDTO[].class);
        } catch(RestClientException ex) {
            throw new RESTAccessException(ex.getMessage(), ex);
        }
        
        logger.info(response.toString());
        
        return Arrays.asList(response.getBody());
    }
    
    /**
     * Get a single excursion by the id.
     * @param id
     * @return 
     */
    public ExcursionDTO getExcursion(long id) {
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.getForEntity(getAddress("excursions/get/" + id), ExcursionDTO.class);
        } catch(RestClientException ex) {
            switch(parseStatusCode(ex.getMessage())) {
                case 404: throw new RESTAccessException("The excursion with ID " + id + " was not found.");
                default:  throw new RESTAccessException(ex);
            }
        }
        
        logger.info(response.toString());
        
        return response.getBody();
    }
    
    /**
     * Create new excursion.
     * @param excursion
     * @return 
     */
    public ExcursionDTO addExcursion(ExcursionDTO excursion) {        
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.postForEntity(getAddress("excursions/new"), excursion, ExcursionDTO.class);
        } catch(RestClientException ex) {
            switch(parseStatusCode(ex.getMessage())) {
                case 400: throw new RESTAccessException("The excursion is not valid.");
                default:  throw new RESTAccessException(ex);
            }
        }

        logger.info(response.toString());
        
        return response.getBody();
    }
    
    /**
     * Modify existing excursion.
     * @param excursion
     * @return 
     */
    public ExcursionDTO editExcursion(ExcursionDTO excursion) {        
        final HttpEntity<ExcursionDTO> request = new HttpEntity<>(excursion, new HttpHeaders());
        
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.exchange(getAddress("excursions/edit/" + excursion.getId()), HttpMethod.PUT, request, ExcursionDTO.class);
        } catch(RestClientException ex) {
            switch(parseStatusCode(ex.getMessage())) {
                case 400: throw new RESTAccessException("A constraint prevented this excursion from modification.");
                default:  throw new RESTAccessException(ex);
            }
        }
        
        logger.info(response.toString());

        return response.getBody();
    }
    
    /**
     * Delete excursion by id.
     * @param id
     * @return 
     */
    public ExcursionDTO deleteExcursion(long id) {               
        ResponseEntity<ExcursionDTO> response;
        try {
            response = template.exchange(getAddress("excursions/delete/" + id), HttpMethod.DELETE, null, ExcursionDTO.class);
        } catch(RestClientException ex) {
            switch(parseStatusCode(ex.getMessage())) {
                case 400: throw new RESTAccessException("The excursion cannot be deleted because of integrity constraints.");
                default:  throw new RESTAccessException(ex);
            }
        }
        
        logger.info(response.toString());
        
        return response.getBody();
    }
    
    private int parseStatusCode(String errorMessage) {
        String[] parts = errorMessage.split(" ", 2);
        
        try {
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
    
    private String getAddress(String suffix) {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), suffix);
        } catch (MalformedURLException ex) {
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("Retrieving from " + address.toString());
        return address.toString();
    }
     
}
