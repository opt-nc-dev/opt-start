package nc.opt.core.poc.app.security.jwt;

import io.github.jhipster.config.JHipsterProperties;
import nc.opt.core.poc.app.config.OptSecurityConfiguration;
import nc.opt.core.poc.app.security.UserOPT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class JWTTokenProviderTest {

    private JWTTokenProvider jwtTokenProvider;

    private final static String PUBLIC_KEY_PATH = "public_key_test.der";

    @Before
    public void setup() throws URISyntaxException, FileNotFoundException {
        // Setup public key path
        JHipsterProperties jHipsterProperties = new JHipsterProperties();
        URL publicKeyUrl = getClass().getClassLoader().getResource(PUBLIC_KEY_PATH);
        String pathPublicKey = publicKeyUrl.toURI().toString().substring("file:".length(), publicKeyUrl.toURI().toString().length());//resourceLocation.toURI().toString();
        jHipsterProperties.getSecurity().getAuthentication().getJwt().setSecret(pathPublicKey);

        // Setup JWT security
        OptSecurityConfiguration options = new OptSecurityConfiguration();
        options.setType("jwt");

        jwtTokenProvider = new JWTTokenProvider(jHipsterProperties, options);
        jwtTokenProvider.init();
    }

    @Test
    public void testInit_JWT() throws URISyntaxException {
        JHipsterProperties jHipsterProperties = new JHipsterProperties();
        URL publicKeyUrl = getClass().getClassLoader().getResource(PUBLIC_KEY_PATH);
        String pathPublicKey = publicKeyUrl.toURI().toString().substring("file:".length(), publicKeyUrl.toURI().toString().length());//resourceLocation.toURI().toString();
        jHipsterProperties.getSecurity().getAuthentication().getJwt().setSecret(pathPublicKey);

        OptSecurityConfiguration options = new OptSecurityConfiguration();
        options.setType("jwt");

        JWTTokenProvider jwtTokenProviderLocal = new JWTTokenProvider(jHipsterProperties, options);
        jwtTokenProviderLocal.init();

        assertThat(ReflectionTestUtils.getField(jwtTokenProviderLocal, "publicKey")).isNotNull();
    }


    @Test(expected = SecurityException.class)
    public void testInit_JWT_failure() throws URISyntaxException {
        JHipsterProperties jHipsterProperties = new JHipsterProperties();
        String pathPublicKey = "file://notexisting.der";
        jHipsterProperties.getSecurity().getAuthentication().getJwt().setSecret(pathPublicKey);

        OptSecurityConfiguration options = new OptSecurityConfiguration();
        options.setType("jwt");

        JWTTokenProvider jwtTokenProviderLocal = new JWTTokenProvider(jHipsterProperties, options);
        jwtTokenProviderLocal.init();
    }

    @Test
    public void testInit_LOCAL() {
        JHipsterProperties jHipsterProperties = new JHipsterProperties();
        OptSecurityConfiguration options = new OptSecurityConfiguration();
        options.setType("local");

        JWTTokenProvider jwtTokenProviderLocal = new JWTTokenProvider(jHipsterProperties, options);
        jwtTokenProviderLocal.init();

        assertThat(ReflectionTestUtils.getField(jwtTokenProviderLocal, "publicKey")).isNull();
    }

    @Test
    public void testReturnTrueWhenJWTisValid() {
        boolean isTokenValid = jwtTokenProvider.validateToken(validToken());

        assertThat(isTokenValid).isEqualTo(true);
    }

    @Test
    public void testReturnFalseWhenJWThasInvalidSignature() {
        boolean isTokenValid = jwtTokenProvider.validateToken(differentKeyToken());

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisMalformed() {
        String token = validToken();
        String invalidToken = token.substring(1);
        boolean isTokenValid = jwtTokenProvider.validateToken(invalidToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisExpired() {
        String expiredToken = expiredToken();

        boolean isTokenValid = jwtTokenProvider.validateToken(expiredToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisUnsupported() {
        String unsupportedToken = unsupportedToken();

        boolean isTokenValid = jwtTokenProvider.validateToken(unsupportedToken);

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void testReturnFalseWhenJWTisInvalid() {
        boolean isTokenValid = jwtTokenProvider.validateToken("");

        assertThat(isTokenValid).isEqualTo(false);
    }

    @Test
    public void getAuthentication_ReturnAuthenticationWithUserOPT() {
        String jwt = validToken();

        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);

        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(authentication.getPrincipal()).isInstanceOf(UserOPT.class);
        assertThat(((UserOPT) authentication.getPrincipal()).getUid()).isEqualTo("testuser");
        assertThat(((UserOPT) authentication.getPrincipal()).getUsername()).isEqualTo("testuser");
        assertThat(((UserOPT) authentication.getPrincipal()).getNom()).isEqualTo("nom");
        assertThat(((UserOPT) authentication.getPrincipal()).getPrenom()).isEqualTo("prenom");
        assertThat(((UserOPT) authentication.getPrincipal()).getRoles()).contains("ROLE_1", "ROLE_2");
        assertThat(((UserOPT) authentication.getPrincipal()).getRessources().get("ROLE_1")).contains("RES_1");
        assertThat(((UserOPT) authentication.getPrincipal()).getRessources().get("ROLE_2")).contains("RES_2", "RES_3");
    }

    /**
     * Returns
     * UserOPT{uid='testuser', prenom='prenom', nom='nom', roles=[ROLE_1, ROLE_2], ressources={ROLE_1=[RES_1], ROLE_2=[RES_2, RES_3]}}
     * with a 30 years validity starting 2017-06-14
     * @return the jwt token
     */
    private String validToken() {
        return "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJKV1RfT1BUX0FVVEgiLCJ1aWQiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfMSIsIlJPTEVfMiJdLCJleHAiOjU4MjYyMjE2NzY1LCJwcmVub20iOiJwcmVub20iLCJub20iOiJub20iLCJyZXNzb3VyY2VzIjp7IlJPTEVfMSI6WyJSRVNfMSJdLCJST0xFXzIiOlsiUkVTXzIiLCJSRVNfMyJdfX0.bOJnef7rRJN5JEUpPFqYn2-fBma7yOZYhfvqd3XVRKitn9t_lfNj64HZtw1UI5yuSZXTPlLuF6Ed0d5MtKg994BjxBHiY4hqoZ2V2GdkCEXLgXK0RdqX7AimUvfFfpoPpywTknMsjaEWDOaDJ7d4jiNtkXmzbwZj_FT8AxIcKN36T1YwK6OOwc0EFNxOmgygTQ4WL01awtP2_GEMo6AKD8fq209dVY6qhxUjpaofoVMJJrNc9MkOcRPmS5Mz0JcEv8LuS79oH6X_3x7XG6XiqElZUMnIs6lBWWtHn_7Qnu2yotQAbpGPsODIWDKNV0xohdUTGRt_4q0nrMLfZk5mog";
    }

    /**
     * Returns
     * UserOPT{uid='testuser', prenom='prenom', nom='nom', roles=[ROLE_1, ROLE_2], ressources={ROLE_1=[RES_1], ROLE_2=[RES_2, RES_3]}}
     * with an expired date
     * @return the jwt token
     */
    private String expiredToken() {
        return "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJKV1RfT1BUX0FVVEgiLCJ1aWQiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfMSIsIlJPTEVfMiJdLCJleHAiOjE0OTc0MTYyNzEsInByZW5vbSI6InByZW5vbSIsIm5vbSI6Im5vbSIsInJlc3NvdXJjZXMiOnsiUk9MRV8xIjpbIlJFU18xIl0sIlJPTEVfMiI6WyJSRVNfMiIsIlJFU18zIl19fQ.FXszynKTO3vy0GOyVUQOvnavsSxnJS2vOMgEiof-dLkP1yjk3R77hFzC4XaAXAavdyUf7LeNwOlYa_bwX-bTFxLRns4xoTCPG51uXJK944KHlyAMyReQXHQZEqdC4xVr9QqLkdXP7wKbs-5MpQpiNpSyAZC-ohVB4ZfTuL277q1kAnmSGBEK659oQUb_HKMJHSzPnFUgOzi9mKULYqU7unCjMeCrYXRmEPqWdow7YJsDm866j865JQD1EuAB6qN-Ziz5yF42E_14CFFwkVeBtTJC8YemcN87-VLpILd8rRlBiRCI5Ohyon4DC6V6xIdzBpiK-MCnulRnicDwNSfAAg";
    }

    /**
     * Returns
     * UserOPT{uid='testuser', prenom='prenom', nom='nom', roles=[ROLE_1, ROLE_2], ressources={ROLE_1=[RES_1], ROLE_2=[RES_2, RES_3]}}
     * with a different public key
     * @return the jwt token
     */
    private String differentKeyToken() {
        return "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJKV1RfT1BUX0FVVEgiLCJ1aWQiOiJ0ZXN0dXNlciIsInJvbGVzIjpbIlJPTEVfMSIsIlJPTEVfMiJdLCJleHAiOi01NTI2NzM4MzA4NSwicHJlbm9tIjoicHJlbm9tIiwibm9tIjoibm9tIiwicmVzc291cmNlcyI6eyJST0xFXzEiOlsiUkVTXzEiXSwiUk9MRV8yIjpbIlJFU18yIiwiUkVTXzMiXX19.XcRK-9nmxNY_UTrqy8yxT5Ap_uO6BHPMoJTciZbA7r87WgT0nq-z3qN0Dm9R1j5b9LBcDOm-kHf_LHVLlBZoVErMXr0mgknTZk0C5AZNCO53c0Ee64MspL6gAWBU08RrZKOsLxZLLyOhw6mGYuuPlONDodAT2sQcIvNw7yUZALwz31rrhZMQhBzj5ILQLUWidpF_PV3oZEm-R-G-gYwDtgXsgmpfxv1EfImVwb-UeUcl3y-CjtTYBD7Wj-BKAKbohvjLn0F26z3wxmybGCk2ntR7IgRxKKh-sWcDg9o7VS313t8-CAA1K1-sUCgSimMzg_9JuPUsPZka27UsrPVgug";
    }

    /**
     * Returns
     * a token without cryptography
     * @return the jwt token
     */
    private String unsupportedToken() {
        return "{uid: 'testuser'}";
    }
}
