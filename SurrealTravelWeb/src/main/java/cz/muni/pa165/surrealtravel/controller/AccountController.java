package cz.muni.pa165.surrealtravel.controller;

import static cz.muni.pa165.surrealtravel.controller.TripController.logger;
import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.utils.AccountWrapper;
import cz.muni.pa165.surrealtravel.utils.AuthCommons;
import cz.muni.pa165.surrealtravel.validator.AccountEditValidator;
import cz.muni.pa165.surrealtravel.validator.AccountNewValidator;
import cz.muni.pa165.surrealtravel.validator.AccountPasswordValidator;
import java.security.Principal;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 * Account registration controller
 * @author Roman Lacko [396157]
 */
@Controller
@RequestMapping("/accounts")
public class AccountController {
    
    private final static Logger                logger  = LoggerFactory.getLogger(AccountController.class);
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private ReservationService reservationService;
    
    @Autowired
    private MessageSource messageSource;
    
    @InitBinder(value = "newWrapper")
    public void initBinderNew(WebDataBinder binder) {
        binder.setValidator(new AccountNewValidator());
    }
    
    @InitBinder(value = "editWrapper")
    public void initBinderEdit(WebDataBinder binder) {
        binder.addValidators(new AccountPasswordValidator(), new AccountEditValidator());
    }
    
    @InitBinder(value = "deleteWrapper")
    public void initBinderDelete(WebDataBinder binder) {
        binder.addValidators(new AccountPasswordValidator());
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String listAccounts(ModelMap model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "account/list";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newAccount(ModelMap model) {
        AccountDTO account = new AccountDTO();
        account.setCustomer(new CustomerDTO());
        model.addAttribute("newWrapper", new AccountWrapper(account));
        return "account/new";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newAccount(
            @Validated @ModelAttribute("newWrapper") AccountWrapper wrapper, 
            BindingResult        bindingResult,
            RedirectAttributes   redirectAttributes,
            UriComponentsBuilder uriBuilder,
            Locale               locale) {

        if (bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "account/new";
        }
        
        AccountDTO account = wrapper.getAccount();
        
        AccountDTO other   = accountService.getAccountByUsername(account.getUsername());
        if (other != null) {
            String message = messageSource.getMessage("account.validator.username.used", new Object[]{ account.getUsername() }, locale);
            bindingResult.addError(new FieldError("wrapper", "account.username", message));
            logFormErrors(bindingResult);
            return "account/new";
        }
        
        if (! wrapper.isCustomer()) {
            account.setCustomer(null);
        }
        
        account.setPassword(encoder.encode(wrapper.getPasswd1()));
        account.setPlainPassword(wrapper.getPasswd1());
        account.setRoles(EnumSet.of(UserRole.ROLE_USER));
        
        String resultStatus = "success";
        try {
            if (account.getCustomer() != null) {
                customerService.addCustomer(account.getCustomer());
            }
            accountService.addAccount(account);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "account.message.add" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{account.getUsername()}, locale));
        return "redirect:" + uriBuilder.path(resultStatus.equals("success") ? "/auth/login" : "/accounts").queryParam("notification", resultStatus).build();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editAccount(
            @PathVariable long   id,
            ModelMap             model,
            RedirectAttributes   redirectAttributes,
            Locale               locale,
            UriComponentsBuilder uriBuilder) {
        AccountDTO account = null;
        
        try {
            account = accountService.getAccountById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        if(account == null) {
            return "account/list";
        }
        
        AccountWrapper wrapper = new AccountWrapper(account);        
        model.addAttribute("editWrapper", wrapper);
        return "account/edit";
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editAccount(
        @Validated @ModelAttribute("editWrapper") AccountWrapper wrapper, 
        BindingResult        bindingResult, 
        RedirectAttributes   redirectAttributes, 
        UriComponentsBuilder uriBuilder, 
        Locale               locale) {
        
        if (bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "account/edit";
        }
        
        String     resultStatus = "success";
        AccountDTO account      = wrapper.getAccount();
        
        if (! wrapper.isCustomer()) {
            // for some reason, null account.customer gets replaced by new CustomerDTO()
            wrapper.getAccount().setCustomer(null);
        }
        
        // make sure nothing else changed
        // this might be later changed to allow administrative changes (customer, roles...)
        AccountDTO original     = accountService.getAccountById(wrapper.getAccount().getId());
        if (  ! Objects.equals(original.getCustomer(), account.getCustomer())
           || ! Objects.equals(original.getUsername(), account.getUsername())
           || ! Objects.equals(original.getRoles(),    account.getRoles()   )) {
            logger.error("I feel a great disturbance in the Force...");
            logger.debug("{} ; {}", original.getCustomer(), account.getCustomer());
            logger.debug("{} ; {}", original.getUsername(), account.getUsername());
            logger.debug("{} ; {}", original.getRoles(),    account.getRoles());
            logger.info("About to throw an exception");
            throw new IllegalArgumentException("Prohibited fields changed in the account");
        }
        
        try {
            account.setPlainPassword(wrapper.getPasswd1());
            account.setPassword(encoder.encode(wrapper.getPasswd1()));
            accountService.updateAccount(account);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }
        
        String messageKey = "account.message.edit" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{wrapper.getAccount().getUsername()}, locale));
        return "redirect:" + uriBuilder.path("/accounts").queryParam("notification", resultStatus).build();
    }    
    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteAccount(
            @PathVariable long   id,
            ModelMap             model,
            RedirectAttributes   redirectAttributes,
            Locale               locale,
            UriComponentsBuilder uriBuilder) {
        AccountDTO account = null;
        
        try {
            account = accountService.getAccountById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        if(account == null) {
            return "account/list";
        }
        
        AccountWrapper wrapper = new AccountWrapper(account);
        model.addAttribute("deleteWrapper", wrapper);
        return "account/delete";
    }
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteAccount(
        @Validated @ModelAttribute("deleteWrapper") AccountWrapper wrapper, 
        BindingResult        bindingResult, 
        RedirectAttributes   redirectAttributes, 
        UriComponentsBuilder uriBuilder,
        Locale               locale) {
        
        if (bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "account/delete";
        }
        
        AccountDTO account      = wrapper.getAccount();
        String     resultStatus = "success";
        
        if (resultStatus.equals("success")) try {
            accountService.deleteAccountById(account.getId());
            
            // If user has removed his own account, invalidate session
            String username = AuthCommons.getUsername();
            if (username.equals(wrapper.getAccount().getUsername())) {
                logger.info("Invalidating session for '" + username + "'");
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        } catch (Exception e) {
            logger.error(e.getClass().getName() + " :: " + e.getMessage());
            resultStatus = "failure";
        }          

        String messageKey = "account.message.delete" + (resultStatus.equals("success") ? "" : ".error");
        String message    = messageSource.getMessage(messageKey, new Object[]{ account.getUsername() }, locale);
        redirectAttributes.addFlashAttribute(resultStatus + "Message", message);
        return "redirect:" + uriBuilder.path("/").queryParam("notification", resultStatus).build();        
    }
    
    private void logFormErrors(BindingResult bindingResult) {
        logger.debug("Encountered following errors when validating form.");
        
        for (ObjectError ge : bindingResult.getGlobalErrors()) {
            logger.debug("ObjectError: {}", ge);
        }
        
        for (FieldError fe : bindingResult.getFieldErrors()) {
            logger.debug("FieldError: {}", fe);
        }
    }
    
}
