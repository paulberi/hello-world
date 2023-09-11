package se.metria.markkoll.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.fastighet.RegisterenhetImportService;
import se.metria.markkoll.service.projekt.*;

@Configuration
public class ProjektImportServiceConfig {
    @Autowired
    private ElnatProjektService elnatProjektService;

    @Autowired
    private FiberProjektService fiberProjektService;

    @Autowired
    private ProjektService projektService;

    @Autowired
    private FastighetRepository fastighetRepository;

    @Autowired
    private FastighetsintrangRepository fastighetsintrangRepository;

    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private RegisterenhetImportService registerenhetImportService;

    @Bean
    ProjektImportService elnatProjektImportService() {
        return createProjektImportService(elnatProjektService);
    }

    @Bean
    ProjektImportService fiberProjektImportService() {
        return createProjektImportService(fiberProjektService);
    }

    ProjektImportService createProjektImportService(AvtalUpdater avtalUpdater) {
        return new ProjektImportService(
            avtalUpdater,
            projektService,
            fastighetRepository,
            fastighetsintrangRepository,
            projektRepository,
            registerenhetImportService
        );
    }
}
