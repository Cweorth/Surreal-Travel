package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Handler for excursions-delete command.
 * @author Jan Klimeš [374259]
 */
@Component
public class ExcursionsDeleteHandler implements CommandHandler {

//    @Autowired(required = true)
//    private RestExcursionClient client;
    private RestExcursionClient client = AppConfig.excursionClient;
    
    final static Logger logger = LoggerFactory.getLogger(ExcursionsDeleteHandler.class);
    
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

    public RestExcursionClient getClient() {
        return client;
    }

    public void setClient(RestExcursionClient client) {
        this.client = client;
    }

}
