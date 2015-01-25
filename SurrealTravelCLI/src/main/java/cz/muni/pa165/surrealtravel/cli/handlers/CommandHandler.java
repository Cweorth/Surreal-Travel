package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.Command;
import cz.muni.pa165.surrealtravel.MainOptions;

/**
 * Interface for handling CLI commands.
 *
 * The interface implementation can contain args4j {@link org.kohsuke.args4j.Option}
 * annotations that will be processed after main arguments and their values
 * will be accessible by the {@code run} method.
 * The handler should be registered in <b>both</b>
 * <ol>
 *   <li>{@link cz.muni.pa165.surrealtravel.MainOptions}'s {@code command}
 * list of subcommands,</li>
 *   <li>Spring XML configuration file {@code surrealtravel.xml}</li>
 * </ol>
 *
 * @author Roman Lacko [396157]
 */
public interface CommandHandler {

    /**
     * Returns the {@link cz.muni.pa165.surrealtravel.Command} that this
     * class handles.
     * @return the command
     */
    Command getCommand();

    /**
     * Returns the short description of the command.
     * @return the description
     */
    String getDescription();

    /**
     * Implements the functionality of the handled command.
     * @param options   the instance of main options
     */
    void run(MainOptions options);

}
