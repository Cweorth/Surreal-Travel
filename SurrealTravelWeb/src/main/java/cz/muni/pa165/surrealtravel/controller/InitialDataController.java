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
import javax.annotation.PostConstruct;
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
    
    @PostConstruct
    public void init() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        
        // Because we are using secured service methods, we need to do authentication.
        // User "pa165" is required to be in the DB - it is created by hibernate on
        // DAO layer (see import.sql).
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken("pa165", "pa165");
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
        
        String password = "password";
        AccountDTO a1 = new AccountDTO();
        a1.setUsername("username");
        a1.setPassword(encoder.encode(password));
        a1.setPlainPassword(password);
        a1.setRoles(EnumSet.of(UserRole.ROLE_USER));
        a1.setCustomer(c1);
        accountService.addAccount(a1);
        
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
    
    private Date mkdate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    } 
    
}
