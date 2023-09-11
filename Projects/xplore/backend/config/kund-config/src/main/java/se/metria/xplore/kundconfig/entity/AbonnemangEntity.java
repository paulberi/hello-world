package se.metria.xplore.kundconfig.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "abonnemang")
public class AbonnemangEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    @JoinColumn(name="kund_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    KundEntity kund;

    String produkt;

    String typ;
}
