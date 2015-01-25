package cz.muni.pa165.surrealtravel.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Roman Lacko [396157]
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
public class EntityNotFoundException extends RestAPIException {

    public EntityNotFoundException(String entityName, long id) {
        super(entityName + " with id " + id + " does not exist");
    }
    
    public EntityNotFoundException(String entityName, long id, Throwable cause) {
        super(entityName + " with id " + id + " does not exist", cause);
    }
    
    public EntityNotFoundException()
    { }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
