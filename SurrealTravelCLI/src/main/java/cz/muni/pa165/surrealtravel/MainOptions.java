package cz.muni.pa165.surrealtravel;

import cz.muni.pa165.surrealtravel.cli.handlers.trip.*;
import cz.muni.pa165.surrealtravel.cli.handlers.excursion.*;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import cz.muni.pa165.surrealtravel.cli.handlers.CommandHandler;
import java.net.URL;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.SubCommand;
import org.kohsuke.args4j.spi.SubCommandHandler;
import org.kohsuke.args4j.spi.SubCommands;
import org.slf4j.LoggerFactory;

/**
 * General program options
 * @author Roman Lacko [396157]
 */
public class MainOptions {
    
    //--[  Options  ]-----------------------------------------------------------
    
    @Option(name = "-h", aliases = {"--help"}, help = true, 
            usage = "show program help")
    private boolean help;
    
    @Option(name = "-?", aliases = {"--command-help"}, help = true, metaVar = "{command|LIST}",
            usage = "show command help or lists commands if \"LIST\" specified")
    private String commandHelp;
    
    @Option(name = "-v", aliases = {"--verbose"}, forbids = "--debug",
            usage = "verbose mode")
    private boolean verbose;
    
    @Option(name = "--debug", forbids = "-v",
            usage = "enables debug messaged")
    private boolean debug;

    @Option(name = "--url", metaVar = "url", usage = "specify base URL")
    public void setURI(URL url) {
        LoggerFactory.getLogger(MainOptions.class).info("Changing URL to \"" + url.toString() + "\"");
        AppConfig.setBase(url);
    }    
    
    @Argument(index = 0, required = true, metaVar = "command", handler = SubCommandHandler.class,
            usage = "main command")
    @SubCommands({
        @SubCommand(name = "excursions-list",   impl = ExcursionsListHandler.class),
        @SubCommand(name = "excursions-get",    impl = ExcursionsGetHandler.class),
        @SubCommand(name = "excursions-add",    impl = ExcursionsAddHandler.class),
        @SubCommand(name = "excursions-edit",   impl = ExcursionsEditHandler.class),
        @SubCommand(name = "excursions-delete", impl = ExcursionsDeleteHandler.class),
        @SubCommand(name = "trips-list",        impl = TripsListHandler.class),
        @SubCommand(name = "trips-get",         impl = TripsGetHandler.class),
        @SubCommand(name = "trips-add",         impl = TripsAddHandler.class),
        @SubCommand(name = "trips-edit",        impl = TripsEditHandler.class),
        @SubCommand(name = "trips-delete",      impl = TripsDeleteHandler.class)
    })   
    private CommandHandler cmd;
    
    //--[  Methods  ]-----------------------------------------------------------
    
    /**
     * Returns the main command
     * @return the main command
     */
    public Command getCommand() {
        return cmd.getCommand();
    }    
    
    //<editor-fold desc="[  Getters | Setters  ]" defaultstate="collapsed">
    
    public boolean isHelp()              { return help;      }
    public void    setHelp(boolean help) { this.help = help; }

    public String getCommandHelp()                   { return commandHelp;             }
    public void   setCommandHelp(String commandHelp) { this.commandHelp = commandHelp; }
    
    public boolean isVerbose()                 { return verbose;         }
    public void    setVerbose(boolean verbose) { this.verbose = verbose; }

    public CommandHandler getCmd()                   { return cmd;     }
    public void           setCmd(CommandHandler cmd) { this.cmd = cmd; }

    public boolean isDebug()               { return debug;       }
    public void    setDebug(boolean debug) { this.debug = debug; }
    
    //</editor-fold>
    
}
