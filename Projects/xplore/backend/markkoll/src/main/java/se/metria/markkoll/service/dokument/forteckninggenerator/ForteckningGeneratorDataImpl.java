package se.metria.markkoll.service.dokument.forteckninggenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ForteckningGeneratorDataImpl implements ForteckningGeneratorData {
    @JsonProperty("MMK-Mottagare-Postort")
    private String mottagarePostort;

    @JsonProperty("MMK-Mottagare-Adress")
    private String mottagareAdress;

    @JsonProperty("MMK-Mottagare-Postnummer")
    private String mottagarePostnummer;

    @JsonProperty("MMK-Mottagare-Organisationsnummer")
    private String mottagareOrganisationsnummer;

    @JsonProperty("MMK-Mottagare-Namn")
    private String mottagareNamn;

    @JsonProperty("MMK-Mottagare-IBAN10")
    private String mottagareIban10;

    @JsonProperty("MMK-Bank-Land")
    private String bankLand;

    @JsonProperty("MMK-Mottagare-Bankkonto")
    private String mottagareBankkonto;

    @JsonProperty("MMK-Mottagare-Typ-Av-Bankkonto")
    private String mottagareTypAvBankkonto;

    @JsonProperty("MMK-Mottagare-Banknamn")
    private String mottagareBanknamn;

    @JsonProperty("MMK-Mottagare-IBAN")
    private String mottagareIban;

    @JsonProperty("MMK-Mottagare-Swift")
    private String mottagareSwift;
}
