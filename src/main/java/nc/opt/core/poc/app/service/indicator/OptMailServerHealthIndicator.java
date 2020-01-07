package nc.opt.core.poc.app.service.indicator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@Component(value = "optMail")
public class OptMailServerHealthIndicator implements HealthIndicator {
    private final Logger log = LoggerFactory.getLogger(OptAuthHealthIndicator.class);

    @Override
    public Health health() {
        String optMailServerHost = System.getenv("OPT_MAIL_SERVER_HOST");
        String optMailServerPort = System.getenv("OPT_MAIL_SERVER_PORT");
        log.info("Health OPT_MAIL_SERVER_HOST " + optMailServerHost + " OPT_MAIL_SERVER_PORT "+ optMailServerPort);

        if (StringUtils.isEmpty(optMailServerHost))
        {
            log.info("Health OPT_MAIL_SERVER_HOST empty");
            return Health.unknown().withDetail("OPT_MAIL_SERVER_HOST", "Valeur non présente").build();
        } else {
            SocketAddress sockaddr = new InetSocketAddress(optMailServerHost, Integer.valueOf(optMailServerPort));
            try {
                Socket socket = new Socket();
                socket.connect(sockaddr, 4000);
                log.info("Health OPT_MAIL_SERVER_HOST OK");
                return Health.up().withDetail("ping", "connexion TCP OK").build();
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.info("Health OPT_MAIL_SERVER_HOST false");
            return Health.down().withDetail("ping", "connexion TCP KO").build();
        }
    }
}
