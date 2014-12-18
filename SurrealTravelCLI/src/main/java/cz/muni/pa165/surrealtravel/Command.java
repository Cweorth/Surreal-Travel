package cz.muni.pa165.surrealtravel;

import java.util.Objects;

/**
 * Main command enumeration
 * @author Jan Klimeš [374259], Roman Lacko [396157]
 */
public enum Command {
    EXCURSIONS_LIST("excursions-list"),
    EXCURSIONS_GET("excursions-get"),
    EXCURSIONS_ADD("excursions-add"),
    EXCURSIONS_EDIT("excursions-edit"),
    EXCURSIONS_DELETE("excursions-delete"),
    TRIPS_LIST("trips-list");
    
    private final String cmd;
    
    Command(String cmd) {
        this.cmd = Objects.requireNonNull(cmd);
    }
    
    @Override
    public String toString() {
        return cmd;
    }
}
