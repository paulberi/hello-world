package se.metria.finfo.service.agare;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.finfo.openapi.model.AgareDto;
import se.metria.finfo.repository.FinfoAgareRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinfoAgareService {

    @NonNull
    private final FinfoAgareRepository finfoAgareRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<AgareDto> getAgare(Collection<UUID> finfoAgareIds) {
        var entities = finfoAgareRepository.findAllById(finfoAgareIds);

        return entities.stream()
            .map(e -> modelMapper.map(e, AgareDto.class))
            .collect(Collectors.toList());
    }
}
