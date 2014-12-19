package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableTrip;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import org.kohsuke.args4j.Option;

/**
 * The {@code trips-get} command handler
 * @author Roman Lacko [396157]
 */
public class TripsGetHandler implements CommandHandler {

    @Option(name = "--id", metaVar = "id", usage = "the trip id", required = true)
    private long id;
    
    @Option(name = "-e", aliases = {"--list-excursions"}, usage = "lists excursions in the trip")
    private boolean listExcursions;    
    
    //--[  Methods  ]-----------------------------------------------------------
    
    //<editor-fold defaultstate="collapsed" desc="[  Setters  ]">
    public void setId(long id) {
        this.id = id;
    }
    
    public void setListExcursions(boolean listExcursions) {
        this.listExcursions = listExcursions;
    }
    //</editor-fold>
    
    //--[  Interface implementation  ]------------------------------------------
    
    @Override
    public Command getCommand() {
        return Command.TRIPS_GET;
    }

    @Override
    public String getDescription() {
        return "lists trips with the given id";
    }

    @Override
    public void run(MainOptions options) {
        TripDTO trip = AppConfig.getTripClient().getTrip(id);
        
        CLITableTrip.print(trip, listExcursions);
    }
    
}
