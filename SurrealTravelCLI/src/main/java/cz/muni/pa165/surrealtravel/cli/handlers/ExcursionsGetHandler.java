package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.cli.utils.CLITableExcursion;
import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import org.kohsuke.args4j.Option;

/**
 * Handler for excursions-get command.
 * @author Jan Klimeš [374259]
 */
public class ExcursionsGetHandler implements CommandHandler {
    
    @Option(name = "--id", metaVar = "id", usage = "specify the excursion id", required = true)
    private long id;
    
    @Override
    public Command getCommand() {
        return Command.EXCURSIONS_GET;
    }

    @Override
    public String getDescription() {
        return "list excursions with the given id";
    }

    @Override
    public void run(MainOptions options) {
        ExcursionDTO excursion = AppConfig.getExcursionClient().getExcursion(id);
        
        if(excursion == null) {
            System.out.println("NO EXCURSION FOUND");
            return;
        }
        
        System.out.println("- The following excursion was found >>");
        
        CLITableExcursion.print(excursion);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
}
