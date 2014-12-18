package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableExcursion;
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

        CLITableExcursion.print(excursions);
    }

}
