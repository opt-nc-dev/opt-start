package nc.opt.core.poc.app.security.jwt.mock;

import nc.opt.core.poc.app.config.OptSecurityConfiguration;
import nc.opt.core.poc.app.security.jwt.TokenProvider;
import nc.opt.core.poc.app.security.UserOPT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is a MockTokenProvider for dev profile.
 * This replaces the normal JWT Token Provider with a mock that simply reads the JWT Token as the user to authenticate :
 * - if the user exists in the application-dev configuration file, it returns its definition.
 * - If it doesn't exist, if fails the authentication.
 *
 * Created by 2617ray on 03/03/2017.
 */
@Component("mockTokenProvider")
public class MockTokenProvider implements TokenProvider {

    @Autowired
    private OptSecurityConfiguration configuration;

    @Override
    public Authentication getAuthentication(String token) {

        String nom = (String)((Map<String, Object>) configuration.getCredentials().getUsers().get(token)).get("nom");
        String prenom = (String)((Map<String, Object>) configuration.getCredentials().getUsers().get(token)).get("prenom");

        List<? extends GrantedAuthority> authorities =
            Arrays.stream(StringUtils.trim((String)((Map<String, Object>) configuration.getCredentials().getUsers().get(token))
                .get("roles")).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Map<String, List<String>> ressources =
            (((Map<String, Map<String, String>>) configuration.getCredentials().getUsers().get(token)).get("ressources")).entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    m -> Arrays.asList(StringUtils.trim(m.getValue()).split(","))));

        UserOPT principal = new UserOPT(token,
            nom,
            prenom,
            authorities,
            ressources);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    @Override
    public boolean validateToken(String authToken) {
        return configuration.getCredentials().getUsers().containsKey(authToken);
    }
}
