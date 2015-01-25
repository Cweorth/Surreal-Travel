package cz.muni.pa165.surrealtravel.cli.handlers;

import java.math.BigDecimal;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

/**
 * args4j option handler to support BigDecimal object.
 * @author Jan Klimeš [374259]
 */
public class BigDecimalOptionHandler extends OptionHandler<BigDecimal> {

    public BigDecimalOptionHandler(CmdLineParser parser, OptionDef option, Setter<? super BigDecimal> setter) {
        super(parser, option, setter);
    }

    @Override
    public int parseArguments(Parameters params) throws CmdLineException {
        try {
            setter.addValue(new BigDecimal(params.getParameter(0)));
        } catch (NumberFormatException ex) {
            throw new CmdLineException(owner, "\"" + params.getParameter(0) + "\" is not a valid value for BigDecimal", ex);
        }

        return 1;
    }

    @Override
    public String getDefaultMetaVariable() {
        return null;
    }

}
