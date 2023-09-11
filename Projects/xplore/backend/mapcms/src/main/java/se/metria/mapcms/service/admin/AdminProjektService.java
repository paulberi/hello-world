package se.metria.mapcms.service.admin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.entity.KundEntity;
import se.metria.mapcms.entity.ProjektEntity;
import se.metria.mapcms.entity.ProjektOversattningEntity;
import se.metria.mapcms.openapi.model.*;
import se.metria.mapcms.repository.KundRepository;
import se.metria.mapcms.repository.ProjektOversattningRepository;
import se.metria.mapcms.repository.ProjektRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProjektService {

    @NonNull
    private final ProjektRepository projektRepository;


    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final ProjektOversattningRepository projektOversattningRepository;

    @NonNull
    private final AdminKundService kundService;

    @NonNull
    private final ModelMapper modelMapper;

    public ProjektRspDto getProjektForKund(UUID kundId, UUID projektId, String lang) {
        ProjektEntity p = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(oversattProjekt(p, lang), ProjektRspDto.class);
    }

    public List<ProjektRspDto> listProjektForKund(UUID kundId, String lang) {
        List<ProjektEntity> projekt = projektRepository.listProjektForKund(kundId).orElseThrow(EntityNotFoundException::new);
        return projekt.stream().map(n -> modelMapper.map(oversattProjekt(n, lang), ProjektRspDto.class)).collect(Collectors.toList());
    }

    public ProjektEntity oversattProjekt(ProjektEntity p, String lang) {
        if (lang != null && !lang.isBlank()) {
            Optional<ProjektOversattningEntity> o = p.getOversattningar().stream().filter(n -> n.getSprakkod().equals(lang)).findAny();
            o.ifPresent(n -> modelMapper.map(n, p));
        }
        return p;
    }


    public ProjektRspDto createProjektForKund(UUID kundId, ProjektReqDto projektReqDto) {
        KundEntity kund = kundRepository.findById(kundId).orElseThrow(EntityNotFoundException::new);
        ProjektEntity projekt = modelMapper.map(projektReqDto, ProjektEntity.class);

        Optional<ProjektEntity> projektEntity=projektRepository.getProjektForKundBySlugOnly(projektReqDto.getSlug());

            if(projektEntity.isPresent()){
                throw new IllegalStateException("Det finns en projekt med "+projektReqDto.getSlug()+" slug i databasen");
            }

        kund.addProjekt(projekt);
        kund = kundRepository.saveAndFlush(kund);

        projekt = kund.getProjekt().stream().filter(n -> n.getSlug().equals(projektReqDto.getSlug())).findAny().orElseThrow(InternalError::new);
        return modelMapper.map(projekt, ProjektRspDto.class);
    }



    public ProjektRspDto updateProjektForKund(UUID kundId, UUID projektId, ProjektReqDto projektReqDto){

        Optional<ProjektEntity> projektEntity=projektRepository.getProjektForKundBySlugOnly(projektReqDto.getSlug());

        if(projektEntity.isPresent() && !projektEntity.get().getId().equals(projektId)){
            throw new IllegalStateException("Det finns en projekt med "+projektReqDto.getSlug()+" slug i databasen");
        }

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        modelMapper.map(projektReqDto, projekt);

        projekt=projektRepository.save(projekt);
        return modelMapper.map(projekt, ProjektRspDto.class);
    }




    public void deleteProjekt(UUID kundId, UUID projektId){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);

        if(projekt.getPubliceringStatus()== PubliceringStatusDto.UTKAST){
            projektRepository.deleteById(projektId);
        }else{
            throw new IllegalStateException("Du får inte tar bort samrådet, borttagning är spärrat");
        }
    }



    public ProjektOversattningDto oversattaProjekt(UUID kundId, UUID projektId, ProjektOversattningDto projektOversattningDto){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        Optional<ProjektOversattningEntity> isOversattning=projektOversattningRepository.getByprojektIdAndSprakkod(projektId, projektOversattningDto.getSprakkod());

        kundService.listSprak(kundId).stream().filter(sprak ->
                        sprak.getKod().equals(projektOversattningDto.getSprakkod()))
                .findAny().orElseThrow(() -> new IllegalStateException("Kunden har inte prenumererat för "+projektOversattningDto.getSprakkod()));

        if(!isOversattning.isPresent() && !projektOversattningDto.getSprakkod().equalsIgnoreCase(projekt.getKund().getStandardsprak().getKod())){
            ProjektOversattningEntity oversattning= modelMapper.map(projektOversattningDto, ProjektOversattningEntity.class);
            oversattning.setProjektId(projektId);
            projekt.addOversattning(oversattning);

            modelMapper.map(oversattning,projektOversattningDto);

        }else{
            throw new IllegalStateException("Det finns en översattning för "+projektOversattningDto.getSprakkod()
                    +" i databas redan för detta projekt, vill du göra ändringar på den, gör en put request");
        }
        projektRepository.save(projekt);
        return  projektOversattningDto;
    }


    public ProjektOversattningDto getOversattningById(UUID kundId, UUID projektId, String sprakkod){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);

        ProjektOversattningDto oversattning=new ProjektOversattningDto();
        ProjektOversattningEntity oversattningFromDB=projektOversattningRepository.getByprojektIdAndSprakkod(projektId, sprakkod).orElseThrow(EntityNotFoundException::new);

        modelMapper.map(oversattningFromDB, oversattning);

        if(projekt.getId().equals(oversattningFromDB.getProjekt().getId())){
            return oversattning;
        }else{
            throw new IllegalStateException("Projektet tillhör inte kunden");
        }
    }


    public List<ProjektOversattningDto> listOversattningarForKundProjekt(UUID kundId, UUID projektId){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        List<ProjektOversattningDto> sprakList=projekt.getOversattningar().stream().map(sprak->
                modelMapper.map(sprak,ProjektOversattningDto.class)).collect(Collectors.toList());
        return sprakList;
    }


    public ProjektOversattningDto updateOversattningForKundProjekt(UUID kundId, UUID projektId, String sprakkod, ProjektTextDto projektTextDto){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);

        ProjektOversattningEntity projektOversattning=projektOversattningRepository.getByprojektIdAndSprakkod(projektId, sprakkod).orElseThrow(EntityNotFoundException::new);

        if(projekt.getId().equals(projektOversattning.getProjekt().getId())){
            modelMapper.map(projektTextDto,projektOversattning);
            projektOversattning=projektOversattningRepository.saveAndFlush(projektOversattning);
            ProjektOversattningDto projektOversattningDto=new ProjektOversattningDto();
            modelMapper.map(projektOversattning, projektOversattningDto);

            return  projektOversattningDto;
        }else{
            throw new IllegalStateException("Projektet tillhör inte kunden");
        }
    }


    public void deleteOversattningForKundProjekt(UUID kundId, UUID projektId, String sprakkod){

        ProjektEntity projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        ProjektOversattningEntity projektOversattning=projektOversattningRepository.getByprojektIdAndSprakkod(projektId, sprakkod).orElseThrow(EntityNotFoundException::new);

        projekt.getOversattningar().remove(projektOversattning);
        projektRepository.save(projekt);

        projektOversattningRepository.delete(projektOversattning);
    }
}
