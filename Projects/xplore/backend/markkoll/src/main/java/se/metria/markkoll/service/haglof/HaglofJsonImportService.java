package se.metria.markkoll.service.haglof;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.HaglofImportVarningarDto;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.service.haglof.model.HaglofFastighet;
import se.metria.markkoll.service.haglof.model.HaglofImport;
import se.metria.markkoll.service.haglof.model.HaglofOwner;
import se.metria.markkoll.service.markagare.MarkagareService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HaglofJsonImportService {

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final HaglofImportService haglofImportService;

    @NonNull
    private final MarkagareService markagareService;

    ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
        .enable(SerializationFeature.WRAP_ROOT_VALUE)
        .enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);

    public HaglofImportVarningarDto importJson(UUID projektId, String json) {
        var varningar = new HaglofImportVarningarDto();
        var jsonImport = readImporter(json);
        var haglofFastigheter = jsonImport.getFastigheter();

        haglofImportService.importProjekt(projektId, jsonImport.getMetadata());

        for (var haglofFastighet: haglofFastigheter) {
            if (StringUtil.isNullOrEmpty(haglofFastighet.getKommun())) {
                varningar.addFastighetKommunMissingItem(haglofFastighet.getFastighetsbeteckning());
                continue;
            }

            var avtalId = avtalRepository.getIdByFastighetsbeteckningAndKommun(projektId,
                haglofFastighet.getFastighetsbeteckning(), haglofFastighet.getKommun());
            if (avtalId == null) {
                varningar.addFastigheterMissingItem(fastighetMissingMsg(haglofFastighet));
                continue;
            }

            haglofImportService.importAvtal(avtalId, haglofFastighet);
            haglofImportService.importVp(avtalId, haglofFastighet, jsonImport.getMetadata());

            var markagareCollection = markagareService.getAgareForAvtal(avtalId);
            for (var haglofOwner: haglofFastighet.getOwners()) {
                var markagare = findMarkagare(markagareCollection, haglofOwner);

                if (markagare.size() == 1) {
                    haglofImportService.importMarkagare(markagare.get(0).getId(), haglofOwner);
                }
                else if (markagare.isEmpty()) {
                    varningar.addAgareMissingItem(haglofOwner.getNamn());
                }
                else {
                    varningar.addAgareAmbiguousItem(haglofOwner.getNamn());
                }
            }
        }

        return varningar;
    }

    public static String fastighetMissingMsg(HaglofFastighet fastighet) {
        return String.format("%s %s", fastighet.getFastighetsbeteckning(), fastighet.getKommun());
    }

    public static String agareMissingMsg(HaglofOwner owner) {
        return owner.getNamn();
    }

    private List<MarkagareDto>
    filterByFodelsedatumEllerOrgnummer(Collection<MarkagareDto> markagareHit, String personnummer) {

        return markagareHit.stream()
            .filter(m -> m.getFodelsedatumEllerOrgnummer() != null)
            .filter(m -> m.getFodelsedatumEllerOrgnummer().equals(personnummer))
            .collect(Collectors.toList());
    }

    private List<MarkagareDto> findMarkagare(Collection<MarkagareDto> markagareCollection, HaglofOwner haglofOwner) {
        var markagareHit = markagareCollection.stream()
            .filter(m -> m.getNamn().equals(convertHaglofName(haglofOwner.getNamn())))
            .collect(Collectors.toList());

        if (haglofOwner.getPersonnummer() != null) {
            if (markagareHit.size() > 1) {
                return filterByFodelsedatumEllerOrgnummer(markagareHit, haglofOwner.getPersonnummer());
            }
            else if (markagareHit.size() == 0) {
                return filterByFodelsedatumEllerOrgnummer(markagareCollection, haglofOwner.getPersonnummer());
            }
        }

        return markagareHit;
    }

    private HaglofImport readImporter(String json) {
        try {
            return objectMapper.readValue(json, HaglofImport.class);
        }
        catch (JsonProcessingException ex) {
            throw new MarkkollException(MarkkollError.HAGLOF_JSON_INVALID, ex);
        }
    }

    private String convertHaglofName(String name) {
        var comma = name.indexOf(',');
        if (comma == -1) {
            return name;
        }
        else {
            var efternamn = name.substring(0, comma).trim();
            var fornamn = name.substring(comma + 1).trim();
            return fornamn + " " + efternamn;
        }
    }
}
