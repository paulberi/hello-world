package se.metria.finfo.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.finfo.entity.samfallighetsforening.SamfallighetsforeningEntity;
import se.metria.finfo.fsokws.Samfallighetsforening;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;
import se.metria.finfo.openapi.model.SamfallighetsforeningDto;
import se.metria.finfo.repository.SamfallighetsforeningRepository;
import se.metria.finfo.service.fsok.FsokImportService;
import se.metria.finfo.service.importjob.ImportJobOptions;
import se.metria.finfo.service.importjob.ImportJobService;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SamfallighetsforeningService {
    @NonNull
    private final FsokImportService fsokImportService;

    @NonNull
    private final ImportJobService importJobService;

    @NonNull
    private final SamfallighetsforeningRepository samfallighetsforeningRepository;

    @NonNull
    private final ModelMapper modelMapper;

    public static final String TRANSAKTION_SAMFALLIGHETSFORENING = "Samfällighetsförening (060)";

    @Transactional
    public SamfallighetsforeningDto create(Samfallighetsforening samfallighetsforening) {
        var entity = modelMapper.map(samfallighetsforening, SamfallighetsforeningEntity.class);

        samfallighetsforeningRepository.save(entity);

        return modelMapper.map(entity, SamfallighetsforeningDto.class);
    }

    @Transactional
    public UUID createJob(RegisterenhetRequestDto request, String resourceBasePath) {
        log.info("Påbörjar import av samfällighetsförening " + request.getNyckel());

        var opts = new ImportJobOptions();
        opts.setType(TRANSAKTION_SAMFALLIGHETSFORENING);

        var jobId = importJobService.createJob(opts, () -> runImport(request, resourceBasePath));

        return jobId;
    }

    public SamfallighetsforeningDto get(UUID samfUuid) {
        var entity = samfallighetsforeningRepository.findTopByUuidOrderByImporteradDatumDesc(samfUuid)
            .orElseThrow(EntityNotFoundException::new);

        var dto = modelMapper.map(entity, SamfallighetsforeningDto.class);

        return dto;
    }

    private String runImport(RegisterenhetRequestDto request, String resourceBasePath) {
        var samf = fsokImportService.importSamfallighetsforening(request);
        var samfUuid = create(samf).getUuid();

        return resourceBasePath + samfUuid;
    }
}
