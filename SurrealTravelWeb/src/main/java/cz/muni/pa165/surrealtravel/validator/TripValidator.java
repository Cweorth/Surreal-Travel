package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.utils.TripModelData;
import java.math.BigDecimal;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Trip validator
 * @author Roman Lacko [396157]
 */
public class TripValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return TripModelData.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "destination", "trip.validator.destination");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateFrom",    "trip.validator.dateFrom");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dateTo",      "trip.validator.dateTo");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "basePrice",   "trip.validator.basePrice");

        if (errors.hasErrors()) {
            return;
        }

        TripDTO trip = (TripDTO) o;

        if (trip.getDateFrom().after(trip.getDateTo())) {
            errors.rejectValue("dateTo", "trip.validator.timemachine");
        }

        if (trip.getBasePrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.rejectValue("basePrice", "trip.validator.negativeBasePrice");
        }
    }

}
