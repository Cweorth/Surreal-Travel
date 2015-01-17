package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.utils.AccountWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Roman Lacko [396157]
 */
public class AccountEditValidator implements Validator {
   
    @Override
    public boolean supports(Class<?> clazz) {
        return AccountWrapper.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd1", "account.validator.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd2", "account.validator.password");
        
        AccountWrapper wrapper = (AccountWrapper) target;
        
        if (! (errors.hasFieldErrors("passwd1") && errors.hasFieldErrors("passwd2"))) {
            if (! StringUtils.equals(wrapper.getPasswd1(), wrapper.getPasswd2())) {
                errors.rejectValue("passwd2", "account.validator.password.mismatch");
            } else if ((wrapper.getPasswd1().length() < 5) || (wrapper.getPasswd1().length() > 32)) {
                errors.rejectValue("passwd2", "account.validator.password.length");
            }
        }     
    }
    
}
