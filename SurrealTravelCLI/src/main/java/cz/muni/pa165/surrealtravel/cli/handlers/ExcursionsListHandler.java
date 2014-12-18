package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The {@code excursions-list} command handler
 * @author Roman Lacko [396157]
 */
@Component
public class ExcursionsListHandler implements CommandHandler {
    
//    @Autowired(required = true)
//    private RestExcursionClient client;
    private RestExcursionClient client = AppConfig.excursionClient;
    
    private final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    private final String[] titles = new String[] { 
        "Id", "Description", "Destination", "Date", "Duration", "Price" 
    };
    
    private void setmax(int[] w, int ix, Object obj) {
        w[ix] = Math.max(w[ix], obj.toString().length());
    }
    
    //--[  Interface implementation  ]------------------------------------------
    
    @Override
    public Command getCommand() {
        return Command.EXCURSIONS_LIST;
    }

    @Override
    public String getDescription() {
        return "lists all excursions";
    }
    
    @Override
    public void run(MainOptions options) {
        List<ExcursionDTO> excursions = client.getAllExcursions();

        if (excursions.isEmpty()) {
            System.out.println("NO EXCURSIONS FOUND");
            return;
        }

        // array of column widths
        int[] w = new int[titles.length];
        
        // insert header widths to the array
        for(int ix = 0; ix < titles.length; ++ix) {
            w[ix] = titles[ix].length();
        }
        
        // take widths of other fields into account
        for(ExcursionDTO excursion : excursions) {
            setmax(w, 0, excursion.getId());
            setmax(w, 1, excursion.getDescription());
            setmax(w, 2, excursion.getDestination());
            setmax(w, 3, df.format(excursion.getExcursionDate()));
            setmax(w, 4, excursion.getDuration());
            setmax(w, 5, excursion.getPrice().toString());
        }
        
        // formatters
        String fmt = "%"  + w[0] + "d  " // ID
                   + "%-" + w[1] + "s  " // description
                   + "%-" + w[2] + "s  " // destination
                   + "%-" + w[3] + "s  " // excursion date
                   + "%"  + w[4] + "d  " // duration
                   + "%"  + w[5] + "s  " // price
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
        
        // now FINALLY print the excursions (about time, right?)
        for(ExcursionDTO excursion : excursions) {
            System.out.printf(fmt, excursion.getId(), excursion.getDescription(), 
                    excursion.getDestination(), df.format(excursion.getExcursionDate()), 
                    excursion.getDuration(), excursion.getPrice().toString());
        }
        System.out.println(separator);
    }

}
