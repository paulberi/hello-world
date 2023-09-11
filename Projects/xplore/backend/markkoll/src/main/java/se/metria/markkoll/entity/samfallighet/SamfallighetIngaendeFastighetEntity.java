package se.metria.markkoll.entity.samfallighet;


import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "samfallighet_fastighet")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SamfallighetIngaendeFastighetEntity {
    @Id
    @GeneratedValue
    UUID id;

    // Fastigheten existerar inte nödvändigtvis som en entitet i vår databas
    UUID fastighetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "samfallighet_id")
    SamfallighetEntity samfallighet;

    String fastighetsbeteckning;
}
