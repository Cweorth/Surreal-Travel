package cz.muni.pa165.surrealtravel.cli.handlers;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

/**
 *
 * @author Roman Lacko [396157]
 */
public class LongArrayOptionHandler extends OptionHandler<Long> {

    public LongArrayOptionHandler(CmdLineParser parser, OptionDef option, Setter<Long> setter) {
        super(parser, option, setter);
    }
    
    @Override
    public String getDefaultMetaVariable() {
        return "Long[]";
    }
    
    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        int counter=0;
        
        while (counter < params.size()) {
            String param = params.getParameter(counter);
            
            if (param.startsWith("-")) {
                break;
            }
            
            try {
                for (String p : param.split(" ")) {
                    setter.addValue(Long.valueOf(p));
                }
            } catch (NumberFormatException ex) {
                throw new CmdLineException(owner, ex.getLocalizedMessage(), ex);
            }
            
            ++counter;
        }
        
        return counter;
    }
}
