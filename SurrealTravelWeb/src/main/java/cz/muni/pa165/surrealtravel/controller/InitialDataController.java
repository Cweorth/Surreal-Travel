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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

/**
 * Controller that inserts default and demo entities into the system.
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
    
    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    private static final Logger                logger  = LoggerFactory.getLogger(InitialDataController.class);
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private List<ExcursionDTO>    excursions;
    private List<TripDTO>         trips;
    private List<CustomerDTO>     customers;
    private List<ReservationDTO>  reservations;
    private List<AccountDTO>      accounts;

    private Date mkdate(int day, int month, int year) {
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

    private ExcursionDTO mkexcursion(Date date, int duration, String description, String destination, int price) {
        ExcursionDTO excursion = new ExcursionDTO();
        excursion.setExcursionDate(date);
        excursion.setDuration(duration);
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setPrice(new BigDecimal(price));

        logger.info("[init-mk] excursion: " + excursion.toString());
        return excursion;
    }

    private TripDTO mktrip(Date from, Date to, String destination, int capacity, int price, int... excursionIxs) {
        TripDTO trip = new TripDTO();
        trip.setDateFrom(from);
        trip.setDateTo(to);
        trip.setDestination(destination);
        trip.setCapacity(capacity);
        trip.setBasePrice(new BigDecimal(price));

        for(int eid : excursionIxs) {
            trip.addExcursion(excursions.get(eid));
        }

        logger.info("[init-mk] trip: " + trip.toString());
        return trip;
    }

    private CustomerDTO mkcustomer(String name, String address) {
        CustomerDTO customer = new CustomerDTO();
        customer.setName(name);
        customer.setAddress(address);

        logger.info("[init-mk] customer: " + customer.toString());
        return customer;
    }

    private AccountDTO mkaccount(String username, String password, UserRole role, CustomerDTO customer) {
        AccountDTO account = new AccountDTO();
        account.setUsername(username);
        account.setPlainPassword(password);
        account.setPassword(encoder.encode(password));
        account.setRoles(EnumSet.of(role));
        account.setCustomer(customer);

        logger.info("[init-mk] account: " + account.toString());
        return account;
    }

    private AccountDTO mkaccount(String username, String password, UserRole role, int customerIx) {
        return mkaccount(username, password, role, customers.get(customerIx));
    }

    private ReservationDTO mkresrv(int customerIx, int tripIx, int... excursionIxs) {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setCustomer(customers.get(customerIx));
        reservation.setTrip(trips.get(tripIx));

        for (int ix : excursionIxs) {
            reservation.getExcursions().add(excursions.get(ix));
        }

        logger.info("[init-mk] reservation: " + reservation.toString());
        return reservation;
    }

    private int[] range(int from, int to) {
        int[] result = new int[to - from + 1];
        for (int ix = 0; ix < result.length; ++ix) {
            result[ix] = ix + from;
        }

        return result;
    }

    @PostConstruct
    public void init() {
        // if there is either rest or pa165, return
        if (   (accountService.getAccountByUsername("rest")  != null)
            || (accountService.getAccountByUsername("pa165") != null)) {
            return;
        }
        
        // Because we are using secured service methods, we need to do authentication.
        // User "root" is created by hibernate on DAO layer (see import.sql).
        // All of the following code is therefore done using root's credentials.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("root", "root");
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //----------------------------------------------------------------------
        //  Core accounts (except root) specified in Security.md
        //----------------------------------------------------------------------
        
        CustomerDTO pa165 = mkcustomer("PA165", "Faculty of Informatics, Masaryk University, Brno");
        customerService.addCustomer(pa165);
        accountService.addAccount(mkaccount("rest",  "rest",  UserRole.ROLE_STAFF,  null));
        accountService.addAccount(mkaccount("pa165", "pa165", UserRole.ROLE_ADMIN, pa165));

        //----------------------------------------------------------------------
        //  Excursions
        //----------------------------------------------------------------------

        excursions = Arrays.asList(
        // The Lord of the Rings
        /* 00 */mkexcursion(mkdate(20, 10, 2941),  2, "Battle of Five Armies",              "Erebor",         1500),
        /* 01 */mkexcursion(mkdate( 1,  2, 3017),  3, "Flora and Fauna of Dead Marches",    "Dead Marches",    899),
        /* 02 */mkexcursion(mkdate(25, 10, 3018),  1, "Council of Elrond",                  "Rivendell",      2799),
        /* 03 */mkexcursion(mkdate( 2,  3, 3019),  1, "Destruction of Isengard",            "Isengard",        799),
        /* 04 */mkexcursion(mkdate( 3,  3, 3019),  1, "Battle of Hornburg",                 "Helm's Deep",    2350),
        /* 05 */mkexcursion(mkdate(14,  3, 3019),  3, "Mt Doom Excursion",                  "Mordor",          899),
        /* 06 */mkexcursion(mkdate(14,  3, 3019), 12, "The Siege of Minas Tirith",          "Minas Tirith",   1300),
        /* 07 */mkexcursion(mkdate(25,  3, 3019),  2, "Downfall of Barad-dûr",              "Mordor",          999),
        /* 08 */mkexcursion(mkdate( 1,  5, 3019),  2, "Coronation of King Aragorn Elessar", "Minas Tirith",   4300),
        /* 09 */mkexcursion(mkdate(10,  7, 3019),  1, "The Funeral of Théoden",             "Edoras",         3299),

        // Game of Thrones
        /* 10 */mkexcursion(mkdate( 7,  1,    2),  1, "Aegon's Landing",                    "King's Landing", 1700),
        /* 11 */mkexcursion(mkdate(14,  9,  130),  9, "Dance over Harrenhal (w/ Dragons)",  "Harrenhal",      2699),
        /* 12 */mkexcursion(mkdate( 3,  1,  299),  2, "Storming of the Crag",               "The Crag",        600),
        /* 13 */mkexcursion(mkdate(14,  3,  299),  1, "Red Wedding",                        "The Twins",      1399),
        /* 14 */mkexcursion(mkdate(17,  8,  299),  2, "Fall of Astapor",                    "Astapor",         750),
        /* 15 */mkexcursion(mkdate( 9, 11,  299),  3, "Siege of Meereen",                   "Meereen",         999),

        // Special offer "Wherever the Wind Takes Me"
        /* 16 */mkexcursion(mkdate(17, 10, 2015),  2, "Surprise Excursion 1",                  "Location 1",  1300),
        /* 17 */mkexcursion(mkdate(24, 11, 2015),  7, "Surprise Excursion 2",                  "Location 2",  3200),
        /* 18 */mkexcursion(mkdate( 5, 12, 2015),  4, "Surprise Excursion 3",                  "Location 3",  1499),
        /* 19 */mkexcursion(mkdate(19, 12, 2015),  3, "Surprise Excursion (iPad owners only)", "Location 4", 24399),

        // Alpha Centauri Bb
        /* 20 */mkexcursion(mkdate(20,  2, 2139),  2, "Interdimensional Drift",                "Bb Capital",   499),
        /* 21 */mkexcursion(mkdate(25,  2, 2139),  3, "Tachyon Mountains Hiking",              "Bb Capital",   799),
        /* 22 */mkexcursion(mkdate(10,  3, 2139),  2, "Muon Cubicuboctahedron",                "Star Planes",  539)
        );

        for (ExcursionDTO excursion : excursions) {
            excursionService.addExcursion(excursion);
        }

        //----------------------------------------------------------------------
        //  Trips
        //----------------------------------------------------------------------

        trips = Arrays.asList(
        /* 00 */mktrip(mkdate(15, 10, 2941), mkdate(25, 10, 2941), "Erebor",                      50,  7399,            0 ),
        /* 01 */mktrip(mkdate(30,  1, 3017), mkdate(15,  7, 3019), "Middle-Earth",               100, 14999, range( 2,  9)),
        /* 02 */mktrip(mkdate(12,  3, 3019), mkdate( 5,  5, 3019), "Gondor",                     100,  3999,        6,  8 ),
        /* 03 */mktrip(mkdate( 6,  1,    2), mkdate(12,  1,    2), "Ancient Westeros",            75,  1999,           10 ),
        /* 04 */mktrip(mkdate( 1,  1,  299), mkdate(16,  3,  299), "Westeros",                   150,  4590,       12, 13 ),
        /* 05 */mktrip(mkdate(15,  8,  299), mkdate(15, 11,  299), "Essos",                       60,  2400,       14, 15 ),
        /* 06 */mktrip(mkdate(15, 10, 2015), mkdate(30, 12, 2015), "Wherever The Wind Takes Me", 100,  9999, range(16, 19)),
        /* 07 */mktrip(mkdate(19,  2, 2139), mkdate(23,  3, 2139), "Alpha Centauri Bb",          400,   699,   20, 21, 22 )
        );

        for (TripDTO trip : trips) {
            tripService.addTrip(trip);
        }

        //----------------------------------------------------------------------
        //  Customers
        //----------------------------------------------------------------------

        customers = Arrays.asList(
        /* 00 */mkcustomer("Gandalf the Grey",   "All Over Middle-Earth"),
        /* 01 */mkcustomer("Gandalf the White",  "All Over Middle-Earth"),
        /* 02 */mkcustomer("Bilbo Baggins",      "Bag End"),
        /* 03 */mkcustomer("Frodo Baggins",      "Bag End"),
        /* 04 */mkcustomer("Aragorn",            "Minas Tirith"),
        /* 05 */mkcustomer("Aegon I Targaryen",  "Dragonstone"),
        /* 06 */mkcustomer("Robb Start",         "Winterfell"),
        /* 07 */mkcustomer("Daenerys Targaryen", "Vaes Dothrak")
        );

        for (CustomerDTO customer : customers) {
            customerService.addCustomer(customer);
        }

        //----------------------------------------------------------------------
        //  Accounts
        //----------------------------------------------------------------------

        accounts = Arrays.asList(
        /* 00 */mkaccount("gandalf",  "mithrandir",   UserRole.ROLE_STAFF, 0),
        /* 01 */mkaccount("gandalfw", "incanus",      UserRole.ROLE_ADMIN, 1),
        /* 02 */mkaccount("bilbo",    "arkenstone",   UserRole.ROLE_USER,  2),
        /* 03 */mkaccount("frodo",    "the_ring",     UserRole.ROLE_USER,  3),
        /* 04 */mkaccount("aragorn",  "telcontar",    UserRole.ROLE_STAFF, 4),
        /* 05 */mkaccount("aegon",    "balerion",     UserRole.ROLE_USER,  5),
        /* 06 */mkaccount("robb",     "glass_garden", UserRole.ROLE_USER,  6),
        /* 07 */mkaccount("dany",     "stormborn",    UserRole.ROLE_ADMIN, 7)
        );

        for (AccountDTO account : accounts) {
            accountService.addAccount(account);
        }

        //----------------------------------------------------------------------
        //  Reservations
        //----------------------------------------------------------------------

        reservations = Arrays.asList(
        //              C  T    Excursions
        /* 00 */mkresrv(0, 0,    0),
        /* 01 */mkresrv(0, 1,    2,  4,  6),
        /* 02 */mkresrv(1, 1,    8),
        /* 03 */mkresrv(2, 0,    0),
        /* 04 */mkresrv(3, 1,    5,  7,  8),
        /* 05 */mkresrv(4, 2,    6,  8),
        /* 06 */mkresrv(5, 3,   10),
        /* 07 */mkresrv(6, 4,   12, 13),
        /* 08 */mkresrv(7, 5,   14, 15)
        );

        for (ReservationDTO reservation : reservations) {
            reservationService.addReservation(reservation);
        }

        //----------------------------------------------------------------------
        //  Logout the root
        //----------------------------------------------------------------------

        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
}
