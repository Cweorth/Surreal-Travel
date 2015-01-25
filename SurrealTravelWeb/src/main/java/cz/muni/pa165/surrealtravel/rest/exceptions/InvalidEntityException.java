package cz.muni.pa165.surrealtravel.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Roman Lacko [396157]
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid entity")
public class InvalidEntityException extends RestAPIException {

    public InvalidEntityException()
    { }

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntityException(Throwable cause) {
        super(cause);
    }

}
