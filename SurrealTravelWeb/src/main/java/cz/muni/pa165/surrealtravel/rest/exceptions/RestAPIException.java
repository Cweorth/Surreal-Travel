package cz.muni.pa165.surrealtravel.rest.exceptions;

/**
 * @author Roman Lacko [396157]
 */
public class RestAPIException extends RuntimeException {

    public RestAPIException() 
    { }

    public RestAPIException(String message) {
        super(message);
    }

    public RestAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestAPIException(Throwable cause) {
        super(cause);
    }
    
}
