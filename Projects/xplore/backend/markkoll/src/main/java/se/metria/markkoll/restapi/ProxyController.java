package se.metria.markkoll.restapi;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.metria.markkoll.service.ProxyService;

import java.util.Map;

@RequestMapping(value = "/proxy")
@RestController
@RequiredArgsConstructor
@Slf4j
public class ProxyController {
    @NonNull
    private final ProxyService proxyService;

    @RequestMapping(value = "/metria-maps/wms", method = RequestMethod.GET)
    public ResponseEntity<?>
    proxyMetriaMaps(ProxyExchange<byte[]> proxy, @RequestParam Map<String,String> allRequestParams) {
        var exch = proxyService.proxyMetriaMaps(proxy, allRequestParams);
        return exch.get();
    }
}
