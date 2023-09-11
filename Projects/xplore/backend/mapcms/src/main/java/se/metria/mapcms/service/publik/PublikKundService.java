package se.metria.mapcms.service.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.commons.utils.FileNameAwareByteArrayResource;
import se.metria.mapcms.entity.KundEntity;
import se.metria.mapcms.openapi.model.KundRspDto;
import se.metria.mapcms.repository.KundRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class PublikKundService {

    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public KundRspDto getBySlug(String slug) {
        KundEntity kund = kundRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(kund, KundRspDto.class);
    }

    public Resource getLogotypBySlug(String slug) {
        KundEntity kund = kundRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);
        if (kund.getLogotyp() == null) throw new EntityNotFoundException();
        return new FileNameAwareByteArrayResource(kund.getLogotyp().getFil(), kund.getLogotyp().getFilnamn());
    }
}
