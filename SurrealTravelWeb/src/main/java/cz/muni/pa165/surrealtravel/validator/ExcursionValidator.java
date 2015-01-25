package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.math.BigDecimal;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Form validator to be used in CustomerController.
 *
 * @author Petr Dvořák [359819]
 */
public class ExcursionValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return ExcursionDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {

        ExcursionDTO excursionDTO = (ExcursionDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destination", "excursion.validator.destination");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "excursion.validator.description");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "excursionDate", "excursion.validator.date");

        if (excursionDTO.getPrice() == null) {
            errors.rejectValue("price", "excursion.validator.price");
        } else if (excursionDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.rejectValue("price", "excursion.validator.pricezero");
        }

        if (excursionDTO.getDuration() == null) {
            errors.rejectValue("duration", "excursion.validator.duration");
        } else if (excursionDTO.getDuration() < 0) {
            errors.rejectValue("duration", "excursion.validator.durationzero");
        }

    }

}
