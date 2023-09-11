package se.metria.markkoll.service.dokument.bindings.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.util.dokument.DocProperty;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ProjektBindings {
    @DocProperty("MMK-Projekt-Beskrivning")
    private String beskrivning;

    @DocProperty("MMK-Ersättning-ska-inte-utgå")
    private Boolean ersattningSkaInteUtga;

    @DocProperty("MMK-Ersättning-ska-erläggas")
    private Boolean ersattningSkaErlaggas;

    @DocProperty("MMK-Se-bilaga-A")
    @Deprecated // Beror på om ersättning ska erläggas
    private Boolean seBilagaA;

    @DocProperty("MMK-Startdatum")
    private LocalDate startdatum;

    @DocProperty("MMK-Projektnamn")
    private String projektnamn;

    @DocProperty("MMK-Värderingsprotokoll-bifogas")
    private Boolean varderingsprotokollBifogas;

    @DocProperty("MMK-Ledningsägare")
    private String ledningsagare;

    @DocProperty("MMK-Ledningssträcka")
    private String ledningsstracka;

    @DocProperty("MMK-Bidragsprojekt")
    private Boolean bidragsprojekt;

    @DocProperty("MMK-Uppdragsnummer")
    private String uppdragsnummer;

    @DocProperty("MMK-Projektkarta-med-projektområde")
    private List<ByteArrayResource> projektkartaMedProjektomrade;

    @DocProperty("MMK-Projektkarta-med-sträckning")
    private List<ByteArrayResource> projektkartaMedStrackning;

    @DocProperty("MMK-Utbetalningskonto")
    private String utbetalningskonto;

    @DocProperty("MMK-Projektnummer")
    private String projektnummer;

    @DocProperty("MMK-Ansvarig-Projektledare")
    private String ansvarigProjektledare;

    @DocProperty("MMK-Ansvarig-Konstruktör")
    private String ansvarigKontruktor;
}
