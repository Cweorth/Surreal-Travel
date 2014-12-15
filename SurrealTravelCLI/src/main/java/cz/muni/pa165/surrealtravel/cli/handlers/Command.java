package cz.muni.pa165.surrealtravel.cli.handlers;

import java.util.Objects;

public enum Command {
    EXCURSIONS_LIST("excursions-list");
    
    private final String cmd;
    
    private Command(final String cmd) {
        this.cmd = Objects.requireNonNull(cmd, "cmd");
    }
    
    @Override
    public String toString() {
        return cmd;
    }
}
