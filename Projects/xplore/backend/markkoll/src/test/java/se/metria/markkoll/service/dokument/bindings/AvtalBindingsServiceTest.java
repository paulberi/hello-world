package se.metria.markkoll.service.dokument.bindings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.AuditorAware;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.dokument.bindings.data.ElIntrangBindings;
import se.metria.markkoll.service.dokument.bindings.data.ErsattningBindings;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringDto;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringService;
import se.metria.markkoll.service.klassificering.FiberKlassificeringDto;
import se.metria.markkoll.service.klassificering.FiberKlassificeringService;
import se.metria.markkoll.service.map.MapService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollGeneratorService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollGeneratorService;
import se.metria.markkoll.util.vardering.ElnatErsattningDto;
import se.metria.markkoll.util.vardering.FiberErsattningDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.openapi.model.ElnatPunktersattningTypDto.*;
import static se.metria.markkoll.service.dokument.bindings.AvtalBindingsService.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class AvtalBindingsServiceTest {

    AvtalBindingsService avtalBindingsService;

    AuditorAware<String> mockAuditorAware;
    ElnatKlassificeringService mockElnatKlassificeringService;
    ElnatVarderingsprotokollService mockElnatVarderingsprotokollService;
    ElnatVarderingsprotokollGeneratorService mockElnatVarderingsprotokollGeneratorService;
    FastighetService mockFastighetService;
    FiberKlassificeringService mockFiberKlassificeringService;
    FiberProjektService mockFiberProjektService;
    FiberVarderingsprotokollGeneratorService mockFiberVarderingsprotokollGeneratorService;
    MapService mockMapService;
    ProjektRepository mockProjektRepository;

    @BeforeEach
    void beforeEach() {
        mockAuditorAware = mock(AuditorAware.class);
        mockElnatKlassificeringService = mock(ElnatKlassificeringService.class);
        mockElnatVarderingsprotokollService = mock(ElnatVarderingsprotokollService.class);
        mockElnatVarderingsprotokollGeneratorService = mock(ElnatVarderingsprotokollGeneratorService.class);
        mockFiberKlassificeringService = mock(FiberKlassificeringService.class);
        mockFiberProjektService = mock(FiberProjektService.class);
        mockFiberVarderingsprotokollGeneratorService = mock(FiberVarderingsprotokollGeneratorService.class);
        mockFastighetService = mock(FastighetService.class);
        mockMapService = mock(MapService.class);
        mockProjektRepository = mock(ProjektRepository.class);

        avtalBindingsService = new AvtalBindingsService(mockAuditorAware, mockElnatKlassificeringService,
            mockElnatVarderingsprotokollService, mockElnatVarderingsprotokollGeneratorService, mockFastighetService,
            mockFiberKlassificeringService, mockFiberProjektService, mockFiberVarderingsprotokollGeneratorService,
            mockMapService, mockProjektRepository);
    }

    @ParameterizedTest
    @EnumSource(value = ProjektTypDto.class, names = {"LOKALNAT", "REGIONNAT", "FIBER"}, mode = EnumSource.Mode.INCLUDE)
    void så_ska_generell_avtalsinformation_vara_korrekt(ProjektTypDto projektTyp) throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        var fastighetsinfo = new FastighetsinfoDto()
            .fastighetsbeteckning("Fastighetsbeteckning")
            .kommunnamn("KOMMUN")
            .ersattning(123)
            .franStation("Station från")
            .stationsnamn("Stationsnamn")
            .tillStation("Station till")
            .markslag("Markslag")
            .lan("LÄN");

        when(mockFastighetService.fastighetsinfo(eq(fastighetId), eq(projektId))).thenReturn(fastighetsinfo);
        when(mockProjektRepository.getProjekttyp(eq(projektId))).thenReturn(projektTyp);

        // When
        var bindings = avtalBindingsService.getAvtalBindings(projektId, fastighetId);

        // Then
        assertEquals("Kommun", bindings.getKommun());
        assertEquals(fastighetsinfo.getFranStation(), bindings.getStationFran());
        assertEquals(fastighetsinfo.getTillStation(), bindings.getStationTill());
        assertEquals(fastighetsinfo.getStationsnamn(), bindings.getStationsnamn());
        assertEquals(fastighetsinfo.getMarkslag(), bindings.getMarkslag());
        assertEquals("Län", bindings.getLan());
        assertEquals(INLOST_BREDD_DEFAULT, bindings.getInlostBreddMeter());
        assertEquals(INLOST_BREDD_DEFAULT, bindings.getInlostBreddSkogMeter());

        switch (projektTyp) {
            case LOKALNAT:
                assertEquals(INLOST_BREDD_SKOG_LUFT_LOKALNAT, bindings.getInlostBreddSkogLuftMeter());
                break;
            case REGIONNAT:
                assertEquals(INLOST_BREDD_SKOG_LUFT_REGIONNAT, bindings.getInlostBreddSkogLuftMeter());
                break;
            case FIBER:
                assertEquals(INLOST_BREDD_DEFAULT, bindings.getInlostBreddSkogLuftMeter());
                break;
        }
    }

    @Test
    void så_ska_kartinformation_vara_korrekt() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        var map3 = new ByteArrayResource("intrangMap3".getBytes());
        var map2 = new ByteArrayResource("intrangMap2".getBytes());

        var delomraden = Arrays.asList(
            new FastighetDelomradeDto().omradeNr(3),
            new FastighetDelomradeDto().omradeNr(2)
        );

        when(mockFastighetService.fastighetsinfo(eq(fastighetId), eq(projektId)))
            .thenReturn(new FastighetsinfoDto().omraden(delomraden));

        when(mockMapService.getAvtalskarta(eq(projektId), eq(fastighetId), eq(Long.valueOf(3))))
            .thenReturn(map3);
        when(mockMapService.getAvtalskarta(eq(projektId), eq(fastighetId), eq(Long.valueOf(2))))
            .thenReturn(map2);
        when(mockProjektRepository.getProjekttyp(any())).thenReturn(ProjektTypDto.LOKALNAT);

        // When
        var bindings = avtalBindingsService.getAvtalBindings(projektId, fastighetId);

        // Then
        assertEquals(Arrays.asList(map2, map3), bindings.getKartor_multi());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_ersättningsinformation_vara_korrekt_för_elnät(boolean hasVp) throws Exception {
        // Given
        var avtalId = mockUUID(0);
        var totalErsattning = 500;
        var rotnetto = 120.75;

        var ersattning = new ElnatErsattningDto();
        ersattning.setTotal(totalErsattning);
        ersattning.setRotnetto(rotnetto);

        when(mockProjektRepository.getProjekttypAvtal(eq(avtalId))).thenReturn(ProjektTypDto.LOKALNAT);
        when(mockElnatVarderingsprotokollGeneratorService.getErsattning(eq(avtalId)))
            .thenReturn(hasVp? Optional.of(ersattning) : Optional.empty());

        // When
        var bindings = avtalBindingsService.getErsattningBindings(avtalId);

        // Then
        var bindingsExpect = ErsattningBindings.builder()
            .ersattningsbelopp(totalErsattning)
            .rotnetto(rotnetto)
            .rubrikUtanordningSjalvfaktura("Självfaktura")
            .rotnettoMoms(30.19)
            .build();

        if (hasVp) {
            assertEquals(bindingsExpect, bindings);
        }
        else {
            assertEquals(new ErsattningBindings(), bindings);
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_ersättningsinformation_vara_korrekt_för_fiber(boolean hasVp) throws Exception {
        // Given
        var avtalId = mockUUID(0);
        var totalErsattning = 500;
        var totalErsattning2 = 600;

        var ersattning = new FiberErsattningDto();
        ersattning.setTotal(totalErsattning);

        when(mockProjektRepository.getProjekttypAvtal(eq(avtalId))).thenReturn(ProjektTypDto.FIBER);
        when(mockFiberVarderingsprotokollGeneratorService.getErsattning(eq(avtalId)))
            .thenReturn(hasVp? Optional.of(ersattning) : Optional.empty());
        when(mockFastighetService.fastighetsinfo(avtalId))
            .thenReturn(new FastighetsinfoDto().ersattning(totalErsattning2));
        when(mockFiberProjektService.shouldHaveVarderingsprotokoll(any())).thenReturn(true);

        // When
        var bindings = avtalBindingsService.getErsattningBindings(avtalId);

        // Then
        if (hasVp) {
            var bindingsExpect = ErsattningBindings.builder()
                .ersattningsbelopp(totalErsattning)
                .build();

            assertEquals(bindingsExpect, bindings);
        }
        else {
            var bindingsExpect = ErsattningBindings.builder()
                .ersattningsbelopp(totalErsattning2)
                .build();

            assertEquals(bindingsExpect, bindings);
        }
    }

    @Test
    void så_ska_handläggarinformation_vara_korrekt() throws IOException {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        var handlaggare = "Handläggare";
        when(mockFastighetService.fastighetsinfo(any(), any())).thenReturn(new FastighetsinfoDto());
        when(mockAuditorAware.getCurrentAuditor()).thenReturn(Optional.of(handlaggare));
        when(mockProjektRepository.getProjekttyp(any())).thenReturn(ProjektTypDto.LOKALNAT);

        // When
        var bindings = avtalBindingsService.getAvtalBindings(projektId, fastighetId);

        // Then
        assertEquals(handlaggare, bindings.getHandlaggare());
    }

    @ParameterizedTest
    @EnumSource(TillvaratagandeTypDto.class)
    void så_ska_tillvaratandeinformation_vara_korrekt(TillvaratagandeTypDto tillvaratagandeTyp) throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        var fastighetsinfo = new FastighetsinfoDto()
            .tillvaratagandeTyp(tillvaratagandeTyp);

        when(mockFastighetService.fastighetsinfo(eq(fastighetId), eq(projektId))).thenReturn(fastighetsinfo);
        when(mockProjektRepository.getProjekttyp(any())).thenReturn(ProjektTypDto.LOKALNAT);

        // When
        var bindings = avtalBindingsService.getAvtalBindings(projektId, fastighetId);

        // Then
        assertEquals(fastighetsinfo.getTillvaratagandeTyp() == TillvaratagandeTypDto.EGET_TILLVARATAGANDE,
            bindings.getEgetTillvaratagandeAgarensVal());
        assertEquals(fastighetsinfo.getTillvaratagandeTyp() == TillvaratagandeTypDto.ROTNETTO,
            bindings.getRotnettoAgarensVal());
    }

    @Test
    void så_ska_elnätsintrångsinformation_vara_korrekt_om_nätstation_finns() {
        // Given
        var avtalId = mockUUID(0);
        var vp = new ElnatVarderingsprotokollDto().punktersattning(
            Arrays.asList(
                punktersattningDto("Ingen transformatorstation", SJOKABELSKYLT_JORDBRUKSIMPEDIMENT_6X6M),
                punktersattningDto("Liten transformatorstation", NATSTATION_JORDBRUKSIMPEDIMENT_6X6M),
                punktersattningDto("Mellan transformatorstation", NATSTATION_OVRIGMARK_8X8M),
                punktersattningDto("Stor transformatorstation", NATSTATION_SKOG_10X10M)
            )
        );

        when(mockElnatKlassificeringService.klassificera(eq(avtalId))).thenReturn(new ElnatKlassificeringDto());
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vp));

        // When
        var elIntrangBindings = avtalBindingsService.getElnatIntrangBindings(avtalId)
            .getElIntrangBindings();

        // Then
        assertEquals(true, elIntrangBindings.getHasTransformatorstation());
        assertEquals("Stor transformatorstation", elIntrangBindings.getTransformatorstationNamn());
        assertEquals(10, elIntrangBindings.getTransformatorstationBreddAMeter());
        assertEquals(10, elIntrangBindings.getTransformatorstationBreddBMeter());
        assertEquals("10x10m", elIntrangBindings.getTransformatorstationYta());
    }

    @Test
    void så_ska_elnätsintrångsinformation_vara_korrekt_om_nätstation_saknas() {
        // Given
        var avtalId = mockUUID(0);

        when(mockElnatKlassificeringService.klassificera(eq(avtalId))).thenReturn(new ElnatKlassificeringDto());
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId)))
            .thenReturn(Optional.of(new ElnatVarderingsprotokollDto()));

        // When
        var bindings = avtalBindingsService.getElnatIntrangBindings(avtalId);

        // Then
        assertNoTransformatorstation(bindings.getElIntrangBindings());
    }

    @Test
    void så_ska_elnätsintrångsinformation_vara_korrekt_för_luft() {
        // Given
        var avtalId = mockUUID(0);

        var elnatklassificering = ElnatKlassificeringDto.builder()
            .langdLuftledning(3.4)
            .langdMarkledning(0.)
            .build();

        when(mockElnatKlassificeringService.klassificera(eq(avtalId))).thenReturn(elnatklassificering);

        // When
        var bindings = avtalBindingsService.getElnatIntrangBindings(avtalId);

        // Then
        assertEquals(3, bindings.getLangdILuft());
        assertEquals(0, bindings.getLangdIMark());
        assertEquals(false, bindings.getMarkforlagdKanalisation());
        assertEquals(true, bindings.getStolplinje());
    }

    @Test
    void så_ska_elnätsintrångsinformation_vara_korrekt_för_mark() {
        // Given
        var avtalId = mockUUID(0);

        var elnatklassificering = ElnatKlassificeringDto.builder()
            .langdLuftledning(0.)
            .langdMarkledning(5.7)
            .build();

        when(mockElnatKlassificeringService.klassificera(eq(avtalId))).thenReturn(elnatklassificering);

        // When
        var bindings = avtalBindingsService.getElnatIntrangBindings(avtalId);

        // Then
        assertEquals(0, bindings.getLangdILuft());
        assertEquals(6, bindings.getLangdIMark());
        assertEquals(true, bindings.getMarkforlagdKanalisation());
        assertEquals(false, bindings.getStolplinje());
    }

    @Test
    void så_ska_ledningslittera_sättas_korrekt() {
        // Given
        var avtalId = mockUUID(0);
        var littera = "littera";

        when(mockElnatKlassificeringService.klassificera(eq(avtalId))).thenReturn(new ElnatKlassificeringDto());
        when(mockElnatVarderingsprotokollService.getWithAvtalId(eq(avtalId))).thenReturn(
            Optional.of(new ElnatVarderingsprotokollDto()
                .metadata(new ElnatVarderingsprotokollMetadataDto().ledning(littera)))
        );

        // When
        var bindings = avtalBindingsService.getElnatIntrangBindings(avtalId);

        // Then
        assertEquals(littera, bindings.getElIntrangBindings().getLedningslittera());
    }

    @Test
    void så_ska_fibernätsintrångsinformation_vara_korrekt_för_luft() {
        // Given
        var avtalId = mockUUID(0);

        var elnatKlassificering = FiberKlassificeringDto.builder()
            .langdLuftledning(3.4)
            .langdMarkledning(0.)
            .build();

        when(mockFiberKlassificeringService.klassificera(eq(avtalId))).thenReturn(elnatKlassificering);

        // When
        var bindings = avtalBindingsService.getFiberIntrangBindings(avtalId);

        // Then
        assertEquals(3, bindings.getLangdILuft());
        assertEquals(0, bindings.getLangdIMark());
        assertEquals(false, bindings.getMarkforlagdKanalisation());
        assertEquals(true, bindings.getStolplinje());
    }

    @Test
    void så_ska_fibernätsintrångsinformation_vara_korrekt_för_mark() {
        // Given
        var avtalId = mockUUID(0);

        var fiberklassificering = FiberKlassificeringDto.builder()
            .langdLuftledning(0.)
            .langdMarkledning(5.7)
            .build();

        when(mockFiberKlassificeringService.klassificera(eq(avtalId))).thenReturn(fiberklassificering);

        // When
        var bindings = avtalBindingsService.getFiberIntrangBindings(avtalId);

        // Then
        assertEquals(0, bindings.getLangdILuft());
        assertEquals(6, bindings.getLangdIMark());
        assertEquals(true, bindings.getMarkforlagdKanalisation());
        assertEquals(false, bindings.getStolplinje());
    }

    @Test
    void så_ska_fiberavtal_inte_ha_någon_transformatorstation() {
        // Given
        var avtalId = mockUUID(0);

        when(mockFiberKlassificeringService.klassificera(eq(avtalId))).thenReturn(new FiberKlassificeringDto());

        // When
        var bindings = avtalBindingsService.getFiberIntrangBindings(avtalId);

        // Then
        assertNoTransformatorstation(bindings.getElIntrangBindings());
    }

    private void assertNoTransformatorstation(ElIntrangBindings bindings) {
        assertEquals(false, bindings.getHasTransformatorstation());
        assertEquals("", bindings.getTransformatorstationNamn());
        assertEquals(0, bindings.getTransformatorstationBreddAMeter());
        assertEquals(0, bindings.getTransformatorstationBreddBMeter());
        assertEquals("0x0m", bindings.getTransformatorstationYta());
    }

    private ElnatPunktersattningDto punktersattningDto(String beskrivning, ElnatPunktersattningTypDto typ) {
        return new ElnatPunktersattningDto().beskrivning(beskrivning).typ(typ);
    }
}
