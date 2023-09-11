package se.metria.finfo.entity.registerenhet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "pagaende_fastighetsbildning", schema= "finfo")
public class PagaendeFastighetsbildningEntity extends BaseEntity<UUID> {

    private String arendeDagboksnummer;
    private String arendestatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private RegisterenhetEntity registerenhet;
}
