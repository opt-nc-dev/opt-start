package nc.opt.core.poc.app.security.jwt;

import org.springframework.security.core.Authentication;

/**
 * Created by 2617ray on 03/03/2017.
 */
public interface TokenProvider {

    Authentication getAuthentication(String token);

    boolean validateToken(String authToken);

}
