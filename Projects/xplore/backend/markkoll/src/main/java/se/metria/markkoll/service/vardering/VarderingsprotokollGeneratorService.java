package se.metria.markkoll.service.vardering;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.Fraction;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickVpDto;
import se.metria.markkoll.util.vardering.Signatar;
import se.metria.markkoll.util.vardering.VarderingsprotokollMetadataExtra;
import se.metria.markkoll.util.vardering.VpGenerator;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
public abstract class VarderingsprotokollGeneratorService<VPDTO, ERSATTNINGDTO> {
    @NonNull
    final protected AuditorAware<String> auditorAware;

    @NonNull
    final protected AvtalService avtalService;

    @NonNull
    final protected FastighetService fastighetService;

    @NonNull
    final protected ProjektRepository projektRepository;

    @NonNull
    final protected VarderingsprotokollService<VPDTO> varderingsprotokollService;

    protected abstract String getAuthor();

    protected abstract VpGenerator<VPDTO, ERSATTNINGDTO> getVpGenerator();

    public List<ByteArrayResource>
    generateVarderingsprotokoll(UUID avtalId, UtskickVpDto utskick)
            throws IOException, InvalidFormatException {
        log.info("Genererar värderingsprotokoll för avtal {}", avtalId);

        var vp = varderingsprotokollService.getWithAvtalId(avtalId);
        if (vp.isEmpty()) {
            return new ArrayList<>();
        }

        var vpGenerator = getVpGenerator();
        var metadataExtra = getMetadataExtra(avtalId, utskick.getKontaktperson());
        var signatarer = getFastighetsagare(utskick.getSignatarer());
        var lastModifiedBy = auditorAware.getCurrentAuditor().orElse(null);

        if (signatarer.isEmpty()) {
            return Arrays.asList(
                vpGenerator.generateVarderingsprotokoll(vp.get(), metadataExtra, signatarer,
                    getAuthor(), lastModifiedBy));
        }
        else {
            var vpList = new ArrayList<ByteArrayResource>();
            for (var signatar : Lists.partition(signatarer, vpGenerator.getMaxSignatarer())) {
                vpList.add(vpGenerator.generateVarderingsprotokoll(vp.get(), metadataExtra, signatar,
                    getAuthor(), lastModifiedBy));
            }
            return vpList;
        }
    }

    public Optional<ERSATTNINGDTO>
    getErsattning(UUID avtalId) throws IOException
    {
        var vp = varderingsprotokollService.getWithAvtalId(avtalId);
        if (vp.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(getVpGenerator().getErsattning(vp.get()));
        }
    }

    private VarderingsprotokollMetadataExtra getMetadataExtra(UUID avtalId, Optional<MarkagareDto> kontaktperson) {
        var fastighet = fastighetService.getFastighet(avtalId);
        var uppdragsnummer = projektRepository.getUppdragsnummerAvtal(avtalId);

        var kontaktPersonOchAdress = kontaktperson.map(ag ->
            String.format("%s %s %s %s", ag.getNamn(), ag.getAdress(), ag.getPostnummer(), ag.getPostort())
        ).orElse(null);

        return VarderingsprotokollMetadataExtra.builder()
            .fastighetsbeteckning(fastighet.getFastighetsbeteckning())
            .kommun(fastighet.getKommunnamn().substring(0, 1) + fastighet.getKommunnamn().substring(1).toLowerCase())
            .kontaktpersonOchAdress(kontaktPersonOchAdress)
            .projektnummer(uppdragsnummer)
            .build();
    }

    private List<Signatar> getFastighetsagare(List<MarkagareDto> markagare) {
        return markagare.stream()
            .map(this::mapFastighetsagare)
            .collect(Collectors.toList());
    }

    private Signatar mapFastighetsagare(MarkagareDto markagare) {
        var andel = StringUtils.isEmpty(markagare.getAndel()) ?
            null : Fraction.getFraction(markagare.getAndel()).doubleValue();
        var adress =  String.format("%s %s %s", markagare.getAdress(), markagare.getPostnummer(), markagare.getPostort());

        return Signatar.builder()
            .andel(andel)
            .namn(markagare.getNamn())
            .personnummer(markagare.getFodelsedatumEllerOrgnummer())
            .adress(adress)
            .build();
    }
}
