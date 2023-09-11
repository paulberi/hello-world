package se.metria.xplore.castor.integration.service;

import com.metria.finfo.data.finfodatamodell.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import se.metria.xplore.castor.openapi.model.RealEstateIdentifierDto;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FastighetsinfoService {

    @NonNull
    final private FastighetsinfoServiceWebServiceTemplateConfig finfoConfig;

    @Value("${finfo.kundmarke}")
    private String kundmarke;

    public List<Fastighet> getFastLista(List<RealEstateIdentifierDto> fastigheter) throws Exception {
        List<Fastighet> result = new ArrayList<>();
        for (var fastighet: fastigheter) {
            var uuid = fastighet.getUuid();

            FsFastighetsNyckel request = new FsFastighetsNyckel();
            request.setNyckel(uuid);
            request.setKundmarke(kundmarke);
            request.setInformation("XML022");

            FsFastighetsInformation fastighetsInformation = makeRequest(request);
            Registerenhet registreradeEnhet = fastighetsInformation.getFastighetsinformation().getRegisterenhet();
            if (registreradeEnhet != null) {
                if (registreradeEnhet instanceof Fastighet) {
                    Fastighet f = (Fastighet) registreradeEnhet;
                    result.add(f);
                }
            }
        }
        return result;
    }

    public FsFastighetsInformation makeRequest(FsFastighetsNyckel request) throws Exception {
        try {
            WebServiceTemplate webServiceTemplate = finfoConfig.getSamfWebServiceTemplate();

            var req = new ObjectFactory()
                    .createSokPaNyckel(request);


            Object response = webServiceTemplate.marshalSendAndReceive(req, message -> {
                        ((SoapMessage)message).setSoapAction("http://www.metria.com/finfo/data/finfodatamodell/sokPaNyckel");
                    }
            );

            if (response == null) {
                throw new RuntimeException("FsokWs error, response was null");
            }

            return ((JAXBElement<FsFastighetsInformation>) response).getValue();
        } catch (Exception e){
            throw new Exception("Error making request to fsokws", e);
        }
    }
}
