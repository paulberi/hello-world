package se.metria.xplore.kundconfig.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "metria_maps")
@RequiredArgsConstructor
@NoArgsConstructor
public class MetriaMapsAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String username = "";

    String password = "";

    @NonNull
    @ManyToOne
    @JoinColumn(name="kund_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    KundEntity kund;
}
