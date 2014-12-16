package cz.muni.pa165.surrealtravel.cli.rest;

import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * The REST client providing operations for {@link cz.muni.pa165.surrealtravel.dto.TripDTO}
 * @author Roman Lacko [396157]
 */
@Component
public class RestTripClient {
    
    final static Logger logger = LoggerFactory.getLogger(RestTripClient.class);
    
    @Autowired
    private RestTemplate template;
    
    public RestTripClient()
    { }
    
    /**
     * Returns the list of all trips
     * @return the list of all trips
     */
    public List<TripDTO> getAllTrips() {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), "trips");
        } catch (MalformedURLException ex) {
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("retrieving from " + address.toString());
        
        ResponseEntity<TripDTO[]> response;
        try {
            response = template.getForEntity(address.toString(), TripDTO[].class);
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex.getMessage(), ex);
        }
        
        logger.info(response.toString());
        
        if (!response.hasBody()) {
            throw new RESTAccessException(response.getStatusCode().toString());
        }
        
        return Arrays.asList(response.getBody());
    }
    
}
