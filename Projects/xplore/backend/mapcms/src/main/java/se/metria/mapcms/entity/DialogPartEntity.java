package se.metria.mapcms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dialog_part")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class DialogPartEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @NonNull
    private String namn;

    @NonNull
    private String epost;

    @NonNull
    private boolean skapare;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id", referencedColumnName = "id")
    private DialogEntity dialog;


    @OneToMany (cascade = CascadeType.ALL, mappedBy = "fran", fetch = FetchType.LAZY)
    private List<MeddelandeEntity> meddelanden;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "person_dialog_part",
            joinColumns = { @JoinColumn(name = "part_id") },
            inverseJoinColumns = { @JoinColumn(name = "person_pnr") })
    private List<PersonEntity> personer;
}
