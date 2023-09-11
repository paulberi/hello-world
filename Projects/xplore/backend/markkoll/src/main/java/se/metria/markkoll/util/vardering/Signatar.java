package se.metria.markkoll.util.vardering;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Signatar {
    String namn;
    String personnummer;
    Double andel;
    String adress;
}
