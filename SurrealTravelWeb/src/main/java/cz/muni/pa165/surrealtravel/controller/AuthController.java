package cz.muni.pa165.surrealtravel.controller;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller dealing with logging in and out.
 * @author Jan Klimeš [374259]
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController {
    
    @Autowired
    private MessageSource messageSource;
    
    /**
     * Standard login page. Error message handling.
     * @param error
     * @param model
     * @param locale
     * @return 
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error, ModelMap model, Locale locale) {
        if(error != null) model.addAttribute("failureMessage", messageSource.getMessage("auth.wrongCredentials", null, locale));
        return "auth/login";
    }
    
    /**
     * Page displaying logout message.
     * @return 
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "auth/logout";
    }
    
    /**
     * Area 51 error message page.
     * @return 
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "auth/403";
    }
    
}
