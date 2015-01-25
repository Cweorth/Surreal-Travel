package cz.muni.pa165.surrealtravel.cli.rest;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

/**
 * Authentication Header Interceptor. 
 * Wraps the request with "username" and "password" attributes.
 * 
 * @author Roman Lacko [396157]
 */
public class AuthHeaderInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthHeaderInterceptor.class);
    
    private final String username;
    private final String password;

    public AuthHeaderInterceptor(String username, String password) {
        logger.debug("Authorization interceptor created for \"" + username + "\"");
        this.username = username;
        this.password = password;
    }
    
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logger.debug("Wrapping request with username \"" + username + "\" and the password");
        HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
        wrapper.getHeaders().add("username", username);
        wrapper.getHeaders().add("password", password);
        
        logger.debug("Executing request");
        return execution.execute(wrapper, body);
    }
    
}
