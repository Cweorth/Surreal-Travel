package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for excursions-delete command.
 * @author Jan Klimeš [374259]
 */
public class ExcursionsDeleteHandler implements CommandHandler {
    
    private final static Logger logger = LoggerFactory.getLogger(ExcursionsDeleteHandler.class);
    
    @Override
    public Command getCommand() {
        return Command.EXCURSIONS_DELETE;
    }

    @Override
    public String getDescription() {
        return "delete existing excursion";
    }

    @Override
    public void run(MainOptions options) {
        // todo tomorow
    }

}
