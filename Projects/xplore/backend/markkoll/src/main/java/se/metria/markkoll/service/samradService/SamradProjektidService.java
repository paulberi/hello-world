package se.metria.markkoll.service.samradService;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.samradEntities.SamradProjektidEntity;
import se.metria.markkoll.openapi.model.SamradDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.samradRepository.SamradProjektidRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SamradProjektidService {

    @NonNull
    private SamradProjektidRepository samradProjektIdRepository;

    @NonNull
    private ProjektRepository projektRepository;


    public SamradDto addSamradId(String kundId, UUID markkollId, SamradDto samradDto){

        samradProjektIdRepository.findAll().forEach(samrad->{
            if(samrad.getSamradId().equals(samradDto.getId())){
                new IllegalStateException("Den här id finns redan i markkolls databas");
            }
        });

        SamradProjektidEntity samradProjektIdEntitty=new SamradProjektidEntity();
        samradProjektIdEntitty.setSamradId(samradDto.getSamradId());

        ProjektEntity markkollsProjekt=projektRepository.findById(markkollId).orElseThrow();

        if(kundId.equals(markkollsProjekt.getKundId())){
            samradProjektIdEntitty.setMarkkollProjekt(markkollsProjekt);
        }

        samradProjektIdEntitty=samradProjektIdRepository.save(samradProjektIdEntitty);

        samradDto.setId(samradProjektIdEntitty.getId());
        samradDto.setSamradId(samradProjektIdEntitty.getSamradId());

        return samradDto;
    }

    public List<SamradDto> listAllSamradId(String kundId){

        List<SamradDto> samradDtoList=new ArrayList<>();
        List<SamradProjektidEntity> samradProjektidEntityList = new ArrayList<>();

        samradProjektidEntityList =samradProjektIdRepository.findAll();

        samradProjektidEntityList.forEach(samrad->{
            SamradDto samradDto=new SamradDto();
            if(samrad.getMarkkollProjekt().getKundId().equals(kundId)){
                samradDto.setSamradId(samrad.getSamradId());
                samradDto.setId(samrad.getId());
                samradDtoList.add(samradDto);
            }
        });
        return samradDtoList;
    }

    public SamradDto getByMarkkollId(String markkollId){

        List<SamradProjektidEntity> samradProjektidEntityList = new ArrayList<>();

        samradProjektidEntityList =samradProjektIdRepository.findAll();

        Optional<SamradProjektidEntity> samrad=samradProjektidEntityList.stream().filter(projekt->
                projekt.getMarkkollProjekt().getId().toString().equals(markkollId)).findAny();

        SamradDto samradDto=new SamradDto();
        samradDto.setSamradId(samrad.get().getSamradId());
        samradDto.setId(samrad.get().getId());

        return samradDto;
    }

    public void deleteBySamradId(String samradId){
        SamradProjektidEntity s=samradProjektIdRepository.getBySamradId(samradId);
        s.setMarkkollProjekt(null);
        samradProjektIdRepository.deleteById(s.getId());
    }
}