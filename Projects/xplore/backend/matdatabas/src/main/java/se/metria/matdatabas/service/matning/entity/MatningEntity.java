package se.metria.matdatabas.service.matning.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import se.metria.matdatabas.service.matning.Detektionsomrade;
import se.metria.matdatabas.service.matning.Matningsfelkod;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.dto.SaveMatning;

@Data
@Entity
@Table(name = "matning")
@SequenceGenerator(name = "matning_id_seq", sequenceName = "matning_id_seq", allocationSize = 1)
public class MatningEntity {
	@Id
	@GeneratedValue(generator = "matning_id_seq")
	private Long id;

	@NotNull
	@Column(name = "avlast_datum")
	private LocalDateTime avlastDatum;

	@Column(name = "avlast_varde")
	private Double avlastVarde;

	@NotNull
	@Column(name = "inom_detektionsomrade")
	private Short inomDetektionsomrade;

	@Column(name = "beraknat_varde")
	private Double beraknatVarde;

	@NotNull
	private Short status;

	@Size(max = 250)
	private String kommentar;

	@NotNull
	@Size(max = 60)
	private String felkod;

	@Size(max = 60)
	private String rapportor;

	@NotNull
	@Column(name = "matningstyp_id")
	private Integer matningstypId;

	private String kallsystem;

	public MatningEntity() {
		setInomDetektionsomrade(Detektionsomrade.INOM);
	}
	
	public MatningEntity(String kallsystem, Integer matningstypId, SaveMatning save) {
		this();
		setMatningstypId(matningstypId);
		updateWithDataFromSaveMatning(kallsystem, save);
	}
	
	public void updateWithDataFromSaveMatning(String kallsystem, SaveMatning save) {
		setKallsystem(kallsystem);
		setAvlastDatum(save.getAvlastDatum());
		setAvlastVarde(save.getAvlastVarde());
		setBeraknatVarde(save.getBeraknatVarde());
		setKommentar(save.getKommentar());
		setRapportor(save.getRapportor());
		var inomDetektionsomrade = save.getInomDetektionsomrade() != null  ? save.getInomDetektionsomrade() : Detektionsomrade.INOM;
		setInomDetektionsomrade(inomDetektionsomrade);
		var felkod = save.getFelkod() != null && !save.getFelkod().isBlank() ? save.getFelkod() : Matningsfelkod.OK; 
		setFelkod(felkod);
		setStatus(Matningstatus.EJGRANSKAT);
	}

	public LocalDateTime getAvlastDatum() {
		return avlastDatum;
	}

	public Short getStatus() { return status; }

	public void setAvlastDatum(LocalDateTime avlastDatum) {
		this.avlastDatum = avlastDatum.truncatedTo(ChronoUnit.SECONDS);
	}

	public boolean hasFelkod() {
		String felkod = this.getFelkod();
		return felkod != null && !felkod.equals(Matningsfelkod.OK);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MatningEntity)) return false;
		MatningEntity that = (MatningEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
