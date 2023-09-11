package se.metria.mapcms.service.commons;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.entity.DialogEntity;
import se.metria.mapcms.entity.ProjektEntity;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.repository.DialogRepository;
import se.metria.mapcms.repository.ProjektRepository;

import java.util.UUID;

import static se.metria.mapcms.commons.utils.GeoJsonParser.convertGeojsonStringToGeometry;

@RequiredArgsConstructor
@Service
@Transactional
public class CommonDialogService {
    @NonNull
    private final DialogRepository dialogRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private ModelMapper modelMapper;

    public DialogRspDto createPublicDialog(DialogReqDto dialog, UUID projektId) throws ParseException {
        var projekt = projektRepository.getReferenceById(projektId);

        DialogEntity dialogEntity= createDialog(projekt, modelMapper.map(dialog,DialogEntity.class), dialog.getPlats());
        dialogEntity.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        dialogEntity.setDialogStatus(DialogStatusDto.PAGAENDE);
        dialogEntity=dialogRepository.save(dialogEntity);

        return modelMapper.map(dialogEntity, DialogRspDto.class);
    }

    public DialogRspDto createAdminDialog(DialogReqAdminDto dialog, UUID projektId) throws ParseException {
        ProjektEntity projekt = projektRepository.getReferenceById(projektId);

        DialogEntity dialogEntity= createDialog(projekt, modelMapper.map(dialog,DialogEntity.class), dialog.getPlats());
        dialogEntity.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        dialogEntity.setDialogStatus(DialogStatusDto.PAGAENDE);
        dialogEntity=dialogRepository.save(dialogEntity);

        return modelMapper.map(dialogEntity, DialogRspDto.class);
    }

    private DialogEntity createDialog(ProjektEntity projekt, DialogEntity dialog, String geoJson) throws ParseException {

        dialog.setProjekt(projekt);
        Geometry geom = convertGeojsonStringToGeometry(geoJson);
        dialog.setPlats(geom);
        return  dialog;
    }
}
