package cz.muni.pa165.surrealtravel.surrealtravelweb.controller;

import cz.muni.pa165.surrealtravel.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    
}
