package se.metria.markkoll.entity.vardering.elnat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(schema = "elnat", name = "rotnetto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElnatRotnettoEntity implements Serializable {
    @Id
    @GeneratedValue
    UUID id;

    @Column
    Integer ersattning;

    @OneToOne(optional = true)
    ElnatVarderingsprotokollEntity varderingsprotokoll;
}
