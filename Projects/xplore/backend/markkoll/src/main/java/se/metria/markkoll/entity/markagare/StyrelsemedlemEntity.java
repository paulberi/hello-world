package se.metria.markkoll.entity.markagare;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "styrelsemedlem")
public class StyrelsemedlemEntity {
    @Id
    @Column(name = "markagare_id")
    private UUID id;

    private String funktion;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @MapsId
    private MarkagareEntity markagare;

    public void setMarkagare(MarkagareEntity markagare) {
        this.markagare = markagare;
        markagare.setStyrelsemedlem(this);
    }
}
