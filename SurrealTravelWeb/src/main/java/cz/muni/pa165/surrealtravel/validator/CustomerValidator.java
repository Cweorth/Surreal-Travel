package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Form validator to be used in CustomerController.
 * @author Jan Klimeš [374259]
 */
public class CustomerValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return CustomerDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "customer.validator.name");
    }

}
