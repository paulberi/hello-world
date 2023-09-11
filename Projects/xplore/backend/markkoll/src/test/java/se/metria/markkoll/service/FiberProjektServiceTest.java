package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollDto;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.openapi.model.ManuellFastighethandelseTypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.FiberProjektRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalDto;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.klassificering.FiberKlassificeringService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
class FiberProjektServiceTest {

    FiberProjektService fiberProjektService;

    AvtalService mockAvtalService;
    FastighetsforteckningService mockFastighetsforteckningService;
    LoggService mockLoggService;
    FiberVarderingsprotokollService mockFiberVarderingsprotokollService;
    FiberKlassificeringService mockFiberKlassificeringService;
    FiberProjektRepository mockFiberProjektRepository;
    OmradesintrangRepository mockOmradesintrangRepository;
    AvtalRepository mockAvtalRepository;
    AclService mockAclService;
    UserService mockUserService;
    GeometristatusService geometristatusService;
    ProjektRepository mockProjektRepository;

    @BeforeEach
    void setUp() {
        mockAvtalService = mock(AvtalService.class);
        mockFastighetsforteckningService = mock(FastighetsforteckningService.class);
        mockLoggService = mock(LoggService.class);
        mockFiberVarderingsprotokollService = mock(FiberVarderingsprotokollService.class);
        mockFiberKlassificeringService = mock(FiberKlassificeringService.class);
        mockFiberProjektRepository = mock(FiberProjektRepository.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockAclService = mock(AclService.class);
        mockUserService = mock(UserService.class);
        geometristatusService = mock(GeometristatusService.class);
        mockProjektRepository = mock(ProjektRepository.class);

        fiberProjektService = new FiberProjektService(mockAvtalService, mockFastighetsforteckningService,
            mockLoggService, geometristatusService, mockFiberVarderingsprotokollService,
            mockFiberKlassificeringService, mockFiberProjektRepository, mockOmradesintrangRepository,
            mockAvtalRepository, mockAclService, mockUserService, mockProjektRepository);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_det_gå_att_uppdatera_ett_avtal(boolean hasVp) {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalId = mockUUID(2);
        var versionId = mockUUID(3);

        var avtal = new AvtalDto();
        avtal.setId(avtalId);
        var vp = new FiberVarderingsprotokollDto();

        when(mockProjektRepository.getCurrentVersionId(eq(projektId))).thenReturn(versionId);
        when(mockAvtalService.getAvtal(eq(projektId), eq(fastighetId))).thenReturn(avtal);
        when(mockOmradesintrangRepository.calculateHighestSpanningsniva(eq(avtalId))).thenReturn(20.);
        when(mockFiberVarderingsprotokollService.getKlassificeratVarderingsprotokoll(eq(avtalId), eq(versionId))).thenReturn(vp);
        when(mockFiberProjektRepository.getShouldHaveVarderingsprotokoll(projektId)).thenReturn(hasVp);
        when(geometristatusService.getGeometristatus(eq(projektId), eq(fastighetId)))
            .thenReturn(GeometristatusDto.UPPDATERAD);

        // When
        fiberProjektService.updateAvtal(projektId, fastighetId);

        // Then
        var avtalDto = new AvtalDto();
        avtalDto.setId(avtalId);
        if (hasVp) {
            verify(mockFiberVarderingsprotokollService).update(eq(vp), eq(avtalId));
        }
        verify(mockFastighetsforteckningService).setExcluded(eq(projektId), eq(fastighetId), eq(false));
    }

    @Test
    void så_ska_det_gå_att_lägga_till_ett_manuellt_tillagt_avtal_med_ett_värderingsprotokoll() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalIdExpect = mockUUID(2);

        when(mockAvtalService.create(eq(projektId), eq(fastighetId))).thenReturn(avtalIdExpect);
        when(mockFastighetsforteckningService.create(eq(projektId), eq(fastighetId), eq(avtalIdExpect),
            eq(FastighetsforteckningAnledning.MANUELLT_TILLAGD))).thenReturn(mockUUID(4));
        when(mockFiberProjektRepository.getShouldHaveVarderingsprotokoll(eq(projektId))).thenReturn(true);

        // When
        var avtalId = fiberProjektService.addManuelltAvtal(projektId, fastighetId);

        // Then
        assertEquals(avtalIdExpect, avtalId);

        verify(mockAvtalService).create(eq(projektId), eq(fastighetId));
        verify(mockFastighetsforteckningService).create(eq(projektId), eq(fastighetId), eq(avtalIdExpect),
            eq(FastighetsforteckningAnledning.MANUELLT_TILLAGD));
        verify(mockLoggService).addManuellFastighetHandelse(eq(projektId), eq(fastighetId),
            eq(ManuellFastighethandelseTypDto.TILLAGD));
    }

    @Test
    void så_ska_det_gå_att_lägga_till_ett_manuellt_tillagt_avtal_utan_ett_värderingsprotokoll() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var avtalIdExpect = mockUUID(2);

        when(mockAvtalService.create(eq(projektId), eq(fastighetId))).thenReturn(avtalIdExpect);
        when(mockFastighetsforteckningService.create(eq(projektId), eq(fastighetId), eq(avtalIdExpect),
            eq(FastighetsforteckningAnledning.MANUELLT_TILLAGD))).thenReturn(mockUUID(4));
        when(mockFiberProjektRepository.getShouldHaveVarderingsprotokoll(eq(projektId))).thenReturn(false);

        // When
        var avtalId = fiberProjektService.addManuelltAvtal(projektId, fastighetId);

        // Then
        assertEquals(avtalIdExpect, avtalId);

        verify(mockAvtalService).create(eq(projektId), eq(fastighetId));
        verify(mockFastighetsforteckningService).create(eq(projektId), eq(fastighetId), eq(avtalIdExpect),
            eq(FastighetsforteckningAnledning.MANUELLT_TILLAGD));
        verify(mockLoggService).addManuellFastighetHandelse(eq(projektId), eq(fastighetId),
            eq(ManuellFastighethandelseTypDto.TILLAGD));
    }
}