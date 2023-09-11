package se.metria.markkoll.service.dokument.bindings.data;

import lombok.Builder;
import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Builder
@Data
public class UtskickBindings {
    @DocProperty("MMK-Löpnummer")
    private String lopnummer;

    @DocProperty("MMK-Mottagare-adress")
    private String mottagareAdress;

    @DocProperty("MMK-Mottagare-bankkonto")
    private String mottagareBankkonto;

    @DocProperty("MMK-Mottagare-co-adress")
    private String mottagareCoAdress;

    @DocProperty("MMK-Mottagare-namn")
    private String mottagareNamn;

    @DocProperty("MMK-Mottagare-organisationsnummer")
    private String mottagareOrganisationsnummer;

    @DocProperty("MMK-Mottagare-postnummer")
    private String mottagarePostnummer;

    @DocProperty("MMK-Mottagare-postort")
    private String mottagarePostort;

    @DocProperty("MMK-Signatärer-namn-multi")
    private List<String> signatarerNamn;
}
