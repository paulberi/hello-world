package se.metria.finfo.service.fsok;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import se.metria.finfo.fsokws.*;
import se.metria.finfo.openapi.model.RegisterenhetRequestDto;

import javax.xml.bind.JAXBElement;

@Service
@Slf4j
public class FsokImportService {
    @Value("${finfo.fsok-url}/fsok-2.3-ws/webservice")
    private String fsokUrl;

    @Value("${finfo.datamodell-url}")
    private String finfoDatamodellUrl;

    public Fastighetsinformation
    importRegisterenhet(RegisterenhetRequestDto request) {
        var webServiceTemplate = getWebServiceTemplate(request.getUsername(), request.getPassword());

        var fsRequest = new FsFastighetsNyckel();
        fsRequest.setNyckel(request.getNyckel());
        fsRequest.setKundmarke(request.getKundmarke());
        fsRequest.setInformation("XML032");

        var response = makeRequest(webServiceTemplate, fsRequest);

        var fastighetsinformation = ((JAXBElement<FsFastighetsInformation>) response).getValue()
            .getFastighetsinformation();

        return fastighetsinformation;
    }

    public Samfallighetsforening
    importSamfallighetsforening(RegisterenhetRequestDto request) {
        var webServiceTemplate = getWebServiceTemplate(request.getUsername(), request.getPassword());

        var fsRequest = new FsFastighetsNyckel();
        fsRequest.setNyckel(request.getNyckel());
        fsRequest.setKundmarke(request.getKundmarke());
        fsRequest.setInformation("XML060");

        var response = makeRequest(webServiceTemplate, fsRequest);

        var samfallighetsforening = ((JAXBElement<FsFastighetsInformation>) response).getValue()
            .getFastighetsinformation()
            .getForvaltning();

        return samfallighetsforening;
    }

    private WebServiceTemplate getWebServiceTemplate(String username, String password) {
        try {
            var webServiceTemplate = new WebServiceTemplate();
            webServiceTemplate.setDefaultUri(fsokUrl);

            webServiceTemplate.setMarshaller(marshaller());
            webServiceTemplate.setUnmarshaller(marshaller());

            webServiceTemplate.setMessageSender(httpComponentsMessageSender(username, password));
            webServiceTemplate.afterPropertiesSet();
            return webServiceTemplate;
        }
        catch (Exception e) {
            throw new FsokException("Error getting SamfWebServiceTemplate", e);
        }
    }

    private Object
    makeRequest(WebServiceTemplate webServiceTemplate,
                FsFastighetsNyckel request) {

        var transactionType = request.getInformation();
        var req = new ObjectFactory().createSokPaNyckel(request);

        Object response;
        try {
            var soapAction = finfoDatamodellUrl + "/" + "sokPaNyckel";
            log.info("Hämtar transaktion {} från {}", transactionType, soapAction);
            System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
            response = webServiceTemplate.marshalSendAndReceive(req, message ->
                ((SoapMessage) message).setSoapAction(finfoDatamodellUrl + "/" + "sokPaNyckel")
            );
        } catch (RuntimeException e) {
            throw new FsokException("Error making request to fsokws:" + "sokPaNyckel", e);
        }

        log.info("Transaktion {} klar", transactionType);

        if (response == null) {
            throw new FsokException("No response from fsokws:" + "sokPaNyckel");
        }

        return response;
    }

    private Jaxb2Marshaller marshaller() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("se.metria.finfo.fsokws");
        marshaller.afterPropertiesSet();

        return marshaller;
    }

    private HttpComponentsMessageSender httpComponentsMessageSender(String username, String password) throws Exception {
        var credentials = new UsernamePasswordCredentials(username, password);

        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        httpComponentsMessageSender.setCredentials(credentials);
        httpComponentsMessageSender.afterPropertiesSet();

        return httpComponentsMessageSender;
    }
}
