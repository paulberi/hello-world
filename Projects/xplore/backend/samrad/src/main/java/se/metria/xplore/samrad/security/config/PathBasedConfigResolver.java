package se.metria.xplore.samrad.security.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.context.annotation.Bean;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PathBasedConfigResolver implements KeycloakConfigResolver {
    private final Map<String, KeycloakDeployment> cache = new ConcurrentHashMap<>();

    private static AdapterConfig adapterConfig;

    @Override
    public KeycloakDeployment resolve(HttpFacade.Request request) {

        String path = request.getURI();
        String realm="";

        if(path.contains("api")){

            int multitenantIndex = path.indexOf("api/");

            if (multitenantIndex == -1) {
                throw new IllegalStateException("Not able to resolve realm from the request path!");
            }

            realm = path.substring(path.indexOf("api/")).split("/")[1];
            if (realm.contains("?")) {
                realm = realm.split("\\?")[0];
            }

            if (!cache.containsKey(realm)) {
                InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
                cache.put(realm, KeycloakDeploymentBuilder.build(is));
            }
        }else{
            realm="admin";
            InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
            cache.put(realm, KeycloakDeploymentBuilder.build(is));
        }
        return cache.get(realm);

    }


    static void setAdapterConfig(AdapterConfig adapterConfig) {
        PathBasedConfigResolver.adapterConfig = adapterConfig;
    }
}