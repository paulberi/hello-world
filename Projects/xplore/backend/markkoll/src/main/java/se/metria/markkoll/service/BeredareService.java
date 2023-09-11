package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.openapi.model.BeredareDto;
import se.metria.markkoll.repository.BeredareRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeredareService {
    @NonNull
    private final BeredareRepository beredareRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public void edit(BeredareDto beredareDto, UUID projektId) {
        var beredareEntity = beredareRepository.findByProjektId(projektId);

        modelMapper.map(beredareDto, beredareEntity);

        beredareRepository.save(beredareEntity);
    }

    public BeredareDto get(UUID projektId) {
        var beredareEntity = beredareRepository.findByProjektId(projektId);

        return modelMapper.map(beredareEntity, BeredareDto.class);
    }
}
