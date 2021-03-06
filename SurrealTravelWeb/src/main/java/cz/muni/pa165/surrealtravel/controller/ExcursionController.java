package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.TripService;
import cz.muni.pa165.surrealtravel.validator.ExcursionValidator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Excursion controller.
 * @author Petr Dvořák [359819]
 */
@Controller
@RequestMapping("/excursions")
public class ExcursionController {

    final static Logger logger = LoggerFactory.getLogger(ExcursionController.class);

    @Autowired
    private ExcursionService excursionService;

    @Autowired
    private TripService tripService;

    @Autowired
    private MessageSource messageSource;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ExcursionValidator());

        CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true);
        binder.registerCustomEditor(Date.class, editor);
    }

    /**
     * Default page - list all excursions.
     * @param model
     * @return redirect
     */
    @RequestMapping(method = RequestMethod.GET)
    public String listExcrusions(ModelMap model) {
        List<ExcursionDTO> excursions = excursionService.getAllExcursions();

        // See if excursions have some attached Trips (used to deny the possiblity
        // to delete them in GUI).
        List<Integer> excursionsOccurence = new ArrayList<>(excursions.size());
        for(ExcursionDTO e : excursions)
            excursionsOccurence.add(tripService.getTripsWithExcursion(e).isEmpty() ? 0 : 1);

        model.addAttribute("excursions", excursions);
        model.addAttribute("excursionsOccurence", excursionsOccurence);
        model.addAttribute("excursionsOccurenceCheck", 1);
        return "excursion/list";
    }

    /**
     * Display a form for creating a new excursion.
     * @param model
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newExcursionForm(ModelMap model) {
        model.addAttribute("excursionDTO", new ExcursionDTO());
        return "excursion/new";
    }

    /**
     * Process POST request for creating of a new excursion.
     * @param excursionDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newExcursion(@Validated @ModelAttribute ExcursionDTO excursionDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {

        // check the form validator output
        if(bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "excursion/new";
        }

        String resultStatus = "success";
        // so far so good, try to save excursion
        try {
            excursionService.addExcursion(excursionDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "excursion.message.add" + (resultStatus.equals("success") ? "" : ".error");
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{excursionDTO.getDestination()}, locale));
        return "redirect:" + uriBuilder.path("/excursions").queryParam("notification", resultStatus).build();
    }

    /**
     * Display a form for editing of a excursion.
     * @param id
     * @param model
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editExcursionForm(@PathVariable long id, ModelMap model) {
        ExcursionDTO excursion = null;

        try {
            excursion = excursionService.getExcursionById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }

        if(excursion == null) return "excursion/list";

        model.addAttribute("excursionDTO", excursion);
        return "excursion/edit";
    }

    /**
     * Process POST request for editing of a excursion.
     * @param excursionDTO
     * @param bindingResult
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editExcursion(@Validated @ModelAttribute ExcursionDTO excursionDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String resultStatus = "success";

        // check the form validator output
        if(bindingResult.hasErrors()) {
            logFormErrors(bindingResult);
            return "excursion/edit";
        }

        // so far so good, try to save excursion
        try {
            excursionService.updateExcursion(excursionDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "excursion.message.edit" + (resultStatus.equals("success") ? "" : ".error");

        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{excursionDTO.getDestination()}, locale));

        // get back to excursion list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/excursions").queryParam("notification", resultStatus).build();
    }

    /**
     * Delete excursion with the given id.
     * @param id
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return redirect
     */
    @Secured("ROLE_STAFF")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteExcursion(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String resultStatus = "success";
        String destination  = null;

        try {
            ExcursionDTO excursion = excursionService.getExcursionById(id);
            destination = excursion.getDestination();

            excursionService.deleteExcursionById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
            resultStatus = "failure";
        }

        String messageKey = "excursion.message.delete" + (resultStatus.equals("success") ? "" : ".error");

        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute(resultStatus + "Message", messageSource.getMessage(messageKey, new Object[]{ destination }, locale));

        // get back to excursion list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/excursions").queryParam("notification", resultStatus).build();
    }

    /**
     * Check BindingResult of a request for errors found by a validator.
     * @param bindingResult
     * @param viewName
     * @return redirect
     */
    private void logFormErrors(BindingResult bindingResult) {
        logger.debug("Encountered following errors when validating form.");
        for(ObjectError ge : bindingResult.getGlobalErrors()) {
            logger.debug("ObjectError: {}", ge);
        }
        for(FieldError fe : bindingResult.getFieldErrors()) {
            logger.debug("FieldError: {}", fe);
        }
    }

}
