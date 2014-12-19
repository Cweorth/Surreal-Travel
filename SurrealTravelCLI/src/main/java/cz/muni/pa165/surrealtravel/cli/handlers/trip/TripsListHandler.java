package cz.muni.pa165.surrealtravel.cli.handlers.trip;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableTrip;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.util.List;
import org.kohsuke.args4j.Option;

/**
 * The {@code trips-list} command handler.
 * @author Roman Lacko [396157]
 */
public class TripsListHandler implements CommandHandler {
    
    @Option(name = "-e", aliases = {"--list-excursions"}, usage = "lists excursions in the trip")
    private boolean listExcursions;
    
    //--[  Methods  ]-----------------------------------------------------------
    
    //<editor-fold defaultstate="collapsed" desc="[  Setters  ]">
    
    public void setListExcursions(boolean listExcursions) {
        this.listExcursions = listExcursions;
    }
    
    //</editor-fold>
    
    //--[  Interface implementation  ]------------------------------------------
    
    @Override
    public Command getCommand() {
        return Command.TRIPS_LIST;
    }

    @Override
    public String getDescription() {
        return "lists all trips";
    }

    @Override
    public void run(MainOptions options) {
        List<TripDTO> trips = AppConfig.getTripClient().getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("NO TRIPS FOUND.");
            return;
        }
        
        CLITableTrip.print(trips, listExcursions);
    }

}
