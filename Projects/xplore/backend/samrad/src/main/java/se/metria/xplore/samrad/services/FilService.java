package se.metria.xplore.samrad.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import se.metria.xplore.samrad.commons.modelMapper.ModelMapping;
import se.metria.xplore.samrad.commons.utils.ThumbnailGenerator;
import se.metria.xplore.samrad.entities.FilEntity;
import se.metria.xplore.samrad.entities.ProjektEntity;
import se.metria.xplore.samrad.openapi.model.FilDto;
import se.metria.xplore.samrad.repositories.FilRepository;
import se.metria.xplore.samrad.repositories.ProjektRepository;

import javax.management.AttributeNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FilService {

    @NonNull
    private FilRepository filRepository;


    public FilEntity getFileById(UUID id) {
        var entity= filRepository.findById(id).orElseThrow();

        return entity;
    }

    public List<FilDto> allFiles(){
        List<FilEntity> filList= filRepository.findAll();
        List<FilDto> filDtoList=new ArrayList<>();

        filList.forEach(filEntity -> {
            FilDto filDto=new FilDto();
            ModelMapping.mapAndMerge(filEntity, filDto);
            filDtoList.add(filDto);
        });
        return  filDtoList;
    }

    public void deleteFile(UUID id) throws AttributeNotFoundException {

        filRepository.findById(id).orElseThrow(() -> new AttributeNotFoundException("File not found for id :: " + id));
        filRepository.deleteById(id);
    }

    @Transactional
    public FilEntity create( MultipartFile file) throws IOException {

        if(file==null){
            throw new RuntimeException("filen är tomt");
        }
        InputStream is= file.getInputStream();

        FilEntity filEntity =new FilEntity();
        filEntity.setFil(is.readAllBytes());
        filEntity.setMimetyp(file.getContentType());
        filEntity.setFilNamn(file.getOriginalFilename());
        
        ThumbnailGenerator.createThumbnail(filEntity);

        FilEntity fileToSave = filRepository.saveAndFlush(filEntity);

        return fileToSave;
    }
}