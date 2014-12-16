package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.cli.rest.RestTripClient;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The {@code trips-list} command handler.
 * @author Roman Lacko [396157]
 */
@Component
public class TripsListHandler implements CommandHandler {

    @Autowired
    private RestTripClient client;
    
    @Autowired
    private RestExcursionClient exclient;
    
    @Option(name = "-e", aliases = {"--list-excursions"}, usage = "lists excursions in the trip")
    private boolean listExcursions;
    
    private final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    private final String[] titles = new String[] { 
        "Id", "Destination", "Date from", "Date to", "Capacity", "Base price", "Full price", "Excursions"
    };
    
    private void setmax(int[] w, int ix, Object obj) {
        w[ix] = Math.max(w[ix], obj.toString().length());
    }
    
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
        List<TripDTO> trips = client.getAllTrips();

        if (trips.isEmpty()) {
            System.out.println("NO TRIPS FOUND");
            return;
        }

        // array of column widths
        int[] w = new int[titles.length];
        
        // insert header widths to the array
        for(int ix = 0; ix < titles.length; ++ix) {
            w[ix] = titles[ix].length();
        }
        
        // take widths of other fields into account
        for(TripDTO trip : trips) {
            setmax(w, 0, trip.getId());
            setmax(w, 1, trip.getDestination());
            setmax(w, 2, df.format(trip.getDateFrom()));
            setmax(w, 3, df.format(trip.getDateTo()));
            setmax(w, 4, trip.getCapacity());
            setmax(w, 5, trip.getBasePrice());
            setmax(w, 6, trip.getFullPrice());
            setmax(w, 7, trip.getExcursions().size());
        }
        
        // formatters
        String fmt = "%"  + w[0] + "d  " // ID
                   + "%-" + w[1] + "s  " // destination
                   + "%-" + w[2] + "s  " // date from
                   + "%-" + w[3] + "s  " // date to
                   + "%"  + w[4] + "d  " // capacity
                   + "%"  + w[5] + "s  " // base price
                   + "%"  + w[6] + "s  " // full price
                   + "%"  + w[7] + "d  " // excursions
                   + "\n";
        
        int sumw = -2;
        for (int n : w) { sumw += n + 2; }
        String separator = StringUtils.repeat("=", sumw);
        
        // print the table header
        System.out.println(separator);
        for(int ix = 0; ix < titles.length; ++ix) {
            System.out.printf("%-" + w[ix] + "s  ", titles[ix]);
        }
        System.out.println();
        System.out.println(separator);
        
        // now FINALLY print the trips (about time, right?)
        for(TripDTO trip : trips) {
            System.out.printf(fmt, trip.getId(), trip.getDestination(), 
                    df.format(trip.getDateFrom()), df.format(trip.getDateTo()), 
                    trip.getCapacity(), trip.getBasePrice().toString(),
                    trip.getFullPrice().toString(), trip.getExcursions().size());
        }
        System.out.println(separator);        
    }

}
