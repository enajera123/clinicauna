package cr.ac.una.clinicaunaws.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.glassfish.jersey.server.ResourceConfig;
import jakarta.ws.rs.ApplicationPath;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * 
 * @author arayaroma
 */
@ApplicationPath("api")
public class JakartaConfiguration extends ResourceConfig {
    public JakartaConfiguration() {
        super();
        packages("cr.ac.una.clinicaunaws.controller", "cr.ac.una.clinicaunaws.util",
                "io.swagger.v3.jaxrs2.integration.resources");
        registerCustomProviders();
    }

    private void registerCustomProviders() {
        Set<String> providerClassNames = new HashSet<>() {
            {
                add(JsonbContextResolver.class.getName());
            }
        };

        Map<String, Object> properties = new HashMap<>() {
            {
                put("jersey.config.server.provider.classnames",
                        providerClassNames.stream().collect(Collectors.joining(",")));
            }
        };

        addProperties(Collections.unmodifiableMap(properties));
    }
}
