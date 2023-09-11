package se.metria.finfo.entity.samfallighetsforening;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "styrelsemedlem", schema= "finfo")
public class StyrelsemedlemEntity extends BaseEntity<UUID> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SamfallighetsforeningEntity samfallighetsforening;

    @ElementCollection
    @CollectionTable(name = "styrelsefunktion", joinColumns = @JoinColumn(name = "styrelsemedlem_id"))
    @Column(name = "styrelsefunktion_typ")
    private List<String> funktion = new ArrayList<>();

    private String namn;
    private String coAdress;
    private String utdelningsadress;
    private String postnummer;
    private String postort;
    private String land;
    private String lansstyrelsediarienummer;
    private LocalDate datumForLansstyrelsebeslut;
    private String medlemstyp;
    private String anmarkning;
}
