package se.metria.xplore.kundconfig.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "fastighetsok")
@NoArgsConstructor
@RequiredArgsConstructor
public class FastighetsokAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String username = "";

    String password = "";

    String kundmarke = "markkoll";

    @NonNull
    @ManyToOne
    @JoinColumn(name="kund_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    KundEntity kund;
}
