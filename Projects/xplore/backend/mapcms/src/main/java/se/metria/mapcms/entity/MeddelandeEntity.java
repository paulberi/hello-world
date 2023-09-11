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
@Table(name = "meddelande")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class MeddelandeEntity extends Auditable<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String text;

    @NonNull
    private PubliceringStatusDto publiceringStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fran", referencedColumnName = "id")
    private DialogPartEntity fran;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "meddelande_filer",
            joinColumns = { @JoinColumn(name = "meddelande_id") },
            inverseJoinColumns = { @JoinColumn(name = "fil_id") })
    private List<FilEntity> filer= new ArrayList<>();



}
