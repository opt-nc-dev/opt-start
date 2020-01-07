package nc.opt.core.poc.app.service.indicator;

import nc.opt.core.kafka.actuate.KafkaBrokersHealthService;
import nc.opt.core.kafka.config.ConditionalOnOPTKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

// Si la configuration contient KAFKA_BROKERS_HOST alors on ne monte pas le composant
// Car la variable d'env n'existe pas
@ConditionalOnOPTKafka
@Component(value = "kafka")
public class KafkaHealthIndicator implements HealthIndicator {

    private final Logger log = LoggerFactory.getLogger(KafkaHealthIndicator.class);
    private final KafkaBrokersHealthService kafkaBrokersHealthService;

    public KafkaHealthIndicator(KafkaBrokersHealthService kafkaBrokersHealthService) {
        this.kafkaBrokersHealthService = kafkaBrokersHealthService;
    }

    @Override
    public Health health() {
        String kafkaBrokerHost = System.getenv("KAFKA_BROKERS_HOST");
        log.info("Health KAFKA_BROKERS_HOST " + kafkaBrokerHost);
        if (StringUtils.isEmpty(kafkaBrokerHost))
        {
            log.info("Health KAFKA_BROKERS_HOST empty");
            return Health.unknown().withDetail("KAFKA_BROKERS_HOST", "Valeur non présente").build();
        } else {
            log.info("Health KAFKA_BROKERS_HOST exist do Health");
            return kafkaBrokersHealthService.health();
        }
    }
}
