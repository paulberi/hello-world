package se.metria.markkoll.service.fastighet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.entity.fastighet.FastighetEntity;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FastighetsOmrInfo {
    public FastighetEntity fastighet;
    public long omrade_nr;
    public Geometry geom;
}
