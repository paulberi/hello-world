package se.metria.xplore.kundconfig.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "configuration")
public class ConfigurationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String key;

    String value;

    @ManyToOne
    @JoinColumn(name="kund_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    KundEntity kund;
}
