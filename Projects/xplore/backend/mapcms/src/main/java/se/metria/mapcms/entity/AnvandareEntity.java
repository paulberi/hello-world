package se.metria.mapcms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "anvandare")
@AllArgsConstructor
@NoArgsConstructor
public class AnvandareEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String kundId;

    private String epost;

    private String roll;

    private boolean aktiv;

    private LocalDateTime senasteInloggad;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "anvandare_dialog_part",
            joinColumns = { @JoinColumn(name = "anvandare_id") },
            inverseJoinColumns = { @JoinColumn(name = "part_id") })
    private List<DialogPartEntity> dialogParter;
}
