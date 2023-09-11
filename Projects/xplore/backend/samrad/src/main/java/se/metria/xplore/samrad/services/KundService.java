package se.metria.xplore.samrad.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.metria.xplore.samrad.commons.modelMapper.ModelMapping;
import se.metria.xplore.samrad.entities.KundEntity;
import se.metria.xplore.samrad.openapi.model.KundDto;
import se.metria.xplore.samrad.repositories.KundRepository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KundService {

    @NonNull
    private KundRepository kundRepository;

    @Autowired
    EntityManager entityManager;


    public KundDto saveKund(KundDto kundDto){

        KundEntity kundEntity =new KundEntity();
        ModelMapping.mapAndMerge(kundDto, kundEntity);

        kundRepository.findAll().forEach(kund -> {
            if(kund.getId().equals(kundDto.getId())){
                throw new IllegalStateException("Det finns en kund med samma id redan");
            }if(kund.getSlug().equals(kundDto.getSlug())){
                throw new IllegalStateException("Det finns en kund med samma slug redan");
            }
        });

        kundEntity= kundRepository.save(kundEntity);
        ModelMapping.mapAndMerge(kundEntity, kundDto);

        return kundDto;
    }


    public KundDto getKundById(String kundid){

        KundEntity kundEntity = kundRepository.findById(kundid).orElseThrow(()->new RuntimeException());

        KundDto kundDto=new KundDto();
        ModelMapping.mapAndMerge(kundEntity, kundDto);

        return kundDto;
    }


    public KundDto getKundBySlug(String slug){
        KundEntity kundEntity=kundRepository.findBySlug(slug).orElseThrow();
        KundDto kundDto=new KundDto();
        ModelMapping.mapAndMerge(kundEntity, kundDto);
        return kundDto;
    }


    public List<KundDto>  KundList(){

        List<KundDto> kundDtoList=new ArrayList<>();

        kundRepository.findAll().forEach(kundEntity -> {
            KundDto kundDto=new KundDto();
            ModelMapping.mapAndMerge(kundEntity, kundDto);
            kundDtoList.add(kundDto);
        });

        return kundDtoList;
    }


    public KundDto updateKund(KundDto kundDto, String kundid){

        if(kundDto.getId().equals(kundid)){

            kundRepository.findAll().forEach(kund -> {
                if(kund.getSlug().equals(kundDto.getSlug()) && !kund.getId().equals(kundDto.getId())){
                    throw new IllegalStateException("Det finns en kund med samma slug redan");
                }
            });
        }else{
            throw new IllegalStateException("Id i endpointsen stämmer inte med inskickat kund id");
        }

        KundEntity kundEntity =kundRepository.findById(kundid).orElseThrow(()-> new RuntimeException());
        ModelMapping.mapAndMerge(kundDto, kundEntity);

        kundEntity = kundRepository.save(kundEntity);
        ModelMapping.mapAndMerge(kundEntity, kundDto);

        return kundDto;
    }


    public void deleteKund(String kundid){
        kundRepository.deleteById(kundid);
    }

}
