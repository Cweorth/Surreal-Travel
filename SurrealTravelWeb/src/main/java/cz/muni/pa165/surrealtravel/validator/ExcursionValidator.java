package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Form validator to be used in CustomerController.
 * @author Petr Dvořák [359819]
 */
public class ExcursionValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return ExcursionDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destination", "excursion.validator.destination");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "excursion.validator.description");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "excursion.validator.price");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "excursion.validator.date");
    }

}
