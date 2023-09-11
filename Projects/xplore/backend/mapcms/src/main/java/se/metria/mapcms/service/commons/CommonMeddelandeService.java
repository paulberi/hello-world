package se.metria.mapcms.service.commons;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.entity.DialogPartEntity;
import se.metria.mapcms.entity.FilEntity;
import se.metria.mapcms.entity.MeddelandeEntity;
import se.metria.mapcms.openapi.model.MeddelandeReqAdminDto;
import se.metria.mapcms.openapi.model.MeddelandeReqDto;
import se.metria.mapcms.openapi.model.PubliceringStatusDto;
import se.metria.mapcms.repository.DialogPartRepository;
import se.metria.mapcms.repository.MeddelandeRepository;
import se.metria.mapcms.service.admin.filer.FilService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class CommonMeddelandeService {

    @NonNull
    private final FilService filService;

    @NonNull
    private final DialogPartRepository dialogPartRepository;

    @NonNull
    private final MeddelandeRepository meddelandeRepository;

    @NonNull
    private  final ModelMapper modelMapper = new ModelMapper();

    public MeddelandeEntity createMeddelandeForAdmin(DialogPartEntity part, MeddelandeReqAdminDto meddelandeReqAdminDto, List<MultipartFile> filer) throws IOException {

        MeddelandeEntity meddelande=modelMapper.map(meddelandeReqAdminDto,MeddelandeEntity.class);
        List<FilEntity> fileList=filService.saveFiler(filer);
        meddelande.setFiler(fileList);
        meddelande.setFran(part);
        return meddelande;
    }

    public MeddelandeEntity createAdminMeddelande(UUID dialogpartId, MeddelandeReqAdminDto meddelande, List<MultipartFile> filer) throws IOException {
        var dialogPart = dialogPartRepository.getReferenceById(dialogpartId);

        MeddelandeEntity meddelandeEntity = createMeddelandeForAdmin(dialogPart, meddelande, filer);
        meddelandeEntity.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        meddelandeEntity=meddelandeRepository.save(meddelandeEntity);

        return  meddelandeEntity;
    }

    public MeddelandeEntity createPublikMeddelande(UUID dialogpartId, MeddelandeReqDto meddelande, List<MultipartFile> filer) throws IOException {
        var dialogPart = dialogPartRepository.getReferenceById(dialogpartId);

        MeddelandeEntity meddelandeEntity= _createMeddelandeForPublik(dialogPart, meddelande, filer);
        meddelandeEntity.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        meddelandeEntity=meddelandeRepository.save(meddelandeEntity);
        return  meddelandeEntity;
    }

    private MeddelandeEntity _createMeddelandeForPublik(DialogPartEntity part, MeddelandeReqDto meddelandeReqDto, List<MultipartFile> filer) throws IOException {

        MeddelandeEntity meddelande=modelMapper.map(meddelandeReqDto,MeddelandeEntity.class);
        List<FilEntity> fileList=filService.saveFiler(filer);
        meddelande.setFiler(fileList);
        meddelande.setFran(part);
        return meddelande;
    }
}
