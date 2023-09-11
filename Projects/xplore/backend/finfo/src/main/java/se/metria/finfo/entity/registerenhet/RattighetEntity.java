package se.metria.finfo.entity.registerenhet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "rattighet", schema= "finfo")
public class RattighetEntity extends BaseEntity<UUID> {

    private String aktbeteckning = "";
    private String andamal = "";
    private String rattighetstyp = "";
    private String rattsforhallande = "";
    private String rattighetsbeskrivning = "";
    private String rattighetsanmarkning = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private RegisterenhetEntity registerenhet;
}
