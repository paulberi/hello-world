package se.metria.xplore.kundconfig.service;

import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import se.metria.xplore.keycloak.service.KeyCloakService;
import se.metria.xplore.kundconfig.entity.KundEntity;
import se.metria.xplore.kundconfig.openapi.model.KundDto;
import se.metria.xplore.kundconfig.openapi.model.KundInfoDto;
import se.metria.xplore.kundconfig.openapi.model.KundPageDto;
import se.metria.xplore.kundconfig.repository.KundConfigRepository;
import se.metria.xplore.kundconfig.service.geoserver.GeofenceService;
import se.metria.xplore.kundconfig.service.geoserver.GeoserverService;
import se.metria.xplore.kundconfig.testData.TestData;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Givet KundService")
public class KundServiceTest {

    KundService kundService;

    KundConfigRepository mockKundConfigRepository;
    GeoserverService mockGeoserverService;
    GeofenceService mockGeofenceService;
    KeyCloakService mockKeycloakService;
    AuthService mockAuthService;
    ModelMapper mockModelMapper;

    @BeforeEach
    void before() {
        mockKundConfigRepository = mock(KundConfigRepository.class);
        mockGeoserverService = mock(GeoserverService.class);
        mockGeofenceService = mock(GeofenceService.class);
        mockKeycloakService = mock(KeyCloakService.class);
        mockModelMapper = mock(ModelMapper.class);
        mockAuthService = mock(AuthService.class);

        kundService = new KundService(mockKundConfigRepository, mockGeoserverService, mockGeofenceService,
            mockKeycloakService, mockAuthService, mockModelMapper);
    }

    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_kunder() {
        // Given
        var page = 2;
        var size = 3;
        var pageKundEntity = (Page<KundEntity>) mock(Page.class);
        var pageKundExpect = new KundPageDto();

        when(mockKundConfigRepository.findAll((Pageable) any())).thenReturn(pageKundEntity);
        when(mockModelMapper.map(any(), any())).thenReturn(pageKundExpect);

        // When
        var pageKundActual = kundService.getKundPage(page, size);

        // Then
        Mockito.verify(mockKundConfigRepository).findAll(ArgumentMatchers.eq(PageRequest.of(page, size, Sort.by("skapadDatum").descending())));
        assertEquals(pageKundExpect, pageKundActual);
    }

    @Test
    void så_ska_det_gå_att_skapa_en_ny_kund_och_dess_behörigheter() throws Exception {
        // Given
        var orgnummer = "orgnr";
        var namn = "namn";
        var kundInfo = new KundInfoDto()
                .organisationsnummer(orgnummer)
                .namn(namn);
        var entity = mock(KundEntity.class);
        var kundExpect = mock(KundDto.class);

        when(mockModelMapper.map(kundInfo, KundEntity.class)).thenReturn(entity);
        when(mockModelMapper.map(entity, KundDto.class)).thenReturn(kundExpect);
        when(mockKundConfigRepository.saveAndFlush(any())).thenReturn(entity);

        // When
        var kund = kundService.createKund(kundInfo);

        // Then
        assertEquals(kundExpect, kund);
        verify(entity).setId(eq(orgnummer));
        verify(mockKundConfigRepository).saveAndFlush(eq(entity));

        verify(mockKeycloakService).createRealmRole(eq(orgnummer), eq(namn));
        verify(mockGeoserverService).createRole(eq(orgnummer));
        verify(mockGeofenceService).deleteRulesForKund(eq(orgnummer));
        verify(mockGeofenceService).createRulesForKund(eq(orgnummer));

        verify(mockAuthService).createMetriaMapsAuth(eq(orgnummer));
        verify(mockAuthService).createFastighetsokAuth(eq(orgnummer));
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en_kund_och_dess_behörigheter() throws Exception {
        // Given
        var orgnummer = "orgnr";
        var namn = "namn";

        when(mockKundConfigRepository.existsById(any())).thenReturn(true);
        when(mockGeofenceService.deleteRulesForKund(any())).thenReturn(23);

        // When
        kundService.deleteKund(orgnummer);

        // Then
        verify(mockKundConfigRepository).existsById(eq(orgnummer));
        verify(mockKundConfigRepository).deleteById(eq(orgnummer));

        verify(mockGeofenceService).deleteRulesForKund(eq(orgnummer));
        verify(mockGeoserverService).deleteRole(eq(orgnummer));
    }

    @Test
    void så_ska_det_gå_att_återskapa_geofence_regler_för_alla_kunder() throws Exception {
        // Given
        var kund1 = TestData.mockKundEntity();
        var kund2 = TestData.mockKundEntity();
        kund2.setId("222");
        var kundEntitites = new ArrayList<KundEntity>(Arrays.asList(kund1, kund2));
        var kundIds = new ArrayList<String>(Arrays.asList(kund1.getId(), kund2.getId()));

        when(mockKundConfigRepository.findAll()).thenReturn(kundEntitites);
        when(mockGeofenceService.deleteRulesForKund(any())).thenReturn(23);
        when(mockGeofenceService.createRulesForKund(any())).thenReturn(new ArrayList<>(Arrays.asList(333, 444)));

        // When
        kundService.resetGeofenceRules();

        // Then
        verify(mockGeofenceService, times(1)).resetAllRules(eq(kundIds));
    }
}
