package cz.muni.pa165.surrealtravel.cli.handlers;

import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.cli.rest.RestTripClient;

public interface CommandHandler {

    Command getHandledCommand();

    Object  getOptionsBean();
    
    String getDescription();

    void run(Object options, RestExcursionClient excrClient, RestTripClient tripClient);

}
