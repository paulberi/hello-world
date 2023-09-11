package se.metria.mapcms.service.commons;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.commons.utils.LoggedInUser;
import se.metria.mapcms.entity.DialogEntity;
import se.metria.mapcms.entity.DialogPartEntity;
import se.metria.mapcms.openapi.model.DialogPartRspDto;
import se.metria.mapcms.repository.DialogPartRepository;
import se.metria.mapcms.repository.DialogRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class CommonDialogpartService {
    @NonNull
    private final LoggedInUser keycloakUser;

    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final DialogPartRepository dialogPartRepository;

    @NonNull
    private ModelMapper modelMapper;

    public DialogPartRspDto CreateDialogpartFromAccessToken(UUID dialogId, boolean skapare) {
        var dialogEntity = dialogRepository.getReferenceById(dialogId);

        AccessToken token= keycloakUser.ActiveUser();
        var dialogPart = createDialogPart(token, dialogEntity, skapare);
        dialogPart = dialogPartRepository.save(dialogPart);

        return modelMapper.map(dialogPart, DialogPartRspDto.class);
    }

    private DialogPartEntity createDialogPart(AccessToken token, DialogEntity dialog, boolean skapare){
        DialogPartEntity dialogPart= new DialogPartEntity();
        String email = token.getEmail();
        if(email==null){
            email=token.getPreferredUsername()+"@mail.com";
        }
        dialogPart.setEpost(email);
        dialogPart.setSkapare(skapare);
        dialogPart.setDialog(dialog);
        dialogPart.setNamn(token.getGivenName()+" "+token.getFamilyName());
        return dialogPart;
    }
}
