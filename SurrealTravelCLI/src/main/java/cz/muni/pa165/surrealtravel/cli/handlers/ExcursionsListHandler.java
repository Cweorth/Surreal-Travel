package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableExcursion;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.util.List;

/**
 * The {@code excursions-list} command handler
 * @author Roman Lacko [396157]
 */
public class ExcursionsListHandler implements CommandHandler {
    
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
        List<ExcursionDTO> excursions = AppConfig.getExcursionClient().getAllExcursions();

        if (excursions.isEmpty()) {
            System.out.println("NO EXCURSIONS FOUND");
            return;
        }

        CLITableExcursion.print(excursions);
    }

}
