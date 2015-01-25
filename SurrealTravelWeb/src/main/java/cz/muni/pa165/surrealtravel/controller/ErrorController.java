package cz.muni.pa165.surrealtravel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author cweorth
 */
@Controller
public class ErrorController {

    @RequestMapping("/403")
    public String error403() {
        return "/err/403";
    }
    
    @RequestMapping("/404")
    public String error404() {
        return "/err/404";
    }
    
    @RequestMapping("/500")
    public String error500() {
        return "/err/500";
    }
    
}
