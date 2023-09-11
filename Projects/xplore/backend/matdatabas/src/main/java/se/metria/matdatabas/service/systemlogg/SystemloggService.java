package se.metria.matdatabas.service.systemlogg;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toList;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.metria.matdatabas.service.anvandare.behorighet.Behorighet;
import se.metria.matdatabas.service.anvandare.dto.Anvandare;
import se.metria.matdatabas.service.anvandargrupp.AnvandargruppService;
import se.metria.matdatabas.service.common.ChangeLog;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.matning.entity.MatningEntity;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.matobjekt.dto.Matobjekt;
import se.metria.matdatabas.service.matobjekt.exception.MatobjektNotFoundException;
import se.metria.matdatabas.service.matobjektgrupp.dto.Matobjektgrupp;
import se.metria.matdatabas.service.matrunda.dto.Matrunda;
import se.metria.matdatabas.service.systemlogg.handelse.Handelse;
import se.metria.matdatabas.service.systemlogg.dto.Systemlogg;
import se.metria.matdatabas.service.systemlogg.dto.SystemloggEntry;
import se.metria.matdatabas.service.systemlogg.entity.SystemloggEntity;
import se.metria.matdatabas.service.systemlogg.entity.SystemloggEntity_;
import se.metria.matdatabas.service.systemlogg.handelse.HandelseTyp;

@Service
public class SystemloggService {
	private final AnvandargruppService anvandargruppService;
	private SystemloggRepository repository;
	private MatningstypService matningstypService;
	private MatobjektService matobjektService;
	private DefinitionMatningstypService definitionMatningstypService;

	private Map<String, String> propertiesMapping = Map.of(
			"beskrivning", "beskrivning",
			"handelse", "handelse",
			"anvandarnamn", "loggatAv",
			"datum", "loggatDatum"
	);

	@FunctionalInterface
	interface StringReplaceFunction {
		public abstract String replace(Integer id);
	}

	public SystemloggService(SystemloggRepository repository,
							 AnvandargruppService anvandargruppService,
							 @Lazy MatningstypService matningstypService,
							 @Lazy MatobjektService matobjektService,
							 @Lazy DefinitionMatningstypService definitionMatningstypService) {
		this.repository = repository;
		this.anvandargruppService = anvandargruppService;
		this.matningstypService = matningstypService;
		this.matobjektService = matobjektService;
		this.definitionMatningstypService = definitionMatningstypService;
	}

	public Page<Systemlogg> getSystemlogg(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection,
										  LocalDateTime fromDatum, LocalDateTime tomDatum, Short handelse) {
		var sortPropertyMapped = propertiesMapping.getOrDefault(sortProperty, sortProperty);
		var pageRequest = PageRequest.of(page, pageSize, sortDirection, sortPropertyMapped);
		var searchFilter = getSearchFilter(fromDatum, tomDatum, handelse);
		var systemloggPage = repository.findAll(searchFilter, pageRequest).map(Systemlogg::fromEntity);

		var anvLogs = systemloggPage.getContent().stream()
				.filter(s -> s.getHandelse().equals(HandelseTyp.ANVANDARE.getId()))
				.collect(toList());
		resolveAnvandare(anvLogs);

		var matningLogs = systemloggPage
				.getContent()
				.stream()
				.filter(s -> s.getHandelse().equals(HandelseTyp.MATNING.getId()))
				.collect(toList());
		resolveMatningar(matningLogs);
		return systemloggPage;
	}

	void resolveMatningar(List<Systemlogg> logs) {
		replaceInLogs(logs, "#mtypenhet:", this::matningstypEnhet);
		replaceInLogs(logs, "#mobj:", this::matobjektNamn);
	}

	private void replaceInLogs(List<Systemlogg> logs, String target, StringReplaceFunction replaceFn) {
		for (final var id: getIdsFromLogs(logs, target)) {
			final String replace = replaceFn.replace(id);
			replaceStringInLogs(logs, target + id, replace);
		}
	}

	private Set<Integer> getIdsFromLogs(List<Systemlogg> logs, String target) {
		var p = Pattern.compile(target + "\\d+");
		return logs.stream()
				.map(Systemlogg::getBeskrivning)
				.map(p::matcher)
				.flatMap(Matcher::results)
				.map(MatchResult::group)
				.map(g -> g.substring(target.length()))
				.map(Integer::parseInt)
				.collect(toSet());
	}

	private String matningstypEnhet(Integer id) {
		final var mtyp = matningstypService.findById(id);
		if (mtyp.isPresent()) {
			return mtyp.get().getEnhet();
		} else {
			return "#" + id;
		}
	}

	private String matobjektNamn(Integer id) {
		try {
			return matobjektService.getMatobjekt(id).getNamn();
		} catch (MatobjektNotFoundException e) {
			return "#" + id;
		}
	}

	public SystemloggEntry addSystemloggEntry(String beskrivning, Short handelse) {
		var entity = new SystemloggEntity(beskrivning, handelse);
		return SystemloggEntry.fromEntity(repository.saveAndFlush(entity));
	}

	private void replaceStringInLogs(List<Systemlogg> logs, String target, String replacement) {
		logs.stream()
				.filter(l -> l.getBeskrivning().contains(target))
				.forEach(l -> l.setBeskrivning(l.getBeskrivning().replace(target, replacement)));
	}

	@Transactional
	public void addHandelseUserLoggedIn(Integer anvandarId) {
		Handelse handelse = Handelse.USER_LOGGED_IN;
		repository.insertHandelseInloggning(LocalDateTime.now(), anvandarId, handelse.getTyp().getId(), handelse.getBeskrivning());
	}

	public SystemloggEntry addHandelseUserCreated(Anvandare anvandare) {
		return createAndPersistHandelse(Handelse.USER_CREATED,
				anvandare.getId(),
				Behorighet.getNameById(anvandare.getBehorighet()));
	}

	public SystemloggEntry addHandelseUserModified(Anvandare before, Anvandare after) {
		return createAndPersistHandelse(Handelse.USER_MODIFIED,
				before.getId(),
				Behorighet.getNameById(before.getBehorighet()),
				diff(before, after));
	}

	public SystemloggEntry addHandelseUserRemoved(Anvandare anvandare) {
		return createAndPersistHandelse(Handelse.USER_REMOVED,
				anvandare.getId(),
				Behorighet.getNameById(anvandare.getBehorighet()));
	}

	public SystemloggEntry addHandelseUserAnonymized(Anvandare anvandare) {
		return createAndPersistHandelse(Handelse.USER_ANONYMIZED,
				anvandare.getId(),
				Behorighet.getNameById(anvandare.getBehorighet()));
	}

	public SystemloggEntry addHandelseMatobjektCreated(Matobjekt matobjekt) {
		return createAndPersistHandelse(Handelse.MATOBJEKT_CREATED,
				matobjekt.getId(),
				matobjekt.getNamn());
	}

	public SystemloggEntry addHandelseMatobjektModified(Matobjekt before, Matobjekt after) {
		return createAndPersistHandelse(Handelse.MATOBJEKT_MODIFIED,
				before.getId(),
				before.getNamn(),
				diff(before, after));
	}

	public SystemloggEntry addHandelseMatobjektRemoved(Matobjekt matobjekt) {
		return createAndPersistHandelse(Handelse.MATOBJEKT_REMOVED,
				matobjekt.getId(),
				matobjekt.getNamn());
	}


	public SystemloggEntry addHandelseMatobjektgruppCreated(Matobjektgrupp matobjektgrupp) {
		return createAndPersistHandelse(Handelse.MATOBJEKTGRUPP_CREATED,
				matobjektgrupp.getId(),
				matobjektgrupp.getNamn());
	}

	public SystemloggEntry addHandelseMatobjektgruppModified(Matobjektgrupp before, Matobjektgrupp after) {
		return createAndPersistHandelse(Handelse.MATOBJEKTGRUPP_MODIFIED,
				before.getId(),
				before.getNamn(),
				diff(before, after));
	}

	public SystemloggEntry addHandelseMatobjektgruppRemoved(Matobjektgrupp matobjektgrupp) {
		return createAndPersistHandelse(Handelse.MATOBJEKTGRUPP_REMOVED,
				matobjektgrupp.getId(),
				matobjektgrupp.getNamn());
	}

	public SystemloggEntry addHandelseMatrundaCreated(Integer id, String namn) {
		return createAndPersistHandelse(Handelse.MATRUNDA_CREATED, id, namn);
	}

	public SystemloggEntry addHandelseMatrundaModified(Integer id, String namn, ChangeLog changeLog) {
		return createAndPersistHandelse(Handelse.MATRUNDA_MODIFIED, id, namn, changeLog.toString());
	}

	public SystemloggEntry addHandelseMatrundaRemoved(Integer id, String namn) {
		return createAndPersistHandelse(Handelse.MATRUNDA_REMOVED, id, namn);
	}

	public SystemloggEntry addHandelseStatusMailSent(Anvandare anvandare) {
		return createAndPersistHandelse(Handelse.STATUS_MAIL_SENT,
				anvandare.getId(),
				anvandare.getEpost());
	}

	public SystemloggEntry addHandelseStatusMailFailed(Anvandare anvandare) {
		return createAndPersistHandelse(Handelse.STATUS_MAIL_FAILED,
				anvandare.getId(),
				anvandare.getEpost());
	}

	public SystemloggEntry addHandelseImportDone(String jobServiceTyp, Integer antal) {
		return createAndPersistHandelse(Handelse.IMPORT_DONE,
				jobServiceTyp,
				antal);
	}

	public SystemloggEntry addHandelseImportFailed(String jobServiceTyp, String fel) {
		return createAndPersistHandelse(Handelse.IMPORT_FAILED,
				jobServiceTyp,
				fel);
	}

	public SystemloggEntry addHandelseRapportCreated(Integer rapportId, String namn) {
		return createAndPersistHandelse(Handelse.RAPPORT_CREATED,
			rapportId,
			namn);
	}

	public SystemloggEntry addHandelseRapportModified(Integer rapportId, String namn) {
		return createAndPersistHandelse(Handelse.RAPPORT_MODIFIED,
				rapportId,
				namn);
	}

	public SystemloggEntry addHandelseRapportRemoved(Integer rapportId, String namn) {
		return createAndPersistHandelse(Handelse.RAPPORT_REMOVED,
				rapportId,
				namn);
	}

	public SystemloggEntry addHandelseMatningRemoved(MatningEntity matning) {
		final var matningstyp = matningstypService.findById(matning.getMatningstypId()).get();

		var enhetMatvarde = matningstyp.getEnhet();
		var matvarde = loggVardeString(matning.getAvlastVarde(), enhetMatvarde);

		var enhetBeraknatVarde = matningstyp.getBeraknadEnhet();
		var beraknatVarde = loggVardeString(matning.getBeraknatVarde(), enhetBeraknatVarde);

		return createAndPersistHandelse(Handelse.MATNING_REMOVED,
				matning.getId(),
				matning.getRapportor(),
				matning.getAvlastDatum().toString(),
				matvarde,
				beraknatVarde,
				matningstyp.getMatobjektId()
		);
	}

	private String loggVardeString(Double varde, String enhet) {
		return varde == null ? "N/A" : new DecimalFormat("0.#").format(varde) + enhet;
	}

	private SystemloggEntry createAndPersistHandelse(Handelse handelse, Object... params) {
		var entity = new SystemloggEntity(
				String.format(handelse.getBeskrivning(), params),
				handelse.getTyp().getId());
		return SystemloggEntry.fromEntity(repository.save(entity));
	}

	private Specification<SystemloggEntity> getSearchFilter(LocalDateTime fromDatum, LocalDateTime tomDatum, Short handelse) {
		return (root, query, builder) -> {
	        var and = builder.conjunction();
			if (fromDatum != null && tomDatum != null) {
				and.getExpressions().add(builder.between(root.get(SystemloggEntity_.loggatDatum), fromDatum, tomDatum));
			} else if (fromDatum != null) {
				and.getExpressions().add(builder.greaterThan(root.get(SystemloggEntity_.loggatDatum), fromDatum));
			} else if (tomDatum != null) {
				and.getExpressions().add(builder.lessThan(root.get(SystemloggEntity_.loggatDatum), tomDatum));
			}
			if (handelse != null) {
				and.getExpressions().add(builder.equal(root.get(SystemloggEntity_.handelse), handelse));
			}
			return and;
		};
	}

	private void resolveAnvandare(List<Systemlogg> logs) {
		var anvIds = getIdsFromLogs(logs, "#");
		if (!anvIds.isEmpty()) {
			var anvandare = repository.getAnvandare(anvIds);
			anvandare.forEach((id, namn) -> {
				logs.stream()
					.filter(l -> l.getBeskrivning().contains(id))
					.forEach(l -> l.setBeskrivning(l.getBeskrivning().replace(id, namn)));
			});
		}
	}

	private String diff(Anvandare before, Anvandare after) {
		ChangeLog log = new ChangeLog(false);

		log.evaluatePropertyChange("Namn", before.getNamn(), after.getNamn());
		log.evaluatePropertyChange("Företag", before.getForetag(), after.getForetag());
		log.evaluatePropertyChange("Aktiv", before.getAktiv(), after.getAktiv());
		log.evaluatePropertyChange("Användarnamn", before.getInloggningsnamn(), after.getInloggningsnamn());
		log.evaluatePropertyChange(
				"Behörighet",
				Behorighet.getNameById(before.getBehorighet()),
				Behorighet.getNameById(after.getBehorighet()),
				true);
		log.evaluatePropertyChange("Kartlager", before.getDefaultKartlagerId(), after.getDefaultKartlagerId());
		log.evaluatePropertyChange("Skicka e-post", before.getSkickaEpost(), after.getSkickaEpost());
		log.evaluatePropertyChange("E-postadress", before.getEpost(), after.getEpost());
		log.evaluatePropertyChangeCollection(
				"Tillagda användargrupper",
				"Borttagna användargrupper",
				(before.getAnvandargrupper() != null ? before.getAnvandargrupper().stream().map(anvandarGruppId -> anvandargruppService.getAnvandargrupp(anvandarGruppId).get().getNamn()).sorted().collect(toList()) : new ArrayList<>()),
				(after.getAnvandargrupper() != null ? after.getAnvandargrupper().stream().map(anvandarGruppId -> anvandargruppService.getAnvandargrupp(anvandarGruppId).get().getNamn()).sorted().collect(toList()): new ArrayList<>())
		);

		return log.toString();
	}

	private String diff(Matobjekt before, Matobjekt after) {
		ChangeLog log = new ChangeLog(true);

		log.evaluatePropertyChange("Typ", before.getTyp(), after.getTyp());
		log.evaluatePropertyChange("Namn", before.getNamn(), after.getNamn());
		log.evaluatePropertyChange("Aktiv", before.getAktiv(), after.getAktiv());
		log.evaluatePropertyChange("Kontrollprogram", before.getKontrollprogram(), after.getKontrollprogram());
		log.evaluatePropertyChange("Bifogad bild-ID", before.getBifogadBildId(), after.getBifogadBildId());
		log.evaluatePropertyChange("Position (N)", before.getPosN(), after.getPosN());
		log.evaluatePropertyChange("Position (E)", before.getPosE(), after.getPosE());
		log.evaluatePropertyChange("Fastighet", before.getFastighet(), after.getFastighet());
		log.evaluatePropertyChange("Läge", before.getLage(), after.getLage());
		log.evaluatePropertyChangeCollection(
				"Tillagda mätobjektgrupper",
				"Borttagna mätobjektgrupper",
				before.getMatobjektgrupper(),
				after.getMatobjektgrupper()
		);
		log.evaluatePropertyChangeCollection(
				"Tillagda dokument",
				"Borttagna dokument",
				before.getDokument(),
				after.getDokument()
		);

		return log.toString();
	}

	private String diff(Matobjektgrupp before, Matobjektgrupp after) {
		ChangeLog log = new ChangeLog(true);

		log.evaluatePropertyChange("Namn", before.getNamn(), after.getNamn());
		log.evaluatePropertyChange("Kategori", before.getKategori(), after.getKategori());
		log.evaluatePropertyChange("Kartsymbol", before.getKartsymbol(), after.getKartsymbol());
		log.evaluatePropertyChange("Beskrivning", before.getBeskrivning(), after.getBeskrivning());
		log.evaluatePropertyChangeCollection(
				"Tillagda mätobjekt",
				"Borttagna mätobjekt",
				before.getMatobjekt().stream().collect(Collectors.toSet()),
				after.getMatobjekt().stream().collect(Collectors.toSet())
		);

		return log.toString();
	}
}
