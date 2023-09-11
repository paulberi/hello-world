package se.metria.markkoll.service.fastighet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.FastighetsforteckningRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.IntrangRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.common.XpSearchService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static se.metria.markkoll.util.CollectionUtil.modelMapperList;

@Service
@RequiredArgsConstructor
@Slf4j
public class FastighetService {
    @PersistenceContext
    private final EntityManager entityManager;

    @NonNull
    private final OmradesintrangRepository omradesintrangRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final FastighetOmradeRepository fastighetOmradeRepository;

    @NonNull
    private final FastighetsintrangRepository fastighetsintrangRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final MarkagareService markagareService;

    @NonNull
    private final SamfService samfService;

    @NonNull
    private final GeometristatusRepository geometristatusRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final AvtalsloggService avtalsloggService;

    @NonNull
    private final RegisterenhetImportService registerenhetImportService;

    @NonNull
    private final FastighetsforteckningRepository fastighetsforteckningRepository;

    @NonNull
    private final XpSearchService xpSearchService;

    @NonNull
    private final IntrangRepository intrangRepository;

    public FastighetPageDto
    fastighetPage(UUID projektId, int page, int size, FastighetsfilterDto fastighetsFilter)
    {
        var pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "fastighetsbeteckning");

        var pageEntity = fastighetRepository.fastighetPageFiltered(projektId, pageRequest,
                fastighetsFilter);

        for (var fastighetDto: pageEntity) {
            setLabels(fastighetDto, projektId);
        }

        return modelMapper.map(pageEntity, FastighetPageDto.class);
    }

    public FastighetPageDto
    samfallighetPage(UUID projektId, int page, int size, FastighetsfilterDto fastighetsFilter)
    {
        var pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "fastighetsbeteckning");

        var pageEntity = fastighetRepository.samfallighetPageFiltered(projektId, pageRequest,
                fastighetsFilter);

        for (var samfDto: pageEntity) {
            setLabels(samfDto, projektId);
        }

        return modelMapper.map(pageEntity, FastighetPageDto.class);
    }

    public boolean
    hasAvvikelse(UUID fastighetId, UUID projektId) {
        return markagareService.getAgareForFastighet(projektId, fastighetId)
                .stream()
                .filter(ag -> ag.getAgartyp() != AgartypDto.OMBUD)
                .map(ag -> ag.getLabels())
                .anyMatch(lbls -> lbls.getOfullstandingInformation() == true);
    }

    public FastighetDto getFastighet(UUID projektId, UUID fastighetId) {
        var fastighetEntity = fastighetRepository.findById(fastighetId)
                .orElseThrow(EntityNotFoundException::new);

        var fastighetDto = modelMapper.map(fastighetEntity, FastighetDto.class);

        setLabels(fastighetDto, projektId);

        return fastighetDto;
    }

    public FastighetDto getFastighet(UUID avtalId) {
        var projektId = avtalRepository.getProjektId(avtalId);
        var fastighetId = avtalRepository.getFastighetId(avtalId);

        return getFastighet(projektId, fastighetId);
    }

    public List<FastighetDto> getFastigheter(UUID projektId) {
        var entities = fastighetRepository.getFastigheter(projektId);

        return CollectionUtil.modelMapperList(entities, modelMapper, FastighetDto.class);
    }

    public FastighetsinfoDto fastighetsinfo(UUID avtalId) {
        var fastighetId = avtalRepository.getFastighetId(avtalId);
        var projektId = avtalRepository.getProjektId(avtalId);

        return fastighetsinfo(fastighetId, projektId);
    }

    @Transactional
    @Deprecated
    public FastighetsinfoDto fastighetsinfo(UUID fastighetId, UUID projektId) {
        log.info("Hämtar fastighetsinformation för fastighet {} i projekt {}", fastighetId, projektId);

        var agareList = markagareService.getAgareForFastighet(projektId, fastighetId);
        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, fastighetId);

        var omraden = delomradeskartor(projektId, fastighetId);

        var fastighet = fastighetRepository.findById(fastighetId).orElseThrow(() ->
                new MarkkollException(MarkkollError.FASTIGHET_ERROR)
        );

        // Om det finns delområden slå ihop deras extent, annars använd fastighetens extent
        Envelope extent;
        if (omraden.size() > 0) {
            extent = fastighetDelomradeExtent(omraden);
        } else {
            extent = fastighetRepository.getExtent(fastighetId).orElseThrow().getEnvelopeInternal();
        }
        // Lägg på en liten buffert för att få med angränsande fastigheter
        extent.expandBy(10);

        var intrang = fastighetRepository.findIntrangInfoForFastighet(fastighetId, projektId);
        var ersattning = fastighetRepository.getErsattningForFastighet(fastighetId, projektId);
        var anteckning = fastighetRepository.getAnteckningForFastighet(fastighetId, projektId);
        var avtalsLogg = avtalsloggService.getAvtalslogg(projektId, fastighetId);
        var trakt = fastighetRepository.getOne(fastighetId).getTrakt();
        var blockenhet = fastighetRepository.getOne(fastighetId).getBlockenhet();
        var kommunnamn = fastighetRepository.getOne(fastighetId).getKommunnamn();

        var avtal = avtalRepository.getOne(avtalId);

        var agareSaknas = avtalRepository.agareSaknas(projektId, fastighetId);

        var samfallighetDto = (fastighet.getDetaljtyp() == DetaljtypDto.SAMF) ?
            samfService.getSamf(projektRepository.getKundId(projektId), fastighetId) : null;

        return new FastighetsinfoDto()
                .id(fastighetId)
                .fastighetsbeteckning(trakt + " " + blockenhet)
                .detaljtyp(fastighet.getDetaljtyp())
                .agare(agareList)
                .intrang(intrang)
                .ersattning(ersattning)
                .omraden(omraden)
                .kommunnamn(kommunnamn)
                .lan(fastighet.getLan())
                .anteckning(anteckning)
                .avtalsLogg(avtalsLogg)
                .fastighetsbeteckning(fastighet.getFastighetsbeteckning())
                .extent(List.of(
                        extent.getMinX(),
                        extent.getMinY(),
                        extent.getMaxX(),
                        extent.getMaxY()))
                .skogsfastighet(avtal.getSkogsfastighet())
                .tillvaratagandeTyp(avtal.getTillvaratagandeTyp())
                .markslag(avtal.getMetadata().getMarkslag())
                .matandeStation(avtal.getMetadata().getMatandeStation())
                .stationsnamn(avtal.getMetadata().getStationsnamn())
                .franStation(avtal.getMetadata().getFranStation())
                .tillStation(avtal.getMetadata().getTillStation())
                .egetTillvaratagande(avtal.getEgetTillvaratagande())
                .hasAnteckningar(!CollectionUtil.isNullOrEmpty(avtal.getAnteckning()))
                .avtalsstatus(avtal.getAvtalsstatus())
                .agareSaknas(agareSaknas)
                .avvikelse(!agareSaknas && this.hasAvvikelse(fastighetId, projektId))
                .samfallighet(samfallighetDto);
    }

    public Integer setFastighetErsattning(UUID fastighetId, UUID projektId, FastighetsProjektInfoDto info)
    {
        fastighetRepository.setFastighetsInfoForProjekt(fastighetId, projektId, info);
        log.info("Intrångsinformation för fastighet {} i projekt {} uppdaterad", fastighetId, projektId);

        return info.getErsattning();
    }

    @Transactional
    public List<UUID> fastigheterFromVersion(VersionDto version) {
        var intrangEntities = intrangRepository.findAllByVersionId(version.getId());
        var groups = getGeometryGroupsFromIntrang(intrangEntities);

        var fastighetIds = fastigheterFromIntrang(groups, version.getBuffert());

        /* Klippverktyget för intrång på frontend förväntar sig LineStrings. Många MultiLineStrings går att förenkla
        * till en LineString, så vi gör den konverteringen där det är möjligt. */
        omradesintrangRepository.mergeLines();

        omradesintrangRepository.saveAllOmradesintrang(fastighetIds, version.getId());
        fastighetsintrangRepository.saveAllFastighetsintrang(fastighetIds, version.getId());

        return fastighetIds;
    }

    private List<UUID> fastigheterFromIntrang(Map<String, List<Geometry>> intrang, Double buffert) {
        var registerenheter = new HashMap<DetaljtypDto, Set<UUID>>();

        intrang.forEach((typeToUse, geometries) -> {
            if (typeToUse.equals(IntrangstypDto.OKAND.toString())) {
                // Oklart varför den här grenen överhuvudtaget fanns med...
                log.error("{} intrång av okänd typ, vågar inte importera registerenheter för dessa!", geometries.size());
            } else {
                log.info("Importerar fastigheter från {} intrång av typ {}", geometries.size(), typeToUse);
                registerenhetImportService.importRegisterenheter(geometries, buffert).forEach((detaljtyp, ids) ->
                        registerenheter.computeIfAbsent(detaljtyp, s -> new HashSet<>())
                                .addAll(ids));
            }
        });

        entityManager.flush();
        entityManager.clear();  // Förhindra onödiga isDirty-checkar, vi är klara med alla FastighetEntitys nu
        registerenheter.forEach((key, value) -> log.info("{} {} importerade", value.size(), key));

        // Ladda databasen med delägande fastigheter (sidoeffekt från samfService.getSamfLista())
        var samfIDs = registerenheter.entrySet().stream()
                .filter(p -> p.getKey() == DetaljtypDto.SAMF) // Ej SAMFO, finns inte i registret
                .map(Map.Entry::getValue)
                .flatMap(Set::stream)
                .collect(toSet());
        log.info("Påbörjar import av delägande fastigheter för {} samfälligheter", samfIDs.size());
        fastighetRepository.findAllById(samfIDs).forEach(this.samfService::getSamfLista);
        log.info("Import av delägande fastigheter klar.");

        return registerenheter.values().stream().flatMap(Set::stream).collect(Collectors.toList());
    }

    public List<FastighetOmradeEntity> getDelomradenForFastigheter(List<UUID> fastighetIds) {
        return fastighetOmradeRepository.findAllByFastighetIdIn(fastighetIds);
    }

    public List<FastighetDelomradeInfoDto> fetchDelomradenForFastighet(UUID fastighetId) {
        return xpSearchService.getFastighetByUuid(fastighetId)
            .stream()
            .map(this::convertFastighetOmrInfoToFastighetDelomradeInfoDto)
            .collect(Collectors.toList());
    }

    private FastighetDelomradeInfoDto convertFastighetOmrInfoToFastighetDelomradeInfoDto(FastighetsOmrInfo entity) {
        return new FastighetDelomradeInfoDto()
                .fastighetId(entity.getFastighet().getId())
                .fastighetsbeteckning(entity.getFastighet().getFastighetsbeteckning())
                .omradeNr(entity.getOmrade_nr())
                .geometry(new GeometryJSON().toString(entity.getGeom()));
    }

    @Transactional
    public void setGeometristatus(UUID fastighetId, UUID versionId, GeometristatusDto geometristatus) {
        var entity = geometristatusRepository
                .findByFastighetIdAndVersionId(fastighetId, versionId);

        // Kolla så att Geometristatus finns, annars är det en manuellt tillagd fastighet.
        if(entity.isPresent()){
            log.info("Uppdaterar geometristatus för fastighet {} i version {} till {}", fastighetId, versionId,
                    geometristatus);

            var previousGeometriStatus = entity.get().getGeometristatus();
            entity.get().setGeometristatus(geometristatus);

            if (previousGeometriStatus != geometristatus && geometristatus != GeometristatusDto.OFORANDRAD) {
                var avtalId = avtalRepository.findIdByVersionIdAndFastighetId(versionId, fastighetId);
                avtalsloggService.logGeometristatus(avtalId, geometristatus);
            }
        }
    }

    public List<FastighetDto> getFastigheter(UUID projektId, FastighetsfilterDto filterDto) {
        var fastigheter = fastighetRepository.fastigheterFiltered(projektId, filterDto);

        return modelMapperList(fastigheter, modelMapper, FastighetDto.class);
    }

    public List<FastighetDto> getFastigheterAndSamfalligheter(UUID projektId, FastighetsfilterDto filterDto) {
        if (filterDto.getFastighetsIds() != null && !filterDto.getFastighetsIds().isEmpty()) {
            var entitys = fastighetRepository.getFastigheterAndSamfalligheterByIdIn(projektId, filterDto.getFastighetsIds());

            return modelMapperList(entitys, modelMapper, FastighetDto.class);
        }
        var fastigheter = fastighetRepository.fastigheterAndSamfalligheterFiltered(projektId, filterDto);

        return modelMapperList(fastigheter, modelMapper, FastighetDto.class);
    }

    @Transactional
    public void removeFastighet(UUID fastighetId, UUID projektId) {
        var versionId = projektRepository.getOne(projektId).getCurrentVersion().getId();
        var avtalId = avtalRepository.findByProjektIdAndFastighetId(projektId, fastighetId)
                .get().getId();
        geometristatusRepository.deleteByVersionIdAndAvtalId(versionId, avtalId);
        fastighetsforteckningRepository.getByAvtalId(avtalId).setExcluded(true);
    }

    private List<FastighetDelomradeDto> delomradeskartor(UUID projektId, UUID fastighetId) {
        var omraden = new ArrayList<FastighetDelomradeDto>();
        for (var omradeNr: fastighetOmradeRepository.getOmradeNrs(fastighetId)) {
            var omradeIntrangExtent = omradesintrangRepository.getExtentBuffered(fastighetId,
                projektId, omradeNr);

            // Om det inte finns några intrång vill vi inte heller visa upp delområdet i sig
            // bland delområdeskartorna, så vi hoppar till nästa.
            if (omradeIntrangExtent.isEmpty() || omradeIntrangExtent.get().isEmpty()) {
               continue;
            }

            String geometryType = fastighetOmradeRepository.getGeometryType(fastighetId, omradeNr);

            var delomrade = new FastighetDelomradeDto()
                    .fastighetsId(fastighetId)
                    .omradeNr(omradeNr.intValue())
                    .extent(List.of(
                            omradeIntrangExtent.get().getEnvelopeInternal().getMinX(),
                            omradeIntrangExtent.get().getEnvelopeInternal().getMinY(),
                            omradeIntrangExtent.get().getEnvelopeInternal().getMaxX(),
                            omradeIntrangExtent.get().getEnvelopeInternal().getMaxY()))
                    .geometryType(GeometryTypeDto.fromValue(geometryType));

            omraden.add(delomrade);
        }

        return omraden;
    }


    private Envelope fastighetDelomradeExtent(List<FastighetDelomradeDto> delomraden) {
        Envelope envelope = new Envelope();

        for (var delomrade: delomraden) {
            var fastighetOmradeExtent = fastighetOmradeRepository.getExtent(delomrade.getFastighetsId(), delomrade.getOmradeNr().longValue()).getEnvelopeInternal();
            envelope.expandToInclude(fastighetOmradeExtent);
        }
        return envelope;
    }

    private void setLabels(FastighetDto fastighetDto, UUID projektId) {
        var agareSaknas = avtalRepository.agareSaknas(projektId, fastighetDto.getId());

        var geometristatus =
            geometristatusRepository.findByFastighetIdAndProjektId(fastighetDto.getId(), projektId)
                .map(GeometristatusEntity::getGeometristatus)
                .orElse(GeometristatusDto.OFORANDRAD);

        fastighetDto.setAgareSaknas(agareSaknas);
        fastighetDto.setAvvikelse(!agareSaknas && this.hasAvvikelse(fastighetDto.getId(), projektId));
        fastighetDto.setGeometristatus(geometristatus);
    }

    private Map<String, List<Geometry>> getGeometryGroupsFromIntrang(Collection<IntrangEntity> intrang) {
        return intrang.stream()
            .collect(groupingBy(IntrangEntity::getType, mapping(IntrangEntity::getGeom, toList())));
    }
}
