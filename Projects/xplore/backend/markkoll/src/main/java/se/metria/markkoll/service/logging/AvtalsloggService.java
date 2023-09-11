package se.metria.markkoll.service.logging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.logging.avtalslogg.LogAvtalsstatusEntity;
import se.metria.markkoll.entity.logging.avtalslogg.LogGeometristatusEntity;
import se.metria.markkoll.entity.logging.avtalslogg.LogUtbetalningsdatumEntity;
import se.metria.markkoll.openapi.model.AvtalsLoggItemDto;
import se.metria.markkoll.openapi.model.AvtalsLoggTypeDto;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.logging.avtalslogg.LogAvtalsstatusRepository;
import se.metria.markkoll.repository.logging.avtalslogg.LogGeometristatusRepository;
import se.metria.markkoll.repository.logging.avtalslogg.LogUtbetalningsdatumRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.service.geometristatus.GeometristatusEvent;
import se.metria.markkoll.service.markagare.AvtalsstatusUpdatedEvent;
import se.metria.markkoll.service.markagare.UtbetalningEvent;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AvtalsloggService {
    @NonNull
    private final LogAvtalsstatusRepository logAvtalsstatusRepository;
    @NonNull
    private final LogGeometristatusRepository logGeometristatusRepository;
    @NonNull
    private final LogUtbetalningsdatumRepository logUtbetalningsdatumRepository;
    @NonNull
    private final AvtalRepository avtalRepository;
    @NonNull
    private final AvtalspartRepository avtalspartRepository;

    public List<AvtalsLoggItemDto> getAvtalslogg(UUID projektId, UUID fastighetId) {

        var avtalEntity = avtalRepository.findByProjektIdAndFastighetId(projektId, fastighetId);
        var loggList = new ArrayList<AvtalsLoggItemDto>();

        // Loggar för avtalsstatus
        var avtalsstatusEntities = new ArrayList<LogAvtalsstatusEntity>();
        avtalEntity.get().getAvtalsparter().forEach(avtalsPart -> avtalsstatusEntities.addAll(avtalsPart.getLogAvtalsstatus()));
        avtalsstatusEntities.forEach(avtalsstatusEntity -> loggList.add(mapAvtalsLoggItemDto(avtalsstatusEntity)));

        // Loggar för geometristatus
        avtalEntity.get().getLogGeometristatus().forEach(geometristatusEntity -> loggList.add(mapAvtalsLoggItemDto(geometristatusEntity)));

        var utbetalningsdatum = logUtbetalningsdatumRepository.findAllByAvtalId(avtalEntity.get().getId());
        utbetalningsdatum.forEach(ub -> loggList.add(mapAvtalsLoggItemDto(ub)));

        // Sortera loggar med senast skapade först
        Collections.sort(loggList, (c1, c2) -> c2.getSkapadDatum().compareTo(c1.getSkapadDatum()));

        return loggList;
    }

    @EventListener
    public void logAvtalsstatus(AvtalsstatusUpdatedEvent event) {
        logAvtalsstatus(event.getUpdatedAvtalspartIds(), event.getAvtalsstatus());
    }

    @Transactional
    public void logAvtalsstatus(Collection<UUID> avtalspartIds, AvtalsstatusDto avtalsstatus) {
        log.info("Loggar avtalsstatus ({}) för {} avtalsparter", avtalsstatus, avtalspartIds.size());

        var logEntities = avtalspartIds.stream()
            .map(id -> avtalspartRepository.getReferenceById(id))
            .map(ap -> new LogAvtalsstatusEntity(ap, avtalsstatus))
            .collect(Collectors.toList());

        logAvtalsstatusRepository.saveAll(logEntities);
    }

    @Transactional
    public void logAvtalsstatus(UUID avtalspartId, AvtalsstatusDto avtalsstatus) {
        var avtalspart = avtalspartRepository.getReferenceById(avtalspartId);
        var log = new LogAvtalsstatusEntity(avtalspart, avtalsstatus);

        logAvtalsstatusRepository.save(log);
    }

    @EventListener
    public void logGeometristatus(GeometristatusEvent event) {
        logGeometristatus(event.getAvtalIds(), event.getGeometristatusDto());
    }

    @Transactional
    public void logGeometristatus(Collection<UUID> avtalIds, GeometristatusDto geometristatus) {
        var logEntities = avtalIds.stream()
            .map(id -> avtalRepository.getReferenceById(id))
            .map(av -> new LogGeometristatusEntity(av, geometristatus))
            .collect(Collectors.toList());

        logGeometristatusRepository.saveAll(logEntities);
    }

    @Transactional
    public void logGeometristatus(UUID avtalId, GeometristatusDto geometristatus) {
        var avtal = avtalRepository.getReferenceById(avtalId);
        var log = new LogGeometristatusEntity(avtal, geometristatus);

        logGeometristatusRepository.save(log);
    }

    @EventListener
    public void logUtbetalningsdatum(UtbetalningEvent event) {
        logUtbetalningsdatum(event.getUpdatedAvtalspartIds(), event.getUtbetalningsdatum());
    }

    @Transactional
    public void logUtbetalningsdatum(Collection<UUID> avtalspartIds, LocalDate utbetalningsdatum) {
        var logEntities = avtalspartIds.stream()
            .map(id -> avtalspartRepository.getReferenceById(id))
            .map(ap -> new LogUtbetalningsdatumEntity(ap, utbetalningsdatum))
            .collect(Collectors.toList());

        logUtbetalningsdatumRepository.saveAll(logEntities);
    }

    @Transactional
    public void logUtbetalningsdatum(UUID avtalspartId, LocalDate utbetalningsdatum) {
        var avtalspart = avtalspartRepository.findById(avtalspartId)
            .orElseThrow(EntityNotFoundException::new);

        var log = new LogUtbetalningsdatumEntity();
        log.setAvtalspart(avtalspart);
        log.setUtbetalningsdatum(utbetalningsdatum);

        logUtbetalningsdatumRepository.save(log);
    }

    public static AvtalsLoggItemDto mapAvtalsLoggItemDto(LogAvtalsstatusEntity logAvtalsstatusEntity) {
        var result = new AvtalsLoggItemDto();
        result.setAvtalsLoggType(AvtalsLoggTypeDto.AVTALSSTATUS);
        result.setAvtalsPartNamn(logAvtalsstatusEntity.getAvtalspart().getMarkagare().getPerson().getNamn());
        result.setAvtalsLoggStatus(logAvtalsstatusEntity.getAvtalsstatus().getValue());
        result.setSkapadAv(logAvtalsstatusEntity.getSkapadAv());
        result.skapadDatum(logAvtalsstatusEntity.getSkapadDatum());
        return result;
    }

    public static AvtalsLoggItemDto mapAvtalsLoggItemDto(LogGeometristatusEntity logGeometristatusEntity) {
        var result = new AvtalsLoggItemDto();
        result.setAvtalsLoggType(AvtalsLoggTypeDto.GEOMETRISTATUS);
        result.setAvtalsLoggStatus(logGeometristatusEntity.getGeometristatus().getValue());
        result.setSkapadAv(logGeometristatusEntity.getSkapadAv());
        result.skapadDatum(logGeometristatusEntity.getSkapadDatum());
        return result;
    }

    public static AvtalsLoggItemDto mapAvtalsLoggItemDto(LogUtbetalningsdatumEntity logUtbetalningsdatumEntity) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        var result = new AvtalsLoggItemDto();
        result.setAvtalsLoggType(AvtalsLoggTypeDto.UTBETALNINGSDATUM);
        result.setAvtalsPartNamn(logUtbetalningsdatumEntity.getAvtalspart().getMarkagare().getPerson().getNamn());
        result.setAvtalsLoggStatus(logUtbetalningsdatumEntity.getUtbetalningsdatum().format(formatter));
        result.setSkapadAv(logUtbetalningsdatumEntity.getSkapadAv());
        result.setSkapadDatum(logUtbetalningsdatumEntity.getSkapadDatum());
        return result;
    }
}
