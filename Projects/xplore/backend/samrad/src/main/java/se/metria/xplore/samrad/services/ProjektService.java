package se.metria.xplore.samrad.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.metria.xplore.samrad.commons.modelMapper.ModelMapping;
import se.metria.xplore.samrad.entities.KundEntity;
import se.metria.xplore.samrad.entities.ProjektEntity;
import se.metria.xplore.samrad.openapi.model.ProjektDto;
import se.metria.xplore.samrad.openapi.model.SamradStatusDto;
import se.metria.xplore.samrad.repositories.KundRepository;
import se.metria.xplore.samrad.repositories.ProjektRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjektService {

    @NonNull
    private ProjektRepository projektRepository;

    @NonNull
    private KundRepository kundRepository;

    @NonNull
    private FilService filService;



    public ProjektDto saveProjekt(String kundid, ProjektDto projektDto){

        ProjektEntity newProjektEntity =new ProjektEntity();
        ModelMapping.mapAndMerge(projektDto, newProjektEntity);
        newProjektEntity.setStatus(SamradStatusDto.UTKAST);

        KundEntity kundEntity =kundRepository.findById(kundid).orElseThrow(()->new RuntimeException());

        newProjektEntity = projektRepository.save(newProjektEntity);
        kundEntity.getProjektEntityList().add(newProjektEntity);
        kundRepository.save(kundEntity);

        ModelMapping.mapAndMerge(newProjektEntity, projektDto);

        return projektDto;
    }



    public ProjektDto getProjektById(String kundid, UUID samradid){

        KundEntity kundEntity =kundRepository.findById(kundid).orElseThrow(()->new RuntimeException());

        ProjektDto projektDto=new ProjektDto();

        if(kundEntity!=null){
            ProjektEntity projektEntity = projektRepository.findById(samradid).orElseThrow(()->new RuntimeException());
            ModelMapping.mapAndMerge(projektEntity, projektDto);
        }

        return projektDto;
    }


    public List<ProjektDto> getProjektForKund(String kundid){

        KundEntity kundEntity =kundRepository.findById(kundid).orElseThrow(()->new RuntimeException());

        List<ProjektDto> projektDtoList=new ArrayList<>();

        kundEntity.getProjektEntityList().forEach(projektEntity -> {
            ProjektDto projektDto=new ProjektDto();
            ModelMapping.mapAndMerge(projektEntity, projektDto);
            projektDtoList.add(projektDto);
        });

        return projektDtoList;
    }
    

    public ProjektDto updateProjekt(ProjektDto projektDto,String kundid, UUID projektid){

        KundEntity kundEntity =kundRepository.findById(kundid).orElseThrow(()->new RuntimeException());

        ProjektDto projektDtoToUpdate=new ProjektDto();

        Optional<ProjektEntity> projektEntity=kundEntity.getProjektEntityList().stream().filter(projekt->
                projekt.getId().equals(projektid)).findAny();
        ProjektEntity samradToUpdate=projektEntity.get();

        ModelMapping.mapAndMerge(projektDto, samradToUpdate);
        samradToUpdate = projektRepository.save(samradToUpdate);
        kundRepository.save(kundEntity);

        ModelMapping.mapAndMerge(samradToUpdate, projektDtoToUpdate);

        return projektDtoToUpdate;
    }


    public void deleteProjekt(String kundid, UUID projektid){

        KundEntity kundEntity =kundRepository.findById(kundid).orElseThrow(()->new RuntimeException());
      
        Optional<ProjektEntity> projektEntity=kundEntity.getProjektEntityList().stream().filter(projekt->
                projekt.getId().equals(projektid)).findAny();
      
        ProjektEntity projektToDelete=projektEntity.get();

        if(projektToDelete.getStatus()== SamradStatusDto.UTKAST){
            kundEntity.getProjektEntityList().remove(projektToDelete);
            projektRepository.deleteById(projektid);
            kundRepository.save(kundEntity);
        }else{
            throw new IllegalStateException("Du får inte tar bort samrådet, borttagning är spärrat");
        }
    }
}
