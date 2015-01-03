package cz.muni.pa165.surrealtravel.cli.rest;

import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * The REST client providing operations for {@link cz.muni.pa165.surrealtravel.dto.TripDTO}
 * @author Roman Lacko [396157]
 */
public class RestTripClient {
    
    private final static Logger logger = LoggerFactory.getLogger(RestTripClient.class);
    private final RestTemplate template;
    
    private String getAddress(String suffix) {
        URL address;
        
        try {
            address = new URL(AppConfig.getBase(), suffix);
        } catch (MalformedURLException ex) {
            throw new RESTAccessException("Malformed URL", ex);
        }
        
        logger.info("retrieving from " + address.toString());
        return address.toString();
    }
    
    public RestTripClient(RestTemplate template) {
        this.template = Objects.requireNonNull(template, "template");
    }
    
    /**
     * Returns the list of all trips
     * @return the list of all trips
     */
    public List<TripDTO> getAllTrips() {
        ResponseEntity<TripDTO[]> response;
        
        try {
            response = template.getForEntity(getAddress("trips"), TripDTO[].class);
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex);
        }
        
        logger.info(response.toString());
        return Arrays.asList(response.getBody());
    }
    
    /**
     * Returns the trip with the given {@code id}.
     * @param  id       The {@code id} of the trip.
     * @return          The trip
     */
    public TripDTO getTrip(long id) {
        ResponseEntity<TripDTO> response;
        
        try {
            response = template.getForEntity(getAddress("trips/get/" + id), TripDTO.class);
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case NOT_FOUND: throw new RESTAccessException("The trip with id " + id + " was not found", ex);
                default:        throw new RESTAccessException(ex);
            }
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex);
        }
        
        logger.info(response.toString());        
        return response.getBody();
    }
    
    /**
     * Adds a new trip.
     * @param  trip     The trip to add.
     * @return          Instance of the trip with a new ID.
     */
    public TripDTO addTrip(TripDTO trip) {       
        ResponseEntity<TripDTO> response;
        
        try {
            response = template.postForEntity(getAddress("trips/new"), trip, TripDTO.class);
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST: throw new RESTAccessException("The trip is not valid", ex);
                case NOT_FOUND:   throw new RESTAccessException("The trip contains invalid excursion ID", ex);
                default:          throw new RESTAccessException(ex);
            }
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex);
        }

        logger.info(response.toString());
        return response.getBody();
    }
    
    /**
     * Modifies the trip.
     * @param  trip     The trip to update.
     * @return          Updated trip.
     */
    public TripDTO editTrip(TripDTO trip) {
        ResponseEntity<TripDTO> response;
        HttpEntity<TripDTO>     request;
        
        try {
            request  = new HttpEntity<>(trip, new HttpHeaders());
            response = template.exchange(getAddress("trips/edit/" + trip.getId()), HttpMethod.PUT, request, TripDTO.class);
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST: throw new RESTAccessException("A constraint prevented this trip from modification", ex);
                case NOT_FOUND:   throw new RESTAccessException("The trip contains invalid excursion ID", ex);
                default:          throw new RESTAccessException(ex);
            }
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex);
        }
        
        logger.info(response.toString());
        return response.getBody();
    }
    
    /**
     * Deletes the trip with the given {@code id}.
     * @param  id       The {@code id} of the trip to delete.
     * @return          The deleted trip.
     */
    public TripDTO deleteTrip(long id) {
        ResponseEntity<TripDTO> response;
        
        try {
            response = template.exchange(getAddress("trips/delete/" + id), HttpMethod.DELETE, null, TripDTO.class);
        } catch (HttpClientErrorException ex) {
            switch (ex.getStatusCode()) {
                case BAD_REQUEST: throw new RESTAccessException("The trip cannot be deleted because of integrity constraints", ex);
                default:          throw new RESTAccessException(ex);
            }
        } catch (RestClientException ex) {
            throw new RESTAccessException(ex);
        }
        
        logger.info(response.toString());        
        return response.getBody();
    }
    
}
