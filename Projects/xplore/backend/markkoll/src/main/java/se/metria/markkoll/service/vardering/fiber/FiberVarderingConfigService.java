package se.metria.markkoll.service.vardering.fiber;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.entity.vardering.fiber.FiberVarderingConfigEntity;
import se.metria.markkoll.entity.vardering.fiber.VarderingKundAgareEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.FiberVarderingConfigCreateDto;
import se.metria.markkoll.openapi.model.FiberVarderingConfigDto;
import se.metria.markkoll.openapi.model.FiberVarderingConfigNamnAgareDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.markagare.PersonRepository;
import se.metria.markkoll.repository.vardering.fiber.FiberVarderingConfigRepository;
import se.metria.markkoll.repository.vardering.fiber.FiberVarderingsprotokollRepository;
import se.metria.markkoll.repository.vardering.fiber.VarderingKundAgareRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FiberVarderingConfigService {
    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    private final FiberVarderingsprotokollRepository fiberVarderingsprotokollRepository;

    @NonNull
    private final FiberVarderingConfigRepository fiberVarderingConfigRepository;

    @NonNull
    private final PersonRepository personRepository;

    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final VarderingKundAgareRepository varderingKundAgareRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public FiberVarderingConfigDto getFiberVarderingConfig(UUID vpId) {
        return getFiberVarderingConfigForAvtal(fiberVarderingsprotokollRepository.getAvtalId(vpId));
    }

    public FiberVarderingConfigDto getFiberVarderingConfigForAvtal(UUID avtalId) {
        var kundId = avtalRepository.getKundId(avtalId);

        var entity = personRepository.getKontaktpersonForAvtal(avtalId)
            .map(p -> p.getPersonnummer())
            .flatMap(pnr -> fiberVarderingConfigRepository.getAgareConfig(kundId, pnr))
            .or(() -> fiberVarderingConfigRepository.getKundDefaultConfig(kundId))
            .or(() -> fiberVarderingConfigRepository.getMetriaDefault());

        return modelMapper.map(entity, FiberVarderingConfigDto.class);
    }

    public FiberVarderingConfigDto getFiberVarderingConfigForKund(String kundId) {

        var entity = fiberVarderingConfigRepository.getKundDefaultConfig(kundId)
                .or(fiberVarderingConfigRepository::getMetriaDefault);

        return modelMapper.map(entity, FiberVarderingConfigDto.class);
    }

    public List<FiberVarderingConfigNamnAgareDto> getAllFiberVarderingConfigsForKund(String kundId) {
        var entities = varderingKundAgareRepository.findAllByKundAndPersonNotNull(kundId);
        return entities.get().stream()
                .map(this::convertVarderingKundAgareEntityToFiberVarderingConfigNamnAgareDto)
                .sorted(Comparator.comparing(FiberVarderingConfigNamnAgareDto::getNamn))
                .collect(Collectors.toList());
    }

    private FiberVarderingConfigNamnAgareDto convertVarderingKundAgareEntityToFiberVarderingConfigNamnAgareDto(VarderingKundAgareEntity varderingKundAgare) {
        var fiberVarderingConfigNamnAgareDto = new FiberVarderingConfigNamnAgareDto();
        fiberVarderingConfigNamnAgareDto.setFiberVarderingConfig(modelMapper.map(varderingKundAgare.getVarderingConfig(), FiberVarderingConfigDto.class));
        fiberVarderingConfigNamnAgareDto.setPersonnummer(varderingKundAgare.getPerson().getPersonnummer());
        fiberVarderingConfigNamnAgareDto.setNamn(varderingKundAgare.getNamn());
        return fiberVarderingConfigNamnAgareDto;

    }

    @Transactional
    public FiberVarderingConfigDto updateDefaultFiberVarderingConfigForKund(String kundId, FiberVarderingConfigDto fiberVarderingConfigDto) {
        var existingConfig = fiberVarderingConfigRepository.getKundDefaultConfig(kundId);
        if(existingConfig.isPresent()) {
            var existing = existingConfig.get();
            modelMapper.map(fiberVarderingConfigDto, existing);
            fiberVarderingConfigRepository.save(existing);
        } else {
            var kundEntity = kundRepository.getReferenceById(kundId);
            var fiberVarderingConfigEntity = modelMapper.map(fiberVarderingConfigDto, FiberVarderingConfigEntity.class);
            var addedVardering = fiberVarderingConfigRepository.save(fiberVarderingConfigEntity);
            var varderingKundAgare = new VarderingKundAgareEntity();
            varderingKundAgare.setVarderingConfig(addedVardering);
            varderingKundAgare.setKund(kundEntity);
            varderingKundAgare.setNamn("kunddefault");
            varderingKundAgareRepository.save(varderingKundAgare);
        }
        return fiberVarderingConfigDto;

    }

    @Transactional
    public FiberVarderingConfigNamnAgareDto createFiberVarderingConfig(String kundId, FiberVarderingConfigCreateDto fiberVarderingConfigCreateDto) {
        var kundEntity = kundRepository.getReferenceById(kundId);

        var person = personRepository.findByPersonnummerAndKundId(fiberVarderingConfigCreateDto.getPersonEllerOrgNummer(), kundId);
        if(person.isEmpty()) {
            throw new MarkkollException(MarkkollError.PERSON_NOT_FOUND);
        }

        var existing = varderingKundAgareRepository.getByKundAndPerson(kundId, person.get().getId());
        if(existing.isPresent()) {
            throw new MarkkollException(MarkkollError.FIBER_VARDERING_CONFIG_ALREADY_EXIST);
        }

        var existingDefaultConfig = fiberVarderingConfigRepository.getKundDefaultConfig(kundId)
                                                                    .or(fiberVarderingConfigRepository::getMetriaDefault);
        // Gör en kopia av kundens defaultconfig
        var configDto = modelMapper.map(existingDefaultConfig, FiberVarderingConfigDto.class);
        configDto.setId(null);
        var fiberVarderingConfigEntity = modelMapper.map(configDto, FiberVarderingConfigEntity.class);
        var addedVardering = fiberVarderingConfigRepository.save(fiberVarderingConfigEntity);

        var varderingKundAgare = new VarderingKundAgareEntity();
        varderingKundAgare.setVarderingConfig(addedVardering);
        varderingKundAgare.setKund(kundEntity);
        varderingKundAgare.setNamn(fiberVarderingConfigCreateDto.getNamn());
        varderingKundAgare.setPerson(person.get());
        var addedVarderingKundAgare = varderingKundAgareRepository.save(varderingKundAgare);
        return modelMapper.map(addedVarderingKundAgare, FiberVarderingConfigNamnAgareDto.class);
    }

    @Transactional
    public FiberVarderingConfigNamnAgareDto updateFiberVarderingConfig(String kundId, FiberVarderingConfigNamnAgareDto configNamnAgareDto) {
        var existingConfig = varderingKundAgareRepository.getByKundAndAndVarderingConfig(kundId, configNamnAgareDto.getFiberVarderingConfig().getId());
        if(!existingConfig.getPerson().getPersonnummer().equals(configNamnAgareDto.getPersonnummer())){
            var person = personRepository.findByPersonnummerAndKundId(configNamnAgareDto.getPersonnummer(), kundId);
            if(person.isEmpty()) {
                throw new MarkkollException(MarkkollError.PERSON_NOT_FOUND);
            }
            existingConfig.setPerson(person.get());
        }
        existingConfig.setNamn(configNamnAgareDto.getNamn());
        fiberVarderingConfigRepository.save(modelMapper.map(configNamnAgareDto.getFiberVarderingConfig(), FiberVarderingConfigEntity.class));

        return convertVarderingKundAgareEntityToFiberVarderingConfigNamnAgareDto(existingConfig);
    }
    @Transactional
    public void deleteFiberVarderingConfig(UUID varderingConfigId) {
        fiberVarderingConfigRepository.deleteById(varderingConfigId);
    }
}
