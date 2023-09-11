package se.metria.matdatabas.service.matrunda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.service.matrunda.dto.RapporteringMatningstyp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MatrundaRapporteringDataService {
	private NamedParameterJdbcTemplate jdbcTemplate;

	public MatrundaRapporteringDataService(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	String getMatrundaName(Integer matrundaId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", matrundaId);
		return jdbcTemplate.queryForObject("select namn from matrunda where id = :id", parameters, String.class);
	}

	/**
	 * Hämta alla mätningstyper, i ett format anpassat för inrapporteringen, för en viss mätrunda.
	 */
	List<RapporteringMatningstyp> getMatningstyper(Integer matrundaId, LocalDateTime startDate) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", matrundaId);

		// Inkludera max 3 av det senaste mätvärdena. Enbart aktiva mätobjekt är relevanta att rapportera.
		String sql =
				"SELECT *\n" +
						"FROM (\n" +
						"         SELECT mrmt.matrunda_id,\n" +
						"                mrmt.matningstyp_id,\n" +
						"                mrmt.ordning,\n" +
						"                m.avlast_varde,\n" +
						"                m.avlast_datum,\n" +
						"                m.status,\n" +
						"                m.felkod,\n" +
						"                m.rank,\n" +
						"                dm.storhet,\n" +
						"                dm.namn AS matningstyp,\n" +
						"                dm.berakningstyp,\n" +
						"                dm.automatisk_inrapportering,\n" +
						"                mt.instrument,\n" +
						"                mt.enhet,\n" +
						"                mt.decimaler,\n" +
						"                mt.fixpunkt,\n" +
						"                mt.max_pejlbart_djup,\n" +
						"                mo.id   AS matobjekt_id,\n" +
						"                mo.namn AS matobjekt,\n" +
						"                mo.bifogad_bild_id,\n" +
						"                mo.lage,\n" +
						"                mo.fastighet\n" +
						"         " +
						"FROM matdatabas.matrunda_matningstyp mrmt\n" +
						"                  LEFT JOIN (SELECT matning.id,\n" +
						"                                    matning.matningstyp_id,\n" +
						"                                    matning.avlast_varde,\n" +
						"                                    matning.avlast_datum,\n" +
						"                                    matning.status,\n" +
						"                                    matning.felkod,\n" +
						"                                    rank() OVER (PARTITION BY matning.matningstyp_id ORDER BY matning.avlast_datum DESC) AS rank\n" +
						"                             FROM matdatabas.matning) m ON mrmt.matningstyp_id = m.matningstyp_id\n" +
						"                  JOIN matdatabas.matningstyp mt ON mrmt.matningstyp_id = mt.id\n" +
						"                  JOIN matdatabas.definition_matningstyp dm ON mt.definition_matningstyp_id = dm.id\n" +
						"                  JOIN matdatabas.matobjekt mo ON mt.matobjekt_id = mo.id\n" +
						"         " +
						"ORDER BY mrmt.ordning, m.rank\n" +
						"\n" +
						"         " +
						") m\n" +
						"         " +
						"INNER JOIN matdatabas.matobjekt ON matobjekt.id = m.matobjekt_id\n" +
						"WHERE m.matrunda_id = :id\n" +
						"  " +
						"AND (matobjekt.aktiv = true)\n" +
						"  " +
				"AND (m.rank is null OR m.rank <= 3)";
		return queryMatningstyper(sql, parameters, true);
	}

	/**
	 * Hämta alla mätningstyper, i ett format anpassat för inrapporteringen, för ett mätobjekt.
	 */
	List<RapporteringMatningstyp> getMatningstyperFromMatobjekt(Integer matobjektId, LocalDateTime startDate) {
		MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("id", matobjektId);

		// Inkludera max 3 av det senaste mätvärdena.
		String sql = "select * from (SELECT\n" +
				"                   mt.id as matningstyp_id,\n" +
				"                   m.avlast_varde,\n" +
				"                   m.avlast_datum,\n" +
				"                   m.status,\n" +
				"                   m.felkod,\n" +
				"                   m.rank,\n" +
				"                   dm.storhet,\n" +
				"                   dm.namn AS matningstyp,\n" +
				"                   dm.berakningstyp,\n" +
				"                   mt.enhet,\n" +
				"                   mt.decimaler,\n" +
				"                   mt.fixpunkt,\n" +
				"                   mt.max_pejlbart_djup,\n" +
				"                   mo.id   AS matobjekt_id,\n" +
				"                   mo.namn AS matobjekt,\n" +
				"                   mo.bifogad_bild_id,\n" +
				"                   mo.lage,\n" +
				"                   mo.fastighet\n" +
				"               FROM matningstyp mt\n" +
				"                        LEFT JOIN (SELECT matning.id,\n" +
				"                                          matning.matningstyp_id,\n" +
				"                                          matning.avlast_varde,\n" +
				"                                          matning.avlast_datum,\n" +
				"                                          matning.status,\n" +
				"                                          matning.felkod,\n" +
				"                                          rank() OVER (PARTITION BY matning.matningstyp_id ORDER BY matning.avlast_datum DESC) AS rank\n" +
				"                                   FROM matning) m ON mt.id = m.matningstyp_id\n" +
				"                        JOIN definition_matningstyp dm ON mt.definition_matningstyp_id = dm.id\n" +
				"                        JOIN matobjekt mo ON mt.matobjekt_id = mo.id\n" +
				"               ORDER BY m.rank\n" +
				"              " +
				") m where m.matobjekt_id = :id and (m.rank is null or m.rank <= 3);";

		return queryMatningstyper(sql, parameters, false);
	}


	private List<RapporteringMatningstyp> queryMatningstyper(String sql, MapSqlParameterSource parameters, boolean sortMatrunda) {
		List<RapporteringMatningstyp> reportRows = new ArrayList<>();

		// Varje "rad" i excel-protokollet samt listan i den normala inrapporteringen motsvarar en mätningstyp,
		// men varje rad i vyn vi ställer frågan mot motsvarar en mätning.
		//
		// Vi grupperar dessa på mätningstyp och skapar upp RapportMatningstyp som motsvarar en "rad" i inrapporteringen.
		jdbcTemplate.queryForList(sql, parameters).stream()
				.collect(groupingBy(row -> row.get("matningstyp_id")))
				.forEach((matningstypId, rows) -> {
					rows.sort(Comparator.comparing(a -> ((Long) a.get("rank"))));
					reportRows.add(RapporteringMatningstyp.fromRows(rows));
				});

		if (sortMatrunda) {
			return reportRows.stream()
					.sorted(Comparator.comparing(RapporteringMatningstyp::getOrdning))
					.collect(Collectors.toList());
		} else {
			return reportRows;
		}
	}
}
