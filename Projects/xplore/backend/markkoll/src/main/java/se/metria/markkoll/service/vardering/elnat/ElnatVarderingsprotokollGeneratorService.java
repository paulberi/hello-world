package se.metria.markkoll.service.vardering.elnat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.vardering.VarderingsprotokollGeneratorService;
import se.metria.markkoll.util.vardering.ElnatErsattningDto;
import se.metria.markkoll.util.vardering.VpGenerator;

@MarkkollService
@Transactional
@Slf4j
public class ElnatVarderingsprotokollGeneratorService
    extends VarderingsprotokollGeneratorService<ElnatVarderingsprotokollDto, ElnatErsattningDto> {

    public static final String AUTHOR_ELNAT = "Henriksson Fredrik (DS-UR)";

    public ElnatVarderingsprotokollGeneratorService(
        AuditorAware<String> auditorAware,
        AvtalService avtalService,
        FastighetService fastighetService,
        ProjektRepository projektRepository,
        ElnatVarderingsprotokollService elnatVarderingsprotokollService) {

        super(auditorAware, avtalService, fastighetService, projektRepository, elnatVarderingsprotokollService);
    }

    @Override
    protected String getAuthor() {
        return AUTHOR_ELNAT;
    }

    @Override
    protected VpGenerator<ElnatVarderingsprotokollDto, ElnatErsattningDto> getVpGenerator() {
        return VpGenerator.elnatVpGenerator();
    }
}
