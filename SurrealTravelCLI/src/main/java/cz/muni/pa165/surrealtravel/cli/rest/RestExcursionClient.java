package cz.muni.pa165.surrealtravel.cli.rest;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestExcursionClient {

    final static Logger logger = LoggerFactory.getLogger(RestExcursionClient.class);
    
    @Autowired
    private RestTemplate template;
    
    @Autowired
    private URL          prefix;
    
    public RestExcursionClient()
    { }
    
    public List<ExcursionDTO> getAllExcursions() {
        URL address;
        try {
            address = new URL(prefix, "excursions");
        } catch (MalformedURLException ex) {
            logger.error("URL format exception", ex);
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("retrieving from " + address.toString());
        
        ResponseEntity<ExcursionDTO[]> response = template.getForEntity(address.toString(), ExcursionDTO[].class);
        logger.info(response.toString());
        
        if (!response.hasBody()) {
            throw new RESTAccessException(response.getStatusCode().toString());
        }
        
        return Arrays.asList(response.getBody());
    }
        
}