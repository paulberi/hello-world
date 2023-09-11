package se.metria.matdatabas.service.inrapportering;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import se.metria.matdatabas.kolibri.api.MeasurementsApi;
import se.metria.matdatabas.kolibri.model.MeasurementValuesListViewModelKolibri;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.exception.DefinitionMatningstypNotFoundException;
import se.metria.matdatabas.service.inrapportering.exception.KolibriDataNotFoundException;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.matning.AlreadyGodkandException;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.MatningSaveResult;
import se.metria.matdatabas.service.matning.dto.SaveMatning;
import se.metria.matdatabas.service.matning.exception.MatningIllegalMatvarde;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektService;
import se.metria.matdatabas.service.scheduler.JobServicetyper;
import se.metria.matdatabas.service.scheduler.Jobstatus;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static se.metria.matdatabas.db.tables.DefinitionMatningstyp.DEFINITION_MATNINGSTYP;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;
import static se.metria.matdatabas.service.inrapportering.ImportFel.*;


@Service
public class ImportKolibriService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final static String RAPPORTOR = "Kolibri";
	private final static Integer P1 = 2;
	private final static Integer PBaro = 7;

	final double BAR_TO_HPA = 1e3;
	final double BAR_TO_PA = 1e5;
	final double M_TO_CM = 1e2;
	final double DENSITY = 999.9; // kg/m3
	final double GRAVITY = 9.80655; // m/s2

	private MatningService matningService;
	private MatobjektService matobjektService;
	private SystemloggService systemloggService;
	private DefinitionMatningstypService definitionMatningstypService;
	private MeasurementsApi kolibriMeasurmentsApi;
	private DSLContext create;

	public ImportKolibriService(MatningService matningService,
								MatobjektService matobjektService,
								SystemloggService systemloggService,
								MeasurementsApi kolibriMeasurmentsApi,
								DefinitionMatningstypService definitionMatningstypService,
								DSLContext create) {
		this.matningService = matningService;
		this.matobjektService = matobjektService;
		this.systemloggService = systemloggService;
		this.definitionMatningstypService = definitionMatningstypService;
		this.kolibriMeasurmentsApi = kolibriMeasurmentsApi;
		this.create = create;
	}

	public Short importKolibri(LocalDateTime fromDate, LocalDateTime toDate) {
		Short status = Jobstatus.FAILED;
		try {
			List<Matningstyp> matningstyper = getMatningstyperWithInstrument();
			if (matningstyper.isEmpty()) {
				logger.info("Kolibri import: Inga mätningstyper konfigurerade.");
				return Jobstatus.ABORTED;
			}

			logger.debug("Kolibri import from " + fromDate.toString() + " to " + toDate.toString());
			List<MatningSaveResult> matningar = importMatningarFromAllDevices(fromDate, toDate);
			systemloggService.addHandelseImportDone(JobServicetyper.IMPORT_KOLIBRI, matningar.size());
			status = Jobstatus.OK;
		} catch (DefinitionMatningstypNotFoundException e) {
			systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_MATNINGSTYP);
			logger.error("Kolibri import: Ingen mätningstyp uppsatt", e);
		} catch (WebClientResponseException e) {
			systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_INLASNING);
			logger.error("Kolibri import:", e);
		} catch (HttpClientErrorException e) {
			systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_INLASNING);
			logger.error("Error fetching measurements from kolibri: " + e.getMessage());
		} catch (Exception e) {
			systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_OKANT);
			logger.error("Unknown error: " + e.getMessage());
			e.printStackTrace();
		}

		return status;
	}


	public List<Matningstyp> getMatningstyperWithInstrument() throws DefinitionMatningstypNotFoundException {
		Integer lufttryckDefinitionId = definitionMatningstypService.findByBerakningstyp(Berakningstyp.LUFTTRYCK_PER_TIMME).getId();

		List<Matningstyp> matningstypList = create.select(MATNINGSTYP.asterisk(), DEFINITION_MATNINGSTYP.NAMN.as("typ"))
				.from(MATNINGSTYP)
				.innerJoin(DEFINITION_MATNINGSTYP).on(MATNINGSTYP.DEFINITION_MATNINGSTYP_ID.eq(DEFINITION_MATNINGSTYP.ID))
				.where(MATNINGSTYP.AKTIV)
				.and(MATNINGSTYP.INSTRUMENT.isNotNull())
				.and(MATNINGSTYP.INSTRUMENT.isDistinctFrom(""))
				.fetchInto(Matningstyp.class);

		// Put lufttryck first in list. For computations of PBaro the values must be retrieved in this particular order.
		matningstypList.sort((o1, o2) -> {
			if (o1.getDefinitionMatningstypId().equals(o2.getDefinitionMatningstypId())) {
				return 0;
			} else if (o1.getDefinitionMatningstypId().equals(lufttryckDefinitionId)) {
				return -1;
			} else if (o2.getDefinitionMatningstypId().equals(lufttryckDefinitionId)) {
				return 1;
			} else {
				return o1.getDefinitionMatningstypId().compareTo(o2.getDefinitionMatningstypId());
			}
		});

		return matningstypList;
	}

	private MeasurementValuesListViewModelKolibri getMeasurementsFromKolibri(Matningstyp matningstyp, LocalDateTime fromDate, LocalDateTime toDate,
																			 Integer measurementDefinitionId) throws HttpClientErrorException  {
		Integer deviceId;
		try {
			deviceId = Integer.parseInt(matningstyp.getInstrument());
		} catch (NumberFormatException e) {
			throw new KolibriDataNotFoundException("Bad format in deviceId");
		}

		try {
			final var measurements = kolibriMeasurmentsApi.measurementsGet(measurementDefinitionId, deviceId, fromDate,
					toDate, false, "").block();
			logger.debug(
					"import from kolibri: #measurements:" + measurements.getValues().size() +
					" deviceId:" + measurements.getDeviceId() +
					" measurementDefinition" + measurements.getMeasurementDefinitionId());
			measurements.getValues().forEach(v -> logger.debug("measurement: value:" + v.getValue() + " time:" + v.getTime()));
			return measurements;
		} catch (WebClientResponseException e) {
			if (e.getRawStatusCode()==500) {
				throw new KolibriDataNotFoundException("500 from kolibri");
			} else {
				throw e;
			}
		}
	}

	private List<MatningSaveResult> saveMeasurements(Matningstyp matningstyp, MeasurementValuesListViewModelKolibri measurements)
			throws DefinitionMatningstypNotFoundException {
		List<MatningSaveResult> imports = new ArrayList<MatningSaveResult>();

		Map<Integer, Integer> matningsDefinitionMap = new HashMap<>();
		matningsDefinitionMap.put(definitionMatningstypService.findByBerakningstyp(Berakningstyp.NIVA_VATTEN_LUFTTRYCK).getId(), P1);
		matningsDefinitionMap.put(definitionMatningstypService.findByBerakningstyp(Berakningstyp.LUFTTRYCK_PER_TIMME).getId(), PBaro);

		Integer deviceId = Integer.parseInt(matningstyp.getInstrument());
		Integer measurementDefinitionId =  matningsDefinitionMap.get(matningstyp.getDefinitionMatningstypId());
		String kommentar = "DeviceId: " +
				measurements.getDeviceId() +
				" MeasurementDefinitionId: " +
				measurements.getMeasurementDefinitionId();
		List<SaveMatning> saveMatningar = measurements.getValues().stream().map(varde ->
				SaveMatning.builder()
						.avlastVarde(varde.getValue())
						.avlastDatum(UTCtoLocalTime(varde.getTime()))
						.rapportor(RAPPORTOR)
						.kommentar(kommentar)
						.build()).collect(Collectors.toList());
		saveMatningar.forEach(saveMatning -> {
			try {
				MatningSaveResult matning = matningService.create(matningstyp, saveMatning, true, StandardKallsystem.Kolibri.getNamn());
				logger.debug(
						"saved kolibri matning:" + kommentar +  " value:" + matning.getMatning().getAvlastVarde() + " date:" + matning.getMatning().getAvlastDatum());
				imports.add(matning);
			} catch (MatningIllegalMatvarde matningIllegalMatvarde) {
				logger.warn("Kolibri import: IllegalMatvarde DeviceId: " + deviceId +
						" MeasuremetDefinitionId: " + measurementDefinitionId +
						" Value: " + saveMatning.getAvlastVarde());
			} catch (AlreadyGodkandException e) {
				if (e.getMatningPrevious().getAvlastVarde().doubleValue() != e.getMatningCurrent().getAvlastVarde().doubleValue()) {
					systemloggService.addHandelseImportFailed(JobServicetyper.IMPORT_KOLIBRI, IMPORT_FEL_GODKAND);
					logger.error(alreadyGodkandExceptionText(e));
				}
			}
		});

		return imports;
	}

	private LocalDateTime UTCtoLocalTime(LocalDateTime utc) {
		return utc.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
	}

	private Optional<Matningstyp> findMatchingMatningstyp(Matningstyp matningstyp, List<Matningstyp> matningstypList, String matningstypToFind) {
		Integer matobjektId = matningstyp.getMatobjektId();

		return matningstypList.stream()
				.filter(mtyp -> {
					return mtyp.getMatobjektId().equals(matobjektId) && mtyp.getTyp().equals(matningstypToFind);
				})
				.findFirst();
	}

	private MeasurementValuesListViewModelKolibri convertMeasurementUnitsPBaro(Matningstyp matningstypP2, LocalDateTime fromDate,
																			   LocalDateTime toDate) {
		var measurementsP2 = getMeasurementsFromKolibri(matningstypP2, fromDate, toDate, PBaro);
		measurementsP2.getValues().forEach(m -> m.setValue(m.getValue() * BAR_TO_HPA));

		return measurementsP2;
	}

	private MeasurementValuesListViewModelKolibri convertMeasurementUnitsP1(Matningstyp matningstypP1, LocalDateTime fromDate,
																			LocalDateTime toDate) {
		var measurementsP1 = getMeasurementsFromKolibri(matningstypP1, fromDate, toDate, P1);
		// Bortse från lufttryck
		final double LUFTTRYCK = 1. * BAR_TO_PA;

		for (int i = 0; i < measurementsP1.getValues().size(); i++) {
			final var vattentryck = measurementsP1.getValues().get(i).getValue() * BAR_TO_PA;

			final var mvp = (vattentryck - LUFTTRYCK)/ (DENSITY * GRAVITY);

			// cmvp
			measurementsP1.getValues().get(i).setValue(mvp * M_TO_CM);
		}

		return measurementsP1;
	}

	private Optional<MeasurementValuesListViewModelKolibri> getMeasurementsForMatningstyp(Matningstyp matningstyp, LocalDateTime fromDate,
																						  LocalDateTime toDate)
	throws MeasurementConversionException {
		var definitionmatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

		if (definitionmatningstyp.getBerakningstyp() != null) {
			switch (definitionmatningstyp.getBerakningstyp()) {
				case LUFTTRYCK_PER_TIMME:
					return Optional.of(convertMeasurementUnitsPBaro(matningstyp, fromDate, toDate));
				case NIVA_VATTEN_LUFTTRYCK:
					return Optional.of(convertMeasurementUnitsP1(matningstyp, fromDate, toDate));
				default:
					logger.debug("Mätningstyp hanteras inte av Kolibri: " + matningstyp.getTyp());
					return Optional.empty();
			}
		} else {
			logger.debug("Mätningstyp hanteras inte av Kolibri: " + matningstyp.getTyp());
			return Optional.empty();
		}
	}

	private List<MatningSaveResult> importMatningarFromAllDevices(LocalDateTime fromDate, LocalDateTime toDate)
			throws DefinitionMatningstypNotFoundException {
		List<Matningstyp> matningstypList = getMatningstyperWithInstrument();

		List<MatningSaveResult> matningar = new ArrayList<MatningSaveResult>();
		for (Matningstyp matningstyp : matningstypList) {
			try {
				var measurements = getMeasurementsForMatningstyp(matningstyp, fromDate, toDate);
				if (measurements.isPresent()) {
					matningar.addAll(saveMeasurements(matningstyp, measurements.get()));
				}
			} catch (MeasurementConversionException e) {
				logger.error("Fel inträffade vid enhetskonvertering av matningstypid: "+matningstyp.getId()+" Fel: " + e.getMessage());
			} catch (KolibriDataNotFoundException e) {
				logger.error("Hittade inte data i kolibri av matningstypid: "+matningstyp.getId()+" Fel: " + e.getMessage());
			}
		}

		return matningar;
	}

	private String alreadyGodkandExceptionText(AlreadyGodkandException godkandException) {
		final var previous = godkandException.getMatningPrevious();
		final var current = godkandException.getMatningCurrent();

		try {
			return String.format("Attempted to overwrite previously approved measurement: Previous: %s Current: %s Mätobjekt id=%d",
					previous.toString(), current.toString(), matobjektService.getMatobjekt(previous.getId().intValue()).getId());
		} catch (Exception e) {
			return String.format("Attempted to overwrite previously approved measurement: Previous: %s Current: %s Mätobjekt id=N/A",
					previous.toString(), current.toString());
		}
	}
}
