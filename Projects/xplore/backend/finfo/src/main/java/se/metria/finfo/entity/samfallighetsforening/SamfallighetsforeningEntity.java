package se.metria.finfo.entity.samfallighetsforening;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import se.metria.finfo.entity.agare.ForvaltningsobjektEntity;
import se.metria.finfo.util.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "samfallighetsforening", schema= "finfo")
public class SamfallighetsforeningEntity extends BaseEntity<UUID> {

    @OneToMany(mappedBy = "samfallighetsforening", cascade = CascadeType.PERSIST)
    private List<ForvaltningsobjektEntity> forvaltningsobjekt = new ArrayList<>();

    @OneToMany(mappedBy = "samfallighetsforening", cascade = CascadeType.PERSIST)
    private List<StyrelsemedlemEntity> styrelsemedlemmar = new ArrayList<>();

    private String foreningstyp;
    private String foreningsnamn;
    private String lan;
    private String sate;
    private String coNamn;
    private String coAdress;
    private String coPostnummer;
    private String coPostort;
    private String telefonnummer;
    private String orgnr;
    private LocalDate faststallelsedatum;
    private LocalDate registreringsdatum;
    private LocalDate avregistreringsdatum;
    private LocalDate styrelsedatum;
    private Integer rakenskapsarFromManad;
    private Integer rakenskapsarFromDag;
    private Integer rakenskapsarTomManad;
    private Integer rakenskapsarTomDag;
    private boolean underAjourforing;
    private LocalDate senasteAjourforingsdatum;
    private String firmatecknare;
    private String anmarkning;
    private UUID uuid;

    @CreatedDate
    private LocalDateTime importeradDatum;

    public void setForvaltningsobjekt(List<ForvaltningsobjektEntity> forvaltningsobjekt) {
        for (var obj: forvaltningsobjekt) {
            obj.setSamfallighetsforening(this);
            this.forvaltningsobjekt.add(obj);
        }
    }

    public void setStyrelsemedlemmar(List<StyrelsemedlemEntity> styrelsemedlemmar) {
        for (var medlem: styrelsemedlemmar) {
            medlem.setSamfallighetsforening(this);
            this.styrelsemedlemmar.add(medlem);
        }
    }
}
