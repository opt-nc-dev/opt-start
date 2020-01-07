package nc.opt.core.poc.app.service.indicator;

import nc.opt.core.search.elasticsearch.actuate.health.ElasticsearchHealthService;
import nc.opt.core.search.elasticsearch.config.ConditionalOnOPTELS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.LinkedHashMap;
import java.util.Map;

@Component(value = "EnvVars")
public class EnvVarHealthIndicator implements HealthIndicator {

    private static final String NO_VALUE = "Valeur non présente";
    private final Logger log = LoggerFactory.getLogger(EnvVarHealthIndicator.class);

    @Override
    public Health health() {
        log.info("Récupération des variables d'env Start");
        Map<String, String> envVarsMap = new LinkedHashMap<>();
        String dbUrl = System.getenv("DB_URL");
        envVarsMap.put("DB_URL", StringUtils.isEmpty(dbUrl) ? NO_VALUE : dbUrl);
        String dbUsername = System.getenv("DB_USERNAME");
        envVarsMap.put("DB_USERNAME", StringUtils.isEmpty(dbUsername) ? NO_VALUE : dbUsername);
        String serverPort = System.getenv("SERVER_PORT");
        envVarsMap.put("SERVER_PORT", StringUtils.isEmpty(serverPort) ? NO_VALUE : serverPort);
        String optAuthServerUrl = System.getenv("OPT_AUTH_SERVER_URL");
        envVarsMap.put("OPT_AUTH_SERVER_URL", StringUtils.isEmpty(optAuthServerUrl) ? NO_VALUE : optAuthServerUrl);
        String optAuthPublicKeyFilePath = System.getenv("OPT_AUTH_PUBLIC_KEY_FILE_PATH");
        envVarsMap.put("OPT_AUTH_PUBLIC_KEY_FILE_PATH", StringUtils.isEmpty(optAuthPublicKeyFilePath) ? NO_VALUE : optAuthPublicKeyFilePath);
        String mailServerHost = System.getenv("OPT_MAIL_SERVER_HOST");
        envVarsMap.put("OPT_MAIL_SERVER_HOST", StringUtils.isEmpty(mailServerHost) ? NO_VALUE : mailServerHost);
        String mailServerPort = System.getenv("OPT_MAIL_SERVER_PORT");
        envVarsMap.put("OPT_MAIL_SERVER_PORT", StringUtils.isEmpty(mailServerPort) ? NO_VALUE : mailServerPort);
        String esClusterNode = System.getenv("ES_CLUSTER_NODES");
        envVarsMap.put("ES_CLUSTER_NODES", StringUtils.isEmpty(esClusterNode) ? NO_VALUE : esClusterNode);
        String esUsername = System.getenv("ES_USERNAME");
        envVarsMap.put("ES_USERNAME", StringUtils.isEmpty(esUsername) ? NO_VALUE : esUsername);
        String kafkaBrokerHost = System.getenv("KAFKA_BROKERS_HOST");
        envVarsMap.put("KAFKA_BROKERS_HOST", StringUtils.isEmpty(kafkaBrokerHost) ? NO_VALUE : kafkaBrokerHost);
        String logFile = System.getenv("LOG_FILE");
        envVarsMap.put("LOG_FILE", StringUtils.isEmpty(logFile) ? NO_VALUE : logFile);
        String logFileJson = System.getenv("LOG_FILE_JSON");
        envVarsMap.put("LOG_FILE_JSON", StringUtils.isEmpty(logFileJson) ? NO_VALUE : logFileJson);
        String maxHistory = System.getenv("MAX_HISTORY");
        envVarsMap.put("MAX_HISTORY", StringUtils.isEmpty(maxHistory) ? NO_VALUE : maxHistory);
        String maxFileSize = System.getenv("MAX_FILE_SIZE");
        envVarsMap.put("MAX_FILE_SIZE", StringUtils.isEmpty(maxFileSize) ? NO_VALUE : maxFileSize);
        String totalSizeCap = System.getenv("TOTAL_SIZE_CAP");
        envVarsMap.put("TOTAL_SIZE_CAP", StringUtils.isEmpty(totalSizeCap) ? NO_VALUE : totalSizeCap);

        log.info("Récupération des variables d'env End");
        return new Health.Builder(Status.UNKNOWN, envVarsMap).build();
    }
}
