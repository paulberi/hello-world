package se.metria.markkoll.service.haglof;

import io.netty.util.internal.StringUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.metria.markkoll.openapi.model.ElnatLedningSkogsmarkDto;
import se.metria.markkoll.openapi.model.TillvaratagandeTypDto;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.haglof.model.HaglofFastighet;
import se.metria.markkoll.service.haglof.model.HaglofMetadata;
import se.metria.markkoll.service.haglof.model.HaglofOwner;
import se.metria.markkoll.service.haglof.model.Tillvaratagande;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.projekt.ElnatProjektService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService.ELEKTRISK_STARKSTROMSLEDNING;

@Service
@Slf4j
@RequiredArgsConstructor
public class HaglofImportService {
    @NonNull
    private final AvtalService avtalService;

    @NonNull
    private final ElnatVarderingsprotokollService elnatVarderingsprotokollService;

    @NonNull
    private final MarkagareService markagareService;

    @NonNull
    private final ElnatProjektService elnatProjektService;

    public void importAvtal(UUID avtalId, HaglofFastighet haglofFastighet) {
        var avtal = avtalService.getAvtal(avtalId);

        avtal.setEgetTillvaratagande(haglofFastighet.getEvaluation().getHighcutvalue());
        avtal.setSkogsfastighet(true);
        avtal.setTillvaratagandeTyp(getTillvaratagandeTyp(haglofFastighet.getTillvaratagande()));
        avtalService.updateAvtal(avtal);
    }

    public void importMarkagare(UUID avtalspartId, HaglofOwner haglofOwner) {
        var markagare = markagareService.getAgare(avtalspartId);

        var ownerBankkonto = getBankkonto(haglofOwner);
        if (StringUtil.isNullOrEmpty(markagare.getBankkonto()) && ownerBankkonto != null) {
            markagare.setBankkonto(ownerBankkonto);
        }
        var ownerTelefon = getTelefonnummer(haglofOwner);
        if (StringUtil.isNullOrEmpty(markagare.getTelefon()) && ownerTelefon != null) {
            markagare.setTelefon(ownerTelefon);
        }
        if (StringUtil.isNullOrEmpty(markagare.getePost()) && !StringUtil.isNullOrEmpty(haglofOwner.getEPost())) {
            markagare.setePost(haglofOwner.getEPost());
        }

        markagareService.updateAgare(avtalspartId, markagare);
    }

    public void importProjekt(UUID projektId, HaglofMetadata metadata) {
        var projekt = elnatProjektService.getProjektDto(projektId).get();

        projekt.getProjektInfo().setProjektnummer(metadata.getProjektnummer());

        elnatProjektService.updateProjektInfo(projektId, projekt);
    }

    public void importVp(UUID avtalId, HaglofFastighet haglofFastighet, HaglofMetadata metadata) {
        var vp = elnatVarderingsprotokollService.getWithAvtalId(avtalId)
            .orElseThrow(EntityNotFoundException::new);

        var varderingstidpunkt = metadata.getVarderingstidpunkt() == null ?
            LocalDateTime.now() : metadata.getVarderingstidpunkt();

        vp.getMetadata().setVarderingsmanOchForetag(metadata.getVarderingsmanOchForetag());
        vp.getMetadata().setVarderingstidpunkt(varderingstidpunkt);
        vp.setRotnetto(haglofFastighet.getEvaluation().getRotvalue().doubleValue());
        vp.getMetadata().setFastighetsnummer(haglofFastighet.getFastighetsnummer());

        var evaluation = haglofFastighet.getEvaluation();
        var markvarde = evaluation.getBordertreevalue() + evaluation.getLandvalue() + evaluation.getEarlycutvalue() +
            evaluation.getStormdryvalue();
        vp.setLedningSkogsmark(Arrays.asList(
            new ElnatLedningSkogsmarkDto().beskrivning(ELEKTRISK_STARKSTROMSLEDNING).ersattning(markvarde)));

        elnatVarderingsprotokollService.update(vp);
    }

    private String getBankkonto(HaglofOwner owner) {
        if (!StringUtil.isNullOrEmpty(owner.getBankgiro())) {
            return owner.getBankgiro();
        }
        else if (!StringUtil.isNullOrEmpty(owner.getPlusgiro())) {
            return owner.getPlusgiro();
        }
        else if (!StringUtil.isNullOrEmpty(owner.getBankkonto())) {
            return owner.getBankkonto();
        }
        else {
            return null;
        }
    }

    private String getTelefonnummer(HaglofOwner owner) {
        if (!StringUtil.isNullOrEmpty(owner.getTelefonHem())) {
            return owner.getTelefonHem();
        }
        else if (!StringUtil.isNullOrEmpty(owner.getTelefonArbete())) {
            return owner.getTelefonArbete();
        }
        else if (!StringUtil.isNullOrEmpty(owner.getMobiltelefon())) {
            return owner.getMobiltelefon();
        }
        else {
            return null;
        }
    }

    private TillvaratagandeTypDto getTillvaratagandeTyp(Tillvaratagande tillvaratagande) {
        switch (tillvaratagande) {
            case EGEN_REGI:
                return TillvaratagandeTypDto.EGET_TILLVARATAGANDE;
            case FORSALJNING:
                return TillvaratagandeTypDto.ROTNETTO;
            case EJ_VALT:
                return TillvaratagandeTypDto.EJ_BESLUTAT;
            default:
                throw new IllegalArgumentException("Okänd tillvaratagandetyp: " + tillvaratagande);
        }
    }
}
