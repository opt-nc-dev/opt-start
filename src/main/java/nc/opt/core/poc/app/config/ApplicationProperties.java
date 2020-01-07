package nc.opt.core.poc.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Properties specific to The application.
 *
 * <p>
 *     Properties are configured in the application.yml file.
 *     front-end : properties for the frontend app
 *     back-end : properties for the backend app
 * </p>
 */
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private Map<String, Object> frontEnd = new HashMap<>();

    private Map<String, Object> backEnd = new HashMap<>();

    public Map<String, Object> getFrontEnd() {
        return frontEnd;
    }

    public Map<String, Object> getBackEnd() {
        return backEnd;
    }
}
