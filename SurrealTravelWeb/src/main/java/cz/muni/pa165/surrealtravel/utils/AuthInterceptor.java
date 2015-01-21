package cz.muni.pa165.surrealtravel.utils;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * This interceptor determines, whether ROLE_USER or ROLE_STAFF authenticated user
 * has Customer attached to the Account. According to this, the appropriate value
 * is set to the model, so that it can be easily used to show/hide some parts of
 * the GUI. Interceptor is neccessary, as having (or not) attached the Customer is not
 * possible to find out directly from auth object or roles.
 * @author Jan Klimeš [374259]
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired
    private AccountService accountService;
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && AuthCommons.isAuthenticated()) {

            AccountDTO account = accountService.getAccountByUsername(AuthCommons.getUsername());
            if(account == null) return;
            
            modelAndView.addObject("userId", account.getId());
            if(account.getCustomer() != null) modelAndView.addObject("customerId", account.getCustomer().getId());
            
            if(!AuthCommons.hasRole(UserRole.ROLE_STAFF)) {

                // ROLE_USER but not ROLE_STAFF 
                if(account.getCustomer() == null)
                    modelAndView.addObject("roleUserNoCustomer", true); // no Customer, hide reservation and customer links

            } 
            
            if(AuthCommons.hasRole(UserRole.ROLE_STAFF) && !AuthCommons.hasRole(UserRole.ROLE_ADMIN)) {

                // ROLE_STAFF but not ROLE_ADMIN
                if(account.getCustomer() == null)
                    modelAndView.addObject("roleStaffNoCustomer", true); // no Customer, hide customer links
                
            }
            
        }
    }

}
