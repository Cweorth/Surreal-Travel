package cz.muni.pa165.surrealtravel.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Roman Lacko [396157]
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The entity cannot be deleted")
public class EntityNotDeletedException extends RestAPIException {

    public EntityNotDeletedException(String entityName, long id) {
        super(entityName + " with id " + id + " cannot be deleted");
    }
    
    public EntityNotDeletedException(String entityName, long id, Throwable cause) {
        super(entityName + " with id " + id + " cannot be deleted", cause);
    }
    
    public EntityNotDeletedException()
    { }
    
    public EntityNotDeletedException(String message) {
        super(message);
    }

    public EntityNotDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotDeletedException(Throwable cause) {
        super(cause);
    }
    
}
