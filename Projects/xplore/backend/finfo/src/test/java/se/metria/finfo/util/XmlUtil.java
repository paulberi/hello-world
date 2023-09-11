package se.metria.finfo.util;

import org.springframework.core.io.ClassPathResource;
import se.metria.finfo.fsokws.Fastighetsinformation;
import se.metria.finfo.fsokws.FsFastighetsInformation;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class XmlUtil {
    public static
    Fastighetsinformation parseFastighetsinformation(String resourcePath) throws JAXBException, IOException {

        var xml = new ClassPathResource(resourcePath);

        JAXBContext context = JAXBContext.newInstance(FsFastighetsInformation.class);
        var unmarshaller = context.createUnmarshaller();
        var obj = unmarshaller.unmarshal(xml.getInputStream());
        var fastighetsinformation = ((JAXBElement<Fastighetsinformation>) obj).getValue();

        return fastighetsinformation;
    }
}
