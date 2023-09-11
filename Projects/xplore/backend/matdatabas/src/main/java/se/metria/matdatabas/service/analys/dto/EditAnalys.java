package se.metria.matdatabas.service.analys.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import se.metria.matdatabas.service.analys.entity.AnalysEntity;
import se.metria.matdatabas.service.matning.dto.Matning;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditAnalys {
	private LocalDateTime analysDatum;
	private Integer matobjektId;
	private String rapportor;
	private String kommentar;
	private Set<UUID> rapporter;
	private List<Matning> matningar;

}
