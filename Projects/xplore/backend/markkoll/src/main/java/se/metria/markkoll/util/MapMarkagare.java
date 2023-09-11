package se.metria.markkoll.util;

import org.modelmapper.ModelMapper;
import se.metria.markkoll.entity.logging.avtalslogg.LogAvtalsstatusEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.openapi.model.AvtalspartLabelsDto;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.MarkagareDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;

import static se.metria.markkoll.util.CollectionUtil.isNullOrEmpty;

public class MapMarkagare {
    public static
    MarkagareDto mapMarkagareDto(AvtalspartEntity avtalspart, Boolean isKontaktperson, ModelMapper modelMapper) {

        var markagareDto = modelMapper.map(avtalspart, MarkagareDto.class);
        markagareDto.setKontaktperson(isKontaktperson);
        markagareDto.setLabels(getLabels(avtalspart));

        return markagareDto;
    }

    private static AvtalspartLabelsDto getLabels(AvtalspartEntity avtalspart) {
        return new AvtalspartLabelsDto()
            .agareSaknas(avtalspart.getMarkagare().isAgareSaknas())
            .avtalsstatusGammal(isAvtalspartOverdue(avtalspart))
            .ofullstandingInformation(isMissingInformation(avtalspart));
    }

    /**
     * Kontrollera om en avtalspart är försenad, dvs. om senaste logghändelsen har avstalsstatus satt till
     * EJ_BEHANDLAT, AVTAL_SKICKAT eller AVTAL_JUSTERAS och skapat datum är längre tillbaka än en vecka.
     */
    private static boolean isAvtalspartOverdue(AvtalspartEntity avtalspart) {
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

    private static boolean isMissingInformation(AvtalspartEntity avtalspartEntity) {
        var markagare = avtalspartEntity.getMarkagare();
        var person = avtalspartEntity.getMarkagare().getPerson();

        return !markagare.isAgareSaknas() &&
            (isNullOrEmpty(person.getAdress()) ||
                isNullOrEmpty(person.getPostort()) ||
                isNullOrEmpty(person.getPostnummer()) ||
                isNullOrEmpty(person.getNamn()) ||
                isNullOrEmpty(markagare.getAndel()));
    }
}
