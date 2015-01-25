package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.service.AccountService;
import cz.muni.pa165.surrealtravel.utils.AccountWrapper;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author Roman Lacko [396157]
 */
public class AccountNewValidator implements Validator {

    private        final AccountService accountService;
    private static final Pattern        username = Pattern.compile("[a-z0-9]+");

    public AccountNewValidator(AccountService accountService) {
        this.accountService = Objects.requireNonNull(accountService, "accountService");
    }

    @Override
    public boolean supports(Class<?> type) {
        return AccountWrapper.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "account.username", "account.validator.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd1",          "account.validator.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwd2",          "account.validator.password");

        AccountWrapper wrapper = (AccountWrapper) o;

        if (wrapper.isCustomer()) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "account.customer.name", "customer.validator.name");
        }

        if (! (errors.hasFieldErrors("passwd1") && errors.hasFieldErrors("passwd2"))) {
            if (! StringUtils.equals(wrapper.getPasswd1(), wrapper.getPasswd2())) {
                errors.rejectValue("passwd2", "account.validator.password.mismatch");
            } else if ((wrapper.getPasswd1().length() < 4) || (wrapper.getPasswd1().length() > 32)) {
                errors.rejectValue("passwd2", "account.validator.password.length");
            }
        }

        if (! errors.hasFieldErrors("account.username")) {
            Matcher matcher = username.matcher(wrapper.getAccount().getUsername());
            if (! matcher.matches()) {
                errors.rejectValue("account.username", "account.validator.username.format");
            } else if (accountService.getAccountByUsername(wrapper.getAccount().getUsername()) != null) {
                errors.rejectValue("account.username", "account.validator.username.used");
            }
        }
    }
}
