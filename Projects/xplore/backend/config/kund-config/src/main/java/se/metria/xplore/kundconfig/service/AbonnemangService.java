package se.metria.xplore.kundconfig.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.xplore.kundconfig.entity.AbonnemangEntity;
import se.metria.xplore.kundconfig.openapi.model.AbonnemangDto;
import se.metria.xplore.kundconfig.repository.AbonnemangRepository;
import se.metria.xplore.kundconfig.repository.KundConfigRepository;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AbonnemangService {

    @NonNull
    private final AbonnemangRepository abonnemangRepository;

    @NonNull
    private final KundConfigRepository kundConfigRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public AbonnemangDto addAbonnemang(String kundId, @Valid AbonnemangDto abonnemangDto) {
        var entity = modelMapper.map(abonnemangDto, AbonnemangEntity.class);
        var kund = kundConfigRepository.getOne(kundId);
        entity.setKund(kund);

        var saved = abonnemangRepository.saveAndFlush(entity);

        return modelMapper.map(saved, AbonnemangDto.class);
    }

    public void deleteAbonnemang(UUID abonnemangId) {
        this.abonnemangRepository.deleteById(abonnemangId);
    }
}
