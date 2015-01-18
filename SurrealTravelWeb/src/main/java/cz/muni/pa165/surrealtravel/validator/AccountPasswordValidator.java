package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.utils.AccountWrapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author cweorth
 */
public class AccountPasswordValidator implements Validator {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountWrapper.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountWrapper wrapper = (AccountWrapper) target;
        
        if (wrapper.isReqpasswd()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "account.plainPassword", "account.validator.password");
            
            if (   !errors.hasFieldErrors("account.plainPassword") 
                && !encoder.matches(wrapper.getAccount().getPlainPassword(), wrapper.getAccount().getPassword())) {
                errors.rejectValue("account.plainPassword", "account.validator.password.invalid");
            }
        }
    }
}
