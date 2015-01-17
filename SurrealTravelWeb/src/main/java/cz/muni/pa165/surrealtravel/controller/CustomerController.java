package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.utils.AuthCommons;
import cz.muni.pa165.surrealtravel.validator.CustomerValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Customer controller.
 * @author Jan Klimeš [374259]
 */
@Controller
@RequestMapping("/customers")
public class CustomerController {
    
    final static Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageSource messageSource;
      
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new CustomerValidator());
    }
    
    /**
     * Default page - list all customers.
     * @param model
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.GET)
    public String listCustomers(ModelMap model) {
        List<CustomerDTO> customers = new ArrayList<>();
        
        // Load all customers in the system for ROLE_ADMIN, or only attached Customer
        // for lesser roles.
        if(!AuthCommons.hasRole(UserRole.ROLE_ADMIN)) {
            AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
            if(account.getCustomer() != null) customers.add(account.getCustomer());
        } else customers = customerService.getAllCustomers();
        
        // See if customers have some attached reservations (used to deny the possiblity
        // to delete them in GUI).
        List<Integer> customersOccurence = new ArrayList<>(customers.size());
        for(CustomerDTO c : customers)
            customersOccurence.add(reservationService.getAllReservationsByCustomer(c).size());
        
        model.addAttribute("customers", customers);
        model.addAttribute("customersOccurence", customersOccurence);
        return "customer/list";
    }
    
    /**
     * Display a form for creating a new customer.
     * @param model
     * @return redirect
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newCustomerForm(ModelMap model) {
        model.addAttribute("customerDTO", new CustomerDTO());
        return "customer/new";
    }
    
    /**
     * Process POST request for creating of a new customer.
     * @param customerDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newCustomer(@Validated @ModelAttribute CustomerDTO customerDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {       
        
        // check the form validator output
        if(bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "customer/new";
        }

        String resultStatus = "success";
        
        // so far so good, try to save customer
        try {
            customerService.addCustomer(customerDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }
        
        String messageKey = "customer.message.new" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{customerDTO.getName()}, locale));
        return "redirect:" + uriBuilder.path("/customers").queryParam("notification", resultStatus).build();
    }
    
    /**
     * Display a form for editing of a customer.
     * @param id
     * @param model
     * @param uriBuilder
     * @param redirectAttributes
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editCustomerForm(@PathVariable long id, ModelMap model, UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes, Locale locale) {
        
        // If ROLE_USER or ROLE_STAFF is trying to edit someone other than himself, deny it.
        if(!AuthCommons.hasRole(UserRole.ROLE_ADMIN)) {
            AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
            if(account.getCustomer() == null || account.getCustomer().getId() != id)
                return AuthCommons.forceDenied(uriBuilder, redirectAttributes, messageSource.getMessage("customer.message.edit.403", null, locale));
        }
        
        CustomerDTO customer = null;
        
        try {
            customer = customerService.getCustomerById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        if(customer == null) return "customer/list";
        
        model.addAttribute("customerDTO", customer);
        return "customer/edit";
    }
    
    /**
     * Process POST request for editing of a customer.
     * @param customerDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_USER")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editCustomer(@Validated @ModelAttribute CustomerDTO customerDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {       
        
        // If ROLE_USER or ROLE_STAFF is trying to edit someone other than himself, deny it.
        if(!AuthCommons.hasRole(UserRole.ROLE_ADMIN)) {
            AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
            if(account.getCustomer() == null || account.getCustomer().getId() != customerDTO.getId())
                return AuthCommons.forceDenied(uriBuilder, redirectAttributes, messageSource.getMessage("customer.message.edit.403", null, locale));
        }
        
        String resultStatus = "success";

        // check the form validator output
        if(bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "customer/edit";
        }

        // so far so good, try to save customer
        try {
            customerDTO = customerService.updateCustomer(customerDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());            
            resultStatus = "failure";
        }
        
        String messageKey = "customer.message.edit" + (resultStatus.equals("success") ? "" : ".error");
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{customerDTO.getName()}, locale));
        
        // get back to customer list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/customers").queryParam("notification", resultStatus).build();
    }
    
    /**
     * Delete customer with the given id.
     * @param id
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteCustomer(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String resultStatus = "success";
        String name = null;
        
        try {
            CustomerDTO customer = customerService.getCustomerById(id);
            name = customer.getName();
            customerService.deleteCustomerById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "customer.message.delete" + (resultStatus.equals("success") ? "" : ".error");
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{name}, locale));
        
        // get back to customer list, add the notification to the url
        return "redirect:" + uriBuilder.path("/customers").queryParam("notification", resultStatus).build();
    }
 
    /**
     * Check BindingResult of a request for errors found by a validator.
     * @param bindingResult
     * @param viewName
     * @return redirect
     */
    private void logFormErrors(BindingResult bindingResult) {
        logger.debug("Encountered following errors when validating form.");
        for(ObjectError ge : bindingResult.getGlobalErrors()) {
            logger.debug("ObjectError: {}", ge);
        }
        for(FieldError fe : bindingResult.getFieldErrors()) {
            logger.debug("FieldError: {}", fe);
        }
    }

}
