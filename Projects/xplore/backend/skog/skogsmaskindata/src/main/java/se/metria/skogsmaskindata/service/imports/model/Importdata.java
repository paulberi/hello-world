package se.metria.skogsmaskindata.service.imports.model;

import javax.persistence.*;

/**
 * This entity is not used because we insert the data using pure JDBC instead of JPA (so that we can easier
 * handle binary data as streams). But it's useful to describe the entity for setting up the in-memory test database.
 */
@Entity
public class Importdata {
	@Id
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	private Import parent;

	@Column(columnDefinition="varchar(50000)")
	private String xml;

	@Lob
	@Column(columnDefinition="bytea")
	private byte[] pdf;

	public Import getParent() {
		return parent;
	}

	public void setParent(Import parent) {
		this.parent = parent;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
}
