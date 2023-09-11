package se.metria.xplore.huggningsklasser;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.base.SkogligRequest;
import se.metria.xplore.base.SkogsanalysService;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.huggningsklasser.model.Huggningsklasser;
import se.metria.xplore.huggningsklasser.model.HuggningsklasserV2;

@RestController
public class HuggningsklasserService extends SkogsanalysService<HuggningsklasserProperties, HuggningsklasserV2[]> {

    public HuggningsklasserService(HuggningsklasserProperties properties, FmeClient fmeClient) {
        super(properties, fmeClient);
    }

    @RequestMapping("/huggningsklasser")
    public ResponseEntity<HuggningsklasserV2[]> post(@RequestBody SkogligRequest request) {

        if (!validateRequest(request)) {
            return invalidRequest();
        }

        return this.fmeClient.post(this.properties.getFmeScript(), createRequestBody(request), HuggningsklasserV2[].class);
    }
}