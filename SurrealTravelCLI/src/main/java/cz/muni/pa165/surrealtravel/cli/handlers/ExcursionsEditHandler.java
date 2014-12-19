package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.utils.CLITableExcursion;
import cz.muni.pa165.surrealtravel.dto.ExcursionDTO;
import java.math.BigDecimal;
import java.util.Date;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler for excursions-edit command.
 * @author Jan Klimeš [374259]
 */
public class ExcursionsEditHandler implements CommandHandler {
    
    private final static Logger logger = LoggerFactory.getLogger(ExcursionsEditHandler.class);
    
    private static final String ERROR_MESSAGE = "OPERATION FAILED. SEE LOG FOR MORE DETAILS.";
    private static final String ERROR_MESSAGE_NOT_FOUND = "OPERATION FAILED. EXCURSION DOES NOT EXIST.";
    
    @Option(name = "--id", metaVar = "id", usage = "specify the excursion id", required = true)
    private long id;
    
    @Option(name = "--description", metaVar = "description", usage = "specify the excursion description [string]")
    private String description;
    
    @Option(name = "--destination", metaVar = "destination", usage = "specify the excursion destination [string]")
    private String destination;
    
    @Option(name = "--duration", metaVar = "duration", usage = "specify the duration of excursion [integer]")
    private Integer duration;
    
    @Option(name = "--excursionDate", handler = DateOptionHandler.class, usage = "specify the excursion date [yyyy/MM/dd]")
    private Date excursionDate;
    
    @Option(name = "--price", handler = BigDecimalOptionHandler.class, metaVar = "price", usage = "specify the excursion price [integer]")
    private BigDecimal price;

    @Override
    public Command getCommand() {
        return Command.EXCURSIONS_EDIT;
    }

    @Override
    public String getDescription() {
        return "edit existing excursion";
    }

    @Override
    public void run(MainOptions options) {
        ExcursionDTO excursion;
        try {
            excursion = AppConfig.getExcursionClient().getExcursion(id);
        } catch(Exception e) {
            logger.info("Following errors encountered when retrieving excursion: " + e.getMessage());
            System.out.println(ERROR_MESSAGE);
            return;
        }
        
        if(excursion == null) {
            logger.info("Excursion with given id does not exist.");
            System.out.println(ERROR_MESSAGE_NOT_FOUND);
            return;
        }
        
        if(description != null) excursion.setDescription(description);
        if(destination != null) excursion.setDestination(destination);
        if(duration != null) excursion.setDuration(duration);
        if(excursionDate != null) excursion.setExcursionDate(excursionDate);
        if(price != null) excursion.setPrice(price);
        
        try {
            excursion = AppConfig.getExcursionClient().editExcursion(excursion);
        } catch(Exception e) {
            logger.info("Following errors encountered when updating excursion: " + e.getMessage());
            System.out.println(ERROR_MESSAGE);
            return;
        }
        
        System.out.println("- The following excursion was modified >>");
        
        CLITableExcursion.print(excursion);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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
