package se.metria.xplore.prisstatistik;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.prisstatistik.model.Prisstatistik;

@RestController
public class PrisstatistikService {
    private PrisstatistikProperties properties;
    private FmeClient<String, Prisstatistik[]> fmeClient;

    public PrisstatistikService(PrisstatistikProperties properties, FmeClient fmeClient) {
        this.properties = properties;
        this.fmeClient = fmeClient;
    }

    @RequestMapping("/prisstatistik")
    public ResponseEntity<Prisstatistik[]> get() {
        return this.fmeClient.get(this.properties.getFmeScript(), Prisstatistik[].class, this.properties.getFmeParams());
    }
}