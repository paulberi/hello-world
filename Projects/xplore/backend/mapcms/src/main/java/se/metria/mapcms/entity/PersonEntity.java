package se.metria.mapcms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "person")
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
public class PersonEntity {
    @Id
    private String pnr;

    @NonNull
    private String namn;

    private LocalDateTime senastInloggad;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "person_dialog_part",
            joinColumns = { @JoinColumn(name = "person_pnr") },
            inverseJoinColumns = { @JoinColumn(name = "part_id") })
    private List<DialogPartEntity> dialogParter;
}
