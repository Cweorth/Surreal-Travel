package cz.muni.pa165.surrealtravel.cli.handlers.trip;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableTrip;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import org.kohsuke.args4j.Option;

/**
 * The {@code trips-delete} handler command
 * @author Roman Lacko [396157]
 */
public class TripsDeleteHandler implements CommandHandler {

    @Option(name = "--id", metaVar = "id", required = true, usage = "the ID of the trip to delete")
    private long id;
    
    @Override
    public Command getCommand() {
        return Command.TRIPS_DELETE;
    }

    @Override
    public String getDescription() {
        return "deletes the trip with the given id";
    }

    @Override
    public void run(MainOptions options) {
        TripDTO trip = AppConfig.getTripClient().deleteTrip(id);
        
        System.out.println("The following trip was deleted:");
        CLITableTrip.print(trip, false);
    }
    
}
