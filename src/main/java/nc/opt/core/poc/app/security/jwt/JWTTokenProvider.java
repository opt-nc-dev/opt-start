package nc.opt.core.poc.app.security.jwt;

import io.github.jhipster.config.JHipsterProperties;
import io.jsonwebtoken.*;
import nc.opt.core.poc.app.config.OptSecurityConfiguration;
import nc.opt.core.poc.app.security.UserOPT;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("jwtTokenProvider")
public class JWTTokenProvider implements TokenProvider {

    private final Logger log = LoggerFactory.getLogger(JWTTokenProvider.class);

    private PublicKey publicKey;

    private final JHipsterProperties jHipsterProperties;

    private final OptSecurityConfiguration optSecurityConfiguration;

    private static final String UID_KEY = "uid";
    private static final String NOM_KEY = "nom";
    private static final String PRENOM_KEY = "prenom";
    private static final String ROLES_KEY = "roles";
    private static final String RESSOURCES_KEY = "ressources";

    public JWTTokenProvider(JHipsterProperties jHipsterProperties, OptSecurityConfiguration optSecurityConfiguration) {
        this.jHipsterProperties = jHipsterProperties;
        this.optSecurityConfiguration = optSecurityConfiguration;
    }

    @PostConstruct
    public void init() {

        if (!"jwt".equals(optSecurityConfiguration.getType())) {
            return;
        }

        String keyPath =
            jHipsterProperties.getSecurity().getAuthentication().getJwt().getSecret();

        log.info("Starting up TokenProvider with public key path [jhipster.security.authentication.jwt.secretKey] = {}", keyPath);

        try (InputStream inputStream = new FileInputStream(keyPath)) {

            X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(inputStream));

            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.publicKey = kf.generatePublic(spec);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SecurityException("Erreur lors de la lecture de la clé publique JWT", e);
        }

        log.info("Started token provider", keyPath);
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(publicKey)
            .parseClaimsJws(token)
            .getBody();

        List<String> roles = claims.get(ROLES_KEY, List.class);

        List<? extends GrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        UserOPT principal = new UserOPT(claims.get(UID_KEY).toString(),
            claims.get(NOM_KEY).toString(),
            claims.get(PRENOM_KEY).toString(),
            authorities,
            (Map<String, List<String>>) claims.get(RESSOURCES_KEY));

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    @Override
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
