package se.metria.markkoll.service.markagare;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.logging.avtalslogg.LogAvtalsstatusEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.repository.markagare.MarkagareRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.MarkagareImportService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.util.MapMarkagare;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkagareService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final AvtalspartRepository avtalspartRepository;

    @NonNull
    private final MarkagareRepository markagareRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final LoggService loggService;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final MarkagareImportService markagareImportService;

    @NonNull
    private final AvtalsloggService avtalsloggService;

    @NonNull
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Integer importAgare(UUID projektId, List<UUID> fastigheterIds, String kundId) {
        var agareTotal = markagareImportService.ImportAgare(fastigheterIds, kundId);
        var numOfAvtalsparter = 0;

        for (var fastighetId: fastigheterIds) {
            var avtal = avtalRepository.findByProjektIdAndFastighetId(projektId, fastighetId)
                    .orElseThrow(EntityNotFoundException::new);

            var avtalsparter = markagareRepository
                    .findByFastighetIdAndKundId(fastighetId, kundId)
                    .stream()
                    .map(this::fromMarkagare)
                    .collect(Collectors.toList());

            for (var avtalspart: avtalsparter) {
                avtal.addAvtalspart(avtalspart);
            }

            avtalsparter = avtalspartRepository.saveAll(avtalsparter);

            if (!avtalsparter.isEmpty()) {
                avtal.setKontaktperson(avtalsparter.get(0));
            }

            for (var savedAvtalspart: avtalsparter) {
                aclService.createObject(savedAvtalspart.getId(), AvtalspartEntity.class, projektId, ProjektEntity.class);
                avtalsloggService.logAvtalsstatus(savedAvtalspart.getId(), savedAvtalspart.getAvtalsstatus());
            }

            numOfAvtalsparter += avtalsparter.size();
        }

        if (numOfAvtalsparter != agareTotal) {
            log.warn("Antalet avtalsparter ({}) överrensstämmer inte med antalet importerade ägare ({})",
                    numOfAvtalsparter, agareTotal);
        }

        loggService.addProjektHandelse(projektId, ProjekthandelseTypDto.HAMTA_MARKAGARE);

        return numOfAvtalsparter;
    }

    private AvtalspartEntity fromMarkagare(MarkagareEntity markagareEntity) {
        return AvtalspartEntity.builder()
                .markagare(markagareEntity)
                .signatar(false)
                .inkluderaIAvtal(true)
                .avtalsstatus(AvtalsstatusDto.EJ_BEHANDLAT)
                .build();
    }

    public MarkagareDto getAgare(UUID avtalspartId) {
        var avtalspart = avtalspartRepository.findById(avtalspartId).orElseThrow(EntityNotFoundException::new);

        return MapMarkagare.mapMarkagareDto(avtalspart, avtalspart.getKontaktpersonAvtal() != null,
            modelMapper);
    }

    public List<MarkagareDto> getAgare(Collection<UUID> avtalspartIds) {
        var entities = avtalspartRepository.findAllByIdIn(avtalspartIds);

        return entities.stream()
            .map(avtalspart -> MapMarkagare.mapMarkagareDto(avtalspart, avtalspart.getKontaktpersonAvtal() != null, modelMapper))
            .collect(Collectors.toList());
    }

    public List<UUID> getAgareIdsForAvtal(UUID avtalId) {
        return getAgareForAvtal(avtalId).stream()
            .map(MarkagareDto::getId)
            .collect(Collectors.toList());
    }

    public List<MarkagareDto> getAgareForAvtal(UUID avtalId) {
        var fastighetId = avtalRepository.getFastighetId(avtalId);
        var projektId = avtalRepository.getProjektId(avtalId);

        return getAgareForFastighet(projektId, fastighetId);
    }

    @Deprecated
    public Optional<MarkagareDto> getKontaktperson(UUID projektId, UUID fastighetId) {
        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, fastighetId);
        return getKontaktperson(avtalId);
    }

    public Optional<MarkagareDto> getKontaktperson(UUID avtalId) {
        var kontaktperson = avtalspartRepository.findKontaktperson(avtalId);

        return kontaktperson.map(ag -> modelMapper.map(ag, MarkagareDto.class));
    }

    @Deprecated
    public List<MarkagareDto> getAgareForFastighet(UUID projektId, UUID fastighetId) {
        var kontaktpersonId = avtalspartRepository.findKontaktpersonId(projektId, fastighetId)
                .orElse(null);

        return avtalspartRepository
                .findByProjektIdAndFastighetId(projektId, fastighetId)
                .stream()
                .map(avtalspart -> MapMarkagare.mapMarkagareDto(avtalspart, avtalspart.getId().equals(kontaktpersonId), modelMapper))
                .sorted(Comparator.comparing(MarkagareDto::getNamn))
                .collect(Collectors.toList());
    }

    public String getPersonnummer(UUID avtalspartId) {
        return avtalspartRepository.getPersonnnummer(avtalspartId);
    }

    @Transactional
    public void updateAvtalsstatus(Collection<UUID> avtalIds, AvtalsstatusDto avtalsstatus) {
        var query = avtalspartRepository.findByAvtalIdIn(avtalIds);
        var avtalspartIds = query.stream().map(q -> q.getId()).collect(Collectors.toList());

        log.info("Uppdaterar avtalsstatus till {} för {} markägare", avtalsstatus, avtalspartIds.size());

        avtalspartRepository.setAvtalsstatus(avtalspartIds, avtalsstatus);
        avtalRepository.refreshAvtalsstatus(avtalIds, avtalsstatus.toString());

        var updatedAvtalspartIds = query.stream()
            .filter(q -> q.getAvtalsstatus() != avtalsstatus)
            .map(q -> q.getId())
            .collect(Collectors.toList());

        publisher.publishEvent(new AvtalsstatusUpdatedEvent(updatedAvtalspartIds, avtalsstatus));

        if (avtalsstatus == AvtalsstatusDto.ERSATTNING_UTBETALD) {
            log.info("Uppdaterar utbetalningsdatum");
            var utbetalningsdatum = LocalDate.now();
            avtalspartRepository.setUtbetalningsdatum(avtalspartIds, utbetalningsdatum);
            publisher.publishEvent(new UtbetalningEvent(avtalspartIds, utbetalningsdatum));
        }

    }

    @Transactional
    public MarkagareDto updateAgare(UUID avtalspartId, MarkagareDto markagare) {
        var avtalspart = avtalspartRepository.getOne(avtalspartId);

        var person = avtalspart.getMarkagare().getPerson();

        // Logga avtalsstatus endast vid ändring av status
        if (avtalspart.getAvtalsstatus() != markagare.getAgareStatus()) {
            avtalsloggService.logAvtalsstatus(avtalspart.getId(), markagare.getAgareStatus());
        }
        if (markagare.getUtbetalningsdatum() != null &&
            !markagare.getUtbetalningsdatum().equals(avtalspart.getUtbetalningsdatum()))
        {
            avtalsloggService.logUtbetalningsdatum(avtalspartId, markagare.getUtbetalningsdatum());
        }

        person.setAdress(markagare.getAdress());
        person.setBankkonto(markagare.getBankkonto());
        person.setNamn(markagare.getNamn());
        person.setPostnummer(markagare.getPostnummer());
        person.setPostort(markagare.getPostort());
        person.setTelefon(markagare.getTelefon());
        person.setEPost(markagare.getePost());
        person.setLand(markagare.getLand());
        avtalspart.getMarkagare().setAndel(markagare.getAndel());
        avtalspart.getMarkagare().setAgartyp(markagare.getAgartyp());
        avtalspart.setInkluderaIAvtal(markagare.getInkluderaIAvtal());
        avtalspart.setAvtalsstatus(markagare.getAgareStatus());
        avtalspart.setUtbetalningsdatum(markagare.getUtbetalningsdatum());

        setKontaktperson(avtalspart, markagare.getKontaktperson());

        log.info("Ägare {} uppdaterad", avtalspartId);

        avtalspartRepository.save(avtalspart);

        avtalRepository.refreshAvtalsstatus(avtalspart.getAvtal().getId());

        return MapMarkagare.mapMarkagareDto(avtalspart, markagare.getKontaktperson(), modelMapper);
    }

    @Transactional
    public MarkagareDto addAgare(UUID projektId, UUID fastighetId, MarkagareInfoDto markagare) {

        var fastighet = fastighetRepository.getOne(fastighetId);
        var markagareEntity = modelMapper.map(markagare, MarkagareEntity.class);
        var kundId = projektRepository.getKundId(projektId);

        markagareEntity.setFastighet(fastighet);
        markagareEntity.setKundId(kundId);
        markagareEntity.getPerson().setKundId(kundId);
        markagareEntity = markagareRepository.saveAndFlush(markagareEntity);

        MarkagareDto agareReturn = null;
        for (var avtal: avtalRepository.findByFastighetIdAndKundId(fastighetId, kundId)) {
            var avtalspartEntity = new AvtalspartEntity();
            avtalspartEntity.setInkluderaIAvtal(markagare.getInkluderaIAvtal());
            avtalspartEntity.setAvtalsstatus(markagare.getAgareStatus());
            avtalspartEntity.setAvtal(avtal);
            avtalspartEntity.setMarkagare(markagareEntity);
            avtalspartEntity = avtalspartRepository.saveAndFlush(avtalspartEntity);
            aclService.createObject(avtalspartEntity.getId(), AvtalspartEntity.class, projektId, ProjektEntity.class);

            if (avtal.getProjekt().getId().equals(projektId)) {
                agareReturn = MapMarkagare.mapMarkagareDto(avtalspartEntity, markagare.getKontaktperson(), modelMapper);
            }

            avtalRepository.refreshAvtalsstatus(avtal.getId());
        }

        return agareReturn;
    }

    @Transactional
    public void deleteAgare(UUID avtalspartId) {
        var avtalId = avtalspartRepository.getAvtalId(avtalspartId);
        avtalspartRepository.deleteById(avtalspartId);
        aclService.deleteObject(avtalspartId, AvtalspartEntity.class);

        avtalRepository.refreshAvtalsstatus(avtalId);

        log.info("Ägare {} borttagen", avtalspartId);
    }

    private AvtalspartLabelsDto getLabels(AvtalspartEntity avtalspart) {
        return new AvtalspartLabelsDto()
            .agareSaknas(avtalspart.getMarkagare().isAgareSaknas())
            .avtalsstatusGammal(isAvtalspartOverdue(avtalspart))
            .ofullstandingInformation(isMissingInformation(avtalspart));
    }

    /**
     * Kontrollera om en avtalspart är försenad, dvs. om senaste logghändelsen har avstalsstatus satt till
     * EJ_BEHANDLAT, AVTAL_SKICKAT eller AVTAL_JUSTERAS och skapat datum är längre tillbaka än en vecka.
     */
    private boolean isAvtalspartOverdue(AvtalspartEntity avtalspart) {
        if (avtalspart.getLogAvtalsstatus() != null && !avtalspart.getLogAvtalsstatus().isEmpty()) {
            var latestLog = Collections.max(avtalspart.getLogAvtalsstatus(), Comparator.comparing(LogAvtalsstatusEntity::getSkapadDatum));
            if ((latestLog.getAvtalsstatus() == AvtalsstatusDto.EJ_BEHANDLAT ||
                    latestLog.getAvtalsstatus() == AvtalsstatusDto.AVTAL_SKICKAT ||
                    latestLog.getAvtalsstatus() == AvtalsstatusDto.AVTAL_JUSTERAS) &&
                    latestLog.getSkapadDatum().isBefore(LocalDateTime.now().minusWeeks(1))) {
                return true;
            }
        }
        return false;
    }

    private boolean isMissingInformation(AvtalspartEntity avtalspartEntity) {
        var markagare = avtalspartEntity.getMarkagare();
        var person = avtalspartEntity.getMarkagare().getPerson();

        return !markagare.isAgareSaknas() &&
            (isNullOrEmpty(person.getAdress()) ||
            isNullOrEmpty(person.getPostort()) ||
            isNullOrEmpty(person.getPostnummer()) ||
            isNullOrEmpty(person.getNamn()) ||
            isNullOrEmpty(markagare.getAndel()));
    }

    private void setKontaktperson(AvtalspartEntity avtalspartEntity, boolean setAsKontaktperson) {
        var avtal = avtalspartEntity.getAvtal();
        var currentKontaktperson = avtal.getKontaktperson();

        if (setAsKontaktperson == true) {
            avtal.setKontaktperson(avtalspartEntity);
        }
        // Vi har satt så att markägaren inte längre är kontaktperson
        else if (currentKontaktperson != null && currentKontaktperson.getId() == avtalspartEntity.getId()) {
            avtal.setKontaktperson(null);
        }
    }
}
