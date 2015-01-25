package cz.muni.pa165.surrealtravel.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Roman Lacko [396157]
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Multiple username or password fields in the request")
public class BadAuthenticationHeaderException extends RestAPIException {

    public BadAuthenticationHeaderException() 
    { }

    public BadAuthenticationHeaderException(String message) {
        super(message);
    }

    public BadAuthenticationHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadAuthenticationHeaderException(Throwable cause) {
        super(cause);
    }
}
