package nc.opt.core.poc.app.web.rest;

import nc.opt.core.poc.app.PocApp;
import nc.opt.core.poc.app.config.ApplicationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by 2617ray on 13/04/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PocApp.class)
public class ConfigurationResourceIntTest {

    @Autowired
    private ApplicationProperties applicationProperties;

    private MockMvc restMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ConfigurationResource configurationResource = new ConfigurationResource(applicationProperties);

        this.restMvc = MockMvcBuilders.standaloneSetup(configurationResource).build();
    }

    @Test
    public void testGetExistingAccount() throws Exception {

        restMvc.perform(get("/management/configuration")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.auth-url").value("https://auth-test.intranet.opt/"));
    }

}
