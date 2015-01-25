package cz.muni.pa165.surrealtravel.rest;

import cz.muni.pa165.surrealtravel.rest.exceptions.BadAuthenticationHeaderException;
import cz.muni.pa165.surrealtravel.rest.exceptions.PermissionDeniedException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * The component that deals with authentication for REST API.
 * @author Roman Lacko [396157]
 */
@Component
public class RestAuthCommons {

    private final static Logger                logger  = LoggerFactory.getLogger(RestAuthCommons.class);
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    /**
     * Logs in the user specified in the {@code request}'s header.
     * The required attributes in the header are {@code username} and {@code password}.
     * If neither is provided, the default credentials of {@code rest} account are used.
     *
     * @param request        the {@code HttpServletRequest} that contains login information
     */
    public void login(HttpServletRequest request) {
        logger.info("Logging in");
        Enumeration<String> usernames = request.getHeaders("username");
        Enumeration<String> passwords = request.getHeaders("password");

        String username;
        String password;

        // if there is no username nor password, use default credentials
        if (!usernames.hasMoreElements() && !passwords.hasMoreElements()) {
            logger.debug("Using default credentials");
            username = "rest";
            password = "rest";
        } else {
            // if there is no username or no password, throw 400
            if (!usernames.hasMoreElements()) throw new BadAuthenticationHeaderException();
            if (!passwords.hasMoreElements()) throw new BadAuthenticationHeaderException();

            username = usernames.nextElement();
            password = passwords.nextElement();
            logger.debug("Username is \"" + username + "\"");

            // if there are still some usernames or passwords, throw 400
            if (usernames.hasMoreElements()) throw new BadAuthenticationHeaderException();
            if (passwords.hasMoreElements()) throw new BadAuthenticationHeaderException();
        }

        logger.info("Setting authentication token");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        token.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(token);
        } catch (AuthenticationException ex) {
            throw new PermissionDeniedException(ex);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Logged in");
    }

    /**
     * Logs out the REST API user.
     */
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        logger.info("Logged out");
    }

}
