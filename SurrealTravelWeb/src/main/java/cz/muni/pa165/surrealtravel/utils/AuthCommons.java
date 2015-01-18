package cz.muni.pa165.surrealtravel.utils;

import cz.muni.pa165.surrealtravel.dto.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Utility class with common functionality for authentication related operations.
 * @author Jan Klimeš [374259]
 */
public class AuthCommons {
    
    /**
     * Check if authentication exists.
     * @return 
     */
    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? false : auth.isAuthenticated();
    }
    
    /**
     * Check if user has specified role.
     * @param role
     * @return 
     */
    public static boolean hasRole(UserRole role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(!auth.isAuthenticated()) return false;
        
        // note: not-authenticated user would have role "ROLE_ANONYMOUS" - default SpringSec behaviour
        for(GrantedAuthority ga : auth.getAuthorities())
            if(ga.getAuthority().equalsIgnoreCase(role.name())) return true;
        
        return false;
    }
    
    /**
     * Return username of authenticated user.
     * @return username or "anonymousUser" when not authenticated
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? null : authentication.getName();
    }
    
    /**
     * Return denied page. Can be invoked if users snoop where they should not. No Star Wars
     * pun intended.
     * @param uriBuilder
     * @return 
     */
    public static String forceDenied(UriComponentsBuilder uriBuilder) {
        return "redirect:" + uriBuilder.path("/auth/403").build();
    }
    
    /**
     * Return denied page. Can be invoked if users snoop where they should not. No Star Wars
     * pun intended. Possibility for displaying a message.
     * @param uriBuilder
     * @param redirectAttributes
     * @param message
     * @return 
     */
    public static String forceDenied(UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("failureMessage", message);
        return "redirect:" + uriBuilder.path("/auth/403").queryParam("notification", "failure").build();
    }
    
}
