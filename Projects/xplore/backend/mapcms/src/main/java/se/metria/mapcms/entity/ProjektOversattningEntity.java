package se.metria.mapcms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.metria.mapcms.entity.oversattningiD.ProjektOversattningId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "projekt_oversattning")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@IdClass(ProjektOversattningId.class)
public class ProjektOversattningEntity implements Serializable {

    @Id
    @Column(name = "sprak_kod")
    private String sprakkod;

    @Id
    @Column(name = "projekt_id")
    private UUID projektId;

    @NonNull
    private String rubrik;

    @NonNull
    private String ingress;

    @NonNull
    private String brodtext;

    @ManyToOne
    @MapsId
    private ProjektEntity projekt;
}
