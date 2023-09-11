package se.metria.xplore.skogligagrunddata;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.base.SkogligRequest;
import se.metria.xplore.base.SkogsanalysService;
import se.metria.xplore.skogligagrunddata.model.Grunddata;

@RestController
public class SkogligaGrunddataService extends SkogsanalysService<SkogligaGrunddataProperties, Grunddata[]> {

    public SkogligaGrunddataService(SkogligaGrunddataProperties properties, FmeClient fmeClient) {
        super(properties, fmeClient);
    }

    @RequestMapping("/skogliga-grunddata")
    public ResponseEntity<Grunddata[]> post(@RequestBody SkogligRequest request)
    {
        if (!validateRequest(request))
        {
            return invalidRequest();
        }

        return this.fmeClient.post(this.properties.getFmeScript(), createRequestBody(request), Grunddata[].class);
    }
}