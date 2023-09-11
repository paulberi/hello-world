package se.metria.finfo.service.registerenhet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.finfo.fsokws.Fastighetsinformation;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.openapi.model.SamfallighetDto;
import se.metria.finfo.repository.RegisterenhetRepository;
import se.metria.finfo.service.fsok.FsokImportService;
import se.metria.finfo.service.importjob.ImportJobOptions;
import se.metria.finfo.service.importjob.ImportJobService;
import se.metria.finfo.util.FastighetsinformationToSamfallighetEntityMapper;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SamfallighetService {
    @NonNull
    private final FastighetsinformationToSamfallighetEntityMapper samfMapper;

    @NonNull
    private final FsokImportService fsokImportService;

    @NonNull
    private final ImportJobService importJobService;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final RegisterenhetRepository registerenhetRepository;

    public static final String TRANSAKTION_ALLMAN_MINI = "Allmän mini (032)";

    @Transactional
    public SamfallighetDto create(Fastighetsinformation fastighetsinformation) {
        var entity = samfMapper.map(fastighetsinformation);

        registerenhetRepository.save(entity);

        return modelMapper.map(entity, SamfallighetDto.class);
    }

    @Transactional
    public UUID createJob(RegisterenhetRequestDto request, String resourceBasePath) {
        log.info("Påbörjar import av registerenhet {} för transaktion {}", request.getNyckel(), TRANSAKTION_ALLMAN_MINI);

        var opts = new ImportJobOptions();
        opts.setType(TRANSAKTION_ALLMAN_MINI);

        var jobId = importJobService.createJob(opts, () -> runImport(request, resourceBasePath));

        return jobId;
    }

    public SamfallighetDto get(UUID samfUuid) {
        var entity = registerenhetRepository.findTopByUuidOrderByImporteradDatumDesc(samfUuid)
            .orElseThrow(EntityNotFoundException::new);

        var dto = modelMapper.map(entity, SamfallighetDto.class);

        return dto;
    }

    private String runImport(RegisterenhetRequestDto request, String resourceBasePath) {
        var fastighetsinformation = fsokImportService.importRegisterenhet(request);
        var registerenhet = create(fastighetsinformation);

        return resourceBasePath + registerenhet.getUuid();
    }
}
