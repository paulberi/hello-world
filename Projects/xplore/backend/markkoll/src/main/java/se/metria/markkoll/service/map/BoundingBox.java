package se.metria.markkoll.service.map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoundingBox {
    private Double minX;
    private Double maxX;
    private Double minY;
    private Double maxY;
}
