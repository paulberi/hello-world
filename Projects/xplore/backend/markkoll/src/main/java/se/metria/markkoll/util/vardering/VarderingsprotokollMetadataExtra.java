package se.metria.markkoll.util.vardering;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VarderingsprotokollMetadataExtra {
    String fastighetsbeteckning;
    String kommun;
    String kontaktpersonOchAdress;
    String projektnummer;
}
