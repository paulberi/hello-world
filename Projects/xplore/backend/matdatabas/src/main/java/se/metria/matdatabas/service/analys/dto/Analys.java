package se.metria.matdatabas.service.analys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.analys.entity.AnalysEntity;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.handelse.dto.Handelse;
import se.metria.matdatabas.service.handelse.entity.HandelseEntity;
import se.metria.matdatabas.service.matning.dto.Matning;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Analys {
	private Integer id;
	private Integer matobjektId;
	private LocalDateTime analysDatum;
	private String rapportor;
	private String kommentar;
	private List<BifogadfilInfo> rapporter;
	private List<Matning> matningar;

	public static Analys fromEntity(AnalysEntity analys, Map<UUID, BifogadfilInfo> bifogadfilMap) {
		return Analys.builder()
				.id(analys.getId())
				.matobjektId(analys.getMatobjektId())
				.analysDatum(analys.getAnalysDatum())
				.rapportor(analys.getRapportor())
				.kommentar(analys.getKommentar())
				.rapporter(analys.getRapportIds().stream()
						.map(bifogadfilMap::get)
						.sorted(comparing(BifogadfilInfo::getFilnamn))
						.collect(toList()))
				.build();
	}
}
