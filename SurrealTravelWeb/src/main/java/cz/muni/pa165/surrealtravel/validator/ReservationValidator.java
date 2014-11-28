/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.pa165.surrealtravel.validator;

import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author Tomáš Kácel [359965]
 */
public class ReservationValidator implements Validator {
    
    @Override
    public boolean supports(Class<?> type) {
        return ReservationDTO.class.isAssignableFrom(type);
    }
    @Override
    public void validate(Object o, Errors errors) {
        //ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "reservation.validator.name");
    }
    
}
