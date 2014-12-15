package cz.muni.pa165.surrealtravel.cli;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    private static URL base;

    static {
        try {
            base = new URL("http://localhost:8080/pa165/rest/");
        } catch (MalformedURLException ex) {
            LoggerFactory.getLogger(AppConfig.class).error("Initializer error", ex);
        }
    }
    
    @Bean(name = "urlBase")
    public static URL getBase() {
        return base;
    }

    public static void setBase(URL base) {
        AppConfig.base = Objects.requireNonNull(base, "base");
    }
}
