package se.metria.mapcms.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.openapi.model.SprakDto;
import se.metria.mapcms.repository.SprakRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPlattformService {

    @NonNull
    private final SprakRepository sprakRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public List<SprakDto> listSprak() {
        return sprakRepository.findAll().stream().map(n -> modelMapper.map(n, SprakDto.class)).collect(Collectors.toList());
    }

}
