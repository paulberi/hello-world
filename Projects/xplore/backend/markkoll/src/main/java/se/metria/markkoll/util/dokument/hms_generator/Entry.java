package se.metria.markkoll.util.dokument.hms_generator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Fyll på datastruktur med ytterligare kolumner när det behövs
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entry {
    private String fastighet;
    private String fastighetsnummer;
    private String sokId;
    private String andel;
    private String namn;
    private String gatuadress;
    private String postnr;
    private String postort;
    private String personnummer;
    private String lan;
    private String kommun;
    private String co;
    private String telefonArbete;
    private String ePost;
    private String bankkonto;
    private String mottagarreferens;
}
