package cz.muni.pa165.surrealtravel.cli;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    
    private URI     base;

    public AppConfig() throws URISyntaxException {
        base = new URI("http://localhost:8080/pa165/rest/");
    }
    
    @Bean(name = "uriBase")
    public URI getBase() {
        return base;
    }

    public void setBase(URI base) {       
        this.base = base;
    }
}
