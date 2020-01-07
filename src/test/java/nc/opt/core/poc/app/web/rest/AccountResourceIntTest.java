package nc.opt.core.poc.app.web.rest;

import nc.opt.core.poc.app.PocApp;
import nc.opt.core.poc.app.security.UserOPT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see AccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocApp.class)
public class AccountResourceIntTest {

    private MockMvc restMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AccountResource accountResource =
            new AccountResource();

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
    }
    @Test
    public void testGetExistingAccount() throws Exception {

        // Roles
        Set<GrantedAuthority> authorities = new HashSet<>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_1");
        authorities.add(authority);

        // Ressources
        Map<String, List<String>> ressources = new HashMap<>();
        ressources.put("ROLE_1", Arrays.asList("UN", "DEUX"));

        // User
        UserOPT userOPT = new UserOPT("2617ray", "Raynaud", "Nicolas", authorities, ressources);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userOPT, "", authorities);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        restMvc.perform(get("/api/account")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.uid").value("2617ray"))
            .andExpect(jsonPath("$.nom").value("Raynaud"))
            .andExpect(jsonPath("$.prenom").value("Nicolas"))
            .andExpect(jsonPath("$.authorities").value("ROLE_1"))
            .andExpect(jsonPath("$.ressources[0].role").value("ROLE_1"))
            .andExpect(jsonPath("$.ressources[0].ressources[0]").value("UN"))
            .andExpect(jsonPath("$.ressources[0].ressources[1]").value("DEUX"));
    }

    @Test
    public void testGetUnknownAccount() throws Exception {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        restMvc.perform(get("/api/account")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

}
