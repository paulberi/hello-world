package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.admin.UserEntity;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.admin.UserRepository;
import se.metria.markkoll.service.UtskicksnummerService;
import se.metria.markkoll.service.geofence.GeofenceService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.xplore.geoserveradmin.service.GeoserverAdminService;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class KundServiceTest {
    KundService kundService;

    AclService mockAclService;
    GeofenceService mockGeofenceService;
    GeoserverAdminService mockGeoserverAdminService;
    KundRepository mockKundRepository;
    ModelMapper mockModelMapper;
    UserRepository mockUserRepository;
    UtskicksnummerService mockUtskicksnummerService;


    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockGeofenceService = mock(GeofenceService.class);
        mockGeoserverAdminService = mock(GeoserverAdminService.class);
        mockKundRepository  = mock(KundRepository.class);
        mockModelMapper = mock(ModelMapper.class);
        mockUserRepository = mock(UserRepository.class);
        mockUtskicksnummerService = mock(UtskicksnummerService.class);

        kundService = new KundService(mockAclService, mockGeofenceService, mockGeoserverAdminService,
            mockKundRepository, mockModelMapper, mockUserRepository, mockUtskicksnummerService);
    }

    @Test
    void ska_skapa_en_ny_kund_om_den_inte_existerar() throws Exception {
        // Given
        var kundId = "kundId";
        
        // When
        kundService.createIfNotExists(kundId);

        // Then
        verify(mockKundRepository).save(eq(KundEntity.of(kundId)));
        verify(mockAclService).createObject(eq(kundId), eq(KundEntity.class));
        verify(mockUtskicksnummerService).create(eq(kundId));

        verify(mockGeoserverAdminService).createRole(eq(kundId));
        verify(mockGeoserverAdminService).createRole(eq(kundService.getExternalRole(kundId)));
        verify(mockGeofenceService).deleteRulesForKund(eq(kundId));
        verify(mockGeofenceService).createRulesForKund(eq(kundId));
    }

    @Test
    void ska_inte_skapa_ny_kund_om_den_redan_existerar() throws Exception {
        // Given
        var kundId = "kundId";
        when(mockKundRepository.existsById(eq(kundId))).thenReturn(true);

        // When
        kundService.createIfNotExists(kundId);

        // Then
        verify(mockKundRepository, never()).save(any());
        verify(mockAclService, never()).createObject(any(), any());

        verify(mockGeoserverAdminService, never()).createRole(any());
        verify(mockGeoserverAdminService, never()).createRole(any());
        verify(mockGeofenceService, never()).deleteRulesForKund(any());
        verify(mockGeofenceService, never()).createRulesForKund(any());
    }

    @Test
    void ska_inte_anropa_externa_tjänster_om_det_är_inaktiverat() throws Exception {
        // Given
        var kundId = "kundId";
        kundService.disableExternalServicesCreate = true;

        // When
        kundService.createIfNotExists(kundId);

        // Then
        verify(mockKundRepository).save(eq(KundEntity.of(kundId)));
        verify(mockAclService).createObject(eq(kundId), eq(KundEntity.class));

        verify(mockGeoserverAdminService, never()).createRole(any());
        verify(mockGeoserverAdminService, never()).createRole(any());
        verify(mockGeofenceService, never()).deleteRulesForKund(any());
        verify(mockGeofenceService, never()).createRulesForKund(any());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_lista_med_användare_för_en_kund() {
        // Given
        var kundId = "kundId";

        var userEntities = CollectionUtil.asSet(
            new UserEntity("id", "efternamn", "email", "fornamn", new KundEntity()),
            new UserEntity("id2", "efternamn2", "email2", "fornamn2", new KundEntity())
        );
        var userDto = new MarkkollUserDto().kundId("kund");

        when(mockModelMapper.map(any(), any())).thenReturn(userDto);
        when(mockUserRepository.getUsersForKund(eq(kundId))).thenReturn(userEntities);

        // When
        var users = kundService.getUsers(kundId);

        // Then
        assertEquals(Arrays.asList(userDto, userDto), users);

        for (var entity: userEntities) {
            verify(mockModelMapper).map(eq(entity), eq(MarkkollUserDto.class));
        }
    }
}