package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.openapi.finfo.model.FinfoAgareDto;
import se.metria.markkoll.openapi.model.AgartypDto;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.MarkagareRepository;
import se.metria.markkoll.repository.markagare.PersonRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.finfo.FinfoService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.metria.markkoll.util.CollectionUtil.isNullOrEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarkagareImportService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final FinfoService finfoService;

    @NonNull
    private final PersonRepository personRepository;

    @NonNull
    private final MarkagareRepository markagareRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @Transactional
    public Integer ImportAgare(List<UUID> fastighetIds, String kundId) {
        var importedAgare = 0;

        var agareIds = finfoService.importAgare(fastighetIds);
        var finfoImportedAgare = finfoService.getAgare(agareIds);

        for (var fastighetId: fastighetIds) {
            var fastighetEntity = fastighetRepository.getOne(fastighetId);
            var agareFastighet = finfoImportedAgare
                    .stream()
                    .filter(ag -> ag.getFastighetId().equals(fastighetId))
                    .collect(Collectors.toList());

            log.info("Importerar {} ägare för fastighet {}", agareFastighet.size(),
                    fastighetEntity.getFastighetsbeteckning());
            if (agareFastighet.size() == 0 && !fastighetRepository.agareSaknas(fastighetId)) {
                importAgareSaknas(fastighetEntity, kundId);
            }
            else for (var finfoAgare: agareFastighet) {
                var personEntity= getPersonUnique(finfoAgare, kundId);
                var agareEntity = saveOrUpdateMarkagare(finfoAgare, personEntity, fastighetEntity, kundId);

                importedAgare++;
                log.info("Markägare {} skapad", agareEntity.getId());
            }
        }

        return importedAgare;
    }

    private PersonEntity getPersonUnique(FinfoAgareDto finfoAgare, String kundId) {
        var person = isNullOrEmpty(finfoAgare.getPersonnummer()) ?
                personRepository.findByNamnAndAdressAndPostnummerAndKundId(
                        finfoAgare.getNamn(), finfoAgare.getAdress(), finfoAgare.getPostnummer(), kundId) :
                personRepository.findByPersonnummerAndKundId(finfoAgare.getPersonnummer(), kundId);

        if (person.isPresent()) {
            log.info("Uppdaterar personuppgifter för person {}", person.get().getId());
            var existing = person.get();
            existing.setPersonnummer(finfoAgare.getPersonnummer());
            existing.setAdress(finfoAgare.getAdress());
            existing.setPostort(finfoAgare.getPostort());
            existing.setNamn(finfoAgare.getNamn());
            return personRepository.save(existing);
        }
        else {
            return personRepository.save(fromFinfoAgare(finfoAgare, kundId));
        }
    }

    /* Skulle det saknas en faktiskt ägare på fastigheten så importerar vi ändå en ny markägare som ska representera
    avsaknaden av en ägare. Det ger fördelen att vi ändå t.ex. kan jobba med avtal på fastigheten (kräver att det finns
    en ägare på fastigheten), och tillföra personuppgifter som hittas på andra ställen än Lantmäteriet. */
    private void importAgareSaknas(FastighetEntity fastighetEntity, String kundId) {
        log.warn("Fastigheten {} saknar ägare", fastighetEntity.getFastighetsbeteckning());

        var person = new PersonEntity();
        person.setKundId(kundId);
        person.setNamn("Ägare saknas");

        var markagare = new MarkagareEntity();
        person.addMarkagare(markagare);
        markagare.setPerson(person);
        markagare.setKundId(kundId);
        markagare.setFastighet(fastighetEntity);
        markagare.setAgartyp(AgartypDto.LF);
        markagare.setAgareSaknas(true);

        markagareRepository.save(markagare);
    }

    private MarkagareEntity
    saveOrUpdateMarkagare(FinfoAgareDto finfoAgare,
                          PersonEntity personEntity,
                          FastighetEntity fastighetEntity,
                          String kundId)
    {

        var markagare = markagareRepository.findByPersonIdAndFastighetId(personEntity.getId(),
                fastighetEntity.getId());

        if (markagare.isPresent()) {
            var agareEntity = markagare.get();

            agareEntity.setAndel(finfoAgare.getAndel());
            agareEntity.setAgartyp(agartypFromFinfoAgartyp(finfoAgare.getTyp()));

            return markagareRepository.save(agareEntity);
        }
        else {
            var agareEntity = MarkagareEntity
                    .builder()
                    .person(personEntity)
                    .agartyp(agartypFromFinfoAgartyp(finfoAgare.getTyp()))
                    .andel(finfoAgare.getAndel())
                    .fastighet(fastighetEntity)
                    .kundId(kundId)
                    .build();

            personEntity.addMarkagare(agareEntity);
            return markagareRepository.save(agareEntity);
        }
    }

    private PersonEntity fromFinfoAgare(FinfoAgareDto finfoAgare, String kundId) {
        return PersonEntity
                .builder()
                .adress(finfoAgare.getAdress())
                .namn(finfoAgare.getNamn())
                .postnummer(finfoAgare.getPostnummer())
                .postort(finfoAgare.getPostort())
                .personnummer(finfoAgare.getPersonnummer())
                .kundId(kundId)
                .build();
    }

    private AgartypDto agartypFromFinfoAgartyp(String finfoAgartyp) {
        try {
            return AgartypDto.valueOf(finfoAgartyp);
        } catch (IllegalArgumentException e) {
            log.warn("Okänd ägartyp: {}", finfoAgartyp);
            return AgartypDto.OKAND;
        }
    }
}
