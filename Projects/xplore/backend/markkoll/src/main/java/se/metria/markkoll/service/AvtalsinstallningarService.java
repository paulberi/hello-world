package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.projekt.AvtalsinstallningarEntity;
import se.metria.markkoll.openapi.model.AvtalsinstallningarDto;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.service.projekt.AvtalUpdaterFactory;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvtalsinstallningarService {
    @NonNull
    private final AvtalUpdaterFactory avtalUpdaterFactory;

    @NonNull
    private final AvtalsinstallningarRepository avtalsinstallningarRepository;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public AvtalsinstallningarDto getAvtalsinstallningar(UUID projektId) {
        var entity = avtalsinstallningarRepository.findById(projektId)
            .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, AvtalsinstallningarDto.class);
    }

    @Transactional
    public void updateAvtalsinstallningar(UUID projektId, AvtalsinstallningarDto avtalsinstallningar) {
        var entity = avtalsinstallningarRepository.findById(projektId)
            .orElseThrow(EntityNotFoundException::new);

        var avtalstyperChanged = changedAvtalstyper(avtalsinstallningar, entity);

        modelMapper.map(avtalsinstallningar, entity);
        avtalsinstallningarRepository.save(entity);

        if (!avtalstyperChanged.isEmpty()) {
            var fastighetIds = fastighetRepository.getFastighetIdsWithAvtalstyper(projektId, avtalstyperChanged);
            avtalUpdaterFactory.getAvtalUpdater(projektId).updateVarderingsprotokoll(projektId, fastighetIds);
        }
    }

    private boolean
    hasBerakningAbel07Changed(AvtalsinstallningarDto avtalsinstallningarDto,
                              AvtalsinstallningarEntity avtalsinstallningarEntity) {

        return avtalsinstallningarDto.getBerakningAbel07() != avtalsinstallningarEntity.getBerakningAbel07();
    }

    private boolean
    hasBerakningRevChanged(AvtalsinstallningarDto avtalsinstallningarDto,
                           AvtalsinstallningarEntity avtalsinstallningarEntity) {

        return avtalsinstallningarDto.getBerakningRev() != avtalsinstallningarEntity.getBerakningRev();
    }

    private List<AvtalstypDto> changedAvtalstyper(AvtalsinstallningarDto avtalsinstallningarDto,
                                                  AvtalsinstallningarEntity avtalsinstallningarEntity) {

        var avtalstyper = new ArrayList<AvtalstypDto>();

        if (hasBerakningAbel07Changed(avtalsinstallningarDto, avtalsinstallningarEntity)) {
            avtalstyper.add(AvtalstypDto.ABEL07);
        }
        if (hasBerakningRevChanged(avtalsinstallningarDto, avtalsinstallningarEntity)) {
            avtalstyper.add(AvtalstypDto.REV);
        }

        return avtalstyper;
    }
}
