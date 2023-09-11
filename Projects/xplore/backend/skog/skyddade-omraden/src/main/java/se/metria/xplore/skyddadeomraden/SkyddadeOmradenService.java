package se.metria.xplore.skyddadeomraden;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.base.SkogligRequest;
import se.metria.xplore.base.SkogsanalysService;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.skyddadeomraden.model.SkyddadeOmraden;

@RestController
public class SkyddadeOmradenService extends SkogsanalysService<SkyddadeOmradenProperties, SkyddadeOmraden[]> {

    public SkyddadeOmradenService(SkyddadeOmradenProperties properties, FmeClient fmeClient) {
        super(properties, fmeClient);
    }

    @RequestMapping("/skyddade-omraden")
    public ResponseEntity<SkyddadeOmraden[]> post(@RequestBody SkogligRequest request) {
        if (!validateRequest(request)) {
            return invalidRequest();
        }

        return this.fmeClient.post(this.properties.getFmeScript(), createRequestBody(request), SkyddadeOmraden[].class);
    }
}
