package se.metria.xplore.kundconfig.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.xplore.kundconfig.openapi.model.PermissionsDto;
import se.metria.xplore.kundconfig.openapi.model.ProduktDto;
import se.metria.xplore.kundconfig.repository.PermissionsRepository;
import se.metria.xplore.kundconfig.util.CollectionUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionsService {

    @NonNull
    private final PermissionsRepository permissionsRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public List<PermissionsDto> getPermissions(String username, ProduktDto produktDto) {
        var entities = permissionsRepository.findByUserIdAndProdukt(username, produktDto.toString());

        return CollectionUtil.modelMapperList(entities, modelMapper, PermissionsDto.class);
    }
}
