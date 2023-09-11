package se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FiberVarderingsprotokollConfigEntity {
    
    @Column
    Double sarskildErsattning = 0.0;

}
