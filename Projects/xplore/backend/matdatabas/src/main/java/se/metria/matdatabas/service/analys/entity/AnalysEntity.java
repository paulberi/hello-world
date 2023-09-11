package se.metria.matdatabas.service.analys.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import se.metria.matdatabas.service.analys.dto.EditAnalys;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "analys")
@SequenceGenerator(name = "analys_id_seq", sequenceName = "analys_id_seq", allocationSize = 1)
public class AnalysEntity {

	@Id
	@GeneratedValue(generator = "analys_id_seq")
	private Integer id;

	@NotNull
	private Integer matobjektId;

	@NotNull
	@Column(name="analys_datum")
	private LocalDateTime analysDatum;

	@Size(max = 500)
	private String kommentar;

	@NotNull
	@Size(max = 60)
	private String rapportor;

	@ElementCollection
	@CollectionTable(name = "analys_bifogad_rapport", joinColumns = @JoinColumn(name = "analys_id"))
	@Column(name = "bifogad_fil_id")
	private Set<UUID> rapportIds = new HashSet<>();

	public AnalysEntity(EditAnalys editAnalys) {
		update(editAnalys);
	}

	public void update(EditAnalys editAnalys) {
		setMatobjektId(editAnalys.getMatobjektId());
		setAnalysDatum(editAnalys.getAnalysDatum());
		setKommentar(editAnalys.getKommentar());
		setRapportor(editAnalys.getRapportor());
		setRapportIds(editAnalys.getRapporter());
		if (getRapportIds() == null) {
			setRapportIds(new HashSet<>());
		}
	}
}
