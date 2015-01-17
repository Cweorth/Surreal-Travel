package cz.muni.pa165.surrealtravel.controller;

import cz.muni.pa165.surrealtravel.dto.AccountDTO;
import cz.muni.pa165.surrealtravel.dto.CustomerDTO;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.ReservationDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import cz.muni.pa165.surrealtravel.dto.UserRole;
import cz.muni.pa165.surrealtravel.service.AccountService;
import cz.muni.pa165.surrealtravel.service.CustomerService;
import cz.muni.pa165.surrealtravel.service.ExcursionService;
import cz.muni.pa165.surrealtravel.service.ReservationService;
import cz.muni.pa165.surrealtravel.service.TripService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Roman Lacko [396157]
 */
@Controller
public class InitialDataController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private TripService tripService;
     
    @Autowired
    private ExcursionService excursionService;
    
    @Autowired
    private ReservationService reservationService;
    
    private static final Logger logger = LoggerFactory.getLogger(InitialDataController.class);
    
    private Date mkdate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }    
    
    private ExcursionDTO mkexcursion(Date date, int duration, String description, String destination, BigDecimal price) {
        ExcursionDTO excursion = new ExcursionDTO();
        excursion.setExcursionDate(date);
        excursion.setDuration(duration);
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setPrice(price);
        
        logger.info(String.format("[mk] Created %s", excursion.toString()));
        return excursion;
    }
    
    private TripDTO mktrip(Date from, Date to, String destination, int capacity, BigDecimal price) {
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        // Because we are using secured service methods, we need to do authentication.
        // User "root" is created by hibernate on DAO layer (see import.sql).
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("root", "root");
        SecurityContextHolder.getContext().setAuthentication(authRequest);

        // Available customers
        CustomerDTO c1 = new CustomerDTO();
        CustomerDTO c2 = new CustomerDTO();
        CustomerDTO c3 = new CustomerDTO();
        c1.setName("Twoflower");
        c1.setAddress("Discworld");
        c2.setName("Luggage");
        c2.setAddress("Discworld");
        c3.setName("Slash");
        c3.setAddress("City of Angels");
        customerService.addCustomer(c1);
        customerService.addCustomer(c2);
        customerService.addCustomer(c3);
        
        // Available accounts
        String passwd = "lysa had poisoned me";
        
        AccountDTO account = new AccountDTO();
        account.setUsername("johnarryn");
        account.setPassword(encoder.encode(passwd));
        account.setPlainPassword(passwd);
        
        account.setRoles(EnumSet.of(UserRole.ROLE_ADMIN));
        accountService.addAccount(account);
        
        String password = "pa165";
        AccountDTO a1 = new AccountDTO();
        AccountDTO a2 = new AccountDTO();
        a1.setUsername("pa165");
        a1.setPassword(encoder.encode(password));
        a1.setPlainPassword(password);
        a1.setRoles(EnumSet.of(UserRole.ROLE_ADMIN));
        password = "password";
        a2.setUsername("username");
        a2.setPassword(encoder.encode(password));
        a2.setPlainPassword(password);
        a2.setRoles(EnumSet.of(UserRole.ROLE_USER));
        a2.setCustomer(c1);
        accountService.addAccount(a1);
        accountService.addAccount(a2);
        
        // Availale excursions
        ExcursionDTO e1 = new ExcursionDTO();
        ExcursionDTO e2 = new ExcursionDTO();
        ExcursionDTO e3 = new ExcursionDTO();
        e1.setDestination("Afgánistán");
        e1.setDescription("Best of war");
        e1.setDuration(3);
        e1.setExcursionDate(mkdate(25, 6, 2014));
        e1.setPrice(new BigDecimal(150));
        e2.setDestination("Afgánistán");
        e2.setDescription("Best of army stock");
        e2.setDuration(2);
        e2.setExcursionDate(mkdate(26, 6, 2014));
        e2.setPrice(new BigDecimal(10));
        e3.setDestination("Afgánistán");
        e3.setDescription("Best places on cemetary");
        e3.setDuration(1);
        e3.setExcursionDate(mkdate(30, 6, 2014));
        e3.setPrice(new BigDecimal(5));
        excursionService.addExcursion(e1);
        excursionService.addExcursion(e2);
        excursionService.addExcursion(e3);

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
        
        // Available trips
        // TODO, there is just one for reservation, can be removed l8r
        TripDTO t1 = new TripDTO();       
        t1.setDateFrom(mkdate(21, 6, 2014));
        t1.setDateTo(mkdate(3, 7, 2014));
        t1.setDestination("Biking in Haradwaith regions");
        t1.setCapacity(15);
        t1.setBasePrice(new BigDecimal(11999));
        t1.setExcursions(Arrays.asList(new ExcursionDTO[] {e1, e2, e3}));
        tripService.addTrip(t1);
        
        // Available reservations
        ReservationDTO r1 = new ReservationDTO();
        r1.setTrip(t1);
        r1.addExcursion(e1);
        r1.addExcursion(e2);
        r1.setCustomer(c1);
        reservationService.addReservation(r1);
        
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);

    }
    
}
