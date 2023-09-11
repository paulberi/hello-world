package se.metria.xplore.avverkningsstatistik;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.avverkningsstatistik.model.Statistik;
import se.metria.xplore.base.SkogligRequest;
import se.metria.xplore.base.SkogsanalysService;
import se.metria.xplore.fme.FmeClient;

@RestController
public class AvverkningsstatistikService extends SkogsanalysService<AvverkningsstatistikProperties, Statistik[]> {

    public AvverkningsstatistikService(AvverkningsstatistikProperties properties, FmeClient fmeClient) {
        super(properties, fmeClient);
    }

    @RequestMapping("/avverkningsstatistik")
    public ResponseEntity<Statistik[]> post(@RequestBody SkogligRequest request)
    {
        if (!validateRequest(request))
        {
            return invalidRequest();
        }

        return this.fmeClient.post(this.properties.getFmeScript(), createRequestBody(request), Statistik[].class);
    }
}
