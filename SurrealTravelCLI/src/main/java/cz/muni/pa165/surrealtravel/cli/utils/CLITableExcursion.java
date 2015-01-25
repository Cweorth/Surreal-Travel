package cz.muni.pa165.surrealtravel.cli.utils;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author Jan Klimeš [374259]
 */
public class CLITableExcursion {
    
    private static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    
    private static final String[] titles = new String[] { 
        "Id", "Description", "Destination", "Date", "Duration", "Price" 
    };
    
    private static void setmax(int[] w, int ix, Object obj) {
        w[ix] = Math.max(w[ix], obj.toString().length());
    }
    
    public static void print(ExcursionDTO excursion) {
        CLITableExcursion.print(Arrays.asList(new ExcursionDTO[] {excursion}));
    }
    
    public static void print(List<ExcursionDTO> excursions) {
        // array of column widths
        int[] w = new int[CLITableExcursion.titles.length];
        
        // insert header widths to the array
        for(int ix = 0; ix < CLITableExcursion.titles.length; ++ix) {
            w[ix] = CLITableExcursion.titles[ix].length();
        }
        
        // take widths of other fields into account
        for(ExcursionDTO excursion : excursions) {
            CLITableExcursion.setmax(w, 0, excursion.getId());
            CLITableExcursion.setmax(w, 1, excursion.getDescription());
            CLITableExcursion.setmax(w, 2, excursion.getDestination());
            CLITableExcursion.setmax(w, 3, CLITableExcursion.df.format(excursion.getExcursionDate()));
            CLITableExcursion.setmax(w, 4, excursion.getDuration());
            CLITableExcursion.setmax(w, 5, excursion.getPrice().toString());
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
        for(int ix = 0; ix < CLITableExcursion.titles.length; ++ix) {
            System.out.printf("%-" + w[ix] + "s  ", CLITableExcursion.titles[ix]);
        }
        System.out.println();
        System.out.println(separator);
        
        // now FINALLY print the excursions (about time, right?)
        for(ExcursionDTO excursion : excursions) {
            System.out.printf(fmt, excursion.getId(), excursion.getDescription(), 
                    excursion.getDestination(), CLITableExcursion.df.format(excursion.getExcursionDate()), 
                    excursion.getDuration(), excursion.getPrice().toString());
        }
        System.out.println(separator);
    }
    
}
