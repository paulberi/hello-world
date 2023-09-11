package se.metria.markkoll.entity.projekt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import se.metria.markkoll.openapi.model.AvtalMappstrategiDto;
import se.metria.markkoll.openapi.model.BerakningAbel07Dto;
import se.metria.markkoll.openapi.model.BerakningRevDto;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "avtalsinstallningar")
@Getter
@Setter
@NoArgsConstructor
public class AvtalsinstallningarEntity {
    @Id
    @Column(name = "projekt_id")
    private UUID id;

    @Enumerated(value = EnumType.STRING)
    private BerakningRevDto berakningRev = BerakningRevDto.ENBART_REV;

    @Enumerated(value = EnumType.STRING)
    private BerakningAbel07Dto berakningAbel07 = BerakningAbel07Dto.ENBART_ABEL07;

    @Enumerated(value = EnumType.STRING)
    private AvtalMappstrategiDto mappstrategi = AvtalMappstrategiDto.HIERARCHICAL;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    private ProjektEntity projekt;

    public AvtalsinstallningarEntity(@NonNull ProjektEntity projekt) {
        this.projekt = projekt;
        this.id = projekt.getId();
    }
}
