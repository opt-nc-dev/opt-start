package nc.opt.core.poc.app.service.indicator;

import nc.opt.core.poc.app.service.MailService;
import nc.opt.core.search.elasticsearch.actuate.health.ElasticsearchHealthService;
import nc.opt.core.search.elasticsearch.config.ConditionalOnOPTELS;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component(value = "elasticSearch")
// Si la configuration contient ES_CLUSTER_NODES alors on ne monte pas le composant
// Car la variable d'env n'existe pas
@ConditionalOnOPTELS
public class ElasticHealthIndicator implements HealthIndicator {

    private ElasticsearchHealthService elasticsearchHealthService;
    private final Logger log = LoggerFactory.getLogger(ElasticHealthIndicator.class);

    public ElasticHealthIndicator(ElasticsearchHealthService elasticsearchHealthService) {
        this.elasticsearchHealthService = elasticsearchHealthService;
    }

    @Override
    public Health health() {
        String esClusterNode = System.getenv("ES_CLUSTER_NODES");
        log.info("Health ES_CLUSTER_NODES " + esClusterNode);
        if (StringUtils.isEmpty(esClusterNode))
        {
            log.info("Health ES_CLUSTER_NODES empty");
            return Health.unknown().withDetail("ES_CLUSTER_NODES", "Valeur non présente").build();
        } else {
            log.info("Health ES_CLUSTER_NODES exist do Health");
            return elasticsearchHealthService.health();
        }
    }
}
