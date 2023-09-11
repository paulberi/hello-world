package se.metria.mapcms.service.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.utils.LoggedInUser;
import se.metria.mapcms.entity.*;
import se.metria.mapcms.openapi.model.DialogPartRspDto;
import se.metria.mapcms.openapi.model.MeddelandeReqDto;
import se.metria.mapcms.openapi.model.MeddelandeRspDto;
import se.metria.mapcms.repository.*;
import se.metria.mapcms.service.commons.CommonMeddelandeService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PublikMeddelandeService {

    @NonNull
    private final LoggedInUser keycloakUser;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final DialogPartRepository dialogPartRepository;
    @NonNull
    private final FilRepository filRepository;

    @NonNull
    private final CommonMeddelandeService commonMeddelandeService;

    public MeddelandeRspDto createMeddelandeForUser(UUID kundId, UUID projektId, UUID dialogId, MeddelandeReqDto meddelande, List<MultipartFile> filer) throws IOException {

        AccessToken token= keycloakUser.ActiveUser();
        DialogPartEntity dialogPart = dialogPartRepository.getPartWithPnrAndDialogId(kundId, projektId, token.getPreferredUsername(), dialogId).orElseThrow(EntityNotFoundException::new);
        MeddelandeEntity meddelandeEntity= commonMeddelandeService.createPublikMeddelande(dialogPart.getId(), meddelande, filer);

        MeddelandeRspDto meddelandeRspDto=new MeddelandeRspDto();
        meddelandeRspDto.setFran(modelMapper.map(dialogPart, DialogPartRspDto.class));

        return modelMapper.map(meddelandeEntity, MeddelandeRspDto.class);
    }

    public FilEntity getDialogFilForUserMeddelande(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId, UUID filId){

        AccessToken token= keycloakUser.ActiveUser();
        Optional<DialogPartEntity> dialogPart=dialogPartRepository.getPartWithPnrForUser(token.getPreferredUsername(), meddelandeId);
        if(dialogPart.isPresent()){
            Optional<FilEntity> filEntity= filRepository.getFilForMeddelandeWithId(kundId, projektId, meddelandeId, filId);
            return filEntity.get();
        }else{
            throw new IllegalStateException("Användaren har inte tillgång till fil med id: "+filId);
        }
    }
}
