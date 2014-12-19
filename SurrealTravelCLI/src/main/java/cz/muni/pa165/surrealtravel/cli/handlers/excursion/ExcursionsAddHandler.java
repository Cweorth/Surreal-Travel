package cz.muni.pa165.surrealtravel.cli.handlers.excursion;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.handlers.BigDecimalOptionHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.DateOptionHandler;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableExcursion;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.math.BigDecimal;
import java.util.Date;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for excursions-add command.
 * @author Jan Klimeš [374259]
 */
public class ExcursionsAddHandler implements CommandHandler {
       
    private final static Logger logger = LoggerFactory.getLogger(ExcursionsAddHandler.class);
    
    @Option(name = "--description", metaVar = "description", usage = "specify the excursion description [string]", required = true)
    private String description;
    
    @Option(name = "--destination", metaVar = "destination", usage = "specify the excursion destination [string]", required = true)
    private String destination;
    
    @Option(name = "--duration", metaVar = "duration", usage = "specify the duration of excursion [integer]")
    private int duration;
    
    @Option(name = "--excursionDate", handler = DateOptionHandler.class, usage = "specify the excursion date [yyyy/MM/dd]", required = true)
    private Date excursionDate;
    
    @Option(name = "--price", handler = BigDecimalOptionHandler.class, metaVar = "price", usage = "specify the excursion price [integer]", required = true)
    private BigDecimal price;

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
        ExcursionDTO excursion = new ExcursionDTO();
        excursion.setDescription(description);
        excursion.setDestination(destination);
        excursion.setDuration(duration);
        excursion.setExcursionDate(excursionDate);
        excursion.setPrice(price);

        excursion = AppConfig.getExcursionClient().addExcursion(excursion);
        
        logger.info("Printing added excursion object.");
        System.out.println("The following excursion was added:");
        
        CLITableExcursion.print(excursion);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getExcursionDate() {
        return excursionDate;
    }

    public void setExcursionDate(Date excursionDate) {
        this.excursionDate = excursionDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
}
