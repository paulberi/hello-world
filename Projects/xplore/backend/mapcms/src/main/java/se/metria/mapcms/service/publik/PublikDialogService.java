package se.metria.mapcms.service.publik;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.locationtech.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.utils.LoggedInUser;
import se.metria.mapcms.entity.DialogEntity;
import se.metria.mapcms.entity.MeddelandeEntity;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.repository.DialogRepository;
import se.metria.mapcms.repository.PersonRepository;
import se.metria.mapcms.repository.ProjektRepository;
import se.metria.mapcms.service.commons.CommonDialogService;
import se.metria.mapcms.service.commons.CommonDialogpartService;
import se.metria.mapcms.service.commons.CommonMeddelandeService;
import se.metria.mapcms.service.commons.PersonService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class PublikDialogService {

    @NonNull
    private final CommonDialogService commonDialogService;

    @NonNull
    private final CommonDialogpartService commonDialogpartService;

    @NonNull
    private final PersonService personService;

    @NonNull
    private final CommonMeddelandeService commonMeddelandeService;

    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final LoggedInUser keycloakUser;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final ProjektRepository projektRepository;

    public DialogKomplettDto getDialogForUserWithId(UUID kundId, UUID projektId, UUID dialogId){

        AccessToken token= keycloakUser.ActiveUser();

        DialogEntity dialog= dialogRepository.getDialogWithDialogIdAndProjektIdAndPnr(kundId, projektId, dialogId, token.getPreferredUsername()).orElseThrow(EntityNotFoundException::new);

        List<MeddelandeEntity> listAvMeddelande=dialogRepository.listMeddelandeForDialog(kundId,projektId,dialogId);
        List<MeddelandeRspDto> medRspDtoList=listAvMeddelande.stream().map(m->modelMapper.map(m,MeddelandeRspDto.class)).collect(Collectors.toList());

        DialogKomplettDto dialogKomplettDto=new DialogKomplettDto();
        dialogKomplettDto=modelMapper.map(dialog, DialogKomplettDto.class);
        dialogKomplettDto.setMeddelanden(medRspDtoList);
        modelMapper.map(dialog, dialogKomplettDto);

        return dialogKomplettDto;
    }

    public DialogRspDto startDialog(UUID kundId, UUID projektId, DialogReqDto dialog, MeddelandeReqDto meddelande, List<MultipartFile> filer) throws IOException, ParseException {

        var projekt= projektRepository.checkIfProjektExists(kundId, projektId);
        if(!projekt){
            throw new IllegalStateException("Projekt med id "+projektId+ " finns inte i databasen");
        }

        var dialogRspDto = commonDialogService.createPublicDialog(dialog, projektId);
        var dialogPartDto = commonDialogpartService.CreateDialogpartFromAccessToken(dialogRspDto.getId(), true);

        personService.addDialogpartToActiveUser(dialogPartDto.getId());
        commonMeddelandeService.createPublikMeddelande(dialogPartDto.getId(), meddelande, filer);

        dialogRspDto.setParter(Arrays.asList(dialogPartDto));

        return dialogRspDto;
    }

    public List<DialogRspDto> listDialogerForUser(UUID kundId, UUID projektId){

        AccessToken token= keycloakUser.ActiveUser();
        List<DialogEntity> dialoger= dialogRepository.listDialogForUser(kundId, projektId, token.getPreferredUsername());
        return dialoger.stream().map(dialog-> modelMapper.map(dialog, DialogRspDto.class)).collect(Collectors.toList());
    }
}
