package se.metria.markkoll.entity.projekt;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "fiber", name = "projekt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@With
public class FiberProjektEntity extends ProjektEntity {
	String bestallare;
	String ledningsagare;
	String ledningsstracka;
	boolean varderingsprotokoll;
	boolean bidragsprojekt;
}
