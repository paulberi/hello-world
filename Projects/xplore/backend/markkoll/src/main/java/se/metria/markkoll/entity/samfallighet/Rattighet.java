package se.metria.markkoll.entity.samfallighet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rattighet {
    private String andamal;
    private String rattsforhallande;
    private String aktbeteckning;
    private String rattighetsbeskrivning;
}
