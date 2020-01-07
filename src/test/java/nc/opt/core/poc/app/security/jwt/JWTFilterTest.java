package nc.opt.core.poc.app.security.jwt;

import nc.opt.core.poc.app.security.AuthoritiesConstants;
import nc.opt.core.poc.app.security.UserOPT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JWTFilterTest {

    private TokenProvider tokenProvider;

    private JWTFilter jwtFilter;

    @Before
    public void setup() {
        tokenProvider = mock(TokenProvider.class);
        jwtFilter = new JWTFilter(tokenProvider);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testJWTFilter() throws Exception {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            "test-user",
            "test-password",
            Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.USER))
        );
        String jwt = validToken();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        when(tokenProvider.validateToken(validToken())).thenReturn(true);
        List<GrantedAuthority> gas = Arrays.asList(new SimpleGrantedAuthority("ROLE_1"));
        UserOPT principal = new UserOPT("test-user", null, null, gas,  new HashMap<>());
        when(tokenProvider.getAuthentication(validToken())).thenReturn(new UsernamePasswordAuthenticationToken(principal, jwt, gas));
        jwtFilter.doFilter(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(principal);
        assertThat(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString()).isEqualTo(jwt);
    }

    @Test
    public void testJWTFilterInvalidToken() throws Exception {
        String jwt = "wrong_jwt";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testJWTFilterMissingAuthorization() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    public void testJWTFilterMissingToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer ");
        request.setRequestURI("/api/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();
        jwtFilter.doFilter(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
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

}
