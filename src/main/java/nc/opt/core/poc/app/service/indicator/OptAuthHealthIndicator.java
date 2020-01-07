package nc.opt.core.poc.app.service.indicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@Component(value = "optAuth")
public class OptAuthHealthIndicator implements HealthIndicator {
    private final Logger log = LoggerFactory.getLogger(OptAuthHealthIndicator.class);

    @Override
    public Health health() {
        String optAuthServerUrl = System.getenv("OPT_AUTH_SERVER_URL");
        String optAuthPublicKey = System.getenv("OPT_AUTH_PUBLIC_KEY_FILE_PATH");
        Map<String, String> details = new LinkedHashMap<>();
        log.info("Health OPT_AUTH_SERVER_URL : " + optAuthServerUrl + ", OPT_AUTH_PUBLIC_KEY_FILE_PATH : " + optAuthPublicKey);
        if (StringUtils.isEmpty(optAuthServerUrl))
        {
            log.info("Health OPT_AUTH_SERVER_URL empty");
            return Health.unknown().withDetail("OPT_AUTH_SERVER_URL", "Valeur non présente").build();
        } else {
            boolean fileExists = false;
            if (!StringUtils.isEmpty(optAuthPublicKey)){
                File f = new File(optAuthPublicKey);
                fileExists = f.exists() && !f.isDirectory();
            }

            if (fileExists)
            {
                return Health.up().withDetail("OPT_AUTH_PUBLIC_KEY_FILE_PATH", optAuthPublicKey + " la clé et le fichier existe").build();
            } else {
                return Health.down().withDetail("OPT_AUTH_PUBLIC_KEY_FILE_PATH", optAuthPublicKey +  " la clé ou le fichier n'existe pas").build();
            }
        }
    }
}
