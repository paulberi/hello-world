package se.metria.markkoll.entity.vardering.bilaga;

import lombok.*;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.BilagaTypDto;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "bilaga")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BilagaEntity {
    @Id
    @GeneratedValue
    UUID id;

    BilagaTypDto bilagaTyp;

    @OneToOne(fetch = FetchType.LAZY)
    FilEntity fil;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    ElnatVarderingsprotokollEntity varderingsprotokoll;
}
