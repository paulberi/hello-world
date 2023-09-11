package se.metria.markkoll.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.markkoll.service.geometristatus.GeometristatusService;

@ExtendWith(MockitoExtension.class)
class GeometristatusServiceTest {
    @Mock
    AvtalRepository mockAvtalRepository;

    @Mock
    GeometristatusRepository mockGeometristatusRepository;

    @Mock
    VersionRepository mockVersionRepository;

    @InjectMocks
    GeometristatusService geometristatusService;

}