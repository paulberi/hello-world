package se.metria.matdatabas.service.rapport.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.rapport.dto.RapportMottagare;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "rapport_mottagare")
@SequenceGenerator(name = "rapport_mottagare_seq", sequenceName = "rapport_mottagare_seq", allocationSize = 1)
public class RapportMottagareEntity {
    @Id
    @GeneratedValue(generator = "rapport_mottagare_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RapportSettingsEntity rapport;

    private String namn;

    private String epost;

    public RapportMottagareEntity(RapportMottagare mottagare) {
        update(mottagare);
    }

    public void update(RapportMottagare mottagare) {
        this.namn = mottagare.getNamn();
        this.epost = mottagare.getEpost();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportMottagareEntity)) {
            return false;
        }
        RapportMottagareEntity that = (RapportMottagareEntity) o;

        return this.namn.equals(that.namn) && this.epost.equals(that.epost);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (this.namn != null ? this.namn.hashCode() : 0);
        result = 31 * result + (this.epost != null ? this.epost.hashCode() : 0);
        return result;
    }
}