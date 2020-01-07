package nc.opt.core.poc.app.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 2617ray on 26/04/2017.
 */
@RestController
public class AliveController {

    /**
     * GET, OPTIONS  /alive : Service that is just here to signal the app is correctly started
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping("/alive")
    void handleFoo(HttpServletResponse response) throws IOException {
        response.sendRedirect("/management/health");
    }
}
