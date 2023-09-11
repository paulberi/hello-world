package se.metria.markkoll.service.vardering.fiber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberVarderingConfigEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.markagare.PersonRepository;
import se.metria.markkoll.repository.vardering.fiber.FiberVarderingConfigRepository;
import se.metria.markkoll.repository.vardering.fiber.FiberVarderingsprotokollRepository;
import se.metria.markkoll.repository.vardering.fiber.VarderingKundAgareRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class FiberVarderingConfigServiceTest {
    FiberVarderingConfigService fiberVarderingConfigService;

    AvtalRepository mockAvtalRepository;
    FiberVarderingsprotokollRepository mockFiberVarderingsprotokollRepository;
    FiberVarderingConfigRepository mockFiberVarderingConfigRepository;
    PersonRepository mockPersonRepository;

    KundRepository mockKundRepository;

    VarderingKundAgareRepository mockVarderingKundAgareRepository;

    FiberVarderingConfigDto configAgareDto = new FiberVarderingConfigDto()
        .markskap(new VarderingConfigMarkskapDto())
        .optobrunn(new VarderingConfigOptobrunnDto())
        .teknikbod(new VarderingConfigTeknikbodDto())
        .schablonersattning(new VarderingConfigSchablonersattningDto())
        .minimiersattningEnbartMarkledning(false)
        .minimiersattningEnbartMarkledning(false)
        .sarskildErsattningMaxbelopp(123)
        .grundersattning(10);
    FiberVarderingConfigEntity configAgareEntity = FiberVarderingConfigEntity.builder()
        .grundersattning(10)
        .minimiersattningEnbartMarkledning(false)
        .sarskildErsattningMaxbelopp(123)
        .build();

    FiberVarderingConfigDto configKundDto = new FiberVarderingConfigDto()
        .markskap(new VarderingConfigMarkskapDto())
        .optobrunn(new VarderingConfigOptobrunnDto())
        .teknikbod(new VarderingConfigTeknikbodDto())
        .schablonersattning(new VarderingConfigSchablonersattningDto())
        .minimiersattningEnbartMarkledning(false)
        .sarskildErsattningMaxbelopp(123)
        .grundersattning(20);
    FiberVarderingConfigEntity configKundEntity = FiberVarderingConfigEntity.builder()
        .grundersattning(20)
        .minimiersattningEnbartMarkledning(false)
        .sarskildErsattningMaxbelopp(123)
        .build();

    FiberVarderingConfigDto configMetriaDto = new FiberVarderingConfigDto()
        .markskap(new VarderingConfigMarkskapDto()
            .skog(1)
            .jordbruksimpediment(2)
            .ovrigMark(3)
        )
        .optobrunn(new VarderingConfigOptobrunnDto()
            .skog(4)
            .jordbruksimpediment(5)
            .ovrigMark(6)
        )
        .teknikbod(new VarderingConfigTeknikbodDto()
            .skog6x6m(7)
            .skog8x8m(8)
            .skog10x10m(9)
            .jordbruksimpediment6x6m(10)
            .jordbruksimpediment8x8m(11)
            .jordbruksimpediment10x10m(12)
            .ovrigMark6x6m(13)
            .ovrigMark8x8m(14)
            .ovrigMark10x10m(15)
        )
        .schablonersattning(new VarderingConfigSchablonersattningDto()
            .optoror1m(16.)
            .optoror2m(17.)
        )
        .grundersattning(18)
        .minimiersattning(19)
        .tillaggExpropriationslagen(20)
        .minimiersattningEnbartMarkledning(false)
        .sarskildErsattningMaxbelopp(123)
        .sarskildErsattning(21);

    FiberVarderingConfigEntity configMetriaEntity = FiberVarderingConfigEntity.builder()
        .markskapSkog(1)
        .markskapJordbruksimpediment(2)
        .markskapOvrigMark(3)
        .optobrunnSkog(4)
        .optobrunnJordbruksimpediment(5)
        .optobrunnOvrigMark(6)
        .teknikbodSkog6x6m(7)
        .teknikbodSkog8x8m(8)
        .teknikbodSkog10x10m(9)
        .teknikbodJordbruksimpediment6x6m(10)
        .teknikbodJordbruksimpediment8x8m(11)
        .teknikbodJordbruksimpediment10x10m(12)
        .teknikbodOvrigMark6x6m(13)
        .teknikbodOvrigMark8x8m(14)
        .teknikbodOvrigMark10x10m(15)
        .schablonersattningOptoror1m(16.)
        .schablonersattningOptoror2m(17.)
        .grundersattning(18)
        .minimiersattning(19)
        .tillaggExpropriationslagen(20)
        .sarskildErsattning(21)
        .minimiersattningEnbartMarkledning(false)
        .sarskildErsattningMaxbelopp(123)
        .build();

    @BeforeEach
    void beforeEach() {
        mockAvtalRepository = mock(AvtalRepository.class);
        mockFiberVarderingsprotokollRepository = mock(FiberVarderingsprotokollRepository.class);
        mockFiberVarderingConfigRepository = mock(FiberVarderingConfigRepository.class);
        mockPersonRepository = mock(PersonRepository.class);
        mockKundRepository = mock(KundRepository.class);
        mockVarderingKundAgareRepository = mock (VarderingKundAgareRepository.class);

        fiberVarderingConfigService = new FiberVarderingConfigService(mockAvtalRepository,
            mockFiberVarderingsprotokollRepository, mockFiberVarderingConfigRepository, mockPersonRepository, mockKundRepository, mockVarderingKundAgareRepository);
    }

    @Test
    void config_för_en_given_ägare() {
        // Given
        var avtalId = mockUUID(0);

        when(mockAvtalRepository.getKundId(any())).thenReturn("kundId");
        when(mockPersonRepository.getKontaktpersonForAvtal(any()))
            .thenReturn(Optional.of(PersonEntity.builder().personnummer("personnummer").build()));

        when(mockFiberVarderingConfigRepository.getAgareConfig(eq("kundId"), eq("personnummer")))
            .thenReturn(Optional.of(configAgareEntity));

        // When
        var config = fiberVarderingConfigService.getFiberVarderingConfigForAvtal(avtalId);

        // Then
        assertEquals(configAgareDto, config);
    }

    @Test
    void kund_default() {
        // Given
        var avtalId = mockUUID(0);

        when(mockAvtalRepository.getKundId(any())).thenReturn("kundId");
        when(mockPersonRepository.getKontaktpersonForAvtal(any()))
            .thenReturn(Optional.of(PersonEntity.builder().personnummer("personnummer").build()));

        when(mockFiberVarderingConfigRepository.getAgareConfig(eq("kundId"), eq("personnummer")))
            .thenReturn(Optional.empty());
        when(mockFiberVarderingConfigRepository.getKundDefaultConfig(eq("kundId")))
            .thenReturn(Optional.of(configKundEntity));

        // When
        var config = fiberVarderingConfigService.getFiberVarderingConfigForAvtal(avtalId);

        // Then
        assertEquals(configKundDto, config);
    }

    @Test
    void metria_default() {
        // Given
        var avtalId = mockUUID(0);

        when(mockAvtalRepository.getKundId(any())).thenReturn("kundId");
        when(mockPersonRepository.getKontaktpersonForAvtal(any()))
            .thenReturn(Optional.of(PersonEntity.builder().personnummer("personnummer").build()));

        when(mockFiberVarderingConfigRepository.getAgareConfig(eq("kundId"), eq("personnummer")))
            .thenReturn(Optional.empty());
        when(mockFiberVarderingConfigRepository.getKundDefaultConfig(eq("kundId")))
            .thenReturn(Optional.empty());
        when(mockFiberVarderingConfigRepository.getMetriaDefault())
            .thenReturn(Optional.of(configMetriaEntity));

        // When
        var config = fiberVarderingConfigService.getFiberVarderingConfigForAvtal(avtalId);

        // Then
        assertEquals(configMetriaDto, config);
    }

    @Test
    void kontaktperson_saknas() {
        // Given
        var avtalId = mockUUID(0);

        when(mockAvtalRepository.getKundId(any())).thenReturn("kundId");
        when(mockPersonRepository.getKontaktpersonForAvtal(any())).thenReturn(Optional.empty());
        when(mockFiberVarderingConfigRepository.getKundDefaultConfig(eq("kundId")))
            .thenReturn(Optional.of(configKundEntity));

        // When
        var config = fiberVarderingConfigService.getFiberVarderingConfigForAvtal(avtalId);

        // Then
        assertEquals(configKundDto, config);
    }
}