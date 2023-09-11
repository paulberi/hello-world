package se.metria.mapcms.service.admin;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.entity.DialogEntity;
import se.metria.mapcms.entity.ProjektEntity;
import se.metria.mapcms.openapi.model.DialogReqAdminDto;
import se.metria.mapcms.openapi.model.DialogRspDto;
import se.metria.mapcms.openapi.model.MeddelandeReqAdminDto;
import se.metria.mapcms.repository.DialogRepository;
import se.metria.mapcms.repository.ProjektRepository;
import se.metria.mapcms.service.commons.CommonDialogService;
import se.metria.mapcms.service.commons.CommonDialogpartService;
import se.metria.mapcms.service.commons.CommonMeddelandeService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static se.metria.mapcms.commons.utils.GeoJsonParser.convertGeojsonStringToGeometry;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminDialogService {

    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final CommonMeddelandeService commonMeddelandeService;

    @NonNull
    private final CommonDialogService commonDialogService;

    @NonNull
    private final CommonDialogpartService commonDialogpartService;


    public DialogRspDto startDialog(UUID kundId, UUID projektId, DialogReqAdminDto dialog, MeddelandeReqAdminDto meddelande,  List<MultipartFile> filer) throws IOException, ParseException {

        var projekt= projektRepository.checkIfProjektExists(kundId, projektId);
        if(!projekt){
            throw new IllegalStateException("Projekt med id "+projektId+ " finns inte i databasen");
        }

        var dialogRspDto = commonDialogService.createAdminDialog(dialog, projektId);
        var dialogPartDto = commonDialogpartService.CreateDialogpartFromAccessToken(dialogRspDto.getId(), true);

        commonMeddelandeService.createAdminMeddelande(dialogPartDto.getId(), meddelande, filer);
        dialogRspDto.setParter(Arrays.asList(dialogPartDto));

        return dialogRspDto;
    }


    public List<DialogRspDto> listDialogForProjekt(UUID kundId, UUID projektId){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);

        List<DialogEntity> dialogList=projekt.getDialoger();
        return dialogList.stream().map(dialog->modelMapper.map(dialog, DialogRspDto.class)).collect(Collectors.toList());
    }

    public DialogRspDto getDialogWithId(UUID kundId, UUID projektId, UUID dialogId){

        DialogEntity dialog=dialogRepository.getDialogWithDialogIdAndProjektId(kundId, projektId, dialogId).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(dialog, DialogRspDto.class);
    }


    public DialogRspDto updateDialogWithId(UUID kundId, UUID projektId, UUID dialogId, DialogReqAdminDto dialogReqAdminDto) throws ParseException {

        DialogEntity dialog=dialogRepository.getDialogWithDialogIdAndProjektId(kundId,projektId, dialogId).orElseThrow(EntityNotFoundException::new);

        modelMapper.map(dialogReqAdminDto, dialog);
        if(dialogReqAdminDto.getPlats()!=null){
            String geoJson = dialogReqAdminDto.getPlats().replace("'", "\"");
            Geometry geom = convertGeojsonStringToGeometry(geoJson);
            dialog.setPlats(geom);
        }
        dialogRepository.save(dialog);

        return  modelMapper.map(dialog, DialogRspDto.class);
    }


    public void deleteDialogWithId(UUID kundId, UUID projektId, UUID dialogId){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        DialogEntity dialog=dialogRepository.getDialogWithDialogIdAndProjektId(kundId,projektId, dialogId).orElseThrow(EntityNotFoundException::new);

        projekt.getDialoger().remove(dialog);
        dialog.getDialogParter().forEach(dialogpart->{
            dialogpart.setDialog(null);
        });
        dialog.getDialogParter().clear();
        dialogRepository.delete(dialog);
    }
}
