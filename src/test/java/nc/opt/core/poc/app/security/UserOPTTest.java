package nc.opt.core.poc.app.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by 2617ray on 15/06/2017.
 */
@RunWith(JUnit4.class)
public class UserOPTTest {

    @Test
    public void test_setUser_verifyToString() {

        // Given
        List<GrantedAuthority> gas = Arrays.asList(new SimpleGrantedAuthority("ROLE_1"));
        Map<String, List<String>> ressources = new HashMap<>();
        ressources.put("ROLE_1", Arrays.asList("RES_1"));
        UserOPT userOPT = new UserOPT("uid", "nom", "prenom", gas, new HashMap<>());

        // When
        userOPT.setUid("UID");
        userOPT.setNom("NOM");
        userOPT.setPrenom("PRENOM");
        userOPT.setRessources(ressources);

        // Then
        assertThat(userOPT.toString()).isEqualTo("UserOPT{uid='UID', nom='NOM', prenom='PRENOM', roles='[ROLE_1]', ressources={ROLE_1=[RES_1]}}");
    }

    @Test
    public void test_equals_verifyEquals() {

        // Given
        List<GrantedAuthority> gas = Arrays.asList(new SimpleGrantedAuthority("ROLE_1"));
        Map<String, List<String>> ressources = new HashMap<>();
        ressources.put("ROLE_1", Arrays.asList("RES_1"));
        UserOPT userOPT = new UserOPT("uid", "nom", "prenom", gas, ressources);

        List<GrantedAuthority> gas2 = Arrays.asList(new SimpleGrantedAuthority("ROLE_1"));
        Map<String, List<String>> ressources2 = new HashMap<>();
        ressources2.put("ROLE_1", Arrays.asList("RES_1"));
        UserOPT userOPT2 = new UserOPT("uid", "nom", "prenom", gas2, ressources2);

        // When & Then
        assertThat(userOPT.equals(userOPT2)).isTrue();
    }

    @Test
    public void test_hashcode_verify() {

        // Given
        List<GrantedAuthority> gas = Arrays.asList(new SimpleGrantedAuthority("ROLE_1"));
        Map<String, List<String>> ressources = new HashMap<>();
        ressources.put("ROLE_1", Arrays.asList("RES_1"));
        UserOPT userOPT = new UserOPT("uid", "nom", "prenom", gas, ressources);

        // When & Then
        assertThat(userOPT.hashCode()).isEqualTo(999869596);
    }
}
