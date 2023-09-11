package se.metria.markkoll.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.BerakningAbel07Dto;
import se.metria.markkoll.openapi.model.BerakningRevDto;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaghallareService {
    @NonNull
    private final AvtalsinstallningarRepository avtalsinstallningarRepository;

    public List<AvtalstypDto> vaghallareAvtalstyperInMap(UUID projektId) {
        var berakningRev = avtalsinstallningarRepository.getBerakningRev(projektId);
        var berakningAbel07 = avtalsinstallningarRepository.getBerakningAbel07(projektId);

        var avtalstyper = new ArrayList<AvtalstypDto>();

        if (berakningRev != BerakningRevDto.ENBART_REV) {
            avtalstyper.add(AvtalstypDto.REV);
        }
        if (berakningAbel07 != BerakningAbel07Dto.ENBART_ABEL07) {
            avtalstyper.add(AvtalstypDto.ABEL07);
        }

        return avtalstyper;
    }

    public List<AvtalstypDto> vaghallareAvtalstyperInVp(UUID versionId) {
        var entity = avtalsinstallningarRepository.findByVersionId(versionId);

        var avtalstyper = new ArrayList<AvtalstypDto>();

        if (entity.getBerakningRev() == BerakningRevDto.REV_FULL_ERSATTNING) {
            avtalstyper.add(AvtalstypDto.REV);
        }
        if (entity.getBerakningAbel07() == BerakningAbel07Dto.ABEL07_FULL_ERSATTNING) {
            avtalstyper.add(AvtalstypDto.ABEL07);
        }

        return avtalstyper;
    }
}
