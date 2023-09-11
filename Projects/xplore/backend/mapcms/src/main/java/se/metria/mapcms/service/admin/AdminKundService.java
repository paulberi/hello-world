package se.metria.mapcms.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.commons.utils.FileNameAwareByteArrayResource;
import se.metria.mapcms.entity.KundEntity;
import se.metria.mapcms.entity.SprakEntity;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.repository.KundRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminKundService {

    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final AdminPlattformService plattformService;

    @NonNull
    private final ModelMapper modelMapper;

    public KundRspDto getByIdOrVhtNyckel(String idOrVhtNyckel) {
        try {
            UUID id = UUID.fromString(idOrVhtNyckel);
            KundEntity kund = kundRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            return modelMapper.map(kund, KundRspDto.class);
        } catch (IllegalArgumentException e) {}
        KundEntity kund = kundRepository.findByVhtNyckel(idOrVhtNyckel).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(kund, KundRspDto.class);
    }


    public Resource getLogotyp(UUID kundId) {
        KundEntity kund = kundRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);
        if (kund.getLogotyp() == null) throw new EntityNotFoundException();
        return new FileNameAwareByteArrayResource(kund.getLogotyp().getFil(), kund.getLogotyp().getFilnamn());
    }

    public List<SprakvalRspDto> listSprak(UUID kundId) {
        KundEntity kund = kundRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);
        return kund.getTillgangligaSprak().
                stream().
                map(n -> {
                    SprakvalRspDto sprakval = modelMapper.map(n, SprakvalRspDto.class);
                    sprakval.setStandardsprak(sprakval.getKod().equals(kund.getStandardsprak().getKod()));
                    return sprakval;
                }).collect(Collectors.toList());
    }

    public List<SprakvalRspDto> updateSprak(UUID kundId, List<SprakvalDto> sprakval) {
        KundEntity kund = kundRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);
        List<SprakDto> available = plattformService.listSprak();
        List<SprakvalRspDto> result = new ArrayList<>();

        available.forEach(n -> {
            SprakvalDto match = sprakval.stream().filter(s -> s.getKod().equals(n.getKod())).findAny().orElse(null);

            if (match != null) {
                SprakvalRspDto rsp = modelMapper.map(n, SprakvalRspDto.class);
                rsp.setStandardsprak(match.getStandardsprak());
                if (match.getStandardsprak()) {
                    kund.setStandardsprak(modelMapper.map(n, SprakEntity.class));
                }
                result.add(rsp);
            }
        });

        List<SprakEntity> l = result.stream().map(n -> modelMapper.map(n, SprakEntity.class)).collect(Collectors.toList());
        kund.setTillgangligaSprak(l);
        kundRepository.saveAndFlush(kund);

        return result;
    }
}
