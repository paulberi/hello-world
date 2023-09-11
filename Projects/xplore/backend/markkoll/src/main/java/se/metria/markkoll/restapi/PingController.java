package se.metria.markkoll.restapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.openapi.api.PingApi;

@RequestMapping(value = "/api")
@RestController
public class PingController implements PingApi {
    @Override
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong!");
    }

    @Override
    public ResponseEntity<String> pingAuth() {
        return ResponseEntity.ok("pong!");
    }
}
