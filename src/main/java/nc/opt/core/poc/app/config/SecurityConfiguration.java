package nc.opt.core.poc.app.config;

import nc.opt.core.poc.app.security.*;
import nc.opt.core.poc.app.security.jwt.*;

import io.github.jhipster.security.*;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    public SecurityConfiguration(@Value("#{optSecurityConfiguration.getType().equals('jwt') ? jwtTokenProvider : mockTokenProvider}")
                                 TokenProvider tokenProvider,
                                 CorsFilter corsFilter) {

        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
    }

    @Bean
    public Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint() {
        return new Http401UnauthorizedEntryPoint();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(http401UnauthorizedEntryPoint())
        .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/configuration").permitAll()
            .antMatchers("/management/**").permitAll()
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN)
        .and()
            .apply(securityConfigurerAdapter());

    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}
