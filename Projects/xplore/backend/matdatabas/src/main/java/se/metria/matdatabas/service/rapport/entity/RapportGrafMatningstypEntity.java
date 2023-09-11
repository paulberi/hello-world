package se.metria.matdatabas.service.rapport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "rapport_graf_matningstyp")
@SequenceGenerator(name = "rapport_graf_matningstyp_seq", sequenceName = "rapport_graf_matningstyp_seq", allocationSize = 1)
public class RapportGrafMatningstypEntity {
    @Id
    @GeneratedValue(generator = "rapport_graf_matningstyp_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rapport_graf_id")
    private RapportGrafSettingsEntity rapportGraf;

    @Column(name = "matningstyp_id")
    private Integer matningstypId;

    public RapportGrafMatningstypEntity(Integer matningstypId) {
        update(matningstypId);
    }

    public void update(Integer matningstypId) {
        this.matningstypId = matningstypId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportGrafMatningstypEntity)) {
            return false;
        }
        RapportGrafMatningstypEntity that = (RapportGrafMatningstypEntity) o;

        return this.getMatningstypId() == that.getMatningstypId();
    }

    @Override
    public int hashCode() {
        return this.getMatningstypId().hashCode();
    }
}
