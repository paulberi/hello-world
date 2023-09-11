package se.metria.markkoll.service.intrang;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.openapi.model.ProjektIntrangDto;
import se.metria.markkoll.openapi.model.VersionDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.AvtalstypEvaluatorService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.indata.Indata;
import se.metria.markkoll.service.indata.IndataService;
import se.metria.markkoll.service.indata.KallfilService;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.service.VersionService;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.FileType;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IntrangImportService {
    @NonNull
    private final AvtalstypEvaluatorService avtalstypEvaluatorService;

    @NonNull
    private final Clock clock;

    @NonNull
    private final IndataService indataService;

    @NonNull
    private final IntrangReader intrangReader;

    @NonNull
    private final KallfilService kallfilService;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final UserService userService;

    @NonNull
    private final VersionService versionService;

    @Transactional
    public VersionDto
    importIntrang(UUID projektId, Collection<ProjektIntrangDto> intrang) {
        var version = new VersionDto()
            .filnamn(getFilename())
            .buffert(0.)
            .skapadDatum(LocalDateTime.now(clock));

        var versionId = versionService.create(projektId, version, intrang);
        version.setId(versionId);

        return version;
    }

    @Transactional
    public VersionDto
    importShape(UUID projektId, Resource shapeFile, IndataTypDto indataTyp, double buffert) throws IOException {

        var kallfilId = createKallfil(projektId, shapeFile, indataTyp);
        var indata = Indata.fromZipFile(shapeFile, indataTyp);

        indataService.update(projektId, kallfilId, indata);

        var intrang = getIntrang(projektId, indata);

        return versionFromShapefile(projektId, shapeFile, buffert, intrang);
    }


    private UUID createKallfil(UUID projektId, Resource shapeFile, IndataTypDto indataTyp) throws IOException {
        var kundId = projektRepository.getKundId(projektId);
        return kallfilService.create(shapeFile, indataTyp, kundId);
    }

    private String getFilename() {
        var user = userService.getCurrentUser();
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        var date = LocalDateTime.now(clock).format(formatter);
        return DokumentUtil.createFilename(FileType.ZIP, user.getFornamn(), user.getEfternamn(), date);
    }

    private List<ProjektIntrangDto> getIntrang(UUID projektId, Indata indata) {
        var avtalstypEvaluator = getAvtalstypEvaluator(projektId, indata);
        var intrang = intrangReader.getIntrang(indata, avtalstypEvaluator);

        return intrang;
    }

    private AvtalstypEvaluator getAvtalstypEvaluator(UUID projektId, Indata indata) {
        var kundId = projektRepository.getKundId(projektId);
        return avtalstypEvaluatorService.getAvtalstypEvaluator(kundId,
            indata.getIndataTyp());
    }

    private VersionDto
    versionFromShapefile(UUID projektId, Resource shapeFile, double buffert, Collection<ProjektIntrangDto> intrang) {

        var version = new VersionDto()
            .filnamn(shapeFile.getFilename())
            .buffert(buffert)
            .skapadDatum(LocalDateTime.now(clock));

        var versionId = versionService.create(projektId, version, intrang);
        version.setId(versionId);

        return version;
    }
}
