package se.metria.markkoll.service.fastighet;

import com.google.common.collect.Iterables;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.WKTWriter2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.union.UnaryUnionOp;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.DetaljtypDto;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.service.common.XpSearchService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;

@MarkkollService
@Slf4j
@AllArgsConstructor
public class RegisterenhetImportService {
	@PersistenceContext
	private final EntityManager entityManager;

	@NonNull
	private final FastighetsomradeImportService fastighetsomradeImportService;

	@NonNull
	private final FastighetOmradeRepository fastighetOmradeRepository;

	@NonNull
	private final FastighetRepository fastighetRepository;

	@NonNull
	private final XpSearchService xpSearchService;

	private static final String EMPTY_WKT = "LINESTRING EMPTY";
	private static final int batchSize = 250;

	@Transactional
	public Optional<UUID> importRegisterenhet(UUID fastighetId) {
		log.info("Importerar registerenhet med id {}", fastighetId);

		var fastighetsomraden = xpSearchService.getFastighetByUuid(fastighetId);

		var fastigheter = fastighetsomradeImportService.importFastighetsomraden(fastighetsomraden);

		if (fastigheter.size() > 1) {
			log.warn("Import innehöll mer än en 1 ({}) registerenheter", fastigheter.size());
		}

		return fastigheter.stream().findAny();
	}

	/**
	 * Beräknar vilka registerenheter som överlappar angivna geometrier, och importerar eventuella ytor som inte
	 * importerats tidigare.
	 * Efter import finns registerenheterna tillgängliga via {{{@link FastighetRepository}}}.
	 *
	 * @return ID för de registerenheter som överlappar input-geometrierna, grupperat på detaljtyp.
	 */
	@Transactional
	public Map<DetaljtypDto, Set<UUID>> importRegisterenheter(List<Geometry> geometries, Double buffert) {
		var registerenheter = new HashMap<DetaljtypDto, Set<UUID>>();

		var batchNum = 0;
		var maxBatch = geometries.size() / batchSize + 1;
		for (var batch : Iterables.partition(geometries, batchSize)) {
			var wkt = getGeometryWKT(batch, buffert);

			if (wkt.equalsIgnoreCase(EMPTY_WKT)) {
				continue;
			}

			log.info("Hämtar batch {}/{}", ++batchNum, maxBatch);

			importBatch(wkt).forEach((detaljtyp, set) ->
					registerenheter.computeIfAbsent(detaljtyp, s -> new HashSet<>())
							.addAll(set));
		}
		entityManager.flush(); // Vi kör jooq-queries mot fastighetstabellen längre upp i stacken
		entityManager.clear(); // Förhindra onödiga isDirty-checkar
		return registerenheter;
	}

	private Map<DetaljtypDto, Set<UUID>> importBatch(String wkt) {
		var registerenheter = new HashMap<DetaljtypDto, Set<UUID>>();
		try {
			log.info("Hämtar områden från söktjänst...");
			var list = xpSearchService.getFastigheterByWkt(wkt);

			log.info("Hämtade {} områden från söktjänst", list.size());
			for (FastighetsOmrInfo fastighetsOmrInfo : list) {
				log.debug("Slår upp fastighet {} för område {}...", fastighetsOmrInfo.fastighet.getId(),
						fastighetsOmrInfo.omrade_nr);
				var existing = fastighetRepository
						.findById(fastighetsOmrInfo.fastighet.getId())
						.orElseGet(() -> {
							log.info("Fastighet {} fanns ej, skapar ny fastighet", fastighetsOmrInfo.fastighet.getId());
							return fastighetRepository.save(fastighetsOmrInfo.fastighet);
						});

				// Uppdatera Beteckning, Blockenhet och Trakt eftersom vi hittat fall där vi sedan tidigare lagrat felaktig data i databasen.
				existing.setFastighetsbeteckning(fastighetsOmrInfo.getFastighet().getFastighetsbeteckning());
				existing.setBlockenhet(fastighetsOmrInfo.getFastighet().getBlockenhet());
				existing.setTrakt(fastighetsOmrInfo.getFastighet().getTrakt());

				var fastOmr = new FastighetOmradeEntity(fastighetsOmrInfo.fastighet.getId(),
						fastighetsOmrInfo.omrade_nr, fastighetsOmrInfo.geom, new HashSet<>());

				log.debug("Slår upp fastighetsområde {}...", fastOmr.getOmradeId());
				var existingOmrade = fastighetOmradeRepository.findById(fastOmr.getOmradeId())
						.orElseGet(() -> {
							log.info("Fastighetsområde {} fanns ej, skapar nytt fastighetsområde ({} points)", fastOmr.getOmradeId(), fastOmr.getGeom().getNumPoints());
							return fastighetOmradeRepository.save(fastOmr);
						});

				existing.addFastighetomrade(existingOmrade);
				existing = fastighetRepository.save(existing);
				registerenheter.computeIfAbsent(existing.getDetaljtyp(), s -> new HashSet<>()).add(existing.getId());
				log.debug("Fastighet {} uppdaterad", existing.getId());
			}
			return registerenheter;
		} catch (IOException ex) {
			throw new MarkkollException(MarkkollError.FASTIGHET_ERROR, ex);
		}
	}


	private String getGeometryWKT(List<Geometry> geometries, Double buffert) {
		if (geometries.size() == 0) {
			return EMPTY_WKT;
		}
		var union = buffert == 0.0 ? UnaryUnionOp.union(geometries) : UnaryUnionOp.union(geometries).buffer(buffert);
		return new WKTWriter2().write(union);
	}
}
