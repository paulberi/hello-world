package se.metria.markkoll.service.dokument.bindings.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkkollBindings {
    private AvtalBindings avtalBindings;

    private BeredareBindings beredareBindings;

    private ErsattningBindings ersattningBindings;

    private IntrangBindings intrangBindings;

    private MarkagareBindings markagareBindings;

    private ProjektBindings projektBindings;

    private UtskickBindings utskickBindings;
}
