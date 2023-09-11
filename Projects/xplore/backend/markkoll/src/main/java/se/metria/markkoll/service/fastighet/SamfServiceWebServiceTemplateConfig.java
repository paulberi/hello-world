package se.metria.markkoll.service.fastighet;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import se.metria.markkoll.service.KundConfigService;

@Configuration
@Component
@RequiredArgsConstructor
public class SamfServiceWebServiceTemplateConfig {

    @NonNull
    private final KundConfigService kundConfigService;

    @Value("${finfo.fsok-url}/fsok-2.3-ws/webservice")
    private String fsokUrl;

    public Jaxb2Marshaller marshaller() throws Exception {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setContextPath("se.metria.markkoll.fsokws");
            marshaller.afterPropertiesSet();
            return marshaller;
    }

    public WebServiceTemplate getSamfWebServiceTemplate() throws Exception {
        var webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setDefaultUri(fsokUrl);

        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());

        webServiceTemplate.setMessageSender(httpComponentsMessageSender());
        webServiceTemplate.afterPropertiesSet();
        return webServiceTemplate;
    }

    public HttpComponentsMessageSender httpComponentsMessageSender() throws Exception {
        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        // set the basic authorization credentials
        httpComponentsMessageSender.setCredentials(usernamePasswordCredentials());
        httpComponentsMessageSender.afterPropertiesSet();
        return httpComponentsMessageSender;
    }

    public UsernamePasswordCredentials usernamePasswordCredentials() {
        // pass the user name and password to be used
        var auth = kundConfigService.getFastighetsokAuth();
        return new UsernamePasswordCredentials(auth.getUsername(), auth.getPassword());
    }
}
