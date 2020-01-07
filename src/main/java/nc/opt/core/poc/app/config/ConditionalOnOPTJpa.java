package nc.opt.core.poc.app.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnExpression("'${spring.datasource.url}' matches '^.*DB_URL.*$'")
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
public @interface ConditionalOnOPTJpa {
}
