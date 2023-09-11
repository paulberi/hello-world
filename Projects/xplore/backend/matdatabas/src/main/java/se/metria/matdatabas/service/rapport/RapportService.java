package se.metria.matdatabas.service.rapport;

import org.jooq.DSLContext;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.matdatabas.openapi.model.GransvardeDto;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandare.AnvandareService;
import se.metria.matdatabas.service.anvandare.exception.AnvandareNotFoundException;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.dto.MatningDataSeries;
import se.metria.matdatabas.service.matning.query.MatningSearchFilter;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.query.MatningstypSearchFilter;
import se.metria.matdatabas.service.rapport.dto.*;
import se.metria.matdatabas.service.rapport.entity.*;
import se.metria.matdatabas.service.rapport.exception.*;
import se.metria.matdatabas.service.systemlogg.SystemloggService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.groupConcat;
import static se.metria.matdatabas.db.tables.Rapport.RAPPORT;
import static se.metria.matdatabas.db.tables.RapportMottagare.RAPPORT_MOTTAGARE;
import static se.metria.matdatabas.service.matobjekt.MatobjektConstants.TYP_VADERSTATION;

@Service
public class RapportService {
	final private DSLContext create;
    private MatningService matningService;
    private GransvardeService gransvardeService;
    private MatningstypService matningstypService;
    private SystemloggService systemloggService;
    private RapportRepository rapportRepository;
    private ModelMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(RapportJob.class);

    static Converter<RapportGrafGransvardeEntity, Integer> gransvardeConverter = new AbstractConverter<RapportGrafGransvardeEntity, Integer>() {
        protected Integer convert(RapportGrafGransvardeEntity source) {
            return source.getGransvardeId();
        }
    };

    static Converter<RapportGrafMatningstypEntity, Integer> matningstypConverter = new AbstractConverter<RapportGrafMatningstypEntity, Integer>() {
        protected Integer convert(RapportGrafMatningstypEntity source) {
            return source.getMatningstypId();
        }
    };

    public RapportService(DSLContext create,
                          MatningService matningService,
                          GransvardeService gransvardeService,
                          MatningstypService matningstypService,
                          SystemloggService systemloggService,
                          RapportRepository repository,
                          ModelMapper mapper)
    {
        this.create = create;
        this.matningService = matningService;
        this.gransvardeService = gransvardeService;
        this.matningstypService = matningstypService;
        this.systemloggService = systemloggService;
        this.rapportRepository = repository;
        this.mapper = mapper;
        mapper.addConverter(gransvardeConverter);
        mapper.addConverter(matningstypConverter);
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public RapportSettings createRapportSettings(RapportSettings rapportSettings)
        throws RapportSettingsErrorException
    {
        try {
            validateRapportNamn(rapportSettings);
            validateMottagare(rapportSettings.getRapportMottagare());
            validateGrafer(rapportSettings.getRapportGraf());
            validateDataperiod(rapportSettings.getDataperiodFrom());

            var entity = new RapportSettingsEntity(rapportSettings);
            entity = rapportRepository.saveAndFlush(entity);

            systemloggService.addHandelseRapportCreated(entity.getId(), rapportSettings.getNamn());

            return mapper.map(entity, RapportSettings.class);
        } catch (RapportSettingsErrorException e) {
            logger.error("Error creating rapport settings " + rapportSettings.getNamn() + ": " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public RapportSettings updateRapportSettings(Integer rapportId, RapportSettings rapportSettings)
        throws RapportSettingsErrorException
    {
        try {
            var entity = rapportRepository.getOne(rapportId);

            validateMottagare(rapportSettings.getRapportMottagare());

            if (!entity.getNamn().equals(rapportSettings.getNamn())) {
                validateRapportNamn(rapportSettings);
            }

            validateGrafer(rapportSettings.getRapportGraf());
            validateDataperiod(rapportSettings.getDataperiodFrom());

            // Se till att senastSkickad bara kan ändrar från servern
            // en rapport kan ha gått ut medans rapporten ändras
            rapportSettings.setSenastSkickad(entity.getSenastSkickad());

            entity.update(rapportSettings);
            entity = rapportRepository.saveAndFlush(entity);

            systemloggService.addHandelseRapportModified(rapportId, rapportSettings.getNamn());

            return mapper.map(entity, RapportSettings.class);
        } catch (Exception e) {
            logger.error("Error modifying rapport settings #" + rapportSettings.getId() + " " +
                    rapportSettings.getNamn() + ": " + e.getMessage());
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public RapportSettings getRapportSettings(Integer rapportId) {
        final var entity = rapportRepository.getOne(rapportId);
        return new RapportSettings(entity);
    }

    @Transactional(readOnly = true)
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public Rapport getRapport(Integer rapportId) throws RapportSettingsErrorException, AnvandareNotFoundException {
        try {
            final var rapportSettings = getRapportSettings(rapportId);
            return RapportFromSettings(rapportSettings);
        } catch (Exception e) {
            logger.error("Error getting rapport #" + rapportId + ": " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    @RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
    public boolean deleteRapportSettings(Integer rapportId) {
        try {
            final var name = rapportRepository.getOne(rapportId).getNamn();
            rapportRepository.deleteById(rapportId);

            systemloggService.addHandelseRapportRemoved(rapportId, name);

            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    public boolean rapportNamnExists(String namn) {
        return rapportRepository.existsByNamn(namn);
    }

    public List<DueReport> getDueReports() {
        return create.select(RAPPORT.ID, RAPPORT.NAMN, RAPPORT.MEJLMEDDELANDE, RAPPORT.SENAST_SKICKAD, RAPPORT.TIDSINTERVALL, groupConcat(RAPPORT_MOTTAGARE.EPOST).as("mottagare")).from(RAPPORT)
                .join(RAPPORT_MOTTAGARE).on(RAPPORT_MOTTAGARE.RAPPORT_ID.eq(RAPPORT.ID))
                .where(RAPPORT.AKTIV.isTrue()).and(RAPPORT.STARTDATUM.lessOrEqual(LocalDateTime.now()))
                .groupBy(RAPPORT.ID, RAPPORT.NAMN, RAPPORT.TIDSINTERVALL, RAPPORT.SENAST_SKICKAD)
                .fetch()
                .map(r -> {
                    var mottagare = (String) r.get("mottagare");
                    return new DueReport(r.get(RAPPORT.ID), r.get(RAPPORT.NAMN), r.get(RAPPORT.MEJLMEDDELANDE), r.get(RAPPORT.SENAST_SKICKAD), r.get(RAPPORT.TIDSINTERVALL), mottagare.split(","));
                });
    }

    public void updateReportStatus(Integer reportId) {
        create.update(RAPPORT).set(RAPPORT.SENAST_SKICKAD, LocalDateTime.now())
                .where(RAPPORT.ID.eq(reportId))
                .execute();
    }

    void validateRapportNamn(RapportSettings rapportSettings) throws RapportSettingsErrorException {
        String rapportNamn = rapportSettings.getNamn();
        if (rapportNamnExists(rapportNamn)) {
            throw new RapportSettingsErrorException("Rapportnamn already exists: " + rapportNamn);
        }
    }

    void validateMottagare(List<RapportMottagare> rapportMottagare) throws RapportSettingsErrorException {
        Set<RapportMottagare> mottagareSet = new HashSet<>();
        List<RapportMottagare> duplicatedMottagare = new ArrayList<>();

        for (final var mottagare: rapportMottagare) {
            if (!mottagareSet.add(mottagare)) {
                duplicatedMottagare.add(mottagare);
            }
        }

        if (duplicatedMottagare.size() > 0) {
            final var mottagareList = duplicatedMottagare.stream().collect(Collectors.toList());
            throw new RapportSettingsErrorException("Duplicate rapportmottagare: " + mottagareList.toString());
        }
    }

    void validateGrafer(List<RapportGrafSettings> grafer) throws RapportSettingsErrorException {
        if (grafer.size() == 0) {
            throw new RapportSettingsErrorException("Rapport contains no graphs");
        }

        for (int i = 0; i < grafer.size(); i++) {
            final var graf = grafer.get(i);
            if (graf.getRubrik() == null || graf.getRubrik() == "") {
                throw new RapportSettingsErrorException("Missing name for graph #" + i);
            } else if (graf.getMatningstyper().size() == 0 && graf.getGransvarden().size() == 0) {
                throw new RapportSettingsErrorException("Empty graph: " + graf.getRubrik());
            }
        }
    }

    void validateDataperiod(String dataperiodFrom) throws RapportSettingsErrorException {
        switch (dataperiodFrom) {
            case RapportConstants.ALL:
            case RapportConstants.MONTHS_6:
            case RapportConstants.YEARS_1:
            case RapportConstants.YEARS_2:
            case RapportConstants.YEARS_5:
            case RapportConstants.YEARS_10:
                return;
            default:
                throw new RapportSettingsErrorException("Invalid dataperiod: " + dataperiodFrom);
        }
    }

    Rapport RapportFromSettings(RapportSettings rapportSettings)
            throws RapportSettingsErrorException, AnvandareNotFoundException
    {
        Rapport rapport = new Rapport();
        rapport.setRubrik(rapportSettings.getInledningRubrik());
        rapport.setInformation(rapportSettings.getInledningInformation());
        rapport.setLagesbildId(rapportSettings.getLagesbild());

        List<RapportGraf> rapportGrafer = new ArrayList<>();
        for (final var grafSettings: rapportSettings.getRapportGraf()) {
            final var rorelsereferensdatum = rapportSettings.getRorelsereferensdatum() == null ?
                    null : rapportSettings.getRorelsereferensdatum().toLocalDate();
            final var from = dataPeriodDateTime(rapportSettings.getDataperiodFrom());

            rapportGrafer.add(rapportGraf(grafSettings, from, rorelsereferensdatum));
        }

        rapport.setGrafer(rapportGrafer);

        return rapport;
    }

    LocalDateTime dataPeriodDateTime(String dataperiodFrom) throws RapportSettingsErrorException {
        LocalDateTime now = LocalDateTime.now();

        switch (dataperiodFrom) {
            case RapportConstants.MONTHS_6:
                return now.minusMonths(6);
            case RapportConstants.YEARS_1:
                return now.minusYears(1);
            case RapportConstants.YEARS_2:
                return now.minusYears(2);
            case RapportConstants.YEARS_5:
                return now.minusYears(5);
            case RapportConstants.YEARS_10:
                return now.minusYears(10);
            case RapportConstants.ALL:
                return null;
            default:
                throw new RapportSettingsErrorException("Dataperiod f.r.o.m. invalid: " + dataperiodFrom);
        }
    }

    RapportGraf rapportGraf(RapportGrafSettings rapportGrafSettings, LocalDateTime from, LocalDate sattningReferensdatum)
            throws RapportSettingsErrorException
    {
        RapportGraf rapportGraf = new RapportGraf();
        rapportGraf.setRubrik(rapportGrafSettings.getRubrik());
        rapportGraf.setInformation(rapportGrafSettings.getInfo());

        rapportGraf.setMatningar(matningstyperFromIds(
            rapportGrafSettings.getMatningstyper()
                               .stream()
                               .filter(id -> !matningstypMatobjektIsType(id, TYP_VADERSTATION))
                               .collect(Collectors.toList()),
            from, sattningReferensdatum
        ));

        rapportGraf.setReferensdata(matningstyperFromIds(
            rapportGrafSettings.getMatningstyper()
                               .stream()
                               .filter(id -> matningstypMatobjektIsType(id, TYP_VADERSTATION))
                               .collect(Collectors.toList()),
            from, sattningReferensdatum
        ));

        rapportGraf.setGransvarden(gransvardenFromIds(
            rapportGrafSettings.getGransvarden()
        ));

        return rapportGraf;
    }

    /* I was shocked and appalled, and quite frankly disgusted, by Java 8 streams not being able to handle checked
    exceptions in a satisfying manner. This seemed like the least worst solution to me. */
    List<MatningDataSeries> matningstyperFromIds(List<Integer> matningstypIds, LocalDateTime from, LocalDate sattningReferensdatum)
            throws RapportSettingsErrorException {
        List<MatningDataSeries> matningstyper = new ArrayList<>();
        for (final var id: matningstypIds) {
            matningstyper.add(matningDataSeries(id, from, sattningReferensdatum));
        }

        return matningstyper;
    }

    List<GransvardeDto> gransvardenFromIds(List<Integer> gransvardeIds) throws RapportSettingsErrorException {
        List<GransvardeDto> gransvarden = new ArrayList<>();
        for (final var id: gransvardeIds) {
            gransvarden.add(gransvarde(id));
        }

        return gransvarden;
    }

    MatningDataSeries matningDataSeries(Integer matningstypId, LocalDateTime from,
                                        LocalDate sattningReferensdatum)
            throws RapportSettingsErrorException {
        try {
            final var filter = MatningSearchFilter.builder()
                    .status(Matningstatus.GODKANT)
                    .filterFelkodOk(true)
                    .from(from)
                    .build();
            return matningService.getMatningDataSeries(matningstypId, filter, sattningReferensdatum);
        } catch (NoSuchElementException e) {
            throw new RapportSettingsErrorException("Mätserie with id:" + matningstypId + " not found");
        }
    }

    GransvardeDto gransvarde(Integer gransvardeId) throws RapportSettingsErrorException {
        try {
            return gransvardeService.findById(gransvardeId).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new RapportSettingsErrorException("Gränsvärde with id:" + gransvardeId + " not found");
        }
    }

    boolean matningstypMatobjektIsType(Integer matningstypId, Short matobjektType) {
        var filter = new MatningstypSearchFilter();
        filter.setIncludeIds(Arrays.asList(new Integer[]{ matningstypId }));
        filter.setMatobjektTyp(matobjektType);
        return matningstypService.getMatningstypMatobjektList(filter).size() > 0;
    }

    public Page<ListRapport> getRapporter(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
        var pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);

        return rapportRepository.findAll(pageRequest).map(ListRapport::fromEntity);
    }
}
