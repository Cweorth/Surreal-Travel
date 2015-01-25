package cz.muni.pa165.surrealtravel.cli.rest;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown in REST clients.
 * @author Roman Lacko [396157]
 */
public class RESTAccessException extends RuntimeException {

    public RESTAccessException()
    { }

    public RESTAccessException(String message) {
        super(message);
    }

    public RESTAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public RESTAccessException(Throwable cause) {
        super(cause);
    }

    public RESTAccessException(HttpStatus status, String text) {
        super("[" + status.toString() + "] " + text);
    }

}
