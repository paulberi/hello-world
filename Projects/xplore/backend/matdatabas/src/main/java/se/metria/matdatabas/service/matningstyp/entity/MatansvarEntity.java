package se.metria.matdatabas.service.matningstyp.entity;

import lombok.Getter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Matansvar represents matningstyper assigned to a particular anvandargrupp.
 * Readonly entity that maps to a view in the database.
 */
@Getter
@Entity
@Immutable
@Table(name = "matansvar")
public class MatansvarEntity {
	@Id
	private Integer id;
	private String matobjekt;
	private Integer matobjektId;
	private String matningstyp;
	private String fastighet;
	private Integer matansvarigAnvandargruppId;
}
