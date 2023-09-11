package se.metria.markkoll.service.indata;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.indata.KallfilEntity;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.repository.indata.KallfilRepository;
import se.metria.markkoll.service.FilService;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KallfilService {
    @NonNull
    private final FilService filService;

    @NonNull
    private final KallfilRepository kallfilRepository;

    /* Lägg i ny transaktion. Vi vill att information om källfilen ska sparas även om importen av den skulle fallera i
    * ett senare skede, just för felsökningssyfte. */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public UUID create(Resource shapeFile, IndataTypDto indataTyp, String kundId) throws IOException {
        var fil = filService.create(shapeFile, "application/zip", kundId);

        var entity = new KallfilEntity();
        entity.setFil(fil);
        entity.setIndataTyp(indataTyp);
        entity = kallfilRepository.save(entity);

        return entity.getId();
    }
}
