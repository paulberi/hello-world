package se.metria.xplore.kundconfig.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.xplore.kundconfig.repository.common.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "kund")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
public class KundEntity extends Auditable<String> {
    @Id
    String id; // Vi sätter organisationsnummer som ID, för att få något som är "portabelt" mellan olika miljöer

    String namn;

    String epost;

    String telefon;

    String kontaktperson;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "kund", cascade = CascadeType.REMOVE)
    Set<PermissionsEntity> permissions = new HashSet<>();

    @OneToMany(mappedBy = "kund", cascade = CascadeType.REMOVE)
    Set<ConfigurationEntity> configurations = new HashSet<>();

    @OneToMany(mappedBy = "kund", cascade = CascadeType.REMOVE)
    Set<AbonnemangEntity> abonnemang = new HashSet<>();

    @OneToMany(mappedBy = "kund", cascade = CascadeType.REMOVE)
    Set<FastighetsokAuthEntity> fastighetsok = new HashSet<>();

    @OneToMany(mappedBy = "kund", cascade = CascadeType.REMOVE)
    Set<MetriaMapsAuthEntity> metriaMaps = new HashSet<>();
}
