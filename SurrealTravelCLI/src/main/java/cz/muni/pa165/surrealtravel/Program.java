package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import cz.muni.pa165.surrealtravel.cli.handlers.ExcursionListHandler;
import cz.muni.pa165.surrealtravel.cli.options.MainOptions;
import cz.muni.pa165.surrealtravel.cli.handlers.Command;
import cz.muni.pa165.surrealtravel.cli.rest.RESTAccessException;
import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.cli.rest.RestTripClient;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Appender;
import org.apache.log4j.LogManager;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Program {
    
    private final static Logger logger = LoggerFactory.getLogger(Program.class);
    
    private final static Map<Command, CommandHandler> handlers;
    
    static {
        handlers = new HashMap<>();
              
        handlers.put(Command.EXCURSIONS_LIST, new ExcursionListHandler());
    }
    
    public static void printCommandUsage(PrintStream ps, CommandHandler handler) {
        ps.println(handler.getHandledCommand() + ": " + handler.getDescription());
        
        if (handler.getOptionsBean() != null) {
            CmdLineParser parser = new CmdLineParser(handler.getOptionsBean());
            parser.printUsage(ps);
        }
    }
    
    public static void main(String[] args) {
        logger.debug("Starting application");
        
        MainOptions   options = new MainOptions();
        CmdLineParser parser  = new CmdLineParser(options);
        
        try {
            parser.parseArgument(args);
        } catch (CmdLineException ex) {
            logger.error("Error while parsing arguments", ex);
            System.err.println(ex.getLocalizedMessage());
            parser.printSingleLineUsage(System.err);
            System.err.println();
            System.err.println();
            parser.printUsage(System.err);
            System.exit(1);
        }
        
        //<editor-fold desc="[  Help, Verbose & Debug handling  ]" defaultstate="collapsed">
        if (options.isHelp()) {
            parser.printSingleLineUsage(System.out);
            System.out.println();
            parser.printUsage(System.out);
            logger.debug("Graceful exit (caused by \"help\" argument)");
            System.exit(0);
        }
        
        if (options.getCommandHelp() != null) {
            Set<Command> commands = handlers.keySet();
            
            if (commands.isEmpty()) {
                System.err.println("THERE ARE NO COMMANDS REGISTERED");
                System.exit(1);
            }
            
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
        
        if (!handlers.containsKey(options.getCommand())) {
            logger.error("No handler for " + options.getCommand());
            System.err.println("No handler for " + options.getCommand());
            System.exit(2);
        }
        
        CommandHandler handler    = handlers.get(options.getCommand());
        Object         cmdOptions = handler.getOptionsBean();
        
        if ((cmdOptions == null) && (!options.getExtra().isEmpty())) {
            System.err.println("Extra arguments for " + options.getCommand());
            System.exit(1);
        }
        
        if (cmdOptions != null) {
            try {
                CmdLineParser cmdParser = new CmdLineParser(cmdOptions);
                cmdParser.parseArgument(options.getExtra());
            } catch (CmdLineException ex) {
                logger.error("Error while parsing command arguments", ex);
                System.err.println(ex.getLocalizedMessage());
                printCommandUsage(System.err, handler);
                System.exit(1);
            }
        }
        
        try {
            logger.debug("About to load application context");
            ApplicationContext context = new ClassPathXmlApplicationContext("surrealtravel.xml");
            
            logger.debug("Context is loaded, now running handler");
            handler.run(cmdOptions, context.getBean(RestExcursionClient.class), context.getBean(RestTripClient.class));

            logger.debug("All is well, exitting");
        } catch (RESTAccessException ex) {
            logger.error("Rest access exception", ex);
            System.err.println("An error occured while accessing REST api: " + ex.getMessage());
            System.err.println("For the details, please see the log file.");
            System.exit(3);
        } catch (RuntimeException ex) {
            logger.error("Runtime exception", ex);
            System.err.println("A runtime exception has been caught: " + ex.getMessage());
            System.err.println("For the details, please see the log file.");
            System.exit(4);
        }
    }
}
