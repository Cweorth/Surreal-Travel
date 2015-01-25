package cz.muni.pa165.surrealtravel.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Roman Lacko [396157]
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid username or password")
public class PermissionDeniedException extends RestAPIException {

    public PermissionDeniedException()
    {}

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PermissionDeniedException(Throwable cause) {
        super(cause);
    }

}
