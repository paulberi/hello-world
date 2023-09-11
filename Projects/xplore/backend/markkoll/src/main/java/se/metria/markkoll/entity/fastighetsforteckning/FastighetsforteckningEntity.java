package se.metria.markkoll.entity.fastighetsforteckning;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "fastighetsforteckning")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FastighetsforteckningEntity {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    ProjektEntity projekt;

    @ManyToOne(fetch = FetchType.LAZY)
    FastighetEntity fastighet;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    AvtalEntity avtal;

    FastighetsforteckningAnledning anledning;
    
    boolean excluded;
    
    public void setExcluded(boolean isExcluded) {
        excluded = isExcluded;
    }
}
