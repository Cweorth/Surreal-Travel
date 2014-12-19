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
        ExcursionDTO excursion = AppConfig.getExcursionClient().getExcursion(id);
        
        if(excursion == null) {
            logger.info("Excursion does not exist.");
            System.out.println("EXCURSION DOES NOT EXIST.");
            return;
        }

        if(description != null) excursion.setDescription(description);
        if(destination != null) excursion.setDestination(destination);
        if(duration != null) excursion.setDuration(duration);
        if(excursionDate != null) excursion.setExcursionDate(excursionDate);
        if(price != null) excursion.setPrice(price);
        
        excursion = AppConfig.getExcursionClient().editExcursion(excursion);
        
        if(excursion == null) {
            logger.info("Excursion does not exist.");
            System.out.println("EXCURSION COULD NOT BE MODIFIED.");
            return;
        }

        logger.info("Printing modified excursion object (with new values).");
        System.out.println("The following excursion was modified:");
        
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
