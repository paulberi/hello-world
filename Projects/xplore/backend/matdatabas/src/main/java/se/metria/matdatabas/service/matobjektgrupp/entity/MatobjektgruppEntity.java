package se.metria.matdatabas.service.matobjektgrupp.entity;

import org.hibernate.annotations.Formula;
import se.metria.matdatabas.service.common.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupp")
@SequenceGenerator(name = "grupp_id_seq", sequenceName = "grupp_id_seq", allocationSize = 1)
public class MatobjektgruppEntity extends Auditable {
	@Id
	@GeneratedValue(generator = "grupp_id_seq")
	private Integer id;
	private Short kategori;
	private String namn;
	private Short kartsymbol;
	private String beskrivning;

	@Formula("(SELECT COUNT(*) FROM matdatabas.grupp_matobjekt gm WHERE gm.grupp_id = id)")
	private Integer matobjektCount;

	@ElementCollection
	@CollectionTable(name = "grupp_matobjekt", joinColumns = @JoinColumn(name = "grupp_id"))
	@Column(name = "matobjekt_id")
	private Set<Integer> matobjektIds = new HashSet<>();

	public MatobjektgruppEntity() {

	}

	public MatobjektgruppEntity(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Short getKategori() {
		return kategori;
	}

	public void setKategori(Short kategori) {
		this.kategori = kategori;
	}

	public String getNamn() {
		return namn;
	}

	public void setNamn(String namn) {
		this.namn = namn;
	}

	public Short getKartsymbol() {
		return kartsymbol;
	}

	public void setKartsymbol(Short kartsymbol) {
		this.kartsymbol = kartsymbol;
	}

	public String getBeskrivning() {
		return beskrivning;
	}

	public void setBeskrivning(String beskrivning) {
		this.beskrivning = beskrivning;
	}

	public Integer getMatobjektCount() {
		return matobjektCount;
	}

	public Set<Integer> getMatobjektIds() {
		return matobjektIds;
	}

	public void setMatobjektIds(Set<Integer> matobjektIds) {
		this.matobjektIds = matobjektIds;
	}
}
