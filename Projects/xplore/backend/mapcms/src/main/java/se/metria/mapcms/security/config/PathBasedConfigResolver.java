package se.metria.mapcms.security.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;

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

        if(path.contains("api/admin")){
            realm="admin";
            if (!cache.containsKey(realm)) {
                InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
                cache.put(realm, KeycloakDeploymentBuilder.build(is));
            }
        }else{
            realm="publik";
            if (!cache.containsKey(realm)) {
                InputStream is = getClass().getResourceAsStream("/" + realm + "-keycloak.json");
                cache.put(realm, KeycloakDeploymentBuilder.build(is));
            }
        }
        return cache.get(realm);

    }


    static void setAdapterConfig(AdapterConfig adapterConfig) {
        PathBasedConfigResolver.adapterConfig = adapterConfig;
    }
}