package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.validator.ExcursionValidator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
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
    private MessageSource messageSource;
      
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new ExcursionValidator());
        
        CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("MM/DD/YYYY"), true);
        binder.registerCustomEditor(Date.class, editor);
    }
    
    /**
     * Default page - list all excursions.
     * @param model
     * @return 
     */
    @RequestMapping(method = RequestMethod.GET)
    public String listExcrusions(ModelMap model) {
        model.addAttribute("excursions", excursionService.getAllExcursions());
        return "excursion/list";
    }
    
    /**
     * Display a form for creating a new excursion.
     * @param model
     * @return 
     */
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
     * @return 
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newExcursion(@Validated @ModelAttribute ExcursionDTO excursionDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {       
        
        // check the form validator output
        if(bindingResult.hasErrors()) {
            String error = checkFormErrors(bindingResult, "excursion/new");
            if(error != null) return error;
        }

        // so far so good, try to save excursion
        try {
            excursionService.addExcursion(excursionDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "excursion/new";
        }
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("excursion.message.new", new Object[]{excursionDTO.getDescription()}, locale));
        
        // get back to excursion list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/excursion").queryParam("notification", "success").build();
    }
    
    /**
     * Display a form for editing of a excursion.
     * @param id
     * @param model
     * @return 
     */
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
     * @return 
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editExcursion(@Validated @ModelAttribute ExcursionDTO excursionDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {       
        
        // check the form validator output
        if(bindingResult.hasErrors()) {
            String error = checkFormErrors(bindingResult, "excursion/edit");
            if(error != null) return error;
            
        }

        // so far so good, try to save excursion
        try {
            excursionService.updateExcursion(excursionDTO);
        } catch(NullPointerException | IllegalArgumentException e) {
            logger.error(e.getMessage());
            return "excursion/edit";
        }
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("excursion.message.edit", new Object[]{excursionDTO.getDescription()}, locale));
        
        // get back to excursion list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/excursions").queryParam("notification", "success").build();
    }
    
    /**
     * Delete excursion with the given id.
     * @param id
     * @param redirectAttributes
     * @param uriBuilder
     * @param locale
     * @return 
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteExcursion(@PathVariable long id, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, Locale locale) {
        String description = null;
        
        try {
            ExcursionDTO excursion = excursionService.getExcursionById(id);
            description = excursion.getDescription();
            excursionService.deleteExcursionById(id);
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
        
        if(description == null) return "excursion/list";
        
        // add to the view message about successfull result
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("excursion.message.delete", new Object[]{description}, locale));
        
        // get back to excursion list, add the notification par to the url
        return "redirect:" + uriBuilder.path("/excursions").queryParam("notification", "success").build();
    }
 
    /**
     * Check BindingResult of a request for errors found by a validator.
     * @param bindingResult
     * @param viewName
     * @return 
     */
    private String checkFormErrors(BindingResult bindingResult, String viewName) {
        logger.debug("Encountered following errors when validating form.");
        for(ObjectError ge : bindingResult.getGlobalErrors()) {
            logger.debug("ObjectError: {}", ge);
        }
        for(FieldError fe : bindingResult.getFieldErrors()) {
            logger.debug("FieldError: {}", fe);
        }
        return viewName;
    }
    
    @PostConstruct
    public void init() {
        ExcursionDTO e1 = new ExcursionDTO();
        e1.setDestination("Afgánistán");
        e1.setDescription("Best of war");
        e1.setDuration(2);
        e1.setExcursionDate(new Date());
        e1.setPrice(new BigDecimal(0));
        
        ExcursionDTO e2 = new ExcursionDTO();
        e2.setDestination("Afgánistán");
        e2.setDescription("Best of army stock");
        e2.setDuration(5);
        e2.setExcursionDate(new Date());
        e2.setPrice(new BigDecimal(10));
        
        ExcursionDTO e3 = new ExcursionDTO();
        e3.setDestination("Afgánistán");
        e3.setDescription("Best places on cemetary");
        e3.setDuration(1);
        e3.setExcursionDate(new Date());
        e3.setPrice(new BigDecimal(5));
        
        excursionService.addExcursion(e1);
        excursionService.addExcursion(e2);
        excursionService.addExcursion(e3);
    }
    
}
