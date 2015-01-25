package cz.muni.pa165.surrealtravel.cli;

import cz.muni.pa165.surrealtravel.cli.rest.RestExcursionClient;
import cz.muni.pa165.surrealtravel.cli.rest.RestTripClient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Application configuration (contains RestTemplate and clients)
 * @author Roman Lacko [396157]
 */
public class AppConfig {

    private static final RestExcursionClient excursionClient;
    private static final RestTripClient      tripClient;
    private static final RestTemplate        template;

    private static URL base;

    static {
        try {
            base = new URL("http://localhost:8080/pa165/rest/");
        } catch (MalformedURLException ex) {
            LoggerFactory.getLogger(AppConfig.class).error("Initializer error", ex);
            throw new RuntimeException(ex);
        }

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));

        template = new RestTemplate();
        template.getMessageConverters().add(converter);

        excursionClient = new RestExcursionClient(template);
        tripClient      = new RestTripClient(template);
    }

    //--[  Methods  ]-----------------------------------------------------------

    public static URL  getBase()         { return base;                                           }
    public static void setBase(URL base) { AppConfig.base = Objects.requireNonNull(base, "base"); }

    public static RestExcursionClient getExcursionClient() { return excursionClient; }
    public static RestTripClient      getTripClient()      { return tripClient;      }
    public static RestTemplate        getTemplate()        { return template;        }

}
