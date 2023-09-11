package se.metria.mapcms.service.admin.filer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.mapcms.entity.FilEntity;
import se.metria.mapcms.openapi.model.FilReqDto;
import se.metria.mapcms.openapi.model.FilRspDto;
import se.metria.mapcms.repository.FilRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class FilService {

    @NonNull
    private FilRepository filRepository;

    @NonNull
    private final ModelMapper modelMapper;


    public FilEntity getFileById(UUID id) {

        var entity= filRepository.findById(id).orElseThrow();
        return entity;
    }

    public FilEntity create(MultipartFile file, FilReqDto filReqDto) throws IOException {

        if(file==null){
            throw new RuntimeException("filen är tomt");
        }
        InputStream is= file.getInputStream();

        FilEntity filEntity =new FilEntity();
        filEntity.setFil(is.readAllBytes());

        if(filReqDto!=null){
            modelMapper.map(filReqDto, filEntity);
            filEntity.setFilnamn(file.getOriginalFilename());
        }
        else{
            filEntity.setMimetyp(file.getContentType());
            filEntity.setFilnamn(file.getOriginalFilename());
        }

        FilEntity fileToSave = filRepository.saveAndFlush(filEntity);

        return fileToSave;
    }


    public FilRspDto updateFile(MultipartFile file, FilReqDto filReqDto, UUID filId) throws IOException {

        var entity= filRepository.findById(filId).orElseThrow(EntityNotFoundException::new);

        if(file==null){
            entity.setMimetyp(filReqDto.getMimetyp());
            entity.setBeskrivning(filReqDto.getBeskrivning());
            entity=filRepository.saveAndFlush(entity);
        }
        else{
            InputStream is= file.getInputStream();
            entity.setFil(is.readAllBytes());
            entity.setMimetyp(filReqDto.getMimetyp());
            entity.setBeskrivning(filReqDto.getBeskrivning());
            entity=filRepository.saveAndFlush(entity);

        }
        FilRspDto filRspDto=new FilRspDto();
        modelMapper.map(entity,filRspDto);

        return filRspDto;
    }

    public List<FilEntity> saveFiler(List<MultipartFile> filer) throws IOException {
        List<FilEntity> fileList=new ArrayList<>();
        if(filer!=null && filer.size()>0){
            for(MultipartFile file:filer){
                FilEntity filEntity=new FilEntity();
                filEntity=create(file, null);
                fileList.add(filEntity);
            }
        }
        return fileList;
    }
}
