package se.metria.markkoll.service.logging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.logging.projektlogg.*;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.repository.infobrev.InfobrevsjobbRepository;
import se.metria.markkoll.repository.logging.projektlogg.ProjektLoggRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjektLoggService {
    @NonNull
    private final ProjektLoggRepository projektLoggRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final AvtalsjobbRepository avtalsjobbRepository;

    @NonNull
    private final InfobrevsjobbRepository infobrevsjobbRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional
    public ProjektLoggItemDto
    createManuellFastighetHandelse(UUID projektId, String fastighetsbeteckning, ManuellFastighethandelseTypDto typ) {
        log.info("Skapar ny loggpost för manuell fastighet-händelse {} för fastighet i projekt {}", typ,
            fastighetsbeteckning, projektId);

        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);

        var entity = new ManuellFastighethandelseEntity();
        entity.setFastighetsbeteckning(fastighetsbeteckning);
        entity.setProjekt(projekt);
        entity.setTyp(typ);

        var saved = projektLoggRepository.save(entity);

        return modelMapper.map(saved, ManuellFastighethandelseDto.class);
    }

    public ProjektLoggItemDto
    createProjektHandelse(UUID projektId, ProjekthandelseTypDto projekthandelsetyp) {
        log.info("Skapar ny loggpost för projekthändelse {} i projekt {}", projekthandelsetyp, projektId);

        var projekt = projektRepository.findById(projektId).orElseThrow(EntityNotFoundException::new);

        var entity = new ProjekthandelseEntity();
        entity.setProjekthandelsetyp(projekthandelsetyp);
        entity.setProjekt(projekt);

        var saved = projektLoggRepository.saveAndFlush(entity);

        return modelMapper.map(saved, ProjektLoggItemDto.class);
    }

    public ProjektLoggItemDto
    createAvtalHandelse(UUID projektId, UUID avtalsjobbId, Integer antalFastigheter) {
        log.info("Skapar ny loggpost för avtal {}", avtalsjobbId);

        var projekt = projektRepository.findById(projektId)
            .orElseThrow(EntityNotFoundException::new);
        var avtalsjobb = avtalsjobbRepository.findById(avtalsjobbId)
            .orElseThrow(EntityNotFoundException::new);

        var entity = new AvtalhandelseEntity();
        entity.setProjekt(projekt);
        entity.setAvtalsjobb(avtalsjobb);
        entity.setAntalFastigheter(antalFastigheter);

        var saved = projektLoggRepository.saveAndFlush(entity);

        return modelMapper.map(saved, AvtalhandelseDto.class);
    }

    public ProjektLoggItemDto
    createInfobrevHandelse(UUID projektId, UUID infobrevsjobbId, Integer antalFastigheter) {
        log.info("Skapar ny loggpost för infobrev {}", infobrevsjobbId);

        var projekt = projektRepository.findById(projektId)
            .orElseThrow(EntityNotFoundException::new);
        var infobrevsjobb = infobrevsjobbRepository.findById(infobrevsjobbId)
            .orElseThrow(EntityNotFoundException::new);

        var entity = new InfobrevhandelseEntity();
        entity.setProjekt(projekt);
        entity.setInfobrevsjobb(infobrevsjobb);
        entity.setAntalFastigheter(antalFastigheter);

        var saved = projektLoggRepository.saveAndFlush(entity);

        return modelMapper.map(saved, InfobrevhandelseDto.class);
    }

    public ProjektLoggPageDto
    getProjektLoggPage(UUID projektId, Pageable pageRequest, List<ProjektLoggFilterDto> filter)
    {
        log.info("Hämtar projektloggssida för projekt " + projektId);

        var page = projektLoggRepository.getProjektLoggPage(projektId, pageRequest, filter);

        Page<ProjektLoggItemDto> pageDto = page.map(entity -> mapProjektloggEntity(entity));

        return modelMapper.map(pageDto, ProjektLoggPageDto.class);
    }

    private ProjektLoggItemDto mapProjektloggEntity(ProjektLoggEntity projektLoggEntity)  {
        if (projektLoggEntity instanceof ProjekthandelseEntity) {
            return modelMapper.map(projektLoggEntity, ProjekthandelseDto.class);
        }
        else if (projektLoggEntity instanceof AvtalhandelseEntity) {
            return modelMapper.map(projektLoggEntity, AvtalhandelseDto.class);
        }
        else if (projektLoggEntity instanceof InfobrevhandelseEntity) {
            return modelMapper.map(projektLoggEntity, InfobrevhandelseDto.class);
        }
        else if (projektLoggEntity instanceof ManuellFastighethandelseEntity) {
            return modelMapper.map(projektLoggEntity, ManuellFastighethandelseDto.class);
        }
        else {
            throw new RuntimeException("Okänd subklass: " + projektLoggEntity.getClass());
        }
    }
}
