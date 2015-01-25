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
 * The {@code trips-edit} command handler
 * @author Roman Lacko [396157]
 */
public class TripsEditHandler implements CommandHandler {

    @Option(name = "--id", metaVar = "id", required = true, usage = "the ID of the trip to edit")
    private long id;

    @Option(name = "--from", aliases = {"-f"}, handler = DateOptionHandler.class, metaVar = "date", usage = "trip start [yyyy/MM/dd]")
    private Date dateFrom;

    @Option(name = "--to", aliases = {"-t"}, handler = DateOptionHandler.class, metaVar = "date", usage = "trip end [yyyy/MM/dd]")
    private Date dateTo;

    @Option(name = "--destination", aliases = {"-d"}, metaVar = "destination", usage = "trip destination")
    private String destination;

    @Option(name = "--capacity", aliases = {"-c"}, metaVar = "capacity", usage = "trip capacity")
    private Integer capacity;

    @Option(name = "--basePrice", aliases = {"-p"}, handler = BigDecimalOptionHandler.class, metaVar = "price", usage = "trip price")
    private BigDecimal basePrice;

    @Option(name = "--excursions", aliases = {"-e"}, handler = LongArrayOptionHandler.class, metaVar = "ids...", usage = "indices of excursions for this trip")
    private List<Long> excursionIDs;

    @Option(name = "--no-excursions", forbids = "--excursions", usage = "remove all excursions")
    private boolean noExcursions;

    @Override
    public Command getCommand() {
        return Command.TRIPS_EDIT;
    }

    @Override
    public String getDescription() {
        return "edits the trip";
    }

    @Override
    public void run(MainOptions options) {
        TripDTO trip = AppConfig.getTripClient().getTrip(id);

        boolean changed = false;

        if (dateFrom     != null) { changed |= true; trip.setDateFrom(dateFrom); }
        if (dateTo       != null) { changed |= true; trip.setDateTo(dateTo);     }
        if (destination  != null) { changed |= true; trip.setDestination(destination); }
        if (capacity     != null) { changed |= true; trip.setCapacity(capacity); }
        if (basePrice    != null) { changed |= true; trip.setBasePrice(basePrice); }

        if (excursionIDs != null) {
            changed |= true;

            for(long eid : excursionIDs) {
                ExcursionDTO excursion = AppConfig.getExcursionClient().getExcursion(eid);
                trip.getExcursions().add(excursion);
            }
        }

        if (noExcursions) {
            changed |= true;
            trip.getExcursions().clear();
        }

        if (! changed) {
            throw new RuntimeException("No change in the trip");
        }

        AppConfig.getTripClient().editTrip(trip);
        System.out.println("The following trip was modified:");
        CLITableTrip.print(trip, true);
    }


}
