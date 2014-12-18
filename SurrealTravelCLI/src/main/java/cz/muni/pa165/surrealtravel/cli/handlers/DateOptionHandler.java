package cz.muni.pa165.surrealtravel.cli.handlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

/**
 * args4j option handler to support Date object.
 * @author Jan Klimeš [374259]
 */
public class DateOptionHandler extends OptionHandler<Date> {

    public DateOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super Date> setter) {
        super(parser, option, setter);
    }
    
    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        DateFormat formatter = new SimpleDateFormat("yy/MM/dd");
        Date date = null;
        try {
            date = formatter.parse(params.getParameter(0));
        } catch (ParseException ex) {
            Logger.getLogger(DateOptionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        setter.addValue(date);
        return 1;
    }

    @Override
    public String getDefaultMetaVariable() {
        return null;
    }
    
}
