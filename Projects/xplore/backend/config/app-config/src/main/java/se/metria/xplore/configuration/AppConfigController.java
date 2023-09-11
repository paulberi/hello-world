package se.metria.xplore.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.metria.xplore.authorization.JwtVerifier;
import se.metria.xplore.configuration.exceptions.ConfigurationNotFoundException;
import se.metria.xplore.configuration.exceptions.ForbiddenException;
import se.metria.xplore.configuration.exceptions.InvalidConfigurationException;
import se.metria.xplore.configuration.exceptions.UnauthorizedException;
import se.metria.xplore.configuration.model.AppConfig;
import se.metria.xplore.configuration.model.AppConfigRequest;
import se.metria.xplore.configuration.model.AppConfigRestriction;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
public class AppConfigController {
    Logger logger = LoggerFactory.getLogger(AppConfigController.class);

    private AppConfigService appConfigService;

    public AppConfigController(AppConfigService appConfigService) {
        this.appConfigService = appConfigService;
    }

    @RequestMapping("/app-config")
    public ResponseEntity<String> post(HttpServletRequest request, @RequestBody AppConfigRequest appConfigRequest)  {
        try {
            DecodedJWT jwt = (DecodedJWT) request.getSession().getAttribute("jwt");
            validateToken(jwt);

            AppConfig appConfig = appConfigService.getAppConfig(
                    appConfigRequest,
                    buildUserProfile(jwt, appConfigRequest.app));

            return new ResponseEntity<>(appConfig.getJson().toString(), new HttpHeaders(), HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (ConfigurationNotFoundException e) {
            logger.warn("Configuration not found for app \""+appConfigRequest.app+
                    "\", configuration \""+appConfigRequest.config+"\"");

            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (InvalidConfigurationException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void validateToken(DecodedJWT jwt) throws UnauthorizedException {
        if (jwt != null) {
            String iss = jwt.getClaim("iss").asString();

            if (!JwtVerifier.verify(iss + "/protocol/openid-connect/certs", jwt.getToken())) {
                throw new UnauthorizedException();
            }
        }
    }

    private AppConfigRestriction buildUserProfile(DecodedJWT jwt, String app) {

        AppConfigRestriction ar = new AppConfigRestriction();

        if (jwt != null) {
            String iss = jwt.getClaim("iss").asString();

            ar.realms = new String[]{iss.substring(iss.lastIndexOf("/") + 1)};
            ar.clients = new String[]{jwt.getClaim("azp").asString()};
            ar.users = new String[]{jwt.getClaim("preferred_username").asString()};
            ar.groups = jwt.getClaim("groups").asArray(String.class);
            ar.roles = new String[]{};
            var realm_access = jwt.getClaim("realm_access");
            if (!realm_access.isNull()) {
                var roles = realm_access.asMap().get("roles");
                if (roles != null) {
                    var rolesList = (ArrayList<String>) roles;
                    ar.roles = rolesList.toArray(new String[]{});
                }
            }
        } else {
            ar.clients = new String[]{app};
        }

        return ar;
    }
}
