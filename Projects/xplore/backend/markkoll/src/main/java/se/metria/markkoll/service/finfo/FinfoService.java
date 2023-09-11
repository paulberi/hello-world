package se.metria.markkoll.service.finfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import se.metria.markkoll.openapi.finfo.api.AgareApi;
import se.metria.markkoll.openapi.finfo.api.ImportJobApi;
import se.metria.markkoll.openapi.finfo.api.RegisterenhetApi;
import se.metria.markkoll.openapi.finfo.api.SamfallighetsforeningApi;
import se.metria.markkoll.openapi.finfo.model.*;
import se.metria.markkoll.service.KundConfigService;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class FinfoService {
    @NonNull
    private final AgareApi agareApi;

    @NonNull
    private final RegisterenhetApi registerenhetApi;

    @NonNull
    private final SamfallighetsforeningApi samfallighetsforeningApi;

    @NonNull
    private final ImportJobApi importJobApi;

    @NonNull
    private final KundConfigService kundConfigService;

    @Value("${finfo.poll-interval:0}")
    private final Integer pollInterval = 0;

    public List<UUID> importAgare(List<UUID> fastighetIds) {
        var auth = kundConfigService.getFastighetsokAuth();

        var request = new FinfoAgareRequestDto()
            .username(auth.getUsername())
            .password(auth.getPassword())
            .kundmarke(auth.getKundmarke())
            .fastighetIds(fastighetIds);

        return agareApi.importAgare(request);
    }

    public List<FinfoAgareDto> getAgare(List<UUID> finfoAgareIds) {
        return agareApi.getAgare(finfoAgareIds);
    }

    public FinfoSamfallighetDto importRegisterenhet(UUID registerenhetId) throws FinfoJobException {
        return runImportJob(registerenhetId,
            req -> registerenhetApi.createAllmanMiniJobWithHttpInfo(req),
            id -> registerenhetApi.getAllmanMini(id)
        );
    }

    public FinfoSamfallighetsforeningDto importSamfallighetsforening(UUID samfId) throws FinfoJobException {
        return runImportJob(samfId,
            req -> samfallighetsforeningApi.createSamfallighetsforeningJobWithHttpInfo(req),
            id -> samfallighetsforeningApi.getSamfallighetsforening(id)
        );
    }

    private <T> T
    runImportJob(UUID id,
                 Function<FinfoRegisterenhetRequestDto, ResponseEntity<?>> resourcePost,
                 Function<UUID, T> resourceGet) throws FinfoJobException
    {

        var auth = kundConfigService.getFastighetsokAuth();
        var request = new FinfoRegisterenhetRequestDto()
            .nyckel(id.toString())
            .kundmarke(auth.getKundmarke())
            .username(auth.getUsername())
            .password(auth.getPassword());

        var response = resourcePost.apply(request);
        var importJobId = getImportJobId(response);
        var resource = pollImportJob(importJobId, resourceGet);

        return resource;
    }

    private <T> T pollImportJob(UUID importJobId, Function<UUID, T> resourceGet) throws FinfoJobException {
        for (var i = 0; i < 30; i++) {
            sleep(pollInterval);
            var jobInfo = importJobApi.getImportJobStatus(importJobId);
            if (jobInfo.getStatus() == FinfoImportJobStatusDto.FAILED) {
                throw new FinfoJobFailedException(importJobId, MessageFormat.format("Importjobb {0} misslyckades", importJobId));
            } else if (jobInfo.getStatus() == FinfoImportJobStatusDto.DONE) {
                var resource = jobInfo.getResource().substring(jobInfo.getResource().lastIndexOf('/') + 1);
                return resourceGet.apply(UUID.fromString(resource));
            }
        }
        throw new FinfoJobTimedOutException(importJobId, MessageFormat.format("Importjobb {0} timade ut", importJobId));
    }

    private UUID getImportJobId(ResponseEntity<?> response) {
        var location = response.getHeaders().get("Location").get(0);

        var index = location.lastIndexOf('/');
        if (index == -1) {
            throw new IllegalArgumentException(
                MessageFormat.format("Kunde inte avgöra id för importjobb från location-header: {0}", location));
        }

        var jobId = location.substring(index + 1);
        return UUID.fromString(jobId);
    }

    private void sleep(int millisecs) {
        try {
            TimeUnit.MILLISECONDS.sleep(millisecs);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
