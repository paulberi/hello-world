package se.metria.markkoll.service.dokument.forteckninggenerator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.openapi.model.FilterAndTemplateDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.dokument.DokumentmallRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.forteckninggenerator.ForteckningGenerator;
import se.metria.markkoll.util.dokument.FileType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ForteckningGeneratorService {
    @NonNull
    private ForteckningGenerator forteckningGenerator;

    @NonNull
    private AvtalRepository avtalRepository;

    @NonNull
    private AvtalspartRepository avtalspartRepository;

    @NonNull
    private DokumentmallRepository dokumentmallRepository;

    @NonNull
    private ProjektRepository projektRepository;

    @NonNull
    private ModelMapper modelMapper;

    public Resource generate(UUID projektId, FilterAndTemplateDto filterAndTemplate) throws IOException {
        var dokumentmallId = filterAndTemplate.getTemplate();
        var filter = filterAndTemplate.getFilter();

        var xlsxTemplate = dokumentmallRepository.getDokumentFileData(dokumentmallId);
        var data = getForteckningGeneratorData(projektId, filter);

        var xlsx = forteckningGenerator.generate(new ByteArrayInputStream(xlsxTemplate), data);

        var filename = getFilename(projektId, dokumentmallId);
        return new FileNameAwareByteArrayResource(xlsx, filename);
    }

    private List<ForteckningGeneratorDataImpl> getForteckningGeneratorData(UUID projektId, FastighetsfilterDto filter) {
        var avtalIds = avtalRepository.avtalIdsFiltered(projektId, filter);
        var data = avtalspartRepository.getForteckningGeneratorData(avtalIds);

        return CollectionUtil.modelMapperList(data, modelMapper, ForteckningGeneratorDataImpl.class);
    }

    private String getFilename(UUID projektId, UUID dokumentmallId) {
        var projektnamn = projektRepository.getNamn(projektId);
        var mallnamn = dokumentmallRepository.getDokumentmallNamn(dokumentmallId);
        return DokumentUtil.createFilename(FileType.XLSX, projektnamn, mallnamn);
    }
}
