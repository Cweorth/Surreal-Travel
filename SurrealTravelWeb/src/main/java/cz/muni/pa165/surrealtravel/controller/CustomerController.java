package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Stub of a customer controller.
 * @author Jan Klimeš [374259]
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    
    final static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageSource messageSource;
    
    @RequestMapping(method = RequestMethod.GET)
    public String test(ModelMap model) {
        model.addAttribute("message", "Template attribute test for customer!");
        return "customer";
    }
    
}
