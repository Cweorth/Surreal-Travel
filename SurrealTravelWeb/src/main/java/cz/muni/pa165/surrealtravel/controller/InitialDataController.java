package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import java.util.EnumSet;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Roman Lacko [396157]
 */
@Controller
public class InitialDataController {
    
    @Autowired
    private AccountService accountService;
    
    @PostConstruct
    public void init() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        String passwd = "lysa had poisoned me";
        
        AccountDTO account = new AccountDTO();
        account.setUsername("johnarryn");
        account.setPassword(encoder.encode(passwd));
        account.setPlainPassword(passwd);
        
        account.setRoles(EnumSet.of(UserRole.ROLE_ADMIN));
        accountService.addAccount(account);
    }
    
}
