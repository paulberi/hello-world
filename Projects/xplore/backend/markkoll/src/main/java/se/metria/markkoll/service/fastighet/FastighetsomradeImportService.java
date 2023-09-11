package se.metria.markkoll.service.fastighet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class FastighetsomradeImportService {
    @NonNull
    private final FastighetOmradeRepository fastighetOmradeRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @Transactional
    public Set<UUID> importFastighetsomraden(List<FastighetsOmrInfo> fastighetsomraden) {
        log.info("Importerar {} fastighetsområden", fastighetsomraden.size());

        var fastigheter = new HashSet<UUID>();
        for (FastighetsOmrInfo fastighetsOmrInfo : fastighetsomraden) {
            log.debug("Slår upp fastighet {} för område {}...", fastighetsOmrInfo.fastighet.getId(),
                fastighetsOmrInfo.omrade_nr);
            var existing = fastighetRepository
                .findById(fastighetsOmrInfo.fastighet.getId())
                .orElseGet(() -> {
                    log.info("Fastighet {} fanns ej, skapar ny fastighet", fastighetsOmrInfo.fastighet.getId());
                    return fastighetRepository.save(fastighetsOmrInfo.fastighet);
                });

            var fastOmr = new FastighetOmradeEntity(fastighetsOmrInfo.fastighet.getId(),
                fastighetsOmrInfo.omrade_nr,fastighetsOmrInfo.geom, new HashSet<>());

            log.debug("Slår upp fastighetsområde {}...", fastOmr.getOmradeId());
            var existingOmrade = fastighetOmradeRepository.findById(fastOmr.getOmradeId())
                .orElseGet(() -> {
                    log.info("Fastighetsområde {} fanns ej, skapar nytt fastighetsområde ({} points)", fastOmr.getOmradeId(), fastOmr.getGeom().getNumPoints());
                    return fastighetOmradeRepository.save(fastOmr);
                });

            existing.addFastighetomrade(existingOmrade);
            existing = fastighetRepository.save(existing);
            fastigheter.add(existing.getId());
            log.debug("Fastighet {} uppdaterad", existing.getId());
        }

        return fastigheter;
    }
}
