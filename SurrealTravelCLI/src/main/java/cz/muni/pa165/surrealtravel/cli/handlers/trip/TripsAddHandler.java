package cz.muni.pa165.surrealtravel.cli.handlers.trip;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.handlers.BigDecimalOptionHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.DateOptionHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.LongArrayOptionHandler;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableTrip;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.kohsuke.args4j.Option;

/**
 * The {@code trips-add} command handler
 *
 * @author Roman Lacko [396157]
 */
public class TripsAddHandler implements CommandHandler {

    @Option(name = "--from", aliases = {"-f"}, handler = DateOptionHandler.class, metaVar = "date", required = true, usage = "trip start [yyyy/MM/dd]")
    private Date dateFrom;

    @Option(name = "--to", aliases = {"-t"}, handler = DateOptionHandler.class, metaVar = "date", required = true, usage = "trip end [yyyy/MM/dd]")
    private Date dateTo;

    @Option(name = "--destination", aliases = {"-d"}, metaVar = "destination", required = true, usage = "trip destination")
    private String destination;

    @Option(name = "--capacity", aliases = {"-c"}, metaVar = "capacity", required = true, usage = "trip capacity")
    private int capacity;

    @Option(name = "--basePrice", aliases = {"-p"}, handler = BigDecimalOptionHandler.class, metaVar = "price", required = true, usage = "trip price")
    private BigDecimal basePrice;

    @Option(name = "--excursions", aliases = {"-e"}, handler = LongArrayOptionHandler.class, metaVar = "ids...", usage = "indices of excursions for this trip")
    private List<Long> excursionIDs;

    //--[  Methods  ]-----------------------------------------------------------

    //<editor-fold defaultstate="collapsed" desc="[  Setters  ]">
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public void setExcursionIDs(List<Long> excursionIDs) {
        this.excursionIDs = excursionIDs;
    }
    //</editor-fold>

    //--[  Interface implementation  ]------------------------------------------

    @Override
    public Command getCommand() {
        return Command.TRIPS_ADD;
    }

    @Override
    public String getDescription() {
        return "adds a new trip";
    }

    @Override
    public void run(MainOptions options) {
        TripDTO trip = new TripDTO();
        trip.setDateFrom(dateFrom);
        trip.setDateTo(dateTo);
        trip.setDestination(destination);
        trip.setCapacity(capacity);
        trip.setBasePrice(basePrice);

        if (excursionIDs != null) {
            for(long id : excursionIDs) {
                ExcursionDTO excursion = AppConfig.getExcursionClient().getExcursion(id);
                trip.getExcursions().add(excursion);
            }
        }

        TripDTO newTrip = AppConfig.getTripClient().addTrip(trip);

        System.out.println("The following trip was added:");
        CLITableTrip.print(newTrip, true);
    }

}
