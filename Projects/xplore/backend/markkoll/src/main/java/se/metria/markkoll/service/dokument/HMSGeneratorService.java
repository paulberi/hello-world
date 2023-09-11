package se.metria.markkoll.service.dokument;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.markkoll.openapi.model.AgartypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.avtal.HaglofExportView;
import se.metria.markkoll.util.dokument.hms_generator.Entry;
import se.metria.markkoll.util.dokument.hms_generator.HMSGenerator;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HMSGeneratorService {
    @NonNull
    private final HMSGenerator HMSGenerator;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public byte[] getVarderingSkogsmark(Collection<UUID> avtalIds) throws IOException {
        log.info("Hämtar data för skogsmarksvärdering för {} avtal", avtalIds.size());
        var entries =  getEntries(avtalIds);

        return HMSGenerator.generate(entries);
    }

    private List<Entry> getEntries(Collection<UUID> avtalIds) {
        var haglofExport = avtalRepository.getHaglofExport(avtalIds);
        Map<UUID, String> mottagarreferenser = avtalRepository.getMottagarreferenser(avtalIds).stream()
            .collect(Collectors.toMap(view -> view.getAvtalspartId(), view -> view.getMottagarreferens()));

        List<Entry> entriesList = new ArrayList<>();
        for (var export: filterIfOmbudPresent(haglofExport)) {
            var entry = modelMapper.map(export, Entry.class);
            modelMapper.map(export.getPerson(), entry);
            entry.setMottagarreferens(mottagarreferenser.get(export.getAvtalspartId()));

            entriesList.add(entry);
        }

        return entriesList;
    }

    private List<HaglofExportView> filterIfOmbudPresent(List<HaglofExportView> haglofExport) {
        var partitionedByAvtalId = haglofExport.stream()
            .collect(Collectors.groupingBy(export -> export.getAvtalId()));

        return partitionedByAvtalId.entrySet().stream().map(entry -> {
            if (entry.getValue().stream().anyMatch(export -> export.getAgartyp() == AgartypDto.OMBUD)) {
                return entry.getValue().stream()
                    .filter(export -> export.getAgartyp() == AgartypDto.OMBUD)
                    .collect(Collectors.toList());
            }
            else {
                return entry.getValue();
            }
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
