package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.NisKallaEntity;
import se.metria.markkoll.openapi.model.NisKallaDto;
import se.metria.markkoll.repository.NisKallaRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class InstallningarService {
    
    @NonNull
    private final NisKallaRepository nisKallaRepository;

    private final ModelMapper modelMapper;

    public NisKallaDto getNisKalla(String kundId) {
        var nisKalla = nisKallaRepository.findById(kundId);
        if(nisKalla.isPresent()){
            return modelMapper.map(nisKalla, NisKallaDto.class);
        } else {
            return null;
        }
    }

    public NisKallaDto updateNisKalla(String kundId, NisKallaDto nisKalla) {
        var entity = modelMapper.map(nisKalla, NisKallaEntity.class);
        entity.setKundId(kundId);

        var savedEntity = nisKallaRepository.save(entity);

        return modelMapper.map(savedEntity, NisKallaDto.class);
    }
}
