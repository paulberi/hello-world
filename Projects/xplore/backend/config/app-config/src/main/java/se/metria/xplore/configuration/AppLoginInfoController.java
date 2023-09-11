package se.metria.xplore.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.metria.xplore.configuration.model.ClientId;

@RestController
@CrossOrigin(origins = "*")
public class AppLoginInfoController {
    private final AppLoginInfoService appLoginInfoService;

    public AppLoginInfoController(AppLoginInfoService appLoginInfoService) {
        this.appLoginInfoService = appLoginInfoService;
    }

    @RequestMapping(value = "/app-logininfo/{realm}/{clientId}", method = RequestMethod.GET)
    @Cacheable("loginPageInfo")
    public JsonNode getLoginPageInfo(
            @PathVariable String realm,
            @PathVariable String clientId) {
        var result =  appLoginInfoService.getLoginInfo(new ClientId(realm, clientId));

        if (result != null) {
            return result.getJson();
        }

        result =  appLoginInfoService.getLoginInfo(new ClientId(realm, "default"));

        if (result != null) {
            return result.getJson();
        }

        result =  appLoginInfoService.getLoginInfo(new ClientId("default", "default"));

        if (result != null) {
            return result.getJson();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
