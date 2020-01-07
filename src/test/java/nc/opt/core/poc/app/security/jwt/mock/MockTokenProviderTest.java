package nc.opt.core.poc.app.security.jwt.mock;

import nc.opt.core.poc.app.PocApp;
import nc.opt.core.poc.app.security.UserOPT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by 2617ray on 13/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocApp.class)
public class MockTokenProviderTest {

    @Autowired
    private MockTokenProvider mockTokenProvider;

    @Test
    public void test_fetchRolesFromToken() {
        // Given
        String login = "admin";

        // When
        Authentication authentication = mockTokenProvider.getAuthentication(login);

        // Then
        assertThat(((UserOPT) authentication.getPrincipal()).getNom()).isEqualTo("AD");
        assertThat(((UserOPT) authentication.getPrincipal()).getPrenom()).isEqualTo("Min");
        assertThat(((UserOPT) authentication.getPrincipal()).getUid()).isEqualTo("admin");
        assertThat(((UserOPT) authentication.getPrincipal()).getRoles()).contains("ROLE_ADMIN", "ROLE_USER");
        assertThat(authentication.getAuthorities())
            .extracting("authority")
            .contains("ROLE_ADMIN", "ROLE_USER");
        assertThat(((UserOPT) authentication.getPrincipal()).getRessources().get("ROLE_ADMIN"))
            .containsExactly("ressource1");
    }

    @Test
    public void test_validateToken_true() {
        // Given
        String login = "admin";

        // When & Then
        assertThat(mockTokenProvider.validateToken(login)).isTrue();

    }

    @Test
    public void test_validateToken_false() {
        // Given
        String login = "test";

        // When & Then
        assertThat(mockTokenProvider.validateToken(login)).isFalse();

    }
}
