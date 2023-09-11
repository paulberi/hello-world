package se.metria.xplore.castor.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Slf4j
@Configuration
@Component
public class FastighetsinfoServiceWebServiceTemplateConfig {

    @Value("${proxy.host}")
    private String proxyHost;
    @Value("${proxy.port}")
    private String proxyPort;
    @Value("${proxy.useproxy}")
    private String useProxy;

    @Value("${finfo.fsok-url}/fsok-2.3-ws/webservice")
    private String fsokUrl;

    @Value("${finfo.auth.username}")
    private String userName;

    @Value("${finfo.auth.password}")
    private String userPassword;

    private static WebServiceTemplate webServiceTemplate;

    @Bean
    public Jaxb2Marshaller marshaller() throws Exception {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setContextPath("com.metria.finfo.data.finfodatamodell");
            marshaller.afterPropertiesSet();
            return marshaller;
    }

    @Bean
    public WebServiceTemplate getSamfWebServiceTemplate() throws Exception {
        log.debug("FSök url: " + fsokUrl);
        log.debug("FSök useProxy: " + useProxy);
        log.debug("FSök proxy: " + proxyHost+":"+proxyPort);
        log.trace("FSök user: " + userName);
        log.trace("FSök password: " + userPassword);
        if(webServiceTemplate!=null){
            log.debug("webServiceTemplate redan skapad, returnera den");
            return webServiceTemplate;
        } else {
            log.debug("webServiceTemplate skapas");
            webServiceTemplate = new WebServiceTemplate();
            webServiceTemplate.setDefaultUri(fsokUrl);

            webServiceTemplate.setMarshaller(marshaller());
            webServiceTemplate.setUnmarshaller(marshaller());

            webServiceTemplate.setMessageSender(httpComponentsMessageSender());

            webServiceTemplate.afterPropertiesSet();
            return webServiceTemplate;
        }
    }


    @Bean
    public HttpComponentsMessageSender httpComponentsMessageSender() throws Exception {
        HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
        // set the basic authorization credentials
        httpComponentsMessageSender.setCredentials(usernamePasswordCredentials());

        httpComponentsMessageSender.afterPropertiesSet();
        return httpComponentsMessageSender;
    }

    @Bean
    public UsernamePasswordCredentials usernamePasswordCredentials() {
        log.debug("Setting credentials");
        log.trace("FSök user:password " + userName + ":" + userPassword);
        // pass the user name and password to be used
        return new UsernamePasswordCredentials(userName, userPassword);
    }
}
