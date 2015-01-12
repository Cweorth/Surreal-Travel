package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.utils.AccountWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Roman Lacko [396157]
 */
public class AccountEditValidator implements Validator {

    private final static Logger logger  = LoggerFactory.getLogger(AccountEditValidator.class);
    
    @Override
    public boolean supports(Class<?> clazz) {
        return AccountWrapper.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "account.plainPassword", "account.validator.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd1",               "account.validator.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd2",               "account.validator.password");
        
        AccountWrapper wrapper = (AccountWrapper) target;
        
        logger.debug("Validating account: {}", wrapper.getAccount());
        logger.debug("Passwd1: {}", wrapper.getPasswd1());
        logger.debug("Passwd2: {}", wrapper.getPasswd2());
        
        if (! (errors.hasFieldErrors("account.plainPassword") && errors.hasFieldErrors("passwd1") && errors.hasFieldErrors("passwd2"))) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            
            if (! encoder.matches(wrapper.getAccount().getPlainPassword(), wrapper.getAccount().getPassword())) {
                errors.rejectValue("account.plainPassword", "account.validator.password.invalid");
            }
            
            if (! StringUtils.equals(wrapper.getPasswd1(), wrapper.getPasswd2())) {
                errors.rejectValue("passwd2", "account.validator.password.mismatch");
            } else if ((wrapper.getPasswd1().length() < 5) || (wrapper.getPasswd1().length() > 32)) {
                errors.rejectValue("passwd2", "account.validator.password.length");
            }
        }     
    }
    
}
