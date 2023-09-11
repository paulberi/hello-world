package se.metria.matdatabas.service.matningstyp.query;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatningstypSearchFilter {
	/** Begränsa sökningen till dessa id:n */
	private List<Integer> includeIds;

	/** Ta inte med dessa id:n i resultaten */
	private List<Integer> excludeIds;

	/** Ingår i någon av dessa mätobjektgrupper */
	private List<Integer> matobjektgrupper;

	/** Ingår i denna mätrunda */
	private Integer matrunda;

	/** filtrera mätningar mellan dessa datum. Sätt till null om du inte vill ha något min- eller max-datum */
	private LocalDateTime matningFromDatum;
	private LocalDateTime matningToDatum;

	private List<Integer> matobjektIds;
	private String matobjektNamn;
	private Short matobjektTyp;
	private String fastighet;
	private List<Integer> matansvarigAnvandargruppIds;
	private Boolean onlyAktiva;
	private Boolean onlyAktivaMatobjekt;
	private Boolean excludeAutomatiska;
	private Short matningStatus;
}
