package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jan Klimeš [374259]
 */
@Component
public class ExcursionsAddHandler implements CommandHandler {
    
//    @Autowired(required = true)
//    private RestExcursionClient client;
    private RestExcursionClient client = AppConfig.excursionClient;

    @Override
    public Command getCommand() {
        return Command.EXCURSIONS_ADD;
    }

    @Override
    public String getDescription() {
        return "create new excursion";
    }

    @Override
    public void run(MainOptions options) {
        // currently in developent
    }
    
}
