package se.metria.mapcms.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import se.metria.mapcms.commons.utils.Auditable;
import se.metria.mapcms.openapi.model.PubliceringStatusDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "projekt")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ProjektEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String slug;

    @NonNull
    private String rubrik;

    @NonNull
    private String ingress;

    @NonNull
    private String brodtext;

    @NonNull
    @Enumerated(EnumType.STRING)
    private PubliceringStatusDto publiceringStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kund_id")
    private KundEntity kund;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "projekt_filer",
            joinColumns = { @JoinColumn(name = "projekt_id") },
            inverseJoinColumns = { @JoinColumn(name = "fil_id") })
    private List<FilEntity> filer = new ArrayList<>();

    @OneToMany(mappedBy = "projekt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjektOversattningEntity> oversattningar;

    @OneToMany(mappedBy = "projekt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<GeometriEntity> geometrier;

    @OneToMany (mappedBy = "projekt", cascade = CascadeType.ALL)
    private List<DialogEntity> dialoger;

    public void addOversattning(ProjektOversattningEntity p){
        p.setProjekt(this);
        this.oversattningar.add(p);
    }

    public void addDialog(DialogEntity d){
        d.setProjekt(this);
        this.dialoger.add(d);
    }
}
