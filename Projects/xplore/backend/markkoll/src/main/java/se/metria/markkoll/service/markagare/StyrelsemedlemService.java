package se.metria.markkoll.service.markagare;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.config.ModelMapperConfiguration;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.entity.markagare.StyrelsemedlemEntity;
import se.metria.markkoll.openapi.finfo.model.FinfoStyrelsemedlemDto;
import se.metria.markkoll.openapi.model.AgartypDto;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.StyrelsemedlemDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.util.MapMarkagare;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StyrelsemedlemService {
    @NonNull
    private final AvtalspartRepository avtalspartRepository;

    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final ModelMapper modelMapper = new ModelMapperConfiguration().modelMapper();

    @Transactional
    public void addStyrelsemedlemmar(UUID avtalId, UUID samfId, String kundId, Collection<FinfoStyrelsemedlemDto> styrelsemedlemmar) {
        var avtalEntity = avtalRepository.getReferenceById(avtalId);
        var fastighetEntity = fastighetRepository.getReferenceById(samfId);

        var avtalsparter = styrelsemedlemmar.stream()
            .map(sm -> createStyrelsemedlem(sm, kundId, fastighetEntity, avtalEntity))
            .collect(Collectors.toList());

        avtalspartRepository.saveAll(avtalsparter);
    }

    public List<StyrelsemedlemDto> getStyrelsemedlemmar(String kundId, UUID samfId) {
        var avtalspartEntities = avtalspartRepository.findStyrelsemedlemmar(kundId, samfId);

        var styrelsemedlemDtos = new ArrayList<StyrelsemedlemDto>();
        for (var avtalspart: avtalspartEntities) {
            var styrelsemedlem = avtalspart.getMarkagare().getStyrelsemedlem();
            var styrelsemedlemDto = new StyrelsemedlemDto()
                .funktion(styrelsemedlem.getFunktion())
                .markagare(MapMarkagare.mapMarkagareDto(
                    avtalspart, avtalspart.getKontaktpersonAvtal() != null, modelMapper)
                );
            styrelsemedlemDtos.add(styrelsemedlemDto);
        }

        return styrelsemedlemDtos;
    }

    private AvtalspartEntity
    createStyrelsemedlem(FinfoStyrelsemedlemDto styrelsemedlem,
                         String kundId,
                         FastighetEntity fastighetEntity,
                         AvtalEntity avtalEntity) {

        var person = new PersonEntity();
        person.setNamn(styrelsemedlem.getNamn());
        person.setPostnummer(styrelsemedlem.getPostnummer());
        person.setPostort(styrelsemedlem.getPostort());
        person.setAdress(styrelsemedlem.getUtdelningsadress());
        person.setKundId(kundId);

        var markagare = new MarkagareEntity();
        markagare.setAgartyp(AgartypDto.STYRELSEMEDLEM);
        markagare.setKundId(kundId);
        markagare.setFastighet(fastighetEntity);
        markagare.setPerson(person);

        var avtalspart = new AvtalspartEntity();
        avtalspart.setMarkagare(markagare);
        avtalspart.setAvtal(avtalEntity);
        avtalspart.setAvtalsstatus(AvtalsstatusDto.EJ_BEHANDLAT);

        var styrelsemedlemEntity = new StyrelsemedlemEntity();
        styrelsemedlemEntity.setFunktion(String.join(", ", styrelsemedlem.getFunktion()));
        styrelsemedlemEntity.setMarkagare(markagare);

        return avtalspart;
    }
}