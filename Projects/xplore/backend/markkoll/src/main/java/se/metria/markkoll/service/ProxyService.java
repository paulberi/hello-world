package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.stereotype.Service;

import java.util.Map;

import static se.metria.markkoll.util.HttpUtil.basicAuth;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProxyService {
    @Value("${markkoll.metria-maps.url}")
    String metriaMapsUrl;

    @NonNull
    private final KundConfigService kundConfigService;

    public ProxyExchange<byte[]>
    proxyMetriaMaps(ProxyExchange<byte[]> proxy, Map<String,String> requestParams)
    {
        var queryParams = getQueryParams(requestParams);
        var path = metriaMapsUrl + proxy.path("/proxy/metria-maps") + queryParams;
      
        log.debug("Proxybegäran från {} till {}", proxy.path(), path);
      
        var mmAuth = kundConfigService.getMetriaMapsAuth();
        var basicAuth = basicAuth(mmAuth.getUsername(), mmAuth.getPassword());        

        return proxy.sensitive("Cookie") // Ta bort Authorization som sensitive
                    .header("Authorization", basicAuth)
                    .uri(path);
    }

    private String getQueryParams(Map<String,String> allRequestParams) {
        var queryString = new StringBuilder();
        for(var entry : allRequestParams.entrySet()) {
            queryString.append(entry.getKey());
            queryString.append("=");
            queryString.append(entry.getValue());
            queryString.append("&");
        }

        return "?" + queryString.toString();
    }
}
