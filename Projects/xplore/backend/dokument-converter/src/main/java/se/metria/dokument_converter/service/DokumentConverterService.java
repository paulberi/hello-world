package se.metria.dokument_converter.service;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@Slf4j
public class DokumentConverterService implements InitializingBean, DisposableBean {

    @Value( "${dokument-converter.office-home}" )
    private String officeHome;

    private OfficeManager officeManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Startar LibreOffice med sökväg " + officeHome);
        officeManager = LocalOfficeManager.builder()
            .officeHome(officeHome)
            .portNumbers(2002)
            .build();

        officeManager.start();
    }

    public Resource convertDocxToPdf(Resource docx) throws IOException, OfficeException {
        log.info("Konverterar fil {} till pdf", docx.getFilename());

        try (var baos = new ByteArrayOutputStream()) {
            var localConverter = LocalConverter.make(officeManager);
            localConverter.convert(docx.getInputStream()).to(baos).as(DefaultDocumentFormatRegistry.PDF).execute();

            log.info("Konvertering av fil {} klar", docx.getFilename());

            return new ByteArrayResource(baos.toByteArray());
        }
    }

    @Override
    public void destroy() {
        log.info("Stänger ner LibreOffice");
        OfficeUtils.stopQuietly(officeManager);
    }
}
