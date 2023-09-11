package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.openapi.model.ManuellFastighethandelseTypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ElnatProjektRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalDto;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.projekt.ElnatProjektService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
class ElnatProjektServiceTest {

    ElnatProjektService elnatProjektService;

    AvtalService mockAvtalService;
    FastighetsforteckningService mockFastighetsforteckningService;
    LoggService mockLoggService;
    ElnatVarderingsprotokollService mockElnatVarderingsprotokollService;
    OmradesintrangRepository mockOmradesintrangRepository;
    ElnatProjektRepository mockElnatProjektRepository;
    AvtalRepository mockAvtalRepository;
    ElnatKlassificeringService mockElnatKlassificeringService;
    AclService mockAclService;
    UserService mockUserService;
    GeometristatusService geometristatusService;
    ProjektRepository mockProjektRepository;

    @BeforeEach
    void setUp() {
        mockAvtalService = mock(AvtalService.class);
        mockFastighetsforteckningService = mock(FastighetsforteckningService.class);
        mockLoggService = mock(LoggService.class);
        mockElnatVarderingsprotokollService = mock(ElnatVarderingsprotokollService.class);
        mockElnatProjektRepository = mock(ElnatProjektRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockElnatKlassificeringService = mock(ElnatKlassificeringService.class);
        mockAclService = mock(AclService.class);
        mockUserService = mock(UserService.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        geometristatusService = mock(GeometristatusService.class);
        mockProjektRepository = mock(ProjektRepository.class);


        elnatProjektService = new ElnatProjektService(mockAvtalService, mockFastighetsforteckningService,
            mockLoggService, mockElnatVarderingsprotokollService, mockElnatProjektRepository,
            mockOmradesintrangRepository, mockAvtalRepository, mockElnatKlassificeringService, mockAclService,
            geometristatusService, mockUserService, mockProjektRepository);

    }

    @Test
    void så_ska_det_gå_att_lägga_till_ett_manuellt_tillagt_avtal() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalIdExpect = mockUUID(2);

        when(mockAvtalService.create(eq(projektId), eq(fastighetId))).thenReturn(avtalIdExpect);
        when(mockFastighetsforteckningService.create(eq(projektId), eq(fastighetId), eq(avtalIdExpect),
            eq(FastighetsforteckningAnledning.MANUELLT_TILLAGD))).thenReturn(mockUUID(4));

        // When
        var avtalId = elnatProjektService.addManuelltAvtal(projektId, fastighetId);

        // Then
        assertEquals(avtalIdExpect, avtalId);

        verify(mockAvtalService).create(eq(projektId), eq(fastighetId));
        verify(mockFastighetsforteckningService).create(eq(projektId), eq(fastighetId), eq(avtalIdExpect),
            eq(FastighetsforteckningAnledning.MANUELLT_TILLAGD));
        verify(mockLoggService).addManuellFastighetHandelse(eq(projektId), eq(fastighetId),
            eq(ManuellFastighethandelseTypDto.TILLAGD));
    }

    @Test
    void så_ska_det_gå_att_uppdatera_ett_avtal() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalId = mockUUID(2);
        var versionId = mockUUID(3);

        var avtal = new AvtalDto();
        avtal.setId(avtalId);
        var vp = new ElnatVarderingsprotokollDto();

        when(mockProjektRepository.getCurrentVersionId(eq(projektId))).thenReturn(versionId);
        when(mockAvtalService.getAvtal(eq(projektId), eq(fastighetId))).thenReturn(avtal);
        when(mockOmradesintrangRepository.calculateHighestSpanningsniva(eq(avtalId))).thenReturn(20.);
        when(mockElnatVarderingsprotokollService.getKlassificeratVarderingsprotokoll(eq(avtalId), eq(versionId))).thenReturn(vp);
        when(geometristatusService.getGeometristatus(eq(projektId), eq(fastighetId))).thenReturn(GeometristatusDto.UPPDATERAD);

        // When
        elnatProjektService.updateAvtal(projektId, fastighetId);

        // Then
        var avtalDto = new AvtalDto();
        avtalDto.setId(avtalId);
        verify(mockElnatVarderingsprotokollService).update(eq(vp), eq(avtalId));
        verify(mockFastighetsforteckningService).setExcluded(eq(projektId), eq(fastighetId), eq(false));
    }
}