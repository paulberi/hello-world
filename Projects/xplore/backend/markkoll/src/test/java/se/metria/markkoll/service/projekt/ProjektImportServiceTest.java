package se.metria.markkoll.service.projekt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.fastighet.RegisterenhetImportService;
import se.metria.markkoll.service.intrang.IntrangImportService;
import se.metria.markkoll.service.logging.LoggService;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class ProjektImportServiceTest {
    @Mock
    private AvtalUpdater avtalUpdater;

    @Mock
    private ProjektService projektService;

    @Mock
    private FastighetsintrangRepository fastighetsintrangRepository;

    @Mock
    private GeometristatusService geometristatusService;

    @Mock
    private LoggService loggService;

    @Mock
    private ProjektRepository projektRepository;

    @Mock
    private RegisterenhetImportService registerenhetImportService;

    @Mock
    private IntrangImportService intrangImportService;

    @Mock
    private UserService userService;

    @Mock
    private FastighetRepository fastighetRepository;

    @InjectMocks
    private ProjektImportService projektImportService;

    @Test
    void så_ska_det_gå_att_importera_intrång_till_ett_nytt_projekt() {
        // Given
        var projektId = mockUUID(0);
        var currentVersionId = mockUUID(2);
        var registerenheterIds = Arrays.asList(mockUUID(3), mockUUID(4));

        // When
        projektImportService.updateProjektVersion(projektId, currentVersionId, registerenheterIds);

        // Then
        verify(projektService).setCurrentVersion(eq(projektId), eq(currentVersionId));
        verify(avtalUpdater).addImportAvtal(eq(projektId), eq(registerenheterIds), eq(GeometristatusDto.OFORANDRAD));
    }

    @Test
    void så_ska_det_gå_att_importera_intrång_till_ett_projekt_med_existerande_versioner() {
        // Given
        var projektId = mockUUID(0);
        UUID previousVersionId = mockUUID(1);
        var currentVersionId = mockUUID(2);

        var removedFastigheterIds = Arrays.asList(mockUUID(3));

        var retainedFastigheterIds = Arrays.asList(mockUUID(4), mockUUID(5));
        var oforandradeFastigheterIds = Arrays.asList(retainedFastigheterIds.get(0));
        var uppdateradeFastigheterIds = Arrays.asList(retainedFastigheterIds.get(1));

        var addedFastigheterIds = Arrays.asList(mockUUID(6), mockUUID(7));
        var addedExistingFastigheterIds = Arrays.asList(addedFastigheterIds.get(0));
        var addedNewFastigheterIds = Arrays.asList(addedFastigheterIds.get(1));

        var previousRegisterenheterIds = Stream
            .concat(removedFastigheterIds.stream(), retainedFastigheterIds.stream())
            .collect(Collectors.toList());

        var currentRegisterenheterIds = Stream
            .concat(addedFastigheterIds.stream(), retainedFastigheterIds.stream())
            .collect(Collectors.toList());

        when(projektService.setCurrentVersion(eq(projektId), eq(currentVersionId))).thenReturn(previousVersionId);

        when(fastighetRepository.getAllByVersionId(eq(previousVersionId))).thenReturn(previousRegisterenheterIds);

        when(projektRepository.filterExistingRegisterenheterInProjekt(eq(projektId), eq(addedFastigheterIds)))
            .thenReturn(addedExistingFastigheterIds);
        when(fastighetsintrangRepository.geometriesEqual(eq(retainedFastigheterIds.get(0)), eq(previousVersionId),
            eq(currentVersionId))).thenReturn(true);

        // When
        projektImportService.updateProjektVersion(projektId, currentVersionId, currentRegisterenheterIds);

        // Then
        verify(projektService).setCurrentVersion(eq(projektId), eq(currentVersionId));

        verify(avtalUpdater).updateAvtal(eq(projektId), eq(removedFastigheterIds), eq(GeometristatusDto.BORTTAGEN));

        verify(avtalUpdater).updateAvtal(eq(projektId), eq(addedExistingFastigheterIds), eq(GeometristatusDto.NY));
        verify(avtalUpdater).addImportAvtal(eq(projektId), eq(addedNewFastigheterIds), eq(GeometristatusDto.NY));

        verify(avtalUpdater).updateAvtal(eq(projektId), eq(oforandradeFastigheterIds), eq(GeometristatusDto.OFORANDRAD));
        verify(avtalUpdater).updateAvtal(eq(projektId), eq(uppdateradeFastigheterIds), eq(GeometristatusDto.UPPDATERAD));
    }
}