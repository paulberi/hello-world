package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.UtskicksnummerEntity;
import se.metria.markkoll.repository.UtskicksnummerRepository;

import javax.persistence.EntityNotFoundException;

@MarkkollService
@RequiredArgsConstructor
@Transactional
public class UtskicksnummerService {
    @NonNull
    private final UtskicksnummerRepository utskicksnummerRepository;

    public void create(String kundId) {
        var entity = UtskicksnummerEntity.builder()
            .kundId(kundId)
            .build();

        utskicksnummerRepository.save(entity);
    }

    public int get(String kundId) {
        var entity = utskicksnummerRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);

        return entity.getUtskicksnummer();
    }

    @Transactional
    public int increment(String kundId) {
        var entity = utskicksnummerRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);
        var utskicksnummer = entity.getUtskicksnummer() + 1;

        entity.setUtskicksnummer(utskicksnummer);

        return utskicksnummer;
    }
}
