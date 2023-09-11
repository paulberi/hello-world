package se.metria.markkoll.service.fastighet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.samfallighet.SamfallighetEntity;
import se.metria.markkoll.entity.samfallighet.SamfallighetIngaendeFastighetEntity;
import se.metria.markkoll.entity.samfallighet.SamfallighetMerInfoEntity;
import se.metria.markkoll.exception.FsokException;
import se.metria.markkoll.fsokws.*;
import se.metria.markkoll.openapi.finfo.model.FinfoStyrelsemedlemDto;
import se.metria.markkoll.openapi.model.SamfallighetDto;
import se.metria.markkoll.openapi.model.SamfallighetMerInformationDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.SamfallighetMerInfoRepository;
import se.metria.markkoll.repository.fastighet.SamfallighetRepository;
import se.metria.markkoll.service.finfo.FinfoJobException;
import se.metria.markkoll.service.finfo.FinfoService;
import se.metria.markkoll.service.markagare.StyrelsemedlemService;

import javax.persistence.EntityNotFoundException;
import javax.xml.bind.JAXBElement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SamfService {
    @NonNull
    private final AvtalRepository avtalRepository;

    @NonNull
    final private FinfoService finfoService;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    final private SamfServiceWebServiceTemplateConfig samfConfig;

    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final SamfallighetRepository samfallighetRepository;

    @NonNull
    private final SamfallighetMerInfoRepository samfallighetMerInfoRepository;

    @NonNull
    private final StyrelsemedlemService styrelsemedlemService;

    @Value("${finfo.kundmarke}")
    private String kundmarke;

    public SamfallighetDto getSamf(String kundId, UUID samfId) {
        var samfEntity = samfallighetRepository.findById(samfId).orElseThrow(EntityNotFoundException::new);
        var samfDto = modelMapper.map(samfEntity, SamfallighetDto.class);

        var samfMoreInfoEntity = samfallighetMerInfoRepository.findBySamfallighetIdAndKundId(samfId, kundId);
        if (samfMoreInfoEntity.isPresent()) {
            var samfMoreInfoDto = modelMapper.map(samfMoreInfoEntity, SamfallighetMerInformationDto.class);
            var styrelsemedlemmar = styrelsemedlemService.getStyrelsemedlemmar(kundId, samfId);
            samfMoreInfoDto.setStyrelsemedlemmar(styrelsemedlemmar);
            samfDto.setMerInformation(samfMoreInfoDto);
        }

        return samfDto;
    }

    @Transactional
    public void importMoreSamfInfo(UUID projektId, UUID samfId) throws FinfoJobException {
        log.info("Importerar ytterligare information om samfällighet {} i projekt {}", samfId, projektId);
        var samf = samfallighetRepository.getReferenceById(samfId);
        var kund = kundRepository.getKundByProjektId(projektId);

        if (samfallighetMerInfoRepository.existsBySamfallighetIdAndKundId(samfId, kund.getId())) {
            throw new IllegalArgumentException(
                MessageFormat.format("Samfälligheten {} för {} har redan ytterligare information importerad", samfId, kund.getId())
            );
        }

        var samfMerInfoEntity = importRegisterenhet(samfId);

        if (samfMerInfoEntity.getForvaltandeBeteckning() != null) {
            log.info("Importerar data om samfällighetsförening {}", samfId);

            var samfallighetsforening = finfoService.importSamfallighetsforening(samfId);
            modelMapper.map(samfallighetsforening, samfMerInfoEntity);
            importStyrelsemedlemmar(projektId, samfId, kund.getId(), samfallighetsforening.getStyrelsemedlemmar());
        }

        samfMerInfoEntity.setSamfallighet(samf);
        samfMerInfoEntity.setKund(kund);

        samfallighetMerInfoRepository.save(samfMerInfoEntity);
    }

    @Transactional
    public List<SamfallighetIngaendeFastighetEntity> getSamfLista(FastighetEntity samf) throws FsokException {
        List<SamfallighetIngaendeFastighetEntity> result = new ArrayList<>();

        FsFastighetsBeteckning request = new FsFastighetsBeteckning();
        request.setKommun(samf.getKommunnamn());
        request.setTrakt(samf.getTrakt());
        request.setBlock(samf.getBlockenhet().split(":")[0]);
        request.setEnhet(samf.getBlockenhet().split(":")[1]);
        request.setKundmarke(kundmarke);
        request.setInformation("XML0314");

        log.info("Importerar delägande fastigheter för " + samf.getFastighetsbeteckning());
        FsFastighetsInformation samfallighetInfo = makeRequest(request);
        Registerenheter refereradeEnheter = samfallighetInfo.getFastighetsinformation().getRefereradeEnheter();

        String samfUuid = samfallighetInfo.getFastighetsinformation().getRegisterenhet().getUUID();
        SamfallighetEntity samfEntity;
        var samfOpt = samfallighetRepository.findById(UUID.fromString(samfUuid));
        if (samfOpt.isPresent()) {
            samfEntity = samfOpt.get();
            samfEntity.setDelagandeFastigheter(new ArrayList<>());
            samfallighetRepository.flush(); //trigga orphan removal
        }
        else {
            samfEntity = new SamfallighetEntity();
            samfEntity.setFastighet(samf);
        }

        if (refereradeEnheter != null) {
            for (var e : refereradeEnheter.getRegisterenhet()) {
                if (e instanceof Fastighet) {
                    Fastighet f = (Fastighet) e;
                    SamfallighetIngaendeFastighetEntity newFastighet = new SamfallighetIngaendeFastighetEntity();

                    newFastighet.setFastighetId(UUID.fromString(f.getUUID()));
                    String beteckning = f.getGallandeBeteckning().getTrakt().getTraktnamn()+ " "
                        + f.getGallandeBeteckning().getBlocknummer()
                        + f.getGallandeBeteckning().getTecken()
                        +f.getGallandeBeteckning().getEnhetsnummer();
                    newFastighet.setFastighetsbeteckning(beteckning);

                    samfEntity.addDelagandeFastighet(newFastighet);
                }
            }
        }
        samfallighetRepository.save(samfEntity);

        return result;
    }

    public FsFastighetsInformation makeRequest(FsFastighetsBeteckning request) throws FsokException {
        WebServiceTemplate webServiceTemplate;
        try {
            webServiceTemplate = samfConfig.getSamfWebServiceTemplate();
        } catch (Exception e) {
            throw new FsokException("Error getting SamfWebServiceTemplate", e);
        }

        var req = new ObjectFactory()
            .createSokPaBeteckning(request);

        Object response;
        try {
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            response = webServiceTemplate.marshalSendAndReceive(req, message ->
                    ((SoapMessage) message).setSoapAction("http://www.metria.com/finfo/data/finfodatamodell/sokPaBeteckning")
            );
        } catch (Exception e) {
            throw new FsokException("Error making request to fsokws:sokPaBeteckning", e);
        }

        if (response == null) {
            throw new FsokException("No response from fsokws:sokPaBeteckning");
        }

        //noinspection unchecked
        return ((JAXBElement<FsFastighetsInformation>) response).getValue();
    }

    private SamfallighetMerInfoEntity importRegisterenhet(UUID samfId) throws FinfoJobException {
        log.info("Importerar data för registerenhet {}", samfId);
        var registerenhet = finfoService.importRegisterenhet(samfId);
        return modelMapper.map(registerenhet, SamfallighetMerInfoEntity.class);
    }

    private void
    importStyrelsemedlemmar(UUID projektId,
                            UUID samfId,
                            String kundId,
                            Collection<FinfoStyrelsemedlemDto> styrelsemedlemmar) {

        log.info("Importerar {} styrelsemedlemmar", styrelsemedlemmar.size());
        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, samfId);
        styrelsemedlemService.addStyrelsemedlemmar(avtalId, samfId, kundId, styrelsemedlemmar);
    }
}
