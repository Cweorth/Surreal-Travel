package cz.muni.pa165.surrealtravel.cli;

import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration (things that can't be in Spring XML config)
 * @author Roman Lacko [396157]
 */
@Configuration
public class AppConfig {
    
    // temp
    public static RestExcursionClient excursionClient;
    
    private static URL base;

    static {
        try {
            base = new URL("http://localhost:8080/pa165/rest/");
        } catch (MalformedURLException ex) {
            LoggerFactory.getLogger(AppConfig.class).error("Initializer error", ex);
        }
    }
    
    //--[  Methods  ]-----------------------------------------------------------
    
    public static URL  getBase()         { return base;                                           }
    public static void setBase(URL base) { AppConfig.base = Objects.requireNonNull(base, "base"); }
    
}
