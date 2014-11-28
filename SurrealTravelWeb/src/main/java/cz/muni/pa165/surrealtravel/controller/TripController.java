package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.TripService;
import cz.muni.pa165.surrealtravel.utils.TripModelData;
import cz.muni.pa165.surrealtravel.validator.TripValidator;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The Trip controller class
 * @author Roman Lacko [396157]
 */
@Controller
@RequestMapping("/trips")
public class TripController {
    
    final static Logger logger = LoggerFactory.getLogger(TripController.class);
    
    @Autowired 
    private TripService tripService;
    
    @Autowired
    private ExcursionService excursionService;
    
    @Autowired
    private MessageSource messageSource;
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new TripValidator());
        
        CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true);
        binder.registerCustomEditor(Date.class, editor);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String listTrips(ModelMap model) {
        model.addAttribute("trips", tripService.getAllTrips());
        return "trip/list";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newTripForm(ModelMap model) {
        model.addAttribute("tripdata", new TripModelData(new TripDTO(), excursionService.getAllExcursions()));
        return "trip/new";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String newTrip(@Validated @ModelAttribute("tripdata") TripModelData data, 
            BindingResult bindingResult, 
            RedirectAttributes redirectAttributes, 
            UriComponentsBuilder uriBuilder, 
            Locale locale) {
        
        List<ExcursionDTO> excursions = excursionService.getAllExcursions();
        data.setAllExcursions(excursions);
        
        if (bindingResult.hasErrors()) {
            return "trip/new";
        }
        
        try {
            TripDTO trip = data.unwrap();
            
            if ((data.getExcursionIDs() != null) && !data.getExcursionIDs().isEmpty()) {
                Map<Long, ExcursionDTO> exmap = new HashMap<>(excursions.size());
                for(ExcursionDTO ex : excursions) {
                    exmap.put(ex.getId(), ex);
                }
                
                for(long id : data.getExcursionIDs()) {
                    ExcursionDTO ex = exmap.get(id);
                    exmap.remove(id);
                    
                    Date     exStart  = ex.getExcursionDate();
                    Calendar calendar = Calendar.getInstance();
            
                    calendar.setTime(exStart);
                    calendar.add(Calendar.DATE, ex.getDuration());
            
                    Date exEnd = calendar.getTime();                    
                    
                    if (exEnd.after(trip.getDateTo()) || exStart.before(trip.getDateFrom())) {
                        String message = messageSource.getMessage("trip.validator.invalidExcursion", 
                                                       new Object[]{ ex.getDestination() }, locale);
                        bindingResult.addError(new FieldError("data", "excursions", message));
                        break;
                    }
                    
                    trip.addExcursion(ex);
                }
            }
            
            if (bindingResult.hasErrors()) {
                logger.error(bindingResult.getAllErrors().toString());
                return "trip/new";
            }
                        
            tripService.addTrip(trip);
        } catch(NullPointerException | IllegalArgumentException e) {
            bindingResult.addError(new ObjectError("data", e.getMessage()));
            logger.error(e.getMessage());
            return "trip/new";
        }
        
        redirectAttributes.addFlashAttribute("successMessage", messageSource.getMessage("trip.message.add", new Object[]{data.getDestination()}, locale));
        return "redirect:" + uriBuilder.path("/trips").queryParam("notification", "success").build();
    }
    
    protected Date mkdate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }    
    
    protected ExcursionDTO mkexcursion(Date date, int duration, String description, String destination, BigDecimal price) {
        ExcursionDTO excursion = new ExcursionDTO();
        excursion.setExcursionDate(date);
        excursion.setDuration(duration);
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setPrice(price);
        
        logger.info(String.format("[mk] Created %s", excursion.toString()));
        return excursion;
    }
    
    protected TripDTO mktrip(Date from, Date to, String destination, int capacity, BigDecimal price) {
        TripDTO trip = new TripDTO();
        trip.setDateFrom(from);
        trip.setDateTo(to);
        trip.setDestination(destination);
        trip.setCapacity(capacity);
        trip.setBasePrice(price);
        
        logger.info(String.format("[mk] Created %s", trip.toString()));
        return trip;
    }    
    
    @PostConstruct
    public void init() {
        List<ExcursionDTO> excursions = Arrays.asList(
            mkexcursion(mkdate(20, 10, 2941), 2, "Battle of Five Armies",   "Erebor",      new BigDecimal(500)),
            mkexcursion(mkdate(25, 10, 3018), 1, "Council of Elrond",       "Rivendell",   new BigDecimal(150)),
            mkexcursion(mkdate(02, 03, 3019), 1, "Destruction of Isengard", "Isengard",    new BigDecimal(400)),
            mkexcursion(mkdate(03, 03, 3019), 1, "Battle of Hornburg",      "Helm's Deep", new BigDecimal(350)),
            mkexcursion(mkdate(14, 03, 3019), 3, "Mt Doom Excursion",       "Mordor",      new BigDecimal(200)),
            mkexcursion(mkdate(25, 03, 3019), 2, "Downfall of Barad-dûr",   "Mordor",      new BigDecimal(300))
        );
        
        for(ExcursionDTO e : excursions) {
            excursionService.addExcursion(e);
        }
        
        List<TripDTO> trips = Arrays.asList(
            mktrip(mkdate(11, 10, 2960), mkdate(18, 11, 2960), "Wonderful Moria",   20, new BigDecimal(800)),
            mktrip(mkdate(24, 10, 3018), mkdate(25, 03, 3019), "The Life of Frodo", 45, new BigDecimal(900))
        );
        
        trips.get(1).setExcursions(excursions.subList(1, 5));
        
        for(TripDTO t : trips) {
            tripService.addTrip(t);
        }
    }
    
}