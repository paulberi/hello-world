package se.metria.matdatabas.service.kallsystem.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "kallsystem")
public class KallsystemEntity {
    @Id
    String namn;

    String beskrivning;

    @NotNull
    @Column(name = "default_godkand")
    boolean defaultGodkand;

    @NotNull
    @Column(name = "manuell_import")
    boolean manuellImport;

    @Column(name = "tips")
    String tips;
}
