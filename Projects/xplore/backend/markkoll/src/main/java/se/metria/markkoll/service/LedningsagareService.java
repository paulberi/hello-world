package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.LedningsagareEntity;
import se.metria.markkoll.openapi.model.LedningsagareDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.LedningsagareRepository;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LedningsagareService {
    @NonNull
    private final LedningsagareRepository ledningsagareRepository;

    @NonNull
    private final KundRepository kundRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public LedningsagareDto addLedningsagare(String namn, String kundId) {
        var kundEntity = kundRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);

        var ledningsagareEntity = new LedningsagareEntity(namn, kundEntity);

        ledningsagareRepository.save(ledningsagareEntity);

        return modelMapper.map(ledningsagareEntity, LedningsagareDto.class);
    }

    @Transactional
    public void deleteLedningsagare(UUID ledningsagareId, String kundId) {
        ledningsagareRepository.deleteByIdAndKundId(ledningsagareId, kundId);
    }

    public List<LedningsagareDto> getLedningsagare(String kundId) {
        var entities = ledningsagareRepository.findAllByKundId(kundId);

        return CollectionUtil.modelMapperList(entities, modelMapper, LedningsagareDto.class);
    }
}
