package cz.muni.pa165.surrealtravel.cli.options;

import cz.muni.pa165.surrealtravel.cli.handlers.Command;
import cz.muni.pa165.surrealtravel.cli.AppConfig;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;

public class MainOptions {
    
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
    
    @Argument(index = 0, required = true, metaVar = "command",
            usage = "main command")
    private Command command;
    
    @Argument(hidden = true, multiValued = true, index = 1)
    private List<String> extra = new ArrayList<>();
    
    @Option(name = "--url", metaVar = "url", usage = "specify base URL")
    public void setURI(URL url) {
        LoggerFactory.getLogger(MainOptions.class).info("Changing URL to \"" + url.toString() + "\"");
        AppConfig.setBase(url);
    }
    
    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public String getCommandHelp() {
        return commandHelp;
    }

    public void setCommandHelp(String commandHelp) {
        this.commandHelp = commandHelp;
    }
    
    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public List<String> getExtra() {
        return extra;
    }

    public void setExtra(List<String> extra) {
        this.extra = extra;
    }

}
