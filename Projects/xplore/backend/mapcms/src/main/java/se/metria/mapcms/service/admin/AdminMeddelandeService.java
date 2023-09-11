package se.metria.mapcms.service.admin;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.commons.utils.LoggedInUser;
import se.metria.mapcms.entity.DialogEntity;
import se.metria.mapcms.entity.DialogPartEntity;
import se.metria.mapcms.entity.MeddelandeEntity;
import se.metria.mapcms.openapi.model.DialogPartRspDto;
import se.metria.mapcms.openapi.model.MeddelandeReqAdminDto;
import se.metria.mapcms.openapi.model.MeddelandeRspDto;
import se.metria.mapcms.repository.DialogPartRepository;
import se.metria.mapcms.repository.DialogRepository;
import se.metria.mapcms.repository.MeddelandeRepository;
import se.metria.mapcms.service.commons.CommonDialogpartService;
import se.metria.mapcms.service.commons.CommonMeddelandeService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminMeddelandeService {

    @NonNull
    private final MeddelandeRepository meddelandeRepository;

    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final DialogPartRepository dialogPartRepository;

    @NonNull
    private final LoggedInUser keycloakUser;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final CommonMeddelandeService commonMeddelandeService;

    @NonNull
    private final CommonDialogpartService commonDialogpartService;


    public MeddelandeRspDto createMeddelande(UUID kundId, UUID projektId, UUID dialogId, MeddelandeReqAdminDto meddelande, List<MultipartFile> filer) throws IOException {

        DialogEntity dialog=dialogRepository.getDialogWithDialogIdAndProjektId(kundId,projektId, dialogId).orElseThrow(EntityNotFoundException::new);

        AccessToken token= keycloakUser.ActiveUser();

        DialogPartEntity dialogPart=new DialogPartEntity();
        DialogPartRspDto dialogPartRspDto= new DialogPartRspDto();
        Optional<DialogPartEntity> dialogPartDB=dialogPartRepository.getPartWithEpostAndDialogId(token.getEmail(), dialogId);

        UUID partId=null;
        if(dialogPartDB.isPresent()){
            dialogPart=dialogPartDB.get();
            dialogPart.setDialog(dialog);
            partId=dialogPart.getId();
        }else{
            dialogPartRspDto=commonDialogpartService.CreateDialogpartFromAccessToken(dialogId, false);
            partId=dialogPartRspDto.getId();
        }
        MeddelandeEntity meddelandeEntity= commonMeddelandeService.createAdminMeddelande(partId, meddelande, filer);

        MeddelandeRspDto meddelandeRspDto=new MeddelandeRspDto();
        meddelandeRspDto.setFran(modelMapper.map(dialogPart, DialogPartRspDto.class));

        return modelMapper.map(meddelandeEntity, MeddelandeRspDto.class);
    }


    public List<MeddelandeRspDto> listMeddelanden(UUID kundId, UUID projektId, UUID dialogId){

        List<MeddelandeEntity> listAvMeddelande=dialogRepository.listMeddelandeForDialog(kundId,projektId,dialogId);
        List<MeddelandeRspDto> medRspDtoList=listAvMeddelande.stream().map(m->modelMapper.map(m,MeddelandeRspDto.class)).collect(Collectors.toList());

        return medRspDtoList;
    }


    public MeddelandeRspDto updateMeddelande(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId, MeddelandeReqAdminDto meddelandeReqAdminDto){

        MeddelandeEntity meddelande=meddelandeRepository.getMeddelandeWithId(kundId,projektId,dialogId,meddelandeId).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(meddelandeReqAdminDto,meddelande);
        meddelande=meddelandeRepository.save(meddelande);

        return modelMapper.map(meddelande,MeddelandeRspDto.class);
    }


    public MeddelandeRspDto getMeddelandeById(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId){

        MeddelandeEntity meddelande=meddelandeRepository.getMeddelandeWithId(kundId, projektId,dialogId,meddelandeId).orElseThrow(EntityNotFoundException::new);
            return modelMapper.map(meddelande,MeddelandeRspDto.class);
    }


    public void deleteMeddelandeById(UUID kundId, UUID projektId, UUID dialogId, UUID meddelandeId){

        MeddelandeEntity meddelande=meddelandeRepository.getMeddelandeWithId(kundId,projektId,dialogId,meddelandeId).orElseThrow(EntityNotFoundException::new);
        meddelandeRepository.delete(meddelande);
    }
}
