package se.metria.markkoll.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.avtal.AvtalIdAvtalsstatus;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.avtal.AvtalsjobbRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.vardering.elnat.ElnatVarderingsprotokollRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.avtal.AvtalDto;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.dokument.DokumentmallService;
import se.metria.markkoll.service.geometristatus.GeometristatusService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.projekt.ProjektService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.avtalsjobbProgress;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@SuppressWarnings("NonAsciiCharacters")
@MarkkollServiceTest
@DisplayName("Givet AvtalService")
public class AvtalServiceTest {

    AvtalsinstallningarRepository mockAvtalsinstallningarRepository;
    AclService mockAclService;
    AvtalRepository mockAvtalRepository;
    AvtalService avtalService;
    AvtalsjobbRepository mockAvtalsjobbRepository;
    AvtalUtskickZipService mockAvtalUtskickZipService;
    CachingService mockCachingService;
    Clock mockClock;
    FastighetRepository mockFastighetRepository;
    LoggService mockLoggService;
    ProjektRepository mockProjektRepository;
    DokumentmallService mockDokumentmallService;
    ElnatVarderingsprotokollService mockElnatVarderingsprotokollService;
    FiberProjektService mockFiberProjektService;
    FiberVarderingsprotokollService mockFiberVarderingsprotokollService;
    ProjektService mockProjektService;
    UtskicksnummerService mockUtskicksnummerService;
    GeometristatusService mockGeometristatusService;
    MarkagareService mockMarkagareService;
    ElnatVarderingsprotokollRepository mockElnatVarderingsprotokollRepository;

    @BeforeEach
    void before() {
        var date = LocalDate.of(2022, 8, 15);
        mockClock = Clock.fixed(date.atStartOfDay(ZoneId.systemDefault()).toInstant(),ZoneId.systemDefault());

        mockAvtalsinstallningarRepository = mock(AvtalsinstallningarRepository.class);
        mockAclService = mock(AclService.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockAvtalUtskickZipService = mock(AvtalUtskickZipService.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockAvtalsjobbRepository = mock(AvtalsjobbRepository.class);
        mockLoggService = mock(LoggService.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockDokumentmallService = mock(DokumentmallService.class);
        mockElnatVarderingsprotokollService = mock(ElnatVarderingsprotokollService.class);
        mockFiberProjektService = mock(FiberProjektService.class);
        mockFiberVarderingsprotokollService = mock(FiberVarderingsprotokollService.class);
        mockProjektService = mock(ProjektService.class);
        mockUtskicksnummerService = mock(UtskicksnummerService.class);
        mockCachingService = mock(CachingService.class);
        mockGeometristatusService = mock(GeometristatusService.class);
        mockMarkagareService = mock(MarkagareService.class);
        mockElnatVarderingsprotokollRepository = mock(ElnatVarderingsprotokollRepository.class);

        avtalService = new AvtalService(mockAvtalsinstallningarRepository, mockAclService, mockAvtalRepository, mockAvtalUtskickZipService, mockClock,
            mockFastighetRepository, mockAvtalsjobbRepository, mockCachingService, mockLoggService, mockProjektRepository,
            mockDokumentmallService, mockUtskicksnummerService, mockMarkagareService, mockGeometristatusService, mockElnatVarderingsprotokollRepository);
    }

    @Test
    void så_ska_det_gå_att_skapa_ett_avtal() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var projektEntity = new ProjektEntity();
        var fastighetEntity = new FastighetEntity();
        var avtalEntity = AvtalEntity.builder()
            .projekt(projektEntity)
            .fastighet(fastighetEntity)
            .build();

        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));
        when(mockFastighetRepository.findById(eq(fastighetId))).thenReturn(Optional.of(fastighetEntity));
        when(mockAvtalRepository.save(argThat(new AvtalEntityMatcher(avtalEntity)))).thenReturn(avtalEntity);

        // When
        var avtalIdActual = avtalService.create(projektId, fastighetId);

        // Then
        verify(mockProjektRepository).findById(eq(projektId));
        verify(mockFastighetRepository).findById(eq(fastighetId));
        verify(mockAvtalRepository).save(argThat(new AvtalEntityMatcher(avtalEntity)));

        assertEquals(avtalEntity.getId(), avtalIdActual);
    }

    @Test
    void så_ska_det_gå_att_skapa_avtalsjobb() {
        // Given
        var projektId = mockUUID(4);
        var idExpect = mockUUID(6);
        var filterAndTemplate = new FilterAndTemplateDto()
            .template(mockUUID(5))
            .filter(new FastighetsfilterDto());
        var avtal = Arrays.asList(
                new AvtalEntity(),
                new AvtalEntity()
        );

        when(mockAvtalRepository.avtalFiltered(any(), any())).thenReturn(avtal);
        when(mockAvtalRepository.findAllByProjektIdAndFastighetIdIn(any(), any())).thenReturn(avtal);
        when(mockAvtalsjobbRepository.saveAndFlush(any())).thenReturn(AvtalsjobbEntity.builder().id(idExpect).build());
        when(mockProjektRepository.getNamn(eq(projektId))).thenReturn("projekt");
        when(mockDokumentmallService.get(eq(mockUUID(5)))).thenReturn(new DokumentmallDto().namn("mall"));

        // When
        var idActual = avtalService.createAvtalsjobb(projektId, filterAndTemplate);

        // Then
        var entitySaved = AvtalsjobbEntity.builder()
            .status(AvtalsjobbStatusDto.NONE)
            .avtal(avtal)
            .generated(0)
            .total(avtal.size())
            .filnamn("projekt_mall_220815.zip")
            .build();

        Mockito.verify(mockAvtalsjobbRepository).saveAndFlush(eq(entitySaved));

        assertEquals(idExpect, idActual);
    }

    @Test
    void så_ska_det_gå_att_köra_ett_avtalsjobb() throws Exception {
        var avtalsjobbId = mockUUID(0);
        var avtalList = Arrays.asList(
            AvtalEntity.builder().id(mockUUID(1)).build(),
            AvtalEntity.builder().id(mockUUID(2)).build()
        );
        var dokumentmallId = mockUUID(3);
        var time = 10; //millisekunder

        when(mockAvtalsjobbRepository.getAvtal(eq(avtalsjobbId))).thenReturn(avtalList);
        when(mockAvtalUtskickZipService.getAvtalZip(any(), eq(dokumentmallId))).thenAnswer(
            (ans) -> new FileNameAwareByteArrayResource("avtalZip".getBytes(), ans.getArgument(0).toString())
        );

        // When
        avtalService.runAvtalsjobb(avtalsjobbId, dokumentmallId);

        // Then
        verify(mockAvtalsjobbRepository, timeout(time).times(1)).setAvtalsjobbStatus(eq(avtalsjobbId),
            eq(AvtalsjobbStatusDto.IN_PROGRESS));

        verify(mockAvtalsjobbRepository).getAvtal(eq(avtalsjobbId));
        for (var avtal: avtalList) {
            verify(mockAvtalUtskickZipService, timeout(time)).getAvtalZip(eq(avtal.getId()), eq(dokumentmallId));
        }

        verify(mockAvtalsjobbRepository, timeout(time).times(avtalList.size())).incrementGeneratedCounter(eq(avtalsjobbId));
        verify(mockAvtalsjobbRepository, timeout(time)).setPath(eq(avtalsjobbId), notNull());
        verify(mockLoggService).addAvtalHandelse(eq(avtalsjobbId));

        verify(mockAvtalsjobbRepository, timeout(time).times(1)).setAvtalsjobbStatus(eq(avtalsjobbId),
            eq(AvtalsjobbStatusDto.DONE));
    }

    @Test
    void så_ska_det_gå_att_hämta_progress_för_ett_avtalsjobb() {
        // Given
        var jobbId = mockUUID(0);
        var total = 14;
        var generated = 2;
        var status = AvtalsjobbStatusDto.IN_PROGRESS;

        var progress = avtalsjobbProgress(total, generated, status);
        var progressExpect = new AvtalsjobbProgressDto()
                .status(status)
                .generated(generated)
                .total(total);

        when(mockAvtalsjobbRepository.findAvtalsjobbProgressById(any())).thenReturn(Optional.of(progress));

        // When
        var progressActual = avtalService.getAvtalsjobbProgress(jobbId);

        // Then
        assertEquals(progressExpect, progressActual);
        verify(mockAvtalsjobbRepository).findAvtalsjobbProgressById(eq(jobbId));
    }

    @Test
    void så_ska_exception_kastas_om_man_hämtar_progress_för_ett_ogiltigt_avtalsjobb() {
        // Given
        var jobbId = mockUUID(0);

        when(mockAvtalsjobbRepository.findAvtalsjobbProgressById(any())).thenReturn(Optional.empty());

        // When / Then
        var exception = assertThrows(MarkkollException.class,
                () -> avtalService.getAvtalsjobbProgress(jobbId));
        assertEquals(MarkkollError.AVTAL_ERROR.getMessage(), exception.getMessage());
        verify(mockAvtalsjobbRepository).findAvtalsjobbProgressById(eq(jobbId));
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_färdigskapat_avtal() throws IOException {
        // Given
        var projektId = mockUUID(1);
        var avtalsjobbId = mockUUID(0);
        var path = "src/test/resources/testData/oneco-skanova-owner.zip";
        var avtalsjobb = AvtalsjobbEntity.builder()
                .status(AvtalsjobbStatusDto.DONE)
                .path(path)
                .build();
        var dataExpect = new ByteArrayResource(Files.readAllBytes(Paths.get(path)));

        when(mockAvtalsjobbRepository.findById(any())).thenReturn(Optional.of(avtalsjobb));
        when(mockProjektRepository.getOne(any())).thenReturn(ProjektEntity.builder().namn("").build());

        // When
        var avtal = avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId);

        // Then
        assertTrue(avtal.isPresent());
        assertEquals(dataExpect, avtal.get());
    }

    @Test
    void så_ska_tom_data_returneras_när_man_försöker_hämta_ett_avtal_för_ett_ostartat_avtalsjobb() {
        // Given
        var projektId = mockUUID(1);
        var avtalsjobbId = mockUUID(0);
        var avtalsjobb = AvtalsjobbEntity.builder()
                .status(AvtalsjobbStatusDto.NONE)
                .build();

        when(mockAvtalsjobbRepository.findById(any())).thenReturn(Optional.of(avtalsjobb));

        // When
        var avtal = avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId);

        // Then
        assertEquals(avtal, Optional.empty());
    }

    @Test
    void så_ska_tom_data_returneras_när_man_försöker_hämta_ett_ej_färdigskapat_avtal() {
        // Given
        var projektId = mockUUID(1);
        var avtalsjobbId = mockUUID(0);
        var avtalsjobb = AvtalsjobbEntity.builder()
                .status(AvtalsjobbStatusDto.IN_PROGRESS)
                .build();

        when(mockAvtalsjobbRepository.findById(any())).thenReturn(Optional.of(avtalsjobb));

        // When
        var avtal = avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId);

        // Then
        assertEquals(avtal, Optional.empty());
    }

    @Test
    void så_ska_exception_kastas_när_man_hämtar_ett_avtal_som_inte_kunnat_genereras() {
        // Given
        var avtalsjobbId = mockUUID(0);
        var projektId = mockUUID(1);
        var avtalsjobb = AvtalsjobbEntity.builder()
                .status(AvtalsjobbStatusDto.ERROR)
                .build();

        when(mockAvtalsjobbRepository.findById(any())).thenReturn(Optional.of(avtalsjobb));

        // When / Then
        var exception = assertThrows(MarkkollException.class,
                () -> avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId));
        assertEquals(exception.getMessage(), MarkkollError.AVTAL_ERROR.getMessage());
    }

    @Test
    void så_ska_exception_kastas_om_man_hämtar_avtal_för_ett_ogiltigt_avtalsjobb() {
        // Given
        var avtalsjobbId = mockUUID(0);
        var projektId = mockUUID(1);

        when(mockAvtalsjobbRepository.findById(any())).thenReturn(Optional.empty());

        // When / Then
        var exception = assertThrows(MarkkollException.class,
                () -> avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId));
        assertEquals(exception.getMessage(), MarkkollError.AVTAL_ERROR.getMessage());
    }

    @Test
    void så_ska_exception_kastas_om_man_hämtar_ett_avtal_med_ogiltig_sökväg() {
        // Given
        var avtalsjobbId = mockUUID(0);
        var projektId = mockUUID(1);
        var path = "en path som inte finns";
        var avtalsjobb = AvtalsjobbEntity.builder()
                .status(AvtalsjobbStatusDto.DONE)
                .path(path)
                .build();

        when(mockAvtalsjobbRepository.findById(any())).thenReturn(Optional.of(avtalsjobb));
        when(mockProjektRepository.getOne(any())).thenReturn(new ProjektEntity());

        // When / Then
        var exception = assertThrows(MarkkollException.class,
                () -> avtalService.getAvtalZipAvtalsjobb(projektId, avtalsjobbId));
        assertEquals(exception.getMessage(), MarkkollError.AVTAL_ERROR.getMessage());
    }

    @Test
    void så_ska_det_gå_att_sätta_att_ett_avtal_är_för_en_skogsfastighet() {
        // Given
        var avtalId = mockUUID(0);
        var skogsfastighet = true;

        when(mockAvtalRepository.setSkogsfastighet(any(), any())).thenReturn(1);

        // When
        avtalService.setSkogsfastighet(avtalId, skogsfastighet);

        // Then
        verify(mockAvtalRepository).setSkogsfastighet(eq(avtalId), eq(skogsfastighet));
    }

    @Test
    void så_ska_exception_kastas_om_man_sätter_skogsfastighetsstatus_för_ett_avtal_som_inte_finns() {
        // Given
        var avtalId = mockUUID(0);
        var skogsfastighet = true;

        when(mockAvtalRepository.setSkogsfastighet(any(), any())).thenReturn(0);

        // When / Then
        var exception = assertThrows(ResponseStatusException.class,
                () -> avtalService.setSkogsfastighet(avtalId, skogsfastighet));
        assertEquals(exception.getMessage(), HttpStatus.NOT_FOUND.toString());
    }

    @Test
    void så_ska_det_gå_att_ta_bort_ett_avtalsjobb() {
        // Given
        var avtalsjobbId = mockUUID(0);

        when(mockAvtalsjobbRepository.existsById(eq(avtalsjobbId))).thenReturn(true);

        // When
        avtalService.deleteAvtalsjobb(avtalsjobbId);

        // Then
        verify(mockAvtalsjobbRepository).deleteById(eq(avtalsjobbId));
    }

    @Test
    void så_ska_det_gå_att_spara_metadata() {
        // Given
        var avtalId = mockUUID(0);
        var metadataExpect = mock(AvtalMetadataDto.class);
        var avtal = AvtalEntity.builder().build();

        when(mockAvtalRepository.save(any())).thenReturn(avtal);
        when(mockAvtalRepository.getOne(any())).thenReturn(avtal);

        // When
        var actual = avtalService.setAvtalMetadata(avtalId, metadataExpect);

        // Then
        assertEquals(actual, metadataExpect);
    }

    @Test
    void så_ska_det_gå_att_erhålla_antalet_avtal_i_ett_projekt() {
        // Given
        var filter = new FastighetsfilterDto();
        var avtalList = Arrays.asList(new AvtalEntity(), new AvtalEntity(), new AvtalEntity());
        var projektId = mockUUID(0);

        when(mockAvtalRepository.avtalFiltered(eq(projektId), eq(filter))).thenReturn(avtalList);

        // When
        var countActual = avtalService.avtalCount(projektId, filter);

        // Then
        assertEquals(avtalList.size(), countActual.longValue());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_zipfil_med_ett_avtal() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var zipExpect = new ByteArrayResource("zip".getBytes());
        var avtalEntity = new AvtalEntity();
        var dokumentmallId = mockUUID(2);
        var kundId = "kundId";

        when(mockAvtalUtskickZipService.getAvtalZip(eq(projektId), eq(fastighetId), eq(dokumentmallId))).thenReturn(zipExpect);
        when(mockAvtalRepository.getAvtalByProjektIdAndFastighetId(eq(projektId), eq(fastighetId))).thenReturn(avtalEntity);
        when(mockAvtalsjobbRepository.saveAndFlush(any())).thenAnswer(returnsFirstArg());
        when(mockProjektRepository.getKundId(eq(projektId))).thenReturn(kundId);
        when(mockDokumentmallService.getSelected(eq(kundId), eq(DokumentTypDto.MARKUPPLATELSEAVTAL)))
            .thenReturn(new DokumentmallDto().id(dokumentmallId));

        // When
        var zipActual = avtalService.getAvtalZipFastighet(projektId, fastighetId);

        // Then
        assertEquals(zipExpect, zipActual);
        verify(mockProjektRepository).getKundId(eq(projektId));
        verify(mockDokumentmallService).getSelected(eq(kundId), eq(DokumentTypDto.MARKUPPLATELSEAVTAL));
        verify(mockAvtalsjobbRepository).saveAndFlush(any());
        verify(mockLoggService).addAvtalHandelse(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_det_gå_att_hämta_och_inkrementera_ett_utskicksnummer(boolean signerat) {
        // Given
        var avtalId = mockUUID(0);
        var kundId = "kundId";
        var utskicksnummerExpect = 11;
        var entity = AvtalEntity.builder().utskicksnummer(utskicksnummerExpect).build();

        when(mockAvtalRepository.getKundId(eq(avtalId))).thenReturn(kundId);
        when(mockAvtalRepository.findById(eq(avtalId))).thenReturn(Optional.of(entity));
        when(mockUtskicksnummerService.increment(eq(kundId))).thenReturn(utskicksnummerExpect);
        when(mockAvtalRepository.getAvtalsstatus(eq(avtalId)))
            .thenReturn(signerat ? AvtalsstatusDto.AVTAL_SIGNERAT : AvtalsstatusDto.EJ_BEHANDLAT);

        // When
        var utskicksnummerActual = avtalService.updateUtskicksnummer(avtalId);

        // Then
        if (!signerat) {
            assertEquals(utskicksnummerExpect, utskicksnummerActual);
            verify(mockUtskicksnummerService).increment(eq(kundId));
            assertEquals(utskicksnummerExpect, entity.getUtskicksnummer());
        }
        else {
            assertEquals(utskicksnummerExpect, utskicksnummerActual);
            verify(mockUtskicksnummerService, never()).increment(any());
            assertEquals(utskicksnummerExpect, entity.getUtskicksnummer());
        }
    }

    @Test
    void så_ska_det_gå_att_avbryta_ett_avtalsjobb() {
        // Given
        var avtalsjobbId = mockUUID(0);

        // When
        avtalService.cancelAvtalsjobb(avtalsjobbId);

        // Then
        verify(mockAvtalsjobbRepository).setAvtalsjobbStatus(eq(avtalsjobbId), eq(AvtalsjobbStatusDto.CANCELLED));
    }

    @Test
    void så_ska_det_gå_att_uppdatera_avtal() {
        // Given
        var avtalDto = avtalDto();
        var avtalEntity = avtalEntity();

        when(mockAvtalRepository.findById(eq(avtalDto.getId()))).thenReturn(Optional.of(avtalEntity));

        // When
        avtalService.updateAvtal(avtalDto);

        // Then
        verify(mockAvtalRepository).save(eq(avtalEntity));
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_avtal() {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        var avtalEntity = avtalEntity();
        var avtalDto = avtalDto();

        when(mockAvtalRepository.findByProjektIdAndFastighetId(eq(projektId), eq(fastighetId)))
            .thenReturn(Optional.of(avtalEntity));

        // When
        var avtalDtoActual = avtalService.getAvtal(projektId, fastighetId);

        // Then
        assertEquals(avtalDto, avtalDtoActual);
    }

    @Test
    void updateAvtalsstatus() {
        // Given
        var projektId = mockUUID(0);
        var fastighetIds = Arrays.asList(mockUUID(1));
        var avtalsstatus = AvtalsstatusDto.EJ_BEHANDLAT;
        var avtalIds = Arrays.asList(mockUUID(2));
        List<AvtalIdAvtalsstatus> previousAvtalsstatusar = Arrays.asList(
            new AvtalIdAvtalsstatusImpl(avtalIds.get(0), AvtalsstatusDto.AVTAL_JUSTERAS)
        );

        when(mockAvtalRepository.findByProjektIdAndFastighetIdIn(eq(projektId), eq(fastighetIds)))
            .thenReturn(previousAvtalsstatusar);

        // When
        avtalService.updateAvtalsstatus(projektId, fastighetIds, avtalsstatus);

        // Then
        verify(mockMarkagareService).updateAvtalsstatus(eq(avtalIds), eq(avtalsstatus));

        verify(mockGeometristatusService, never()).setGeometristatus(any(), any(), any());
    }

    @Test
    void updateAvtalsstatusFromEjBehandlat() {
        // Given
        var projektId = mockUUID(0);
        var fastighetIds = Arrays.asList(mockUUID(1));
        var avtalsstatus = AvtalsstatusDto.AVTAL_SIGNERAT;
        var avtalIds = Arrays.asList(mockUUID(2), mockUUID(3));
        List<AvtalIdAvtalsstatus> previousAvtalsstatusar = Arrays.asList(
            new AvtalIdAvtalsstatusImpl(avtalIds.get(0), AvtalsstatusDto.EJ_BEHANDLAT),
            new AvtalIdAvtalsstatusImpl(avtalIds.get(1), AvtalsstatusDto.AVTALSKONFLIKT)
        );

        when(mockAvtalRepository.findByProjektIdAndFastighetIdIn(eq(projektId), eq(fastighetIds)))
            .thenReturn(previousAvtalsstatusar);

        // When
        avtalService.updateAvtalsstatus(projektId, fastighetIds, avtalsstatus);

        // Then
        verify(mockMarkagareService).updateAvtalsstatus(eq(avtalIds), eq(avtalsstatus));

        verify(mockGeometristatusService).setGeometristatus(eq(projektId), eq(Arrays.asList(avtalIds.get(0))),
            eq(GeometristatusDto.OFORANDRAD));
    }

    private AvtalDto avtalDto() {
        var avtalDto = new AvtalDto();

        avtalDto.setId(mockUUID(2));
        avtalDto.setEgetTillvaratagande(200);
        avtalDto.setSkogsfastighet(true);
        avtalDto.setTillvaratagandeTyp(TillvaratagandeTypDto.EGET_TILLVARATAGANDE);
        avtalDto.setErsattning(100);

        return avtalDto;
    }

    private AvtalEntity avtalEntity() {
        var avtalEntity = new AvtalEntity();

        avtalEntity.setId(mockUUID(2));
        avtalEntity.setEgetTillvaratagande(200);
        avtalEntity.setSkogsfastighet(true);
        avtalEntity.setTillvaratagandeTyp(TillvaratagandeTypDto.EGET_TILLVARATAGANDE);
        avtalEntity.setErsattning(100);

        return avtalEntity;
    }

    @AllArgsConstructor
    private class AvtalEntityMatcher implements ArgumentMatcher<AvtalEntity> {
        private AvtalEntity left;

        @Override
        public boolean matches(AvtalEntity right) {
            return left.getProjekt().equals(right.getProjekt()) &&
                   right.getFastighet().equals(right.getFastighet());
        }
    }

    @Data
    @AllArgsConstructor
    class AvtalIdAvtalsstatusImpl implements AvtalIdAvtalsstatus {
        private UUID id;
        private AvtalsstatusDto avtalsstatus;
    }
}
