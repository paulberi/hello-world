package se.metria.markkoll.service.dokument.bindings.data;

import lombok.Builder;
import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

@Builder
@Data
public class BeredareBindings {
    @DocProperty("MMK-Beredare-Namn")
    private String namn;

    @DocProperty("MMK-Beredare-Telefonnummer")
    private String telefonnummer;

    @DocProperty("MMK-Beredare-Adress")
    private String adress;

    @DocProperty("MMK-Beredare-Postnummer")
    private String postnummer;

    @DocProperty("MMK-Beredare-Ort")
    private String ort;
}
