package nc.opt.core.poc.app.service.dto.security;

import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Created by 2617ray on 03/03/2017.
 */
public class RessourceDTOTest {

    @Test
    public void test_constructor() {

        // Given
        String role = "ROLE_1";
        List<String> ressources = Arrays.asList("RES_1", "RES_2");

        // When
        RessourceDTO result = new RessourceDTO(role, ressources);

        // Then
        assertThat(result.getRole()).isEqualTo("ROLE_1");
        assertThat(result.getRessources()).asList().containsExactly("RES_1", "RES_2");

    }
}
