package nc.opt.core.poc.app.service.dto.security;

import nc.opt.core.poc.app.security.UserOPT;
import nc.opt.core.poc.app.service.dto.security.UserDTO;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by 2617ray on 03/03/2017.
 */
public class UserDTOTest {

    @Test
    public void test_constructor_From_UserOPT() {

        // Given
        List<GrantedAuthority> roles = Arrays.asList("ROLE1", "ROLE2").stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        Map<String, List<String>> ressources = new HashMap<>();
        ressources.put("ROLE1", new ArrayList<>());
        ressources.put("ROLE2", new ArrayList<>());
        ressources.get("ROLE1").add("TEST");

        UserOPT userOPT = new UserOPT("1", "2", "3", roles ,ressources);

        // When
        UserDTO result = new UserDTO(userOPT);

        // Then
        assertThat(result.getUid()).isEqualTo("1");
        assertThat(result.getNom()).isEqualTo("2");
        assertThat(result.getPrenom()).isEqualTo("3");

    }

    @Test
    public void test_toString() {

        // Given
        UserDTO dto = new UserDTO();
        dto.setUid("admin");
        dto.setNom("ad");
        dto.setPrenom("min");
        dto.setAuthorities(Arrays.asList("ROLE_1", "ROLE_2"));
        dto.setRessources(new ArrayList<>());
        dto.getRessources().add(new RessourceDTO("ROLE_1", Arrays.asList("RES_1")));

        // When
        String str = dto.toString();

        // Then
        assertThat(str).isEqualTo("UserDTO{uid='admin', nom='ad', prenom='min', authorities=[ROLE_1, ROLE_2], ressources=[RessourceDTO{role='ROLE_1', ressources=[RES_1]}]}");
    }
}
