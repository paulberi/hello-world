package se.metria.markkoll.service.vardering.fiber;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.utskick.UtskickVpDto;
import se.metria.markkoll.service.vardering.VarderingsprotokollGeneratorService;
import se.metria.markkoll.util.vardering.FiberErsattningDto;
import se.metria.markkoll.util.vardering.VpGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@MarkkollService
@Transactional
@Slf4j
public class FiberVarderingsprotokollGeneratorService
    extends VarderingsprotokollGeneratorService<FiberVarderingsprotokollDto, FiberErsattningDto> {

    public static final String AUTHOR_FIBER = "Jonny Siikavaara";

    private VpGenerator<FiberVarderingsprotokollDto, FiberErsattningDto> vpGenerator;

    private FiberVarderingConfigService fiberVarderingConfigService;

    public FiberVarderingsprotokollGeneratorService(
        AuditorAware<String> auditorAware,
        AvtalService avtalService,
        FastighetService fastighetService,
        ProjektRepository projektRepository,
        FiberVarderingsprotokollService fiberVarderingsprotokollService,
        FiberVarderingConfigService fiberVarderingConfigService) {

        super(auditorAware, avtalService, fastighetService, projektRepository, fiberVarderingsprotokollService);
        this.fiberVarderingConfigService = fiberVarderingConfigService;
    }

    @Override
    protected String getAuthor() {
        return AUTHOR_FIBER;
    }

    @Override
    protected VpGenerator<FiberVarderingsprotokollDto, FiberErsattningDto> getVpGenerator() {
        if (vpGenerator == null) {
            throw new NullPointerException("vpGenerator borde inte vara null");
        }

        return vpGenerator;
    }

    @Override
    public List<ByteArrayResource> generateVarderingsprotokoll(UUID avtalId, UtskickVpDto utskick) throws IOException, InvalidFormatException {
        var config = fiberVarderingConfigService.getFiberVarderingConfigForAvtal(avtalId);
        this.vpGenerator = VpGenerator.fiberVpGenerator(config);

        var vp = super.generateVarderingsprotokoll(avtalId, utskick);

        this.vpGenerator = null;

        return vp;
    }

    @Override
    public Optional<FiberErsattningDto> getErsattning(UUID avtalId) throws IOException {
        var config = fiberVarderingConfigService.getFiberVarderingConfigForAvtal(avtalId);
        this.vpGenerator = VpGenerator.fiberVpGenerator(config);

        var ersattning = super.getErsattning(avtalId);

        this.vpGenerator = null;

        return ersattning;
    }
}
