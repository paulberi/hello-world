package se.metria.finfo.service.agare;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import se.metria.finfo.entity.agare.FinfoAgareEntity;
import se.metria.finfo.openapi.model.AgareRequestDto;
import se.metria.finfo.repository.FinfoAgareRepository;
import se.metria.finfo.service.agare.model.Agarforteckning;
import se.metria.finfo.util.PersonGenerator;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinfoImportService {
    @NonNull
    private final AgarforteckningService agarforteckningService;

    @NonNull
    private final FinfoAgareRepository finfoAgareRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${finfo.generate-person-info:true}")
    private boolean generatePersonalInfo;

    @Transactional
    public List<UUID> importAgare(AgareRequestDto finfoAgareRequestDto) throws InterruptedException {
        var imported = new ArrayList<UUID>();

        var fastighetIds = finfoAgareRequestDto.getFastighetIds();

        if (fastighetIds.isEmpty()) {
            log.info("Inga fastigheter att hämta markägare för från Fastighetssök.");
            return imported;
        }

        log.info("Påbörjar import av markägare för {} fastigheter...", fastighetIds.size());

        var fastigheter =
                agarforteckningService.fetch(finfoAgareRequestDto)
                .stream()
                .map(Agarforteckning::getFastighet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        if (generatePersonalInfo) {
            log.info("Import med slumpade personuppgifter");
        }

        var gen = new PersonGenerator();
        for (var fastighet: fastigheter) {
            log.info("Importerar {} markägare från fastighet '{}'...", fastighet.getAgare().size(),
                    fastighet.getBeteckning());

			if (generatePersonalInfo) {
				gen.reseed(fastighet.getUuid()); // Personuppgifterna per fastighet ska vara deterministiska
			}
            for (var agare: fastighet.getAgare()) {
				if (generatePersonalInfo) {
				    // De slumpade namnen i testtjänsten är inte i formen Efternamn, Förnamn
					agare.setNamn(gen.generateNamn());

					// Vi får inte alltid unika personnummer från testtjänsten
					agare.setPersonnummer(gen.generatePersonnummer());
				}

                if(agare.getAdress() == null) {
                    agare.setAdress("");
                }
                if(agare.getPostnummer() == null) {
                    agare.setPostnummer("");
                }
                if(agare.getPostort() == null) {
                    agare.setPostort("");
                }
                if(agare.getPersonnummer() == null) {
                    agare.setPersonnummer("");
                }

                var agareEntity = modelMapper.map(agare, FinfoAgareEntity.class);
                agareEntity.setNamn(namnParser(agareEntity.getNamn()));
                agareEntity.setFastighetId(fastighet.getUuid());
                agareEntity.setImporteradDatum(LocalDateTime.now());

                var savedAgare = finfoAgareRepository.saveAndFlush(agareEntity);
                imported.add(savedAgare.getId());
                log.info("Markägare {} importerad.", savedAgare.getId());
            }
        }

        log.info("Markägarimport från Fastighetssök klar.");

        return imported;
    }

    private String namnParser(String namn) {
        if (namn == null) {
            return "Okänt";
        }

        var efternamn_fornamn = namn.split(",");
        if (efternamn_fornamn.length != 2) {
            // Skulle kunna vara namnet på företag, stiftelse, eller något helt annat.
            return namn;
        }

        return efternamn_fornamn[1].strip() + " " + efternamn_fornamn[0].strip();
    }
}
