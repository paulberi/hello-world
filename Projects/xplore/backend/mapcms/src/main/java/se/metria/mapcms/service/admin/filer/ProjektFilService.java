package se.metria.mapcms.service.admin.filer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.entity.FilEntity;
import se.metria.mapcms.entity.ProjektEntity;
import se.metria.mapcms.openapi.model.FilReqDto;
import se.metria.mapcms.openapi.model.FilRspDto;
import se.metria.mapcms.repository.FilRepository;
import se.metria.mapcms.repository.ProjektRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjektFilService {

    @NonNull
    private FilService filService;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final FilRepository filRepository;

    public FilEntity getProjektFilById(UUID kundId, UUID projektId, UUID filId){

        FilEntity fil= filRepository.getFilForProjekt(kundId, projektId , filId).orElseThrow(EntityNotFoundException::new);
        return  fil;
    }

    public FilRspDto getProjektFilRspById(UUID kundId, UUID projektId, UUID filId){

        FilEntity fil= filRepository.getFilForProjekt(kundId, projektId , filId).orElseThrow(EntityNotFoundException::new);
        return  modelMapper.map(fil, FilRspDto.class);
    }

    public List<FilRspDto> listProjektFiler(UUID kundId, UUID projektId){

        ProjektEntity projektEntity=projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        List<FilEntity> filEntityList=projektEntity.getFiler();

        return filEntityList.stream().map(fil -> modelMapper.map(fil, FilRspDto.class)).collect(Collectors.toList());
    }

    public FilRspDto saveProjektFil(UUID kundId, UUID projektId, FilReqDto metadata, MultipartFile fil) throws IOException {

        ProjektEntity projektEntity=projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        var filEntity=filService.create(fil, metadata);

        projektEntity.getFiler().add(filEntity);
        projektRepository.save(projektEntity);

        FilRspDto filRspDto=new FilRspDto();
        modelMapper.map(filEntity,filRspDto);

        return filRspDto;
    }

    public void deleteProjektFile(UUID kundId, UUID projektId, UUID filId)  {

        ProjektEntity projektEntity=projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);

        List<FilEntity> filEntityList=projektEntity.getFiler();
        var fil=filEntityList.stream().filter(filEntity ->
                filEntity.getId().equals(filId)).findAny().orElseThrow(EntityNotFoundException::new);

        filEntityList.remove(fil);
        projektRepository.save(projektEntity);
        filRepository.deleteById(filId);

    }
}
