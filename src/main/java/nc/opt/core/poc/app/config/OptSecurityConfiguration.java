package nc.opt.core.poc.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 2617ray on 02/03/2017.
 */
@Component("optSecurityConfiguration")
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "opt.security")
public class OptSecurityConfiguration {

    private String type = "";

    private Credentials credentials = new Credentials();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public static class Credentials {

        private Map<String, Object> users = new HashMap<>();

        public Map<String, Object> getUsers() {
            return users;
        }

    }

}
