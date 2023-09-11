package se.metria.xplore.nmd;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.base.SkogligRequest;
import se.metria.xplore.base.SkogsanalysService;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.nmd.model.Tradslag;

@RestController
public class NmdService extends SkogsanalysService<NmdProperties, Tradslag[]> {

    public NmdService(NmdProperties properties, FmeClient fmeClient) {
        super(properties, fmeClient);
    }

    @RequestMapping("/nmd")
    public ResponseEntity<Tradslag[]> post(@RequestBody SkogligRequest request)
    {
        if (!validateRequest(request))
        {
            return invalidRequest();
        }

        return this.fmeClient.post(this.properties.getFmeScript(), createRequestBody(request), Tradslag[].class);
    }
}
