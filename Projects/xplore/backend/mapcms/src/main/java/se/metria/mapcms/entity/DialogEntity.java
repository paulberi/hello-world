package se.metria.mapcms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import se.metria.mapcms.commons.utils.Auditable;
import se.metria.mapcms.openapi.model.DialogStatusDto;
import se.metria.mapcms.openapi.model.PubliceringStatusDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dialog")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class DialogEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String rubrik;


    private Geometry plats;

    @NonNull
    @Enumerated(EnumType.STRING)
    private PubliceringStatusDto publiceringStatus;

    @NonNull
    @Enumerated(EnumType.STRING)
    private DialogStatusDto dialogStatus;

    @ManyToOne( fetch = FetchType.LAZY)
    private ProjektEntity projekt;


    @OneToMany(mappedBy = "dialog", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DialogPartEntity> dialogParter;

}
