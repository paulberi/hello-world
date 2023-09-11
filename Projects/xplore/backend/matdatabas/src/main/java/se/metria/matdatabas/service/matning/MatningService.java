package se.metria.matdatabas.service.matning;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.db.tables.records.MatningRecord;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypConstants;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypRepository;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.kallsystem.KallsystemRepository;
import se.metria.matdatabas.service.kallsystem.entity.KallsystemEntity;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.matning.dto.ReviewMatning;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.dto.*;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportError;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportFormat;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportMatning;
import se.metria.matdatabas.service.matning.entity.MatningEntity;
import se.metria.matdatabas.service.matning.entity.MatningEntity_;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matning.query.MatningSearchFilter;
import se.metria.matdatabas.service.matningslogg.MatningsloggService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.jooq.impl.DSL.noCondition;
import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matning.MATNING;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;

@Service
public class MatningService {
	private final DSLContext create;
	private final MatningRepository matningRepository;
	private final DefinitionMatningstypRepository definitionMatningstypRepository;
	private final MatningsloggService matningsloggService;
	private final LarmService larmService;
	private final MatningvardeValidator validator;
	private final MatningvardeControl controller;
	private final KallsystemRepository kallsystemRepository;
	private final SystemloggService systemloggService;
	private final DefinitionMatningstypService definitionMatningstypService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public MatningService(DSLContext create,
						  MatningRepository matningRepository,
						  MatningsloggService matningsloggService,
						  MatningvardeValidator validator,
						  MatningvardeControl controller,
						  KallsystemRepository kallsystemRepository,
						  SystemloggService systemloggService,
						  LarmService larmService,
						  DefinitionMatningstypRepository definitionMatningstypRepository,
						  DefinitionMatningstypService definitionMatningstypService) {
		this.create = create;
		this.matningRepository = matningRepository;
		this.matningsloggService = matningsloggService;
		this.validator = validator;
		this.controller = controller;
		this.kallsystemRepository = kallsystemRepository;
		this.systemloggService = systemloggService;
		this.larmService = larmService;
		this.definitionMatningstypService = definitionMatningstypService;
		this.definitionMatningstypRepository = definitionMatningstypRepository;
	}

	public Optional<Matning> getLatestMatningForMatningstyp(Integer matningstypId) {
		// Mätrapportör eller bättre får se icke godkända mätningar
		Short status = Matningstatus.GODKANT;

		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (authority.getAuthority().equalsIgnoreCase(MatdatabasRole.MATRAPPORTOR)) {
				status = null;
			}
		}

		List<Matning> matningar = getMatningarForMatningstyp(matningstypId, 0, 1,
				"avlastDatum", Sort.Direction.DESC, null, null, status
		).getContent();

		return matningar.size() == 0 ? Optional.empty() : Optional.of(matningar.get(0));
	}

	@PreAuthorize("hasRole('" + MatdatabasRole.MATRAPPORTOR + "') or (hasRole('" + MatdatabasRole.OBSERVATOR + "') AND #status=="+ Matningstatus.GODKANT+" )")
	public Page<Matning> getMatningarForMatningstyp(Integer matningstypId, Integer page, Integer pageSize,
			String sortProperty, Sort.Direction sortDirection, LocalDateTime fromDatum, LocalDateTime tomDatum, Short status) {
		var pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		var searchFilter = getSearchFilter(matningstypId, fromDatum, tomDatum, status);
		
		return matningRepository.findAll(searchFilter, pageRequest).map(Matning::fromEntity);
	}

	public boolean hasMatningar(Integer matningstypId) {
		return matningRepository.existsByMatningstypId(matningstypId);
	}

	public boolean hasMatning(Integer matningstypId, Long matningId) {
		return matningRepository.existsByMatningstypIdAndId(matningstypId, matningId);
	}

	private MatningSaveResult saveMatning(MatningEntity matningEntity, Matningstyp matningstyp, boolean importerad, String kallsystem) {
		KallsystemEntity kallsystemEntity = getKallsystem(kallsystem);

		if (matningEntity.getBeraknatVarde() == null) {
			if (!kallsystemEntity.isDefaultGodkand()) {
				beraknaVarde(matningstyp, matningEntity);
			}
		}

		if (kallsystemEntity.isDefaultGodkand()) {
			matningEntity.setStatus(Matningstatus.GODKANT);
		} else {
			if ((matningstyp.getGranskasAutomatiskt() && importerad) && !matningEntity.hasFelkod()) {
				granskaAutomatiskt(matningstyp, matningEntity);
			}
		}
		matningEntity = matningRepository.save(matningEntity);

		var matning = Matning.fromEntity(matningEntity);

		List<Larm> larmList = this.controller.controlMatning(matningstyp, matning);

		return new MatningSaveResult(matning, larmList);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	@Transactional
	public MatningSaveResult create(Matningstyp matningstyp, SaveMatning save, boolean importerad, String kallsystem) throws MatningIllegalMatvarde, AlreadyGodkandException {
		var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

		if (!save.hasFelkod() && !validator.validate(definitionMatningstyp, matningstyp, save.getAvlastVarde())) {
			throw new MatningIllegalMatvarde();
		}

		var foundMatningEntity = matningRepository.findByMatningstypIdAndAvlastDatum(matningstyp.getId(),
				save.getAvlastDatum().truncatedTo(ChronoUnit.SECONDS));

		MatningEntity matningEntity;

		if (foundMatningEntity.isPresent()){
			if (foundMatningEntity.get().getStatus() == Matningstatus.GODKANT) {
				throw new AlreadyGodkandException(save, foundMatningEntity.get());
			}

			matningEntity = foundMatningEntity.get();

			matningEntity.updateWithDataFromSaveMatning(kallsystem, save);
		} else {
			matningEntity = matningRepository.save(new MatningEntity(kallsystem, matningstyp.getId(), save));
		}

		if (importerad) {
			logImporterad(matningstyp, matningEntity);
		} else {
			logRapporterad(matningstyp, matningEntity);
		}

		return saveMatning(matningEntity, matningstyp, importerad, kallsystem);
	}

	public KallsystemEntity getKallsystem(String kallsystem) {
		Optional<KallsystemEntity> kallsystemEntityOptional = kallsystemRepository.findById(kallsystem);

		kallsystemEntityOptional.orElseThrow(() -> new IllegalArgumentException("Unknown källsystem: "+kallsystem));

		return kallsystemEntityOptional.get();
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	@Transactional
	public MatningSaveResult update(Long matningId, Matningstyp matningstyp, SaveMatning save, String kallsystem) throws MatningIllegalMatvarde, AlreadyGodkandException {
		var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

		if (!save.hasFelkod() && !validator.validate(definitionMatningstyp, matningstyp, save.getAvlastVarde())) {
			throw new MatningIllegalMatvarde();
		}

		var matningEntity = findMatning(matningId);

		if (matningEntity.getStatus() == Matningstatus.GODKANT) {
			throw new AlreadyGodkandException(save, matningEntity);
		}

		Double oldVarde = matningEntity.getAvlastVarde();
		String oldKommentar = matningEntity.getKommentar();
		LocalDateTime oldAvlastDatum = matningEntity.getAvlastDatum();
		String oldFelkod = matningEntity.getFelkod();

		matningEntity.updateWithDataFromSaveMatning(kallsystem, save);

		matningsloggService.addHandelseAndrat(matningId,
				oldVarde, matningEntity.getAvlastVarde(),
				oldKommentar, matningEntity.getKommentar(),
				oldAvlastDatum, matningEntity.getAvlastDatum(),
				oldFelkod, matningEntity.getFelkod());

		return saveMatning(matningEntity, matningstyp, false, kallsystem);
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Matning getMatning(Long id) throws EntityNotFoundException {
		return Matning.fromEntity(findMatning(id));
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Matning getMatningByMatningstypAndDatum(Matningstyp matningstyp, LocalDateTime datum) {
		var optionalMatningEntityatningEntity = matningRepository.findByMatningstypIdAndAvlastDatum(matningstyp.getId(),
				datum.truncatedTo(ChronoUnit.SECONDS));

		return optionalMatningEntityatningEntity.map(Matning::fromEntity).orElse(null);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Integer getEjGranskadeMatningarForMatningstyp(Integer matningstypId) {
		return matningRepository.countByMatningstypIdAndStatus(matningstypId, Matningstatus.EJGRANSKAT);
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public void godkann(List<Long> ids) {
		for (MatningEntity matningEntity : matningRepository.findAllById(ids)) {
			setStatus(matningEntity, Matningstatus.GODKANT);
		}
	}

	@Transactional
	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void deleteMatningar(List<Long> ids) {
		for (final var id: ids) {
			try {
				final var matning = matningRepository.findById(id).get();

				matningsloggService.deleteLogsForMatning(id);

				for (var larm: larmService.getLarmsForMatning(id)) {
					larmService.deleteLarm(larm.getId());
				}

				matningRepository.deleteById(id);

				systemloggService.addHandelseMatningRemoved(matning);
			} catch (NoSuchElementException e) {
				logger.error("Matning id not found: " + id);
			}
		}
	}

	private void logImporterad(Matningstyp matningstyp, MatningEntity matningEntity) {
		if (matningEntity.hasFelkod()) {
			matningsloggService.addHandelseImporteradFelkod(matningEntity.getId(), matningEntity.getFelkod(), matningEntity.getKommentar() );
		} else {
			var beraknatVardeEnhet = matningstyp.getBeraknadEnhet();

			matningsloggService.addHandelseImporterad(matningstyp, matningEntity.getId(),
					matningEntity.getAvlastVarde(), matningEntity.getBeraknatVarde(), beraknatVardeEnhet, matningEntity.getKommentar());
		}
	}

	private void logRapporterad(Matningstyp matningstyp, MatningEntity matningEntity) {
		if (matningEntity.hasFelkod()) {
			matningsloggService.addHandelseRapporteradFelkod(matningEntity.getId(), matningEntity.getFelkod(), matningEntity.getKommentar() );
		} else {
			matningsloggService.addHandelseRapporterad(matningstyp, matningEntity.getId(), matningEntity.getAvlastVarde(), matningEntity.getKommentar());
		}
	}

	private void setStatus(MatningEntity entity, Short status) {
		entity.setStatus(status);
		matningsloggService.addHandelseStatus(entity.getId(), status);
	}

	// TODO: Would be far more convenient and probably better overall to handle more than one review in each call.
	// You could potentially end up with A LOT of requests here.
	@Transactional
	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matning review(long matningId, Matningstyp matningstyp, ReviewMatning review) throws EntityNotFoundException,
			MatningIllegalMatvarde {
		MatningEntity matningEntity = this.findMatning(matningId);
		Short status = review.getStatus();
		Double avlastVarde = review.getAvlastVarde();
		String kommentar = review.getKommentar();
		Short operation = review.getOperation();

		if (status != null) {
			setStatus(matningEntity, status);
		}

		if (avlastVarde != null) {
			Double oldVarde = matningEntity.getAvlastVarde();
			if (operation == Matningoperation.ADD) {
				avlastVarde = oldVarde + avlastVarde;
			} else if (operation == Matningoperation.SUBTRACT) {
				avlastVarde = oldVarde - avlastVarde;
			}

			var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

			if (!validator.validate(definitionMatningstyp, matningstyp, avlastVarde)) {
				throw new MatningIllegalMatvarde();
			}
			matningEntity.setStatus(Matningstatus.EJGRANSKAT);
			matningEntity.setAvlastVarde(avlastVarde);
			matningsloggService.addHandelseKorrigerad(matningId, oldVarde, avlastVarde);
			beraknaVarde(matningstyp, matningEntity);
			var matning = Matning.fromEntity(matningEntity);
			this.controller.controlMatning(matningstyp, matning);
		}

		if (kommentar != null) {
			matningsloggService.addHandelseKorrigeradKommentar(matningId, matningEntity.getKommentar(), kommentar);
			matningEntity.setKommentar(kommentar);
		}

		matningRepository.save(matningEntity);
		return Matning.fromEntity(matningEntity);

	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<ImportMatning> parseCsvImport(InputStream csv, ImportFormat format) throws IllegalArgumentException, IOException {
		return format.parse(csv);
	}

	public void validateStandard(Optional<Matningstyp> optMatningstyp, ImportMatning importMatning) {
		validateFormats(importMatning);
		if (optMatningstyp.isPresent()) {
			var matningstyp = optMatningstyp.get();

			if (!isNullOrEmpty(importMatning.getAvlastVarde()) &&
				hasCorrectEnhet(importMatning.getEnhetAvlast(), matningstyp.getEnhet()))
			{
				importMatning.getImportFel().add(ImportError.ENHET_AVLAST);
			}

			if (!isNullOrEmpty(importMatning.getBeraknatVarde()) &&
				hasCorrectEnhet(importMatning.getEnhetBeraknat(), matningstyp.getBeraknadEnhet()))
			{
				importMatning.getImportFel().add(ImportError.ENHET_BERAKNAT);
			}

			if (hasVarden(importMatning.getAvlastVarde(), importMatning.getBeraknatVarde()) &&
				hasFelkod(importMatning.getFelkod()))
			{
				importMatning.getImportFel().add(ImportError.HINDER_AND_VARDE);
			}

			validateVarden(matningstyp, importMatning);
			validateExists(matningstyp, importMatning);

		}
	}

	public void validateInstrument(Optional<Matningstyp> optMatningstyp, ImportMatning importMatning) {
		validateFormats(importMatning);
		if (optMatningstyp.isPresent()) {
			var matningstyp = optMatningstyp.get();
			validateVarden(matningstyp, importMatning);
			validateExists(matningstyp, importMatning);
		}
	}

	private boolean hasFelkod(String felkod) {
		return !isNullOrEmpty(felkod) && !felkod.equals(Matningsfelkod.OK);
	}

	private boolean hasVarden(String avlastVarde, String beraknatVarde) {
		return !isNullOrEmpty(avlastVarde) || !isNullOrEmpty(beraknatVarde);
	}

	private boolean hasCorrectEnhet(String enhetActual, String enhetExpected) {
		return 	enhetActual != null &&
				!enhetActual.isBlank() &&
				!enhetActual.equals(enhetExpected);
	}

	private void validateFormats(ImportMatning importMatning) {
		validateFormat(importMatning, im -> im.getAvlastDatum() != null ? im.getAvlastDatum().replace(" ", "T") : null,
				LocalDateTime::parse, ImportError.AVLASTDATUM_MISSING, ImportError.AVLASTDATUM_FORMAT);

		if (importMatning.getFelkod() != null && importMatning.getFelkod().equals(Matningsfelkod.OK)) {
			validateFormat(importMatning, im -> im.getAvlastVarde() != null ? im.getAvlastVarde().replace(",", ".") : null,
					Double::parseDouble, ImportError.AVLASTVARDE_MISSING, ImportError.AVLASTVARDE_FORMAT);
		}

		validateFormat(importMatning, ImportMatning::getInomDetektionsomrade, Set.of("", ">", "<"), ImportError.INOMDETEKTIONSOMRADE_VALUE);
		validateFormat(importMatning, ImportMatning::getFelkod,
				Set.of("", Matningsfelkod.ANNAT_FEL, Matningsfelkod.FLODAR, Matningsfelkod.FRUSET, Matningsfelkod.HINDER, Matningsfelkod.OK, Matningsfelkod.TORR),
				ImportError.FELKOD_VALUE);
		if (importMatning.getKommentar() != null && importMatning.getKommentar().length() > 250) {
			importMatning.getImportFel().add(ImportError.KOMMENTAR_LENGTH);
		}
	}

	private void validateFormat(ImportMatning importMatning, Function<ImportMatning, String> getter, Function<String, ?> parser, ImportError missingError, ImportError formatError) {
		var value = getter.apply(importMatning);
		if (value == null || value.isBlank()) {
			importMatning.getImportFel().add(missingError);
		} else {
			try {
				parser.apply(value);
			} catch (Exception e) {
				importMatning.getImportFel().add(formatError);
			}
		}

	}

	private void validateFormat(ImportMatning importMatning, Function<ImportMatning, String> getter, Set<String> values, ImportError valueError) {
		var value = getter.apply(importMatning);
		if (value != null) {
			if (!values.contains(value)) {
				importMatning.getImportFel().add(valueError);
			}
		}
	}

	private void validateVarden(Matningstyp matningstyp, ImportMatning importMatning) {
		if (importMatning.getFelkod().equals(Matningsfelkod.OK) &&
				isNullOrEmpty(importMatning.getAvlastVarde()) &&
				isNullOrEmpty(importMatning.getBeraknatVarde())) {
			importMatning.getImportFel().add(ImportError.VARDE_MISSING);
		} else if (importMatning.getAvlastVarde() != null) {
			try {
				var varde = Double.parseDouble(importMatning.getAvlastVarde().replace(",", "."));

				var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

				if (!validator.validate(definitionMatningstyp, matningstyp, varde)) {
					switch (definitionMatningstyp.getBerakningstyp()) {
						case NIVA_NEDMATNING:
							if (varde < 0d) {
								importMatning.getImportFel().add(ImportError.AVLASTVARDE_LESS_ZERO);
							} else {
								importMatning.getImportFel().add(new ImportError("avlastVarde",
										String.format("Värde är högre än maximalt pejlbart djup %1$s %2$s.", matningstyp.getMaxPejlbartDjup(), matningstyp.getEnhet())));
							}
							break;
						case NIVA_PORTRYCK:
							importMatning.getImportFel().add(new ImportError("avlastVarde",
									String.format("Värde är högre än maximalt portryck 200 %1$s.", matningstyp.getEnhet())));
							break;
						default:
							importMatning.getImportFel().add(ImportError.AVLASTVARDE_LESS_EQ_ZERO);
							break;
					}
				}
			} catch (NumberFormatException e) {
				// Format error borde vara satt så gör inget.
			}
		}

	}

	private boolean isNullOrEmpty(String s) {
		return (s == null) || s.equals("");
	}
	private void validateExists(Matningstyp matningstyp, ImportMatning importMatning) {
		if (importMatning.getAvlastDatum() != null) {
			try {
				var avlastDatum = LocalDateTime.parse(importMatning.getAvlastDatum().replace(" ", "T"));
				if (matningRepository.existsByMatningstypIdAndAvlastDatum(matningstyp.getId(), avlastDatum)) {
					importMatning.getImportFel().add(ImportError.EXISTS);
				}
			} catch (DateTimeParseException e) {
				// Format error borde vara satt så gör inget.
			}
		}
	}

	private MatningEntity findMatning(Long id) throws EntityNotFoundException {
		return matningRepository.findById(id).orElseThrow(EntityNotFoundException::new);
	}

	private void granskaAutomatiskt(Matningstyp matningstyp, MatningEntity matningEntity) {
		Double varde = matningEntity.getBeraknatVarde() == null ? matningEntity.getAvlastVarde() : matningEntity.getBeraknatVarde();

		if (varde > matningstyp.getGranskasMax()) {
			matningEntity.setStatus(Matningstatus.EJGRANSKAT);
			matningEntity.setKommentar(matningEntity.getKommentar() + " #Mätvärdet överstiger intervall för automatisk granskning");
			matningsloggService.addHandelseAutomatisktGranskat(matningEntity.getId(), Matningstatus.EJGRANSKAT);
		} else if (varde < matningstyp.getGranskasMin()) {
			matningEntity.setStatus(Matningstatus.EJGRANSKAT);
			matningEntity.setKommentar(matningEntity.getKommentar() + " #Mätvärdet understiger intervall för automatisk granskning");
			matningsloggService.addHandelseAutomatisktGranskat(matningEntity.getId(), Matningstatus.EJGRANSKAT);
		} else {
			matningEntity.setStatus(Matningstatus.GODKANT);
			matningsloggService.addHandelseAutomatisktGranskat(matningEntity.getId(), Matningstatus.GODKANT);
		}
	}

	private void beraknaVarde(Matningstyp matningstyp, MatningEntity matning) {
		if (!Matningsfelkod.OK.equals(matning.getFelkod())) {
			matning.setBeraknatVarde(null);
			return;
		}

		var definitionmatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId());

		if (definitionmatningstyp.isPresent() && definitionmatningstyp.get().getBerakningstyp() != null) {
			switch (definitionmatningstyp.get().getBerakningstyp()) {
				case NIVA_NEDMATNING:
					beraknaFromParams(matningstyp, matning, (mt, av) -> mt.getBerakningReferensniva() - av * mt.getBerakningKonstant(), "Referensnivå");
					break;
				case NIVA_PORTRYCK:
					beraknaFromParams(matningstyp, matning, (mt, av) -> mt.getBerakningReferensniva() + av / 9.82d + 0.2d * mt.getBerakningKonstant(), "Spetsnivå");
					break;
				case NIVA_VATTEN_LUFTTRYCK:
					beraknaNivaVattenLufttryck(matningstyp, matning);
					break;
				case INFILTRATION_MEDEL_FLODE:
					beraknaFromPrev(matningstyp, matning);
					break;
				case TUNNELVATTEN_MEDEL_FLODE:
					beraknaFromPrev(matningstyp, matning);
					break;
			}
		}
	}
	
	private void beraknaFromParams(Matningstyp matningstyp, MatningEntity matning, BiFunction<Matningstyp, Double, Double> beraknaFunc, String refnivaNamn) {
		boolean ok = matningstyp.getBerakningReferensniva() != null && matningstyp.getBerakningKonstant() != null;
		if (ok) {
			matning.setBeraknatVarde(beraknaFunc.apply(matningstyp, matning.getAvlastVarde()));
		}
		matningsloggService.addHandelseBeraknadFromParams(ok, matningstyp, matning.getId(), refnivaNamn);
	}

	private void beraknaFromPrev(Matningstyp matningstyp, MatningEntity matning) {
		var optPrev = getPreviousMatning(matning.getMatningstypId(), matning.getAvlastDatum().minusSeconds(1));
		if (optPrev.isPresent()) {
			var prev = optPrev.get();
			var diffMinutes = diffSeconds(prev.getAvlastDatum(), matning.getAvlastDatum()) / 60d;
			if (prev.getAvlastVarde() != null) {
				var varde = 1000d * (matning.getAvlastVarde() - prev.getAvlastVarde()) / diffMinutes;
				matning.setBeraknatVarde(varde);
			}
		}
		var prevPresent = optPrev.isPresent() && optPrev.get().getAvlastVarde() != null;
		matningsloggService.addHandelseBeraknadFromPrev(prevPresent, matningstyp, matning.getId(), optPrev);
	}

	private void beraknaNivaVattenLufttryck(Matningstyp matningstyp, MatningEntity matning) {
		var optLuft = getLuftMatning(matningstyp, matning.getAvlastDatum());
		var ok = optLuft.isPresent() && matningstyp.getBerakningKonstant() != null; 
		if (ok) {
			var luft = optLuft.get();
			var varde = matningstyp.getBerakningKonstant() + .01 * matning.getAvlastVarde() - .0102 * luft.getAvlastVarde();
			matning.setBeraknatVarde(varde);
		}
		matningsloggService.addHandelseBeraknadFromOther(ok, matningstyp, matning.getId(), optLuft, DefinitionMatningstypConstants.LUFTTRYCK_PER_TIMME);
	}
	
	private Optional<Matning> getPreviousMatning(Integer matningstypId, LocalDateTime avlastDatum) {
		PageRequest pageRequest = PageRequest.of(0, 1, Sort.Direction.DESC, "avlastDatum");
		return matningRepository.findByMatningstypIdAndFelkodAndStatusNotAndAvlastDatumBefore(
				matningstypId,
				Matningsfelkod.OK,
				Matningstatus.FEL,
				avlastDatum,
				pageRequest
		).stream()
				.findFirst()
				.map(Matning::fromEntity);
	}
	
	private Optional<Matning> getLuftMatning(Matningstyp matningstyp, LocalDateTime avlastDatum) {
		var lufttryckDefinition = definitionMatningstypRepository.getDefinitionMatningstypEntityByBerakningstyp(Berakningstyp.LUFTTRYCK_PER_TIMME).orElseThrow();

		return matningRepository.findByOtherMatningstyp(matningstyp, lufttryckDefinition, avlastDatum.minusMinutes(120), avlastDatum.plusMinutes(120)).stream()
			.reduce((m0, m1) -> closest(avlastDatum, m0, m1))
			.map(Matning::fromEntity);			
	}
	
	private MatningEntity closest(LocalDateTime ldt, MatningEntity m0, MatningEntity m1) {
		return diffSeconds(ldt, m0.getAvlastDatum()) < diffSeconds(ldt, m1.getAvlastDatum()) ? m0 : m1;  
	}

	private long diffSeconds(LocalDateTime ldt0, LocalDateTime ldt1) {
		return Math.abs(ChronoUnit.SECONDS.between(ldt0, ldt1));
	}

	private Specification<MatningEntity> getSearchFilter(Integer matningstypId, LocalDateTime fromDatum, LocalDateTime tomDatum, Short status) {
		return (root, query, builder) -> {
			var and = builder.conjunction();
			if (matningstypId != null) {
				and.getExpressions().add(builder.equal(root.get(MatningEntity_.matningstypId), matningstypId));
			}
			if (fromDatum != null && tomDatum != null) {
				and.getExpressions().add(builder.between(root.get(MatningEntity_.avlastDatum), fromDatum, tomDatum));
			} else if (fromDatum != null) {
				and.getExpressions().add(builder.greaterThan(root.get(MatningEntity_.avlastDatum), fromDatum));
			} else if (tomDatum != null) {
				and.getExpressions().add(builder.lessThan(root.get(MatningEntity_.avlastDatum), tomDatum));
			}
			if (status != null) {
				and.getExpressions().add(builder.equal(root.get(MatningEntity_.status), status));
			}
			return and;
		};
	}

	public MatningDataSeries getMatningDataSeries(Integer matningstypId, MatningSearchFilter filter, LocalDate sattningReferensdatum) {

		Record matningInfo = create.select().from(MATOBJEKT)
				.join(MATNINGSTYP).on(MATOBJEKT.ID.eq(MATNINGSTYP.MATOBJEKT_ID))
				.join(DEFINITION_MATNINGSTYP).on(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID.eq(DEFINITION_MATNINGSTYP.ID))
				.where(MATNINGSTYP.ID.eq(matningstypId))
				.fetchOne();

		if (matningInfo == null) {
			throw new NoSuchElementException();
		}

		Double referensValue = null;

		if (Berakningstyp.SATTNING.name().equalsIgnoreCase(matningInfo.get(DEFINITION_MATNINGSTYP.BERAKNINGSTYP))) {
			Condition condition = noCondition();

			condition = condition.and(MATNING.STATUS.notEqual(Matningstatus.FEL));
			condition = condition.and(MATNING.FELKOD.equal(Matningsfelkod.OK));

			if (filter.getFilterGranskade() != null && filter.getFilterGranskade()) {
				condition = condition.and(MATNING.STATUS.notEqual(Matningstatus.EJGRANSKAT));
			}

			if (sattningReferensdatum != null) {
				LocalDateTime referensFrom = sattningReferensdatum.atTime(23, 59);

				MatningRecord referensRecord = create.selectFrom(MATNING)
						.where(MATNING.MATNINGSTYP_ID.eq(matningstypId))
						.and(condition)
						.and(MATNING.AVLAST_DATUM.lt(referensFrom))
						.orderBy(MATNING.AVLAST_DATUM.desc())
						.limit(1)
						.fetchOne();

				if (referensRecord != null) {
					referensValue = referensRecord.getAvlastVarde();
				}
			}

			if (referensValue == null) {
				MatningRecord referensRecord = create.selectFrom(MATNING)
						.where(MATNING.MATNINGSTYP_ID.eq(matningstypId))
						.and(condition)
						.orderBy(MATNING.AVLAST_DATUM.asc())
						.limit(1)
						.fetchOne();

				if (referensRecord != null) {
					referensValue = referensRecord.getAvlastVarde();
				}
			}
		}

		MatningDataSeries dataSeries = new MatningDataSeries(matningInfo);

		Condition condition = noCondition();
		if (filter.getFilterGranskade() != null && filter.getFilterGranskade()) {
			condition = condition.and(MATNING.STATUS.notEqual(Matningstatus.EJGRANSKAT));
		}

		if (filter.getFilterFelkodOk() != null && filter.getFilterFelkodOk()) {
			condition = condition.and(MATNING.FELKOD.equal(Matningsfelkod.OK));
		}

		if (filter.getStatus() != null) {
			condition = condition.and(MATNING.STATUS.eq(filter.getStatus()));
		}

		if (filter.getFrom() != null) {
			condition = condition.and(MATNING.AVLAST_DATUM.gt(filter.getFrom()));
		}

		Result<MatningRecord> matningar = create.selectFrom(MATNING)
				.where(MATNING.MATNINGSTYP_ID.eq(matningstypId))
				.and(condition)
				.orderBy(MATNING.AVLAST_DATUM)
				.fetch();

		dataSeries.setData(matningar, referensValue);

		return dataSeries;
	}
}

