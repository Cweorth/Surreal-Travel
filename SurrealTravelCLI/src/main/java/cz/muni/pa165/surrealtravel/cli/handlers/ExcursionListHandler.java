package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.cli.rest.RestTripClient;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;

public class ExcursionListHandler implements CommandHandler {
    
    private final String[] titles = new String[] { 
        "Id", "Description", "Destination", "Date", "Duration", "Price" 
    };
    
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    @Override
    public Command getHandledCommand() {
        return Command.EXCURSIONS_LIST;
    }

    @Override
    public Object getOptionsBean() {
        return null;
    }

    @Override
    public String getDescription() {
        return "lists all excursions";
    }
    
    private void setmax(int[] w, int ix, Object obj) {
        w[ix] = Math.max(w[ix], obj.toString().length());
    }
    
    @Override
    public void run(Object options, RestExcursionClient client, RestTripClient dummy) {
        List<ExcursionDTO> excursions = client.getAllExcursions();

        if (excursions.isEmpty()) {
            System.out.println("NO EXCURSIONS FOUND");
            return;
        }

        // array of column widths
        int[]    w   = new int[] { 0, 0, 0, 0, 0, 0 };
        
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
                   + "%-" + w[1] + "s  "  // description
                   + "%-" + w[2] + "s  "  // destination
                   + "%-" + w[3] + "s  "  // excursion date
                   + "%"  + w[4] + "d  "  // duration
                   + "%"  + w[5] + "s  "  // price
                   + "\n";
        
        int sumw = -2;
        for (int n : w) { sumw += n + 2; }
        
        // now print the table
        System.out.println(StringUtils.repeat("=", sumw));
        for(int ix = 0; ix < titles.length; ++ix) {
            System.out.printf("%-" + w[ix] + "s  ", titles[ix]);
        }
        System.out.println();
        System.out.println(StringUtils.repeat("=", sumw));
        
        // now FINALLY print the excursions (about time, right?)
        for(ExcursionDTO excursion : excursions) {
            System.out.printf(fmt, excursion.getId(), excursion.getDescription(), 
                    excursion.getDestination(), df.format(excursion.getExcursionDate()), 
                    excursion.getDuration(), excursion.getPrice().toString());
        }
        System.out.println(StringUtils.repeat("=", sumw));
    }

}
