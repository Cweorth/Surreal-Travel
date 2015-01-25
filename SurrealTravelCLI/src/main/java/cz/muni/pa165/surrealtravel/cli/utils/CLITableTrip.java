package cz.muni.pa165.surrealtravel.cli.utils;

import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import cz.muni.pa165.surrealtravel.dto.TripDTO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author Roman Lacko [396157]
 */
public class CLITableTrip {
    private static final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    private static final String[] titles = new String[] {
        "Id", "Destination", "Date from", "Date to", "Capacity", "Base price", "Full price", "Excursions"
    };

    private static void setmax(int[] w, int ix, Object obj) {
        w[ix] = Math.max(w[ix], obj.toString().length());
    }


    //--[  Methods  ]-----------------------------------------------------------

    public static void print(TripDTO trip, boolean excursions) {
        print(Arrays.asList(trip), excursions);
    }

    public static void print(List<TripDTO> trips, boolean excursions) {

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

            if (excursions) {
                for (ExcursionDTO excursion : trip.getExcursions()) {
                    setmax(w, 0, excursion.getId());
                    setmax(w, 1, excursion.getDestination());
                    setmax(w, 2, df.format(excursion.getExcursionDate()));
                    setmax(w, 3, df.format(excursion.getExcursionDate()));
                    setmax(w, 5, excursion.getPrice());
                }
            }

        }

        // formatters
        String fmt  = (excursions ? "+ " : "")
                    + "%"  + w[0] + "d  " // ID
                    + "%-" + w[1] + "s  " // destination
                    + "%-" + w[2] + "s  " // date from
                    + "%-" + w[3] + "s  " // date to
                    + "%"  + w[4] + "d  " // capacity
                    + "%"  + w[5] + "s  " // base price
                    + "%"  + w[6] + "s  " // full price
                    + "%"  + w[7] + "d  " // excursions
                    + "\n";

        String efmt = "  "
                    + "%"  + w[0] + "d  "  // ID
                    + "%-" + w[1] + "s  "  // Destination
                    + "%-" + w[2] + "s  "  // Date
                    + "%-" + w[3] + "s  "  // Date
                    + "  " + StringUtils.repeat(" ", w[4])
                    + "%"  + w[5] + "s  "  // Price
                    + "\n";

        int sumw = (excursions ? 0 : -2);
        for (int n : w) { sumw += n + 2; }
        String separator = StringUtils.repeat("=", sumw);
        String lightsep  = StringUtils.repeat("-", sumw);

        // print the table header
        System.out.println(separator);
        for(int ix = 0; ix < titles.length; ++ix) {
            System.out.printf((ix == 0 && excursions ? "  " : "") + "%-" + w[ix] + "s  ", titles[ix]);
        }
        System.out.println();
        System.out.println(separator);

        // now FINALLY print the trips (about time, right?)
        for(int ix = 0; ix < trips.size(); ++ix) {
            TripDTO trip = trips.get(ix);
            System.out.printf(fmt,
                    trip.getId(),
                    trip.getDestination(),
                    df.format(trip.getDateFrom()),
                    df.format(trip.getDateTo()),
                    trip.getCapacity(),
                    trip.getBasePrice().toString(),
                    trip.getFullPrice().toString(),
                    trip.getExcursions().size());

            if (excursions) {
                if (!trip.getExcursions().isEmpty()) {
                    for (ExcursionDTO excursion : trip.getExcursions()) {
                        Calendar end = Calendar.getInstance();
                        end.setTime(excursion.getExcursionDate());
                        end.add(Calendar.DATE, excursion.getDuration());

                        System.out.printf(efmt,
                                excursion.getId(),
                                excursion.getDestination(),
                                df.format(excursion.getExcursionDate()),
                                df.format(end.getTime()),
                                excursion.getPrice().toString());
                    }
                }

                if (ix < trips.size() - 1) {
                    System.out.println(lightsep);
                }
            }
        }
        System.out.println(separator);
    }

}
