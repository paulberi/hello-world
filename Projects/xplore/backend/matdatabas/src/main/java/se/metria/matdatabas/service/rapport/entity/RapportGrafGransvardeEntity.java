package se.metria.matdatabas.service.rapport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "rapport_graf_gransvarde")
@SequenceGenerator(name = "rapport_graf_matningstyp_seq", sequenceName = "rapport_graf_matningstyp_seq", allocationSize = 1)
public class RapportGrafGransvardeEntity {
    @Id
    @GeneratedValue(generator = "rapport_graf_matningstyp_seq")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rapport_graf_id")
    private RapportGrafSettingsEntity rapportGraf;

    @Column(name = "gransvarde_id")
    private Integer gransvardeId;

    public RapportGrafGransvardeEntity(Integer gransvardeId) {
        update(gransvardeId);
    }

    public void update(Integer gransvardeId) {
        this.gransvardeId = gransvardeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RapportGrafGransvardeEntity)) {
            return false;
        }
        RapportGrafGransvardeEntity that = (RapportGrafGransvardeEntity) o;

        return this.getGransvardeId() == that.getGransvardeId();
    }

    @Override
    public int hashCode() {
        return this.getGransvardeId().hashCode();
    }
}
