package se.metria.markkoll.service.vardering.fiber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.vardering.fiber.FiberVarderingsprotokollRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.klassificering.FiberKlassificeringService;
import se.metria.markkoll.util.modelmapper.FiberVpConverterTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class FiberVarderingsprotokollServiceTest {
    FiberVarderingsprotokollService fiberVarderingsprotokollService;

    AclService mockAclService;
    FiberKlassificeringService mockFiberKlassificeringService;
    FiberVarderingsprotokollRepository mockFiberVarderingsprotokollRepository;
    AvtalRepository mockAvtalRepository;
    OmradesintrangRepository mockOmradesintrangRepository;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockFiberKlassificeringService = mock(FiberKlassificeringService.class);
        mockFiberVarderingsprotokollRepository = mock(FiberVarderingsprotokollRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);

        fiberVarderingsprotokollService = new FiberVarderingsprotokollService(mockAclService,
            mockFiberKlassificeringService, mockFiberVarderingsprotokollRepository, mockAvtalRepository,
            mockOmradesintrangRepository);
    }

    @Test
    void så_ska_det_gå_att_uppdatera_ett_värderingsprotokoll_för_ett_visst_avtal() {
        // Given
        var avtalId = mockUUID(0);

        var vpDto = FiberVpConverterTest.fiberDto();
        var vpEntity = new FiberVarderingsprotokollEntity();
        vpEntity.setId(mockUUID(1));

        assertNotEquals(vpDto.getId(), vpEntity.getId());

        when(mockFiberVarderingsprotokollRepository.findByAvtalId(eq(avtalId))).thenReturn(Optional.of(vpEntity));

        // When
        fiberVarderingsprotokollService.update(vpDto, avtalId);

        // Then
        var vpEntityExpect = FiberVpConverterTest.fiberEntity();
        vpEntityExpect.setId(mockUUID(1));
        verify(mockFiberVarderingsprotokollRepository).save(eq(vpEntityExpect));
    }
}