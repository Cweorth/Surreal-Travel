package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.trip.*;
import cz.muni.pa165.surrealtravel.cli.handlers.excursion.*;
import cz.muni.pa165.surrealtravel.cli.rest.RESTAccessException;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.ResourceAccessException;

/**
 * The main class, provides CLI handling
 * @author Roman Lacko [396157]
 */
public class Program {
    
    private final static Logger logger = LoggerFactory.getLogger(Program.class);
    
    /**
     * Shows program help
     * @param ps        target print stream
     */
    public static void printHelp(PrintStream ps) {
        CmdLineParser parser = new CmdLineParser(new MainOptions());
        
        ps.print("usage: ");
        parser.printSingleLineUsage(ps);
        ps.println();
        ps.print("use -? [--command-help] to show help for each subcommand");
        ps.println();
        ps.println();
        
        parser.printUsage(ps);        
    }
    
    /**
     * Shows command help
     * @param ps        target print stream
     * @param handler   command handler
     */
    public static void printCommandUsage(PrintStream ps, CommandHandler handler) {
        CmdLineParser parser = new CmdLineParser(handler);
        
        ps.println(handler.getCommand() + ": " + handler.getDescription());
        ps.print("command usage: ");
        
        parser.printSingleLineUsage(ps);
        ps.println();
        ps.println();
        
        parser.printUsage(ps);
    }
    
    /**
     * Program entry method
     * @param args      CLI arguments
     */
    public static void main(String[] args) {
        logger.debug("Starting application");
        
        try {       
            logger.debug("Parsing command-line arguments");
            MainOptions   options = new MainOptions();
            CmdLineParser parser  = new CmdLineParser(options);
            
            parser.parseArgument(args);
        
            Map<Command, CommandHandler> handlers = new EnumMap<>(Command.class);
            handlers.put(Command.EXCURSIONS_ADD,    new ExcursionsAddHandler());
            handlers.put(Command.EXCURSIONS_DELETE, new ExcursionsDeleteHandler());
            handlers.put(Command.EXCURSIONS_EDIT,   new ExcursionsEditHandler());
            handlers.put(Command.EXCURSIONS_GET,    new ExcursionsGetHandler());
            handlers.put(Command.EXCURSIONS_LIST,   new ExcursionsListHandler());
            handlers.put(Command.TRIPS_ADD,         new TripsAddHandler());
            handlers.put(Command.TRIPS_DELETE,      new TripsDeleteHandler());
            handlers.put(Command.TRIPS_EDIT,        new TripsEditHandler());
            handlers.put(Command.TRIPS_GET,         new TripsGetHandler());
            handlers.put(Command.TRIPS_LIST,        new TripsListHandler());
            
            //<editor-fold desc="[  Debug & Verbose handling  ]" defaultstate="collapsed">
            if (options.isVerbose() || options.isDebug()) {
                logger.debug("entering log-verbose mode");
                org.apache.log4j.Logger rootLogger  = LogManager.getRootLogger();
                org.apache.log4j.Logger debuglogger = LogManager.getLogger("DebugLogger");

                rootLogger.removeAppender("console");
                rootLogger.addAppender((Appender) debuglogger.getAllAppenders().nextElement());

                if (options.isDebug()) {
                    logger.debug("entering log-debugging mode");
                    LogManager.getRootLogger().getAppender("console").clearFilters();
                    logger.debug("log-debugging mode enabled");
                }

                logger.debug("log-verbose mode enabled");
            }
            //</editor-fold>
                        
            //<editor-fold desc="[  Help handling  ]" defaultstate="collapsed">
            if (options.isHelp()) {
                printHelp(System.out);
                logger.debug("Graceful exit (caused by \"help\" argument)");
                System.exit(0);
            }

            if (options.getCommandHelp() != null) {
                Set<Command> commands = handlers.keySet();

                if (options.getCommandHelp().equalsIgnoreCase("LIST")) {                
                    System.out.println("Available commands:");
                    System.out.println("(use --command-help command to show help)");
                    for (Command cmd : commands) {
                        System.out.println(cmd);
                    }

                    logger.debug("Graceful exit (caused by \"--command-help\" argument)");
                    System.exit(0);
                } else {
                    logger.debug("Looking for command " + options.getCommandHelp());

                    for(Command cmd : commands) {
                        if (cmd.toString().equals(options.getCommandHelp())) {
                            printCommandUsage(System.out, handlers.get(cmd));
                            logger.debug("Graceful exit (caused by \"--command-help\" argument)");
                            System.exit(0);
                        }
                    }
                    System.err.println("Command \"" + options.getCommandHelp() + "\" not found!");
                    System.exit(1);
                }
            }
            //</editor-fold>

            if (!handlers.containsKey(options.getCommand())) {
                throw new RuntimeException("No handler for command " + options.getCommand());
            }
        
            logger.debug("Running command handler");
            options.getCmd().run(options);

            logger.debug("Done");
        } catch (CmdLineException ex) {
            logger.error("Command line exception", ex);
            System.err.println("Command line error: " + ex.getMessage());
            printHelp(System.err);
            System.exit(2);
        } catch (RESTAccessException ex) {
            logger.error("Rest access exception", ex);
            
            Throwable cause = ex.getCause();
            
            if ((cause != null) && cause.getClass().equals(ResourceAccessException.class)) {
                System.err.println("OPERATION FAILED: Unable to connect to the resource");
            } else {
                System.err.println("OPERATION FAILED: " + ex.getMessage());
            }
            
            System.err.println("For the details, please see the log file.");
            System.exit(3);
        } catch (RuntimeException ex) { // this is bad, but can't be helped
            logger.error("Runtime exception", ex);
            System.err.println("RUNTIME ERROR: " + ex.getMessage());
            System.err.println("For the details, please see the log file.");
            System.exit(4);
        }
    }
}
