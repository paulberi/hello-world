package se.metria.markkoll.service.fastighet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.service.common.XpSearchService;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class RegisterenhetImportServiceTest {

    RegisterenhetImportService registerenhetImportService;

    EntityManager mockEntityManager;
    FastighetsomradeImportService mockFastighetsomradeImportService;
    FastighetOmradeRepository mockFastighetOmradeRepository;
    FastighetRepository mockFastighetRepository;
    XpSearchService mockXpSearchService;

    @BeforeEach
    void beforeEach() {
        mockEntityManager = mock(EntityManager.class);
        mockFastighetOmradeRepository = mock(FastighetOmradeRepository.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockFastighetsomradeImportService = mock(FastighetsomradeImportService.class);
        mockXpSearchService = mock(XpSearchService.class);

        registerenhetImportService = new RegisterenhetImportService(mockEntityManager,
            mockFastighetsomradeImportService, mockFastighetOmradeRepository, mockFastighetRepository,
            mockXpSearchService);
    }

    @Test
    void så_ska_det_gå_att_importera_en_registerenhet_för_ett_uuid() {
        // Given
        var registerenhetId = mockUUID(0);
        var fastighetsomraden = Arrays.asList(new FastighetsOmrInfo(), new FastighetsOmrInfo());
        var fastighetId = mockUUID(1);

        when(mockXpSearchService.getFastighetByUuid(eq(registerenhetId))).thenReturn(fastighetsomraden);
        when(mockFastighetsomradeImportService.importFastighetsomraden(eq(fastighetsomraden)))
            .thenReturn(CollectionUtil.asSet(fastighetId));

        // When
        var fastighetIdActual = registerenhetImportService.importRegisterenhet(registerenhetId);

        // Then
        assertEquals(Optional.of(fastighetId), fastighetIdActual);
    }
}