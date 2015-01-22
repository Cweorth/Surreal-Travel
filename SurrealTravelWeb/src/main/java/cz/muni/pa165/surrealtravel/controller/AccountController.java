package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.utils.AccountWrapper;
import cz.muni.pa165.surrealtravel.utils.AuthCommons;
import cz.muni.pa165.surrealtravel.validator.AccountEditValidator;
import cz.muni.pa165.surrealtravel.validator.AccountNewValidator;
import cz.muni.pa165.surrealtravel.validator.AccountPasswordValidator;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
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
    
    //--[  Convenience methods  ]-----------------------------------------------
    
    //<editor-fold desc="[  Roles  ]" defaultstate="collapsed">
    
    private boolean iAmUser() {
        return AuthCommons.hasRole(UserRole.ROLE_USER);
    }
    
    private boolean isUser(AccountDTO account) {
        return account.getRoles().contains(UserRole.ROLE_USER);
    }
    
    private boolean iAmAdmin() {
        return AuthCommons.hasRole(UserRole.ROLE_ADMIN);
    }
    
    private boolean isAdmin(AccountDTO account) {
        return account.getRoles().contains(UserRole.ROLE_ADMIN);
    }
    
    private boolean iAmRoot() {
        return AuthCommons.hasRole(UserRole.ROLE_ROOT);
    }
    
    private boolean isRoot(AccountDTO account) {
        return account.getRoles().contains(UserRole.ROLE_ROOT);
    }
    
    private boolean isSelf(AccountDTO account) {
        return Objects.equals(account.getUsername(), AuthCommons.getUsername());
    }
    
    //</editor-fold>
    
    //--[  Init binders  ]------------------------------------------------------
    
    @InitBinder(value = "newWrapper")
    public void initBinderNew(WebDataBinder binder) {
        binder.setValidator(new AccountNewValidator(accountService));
    }
    
    @InitBinder(value = "editWrapper")
    public void initBinderEdit(WebDataBinder binder) {
        binder.addValidators(new AccountPasswordValidator(), new AccountEditValidator());
    }
    
    @InitBinder(value = "deleteWrapper")
    public void initBinderDelete(WebDataBinder binder) {
        binder.addValidators(new AccountPasswordValidator());
    }
    
    //--[  Controller Methods  ]------------------------------------------------
    
    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    public String listAccounts(ModelMap model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "account/list";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newAccount(ModelMap model, UriComponentsBuilder uriBuilder) {
        // only guest or admin can access this page
        if (iAmUser() && !iAmAdmin()) {
            return AuthCommons.forceDenied(uriBuilder);
        }
        
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
        // only guest or admin can access this page
        if (iAmUser() && !iAmAdmin()) {
            return AuthCommons.forceDenied(uriBuilder);
        }
        
        if (bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "account/new";
        }
        
        AccountDTO account = wrapper.getAccount();
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

        String redirect   = iAmAdmin() ? "/accounts" : (resultStatus.equals("success") ? "/auth/login" :      "/");
        String messageKey = "account.message.add"   + (resultStatus.equals("success") ? ""            : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{account.getUsername()}, locale));
        return "redirect:" + uriBuilder.path(redirect).queryParam("notification", resultStatus).build();
    }

    @Secured("ROLE_USER")
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
        
        if (account == null) {
            return "redirect:" + uriBuilder.path("/404").build();
        }
        
        //======================================================================
        // the security specification for this operation is quite complex,
        // see Security.md for clarification
        //======================================================================
        
        // the following (!A || B) conditions should be seen as implication A => B
            //     I am not root ...
        if (       !iAmRoot()
            // ... but admin trying to edit root's account ...
            &&   (( iAmAdmin() &&  isRoot(account))
            // ... or user / staff trying to edit other user's account ...
               || (!iAmAdmin() && !isSelf(account)))) {
            // ... so I should be punished by error like a crook that I am :D
            return AuthCommons.forceDenied(uriBuilder);
        }
        
        // require my password?
        boolean password    = isSelf(account) || isAdmin(account);
        
        // can change target's permissions?
        boolean permissions = iAmAdmin() && !isRoot(account);
        
        // fool the authentication by setting admin's password to this account
        if (password && !isSelf(account)) {
            AccountDTO myself = accountService.getAccountByUsername(AuthCommons.getUsername());
            account.setPassword(myself.getPassword());
        }
        
        AccountWrapper wrapper = new AccountWrapper(account);
        wrapper.setReqpasswd(password);
        wrapper.setModperm(permissions);
        model.addAttribute("editWrapper", wrapper    );
        model.addAttribute("allRoles",    EnumSet.complementOf(EnumSet.of(UserRole.ROLE_ROOT)));
        return "account/edit";
    }
    
    @Secured("ROLE_USER")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editAccount(
        @Validated @ModelAttribute("editWrapper") AccountWrapper wrapper, 
        BindingResult        bindingResult, 
        RedirectAttributes   redirectAttributes, 
        UriComponentsBuilder uriBuilder,
        ModelMap             model,
        Locale               locale) {
        
        if (bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            model.addAttribute("allRoles", EnumSet.complementOf(EnumSet.of(UserRole.ROLE_ROOT)));
            return "account/edit";
        }
        
        String     resultStatus  = "success";
        String     messageSuffix = "";
        AccountDTO account       = wrapper.getAccount();
        
        // Same as previous
        if (    !iAmRoot()
            && (!iAmAdmin() || isRoot(account))
            && (!iAmUser()  || !isSelf(account))) {
            return AuthCommons.forceDenied(uriBuilder);
        }  
        
        if (! wrapper.isCustomer()) {
            // for some reason, null account.customer gets replaced by new CustomerDTO()
            wrapper.getAccount().setCustomer(null);
        }
        
        // make sure that neither customer nor username changed
        AccountDTO original     = accountService.getAccountById(wrapper.getAccount().getId());
        if (  ! Objects.equals(original.getCustomer(), account.getCustomer())
           || ! Objects.equals(original.getUsername(), account.getUsername())) {
            logger.error("Customer or username changed");
            logger.debug("customer original {} ; received {}", original.getCustomer(), account.getCustomer());
            logger.debug("username original {} ; received {}", original.getUsername(), account.getUsername());
            logger.info("About to throw an exception");
            throw new IllegalArgumentException("Prohibited fields changed in the account!");
        }
        
        // make sure that root's permissions are intact
        if (     isRoot(account)
            && ! Objects.equals(original.getRoles(), account.getRoles())) {
            logger.error("Root permissions were modified");
            throw new IllegalArgumentException("Cannot modify root's permissions!");
        }
        
        // make sure that only administrator can change roles
        if (  ! Objects.equals(original.getRoles(), account.getRoles())
           && ! iAmAdmin()) {
            logger.error("User role changed by non-privileged user");
            logger.debug("roles original {} ; received {}", original.getRoles(), account.getRoles());
            logger.info("About to throw an exception... that will show him!");
            throw new IllegalArgumentException("Illegal modification of user roles!");
        }
        
        if (StringUtils.isNotBlank(wrapper.getPasswd1())) {
            account.setPlainPassword(wrapper.getPasswd1());
            account.setPassword(encoder.encode(wrapper.getPasswd1()));
        } else {
            // hack to pass the password length constraint, the value will be ignored
            account.setPlainPassword("----");
        }
        
        try {
            accountService.updateAccount(account);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus  = "failure";
            messageSuffix = ".error";
        }
        
        // if an administrator renounces his administrator rights, log him off
        if ((isSelf(account)) && (!account.getRoles().contains(UserRole.ROLE_ADMIN))) {
            SecurityContextHolder.getContext().setAuthentication(null);
            messageSuffix = ".loggedOut";
        }
        
        String messageKey = "account.message.edit" + messageSuffix;
        redirectAttributes.addFlashAttribute(resultStatus + "Message", 
                messageSource.getMessage(messageKey, new Object[]{ wrapper.getAccount().getUsername() }, locale));
        return "redirect:" + uriBuilder.path(iAmAdmin() ? "/accounts" : "/").queryParam("notification", resultStatus).build();
    }
    
    @Secured("ROLE_USER")
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
        
        if (account == null) {
            return "redirect:" + uriBuilder.path("/404").build();
        }
          
        // we cannot delete root,
        // only admin and the user with the specified ID can access this page
        if (isRoot(account) || (!iAmAdmin() && !isSelf(account))) {
            return AuthCommons.forceDenied(uriBuilder);
        }
                
        // does the user need to provide credentials?
        if (!isSelf(account) && !isAdmin(account)) {
            // fast-forward delete
            String resultStatus = doDeleteAccount(account);
            String messageKey   = "account.message.delete" + (resultStatus.equals("success") ? "" : ".error");
            String message      = messageSource.getMessage(messageKey, new Object[]{ account.getUsername() }, locale);
            redirectAttributes.addFlashAttribute(resultStatus + "Message", message);
            return "redirect:" + uriBuilder.path(iAmAdmin() ? "/accounts" : "/").queryParam("notification", resultStatus).build(); 
        }
        
        // fool the authentication by setting admin's password to this account
        if (!isSelf(account)) {
            AccountDTO myself = accountService.getAccountByUsername(AuthCommons.getUsername());
            account.setPassword(myself.getPassword());
        }
        
        AccountWrapper wrapper = new AccountWrapper(account);
        wrapper.setReqpasswd(true);
        model.addAttribute("deleteWrapper", wrapper);
        return "account/delete";
    }
    
    @Secured("ROLE_USER")
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
        String     resultStatus = doDeleteAccount(account);
        String     messageKey   = "account.message.delete" + (resultStatus.equals("success") ? "" : ".error");
        String     message      = messageSource.getMessage(messageKey, new Object[]{ account.getUsername() }, locale);
        redirectAttributes.addFlashAttribute(resultStatus + "Message", message);
        return "redirect:" + uriBuilder.path(iAmAdmin() ? "/accounts" : "/").queryParam("notification", resultStatus).build();        
    }
    
    // common method for account deletion
    private String doDeleteAccount(AccountDTO account) {
        String resultStatus = "success";
        
        try {
            accountService.deleteAccountById(account.getId());
            
            // if user has removed his own account, invalidate session
            String username = AuthCommons.getUsername();
            if (username.equals(account.getUsername())) {
                logger.info("Invalidating session for '" + username + "'");
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        } catch (Exception e) {
            logger.error(e.getClass().getName() + " :: " + e.getMessage());
            resultStatus = "failure";
        }
        
        return resultStatus;
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
