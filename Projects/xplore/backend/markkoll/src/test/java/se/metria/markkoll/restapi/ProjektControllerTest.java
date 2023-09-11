package se.metria.markkoll.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.security.MkPermission;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;
import se.metria.markkoll.service.AvtalUtskickZipService;
import se.metria.markkoll.service.AvtalsinstallningarService;
import se.metria.markkoll.service.BeredareService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.dokument.DokumentmallService;
import se.metria.markkoll.service.dokument.forteckninggenerator.ForteckningGeneratorService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.intrang.IntrangImportService;
import se.metria.markkoll.service.intrang.IntrangService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.markagare.MarkagareService;
import se.metria.markkoll.service.projekt.*;
import se.metria.markkoll.testdata.TestData;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.ObjectMapperUtil;
import se.metria.xplore.maputils.GeometryUtil;

import javax.persistence.EntityExistsException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static se.metria.markkoll.testdata.TestData.*;
import static se.metria.markkoll.util.ResponseBodyMatchers.responseBody;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Givet ProjektController")
@MarkkollMVCTest(controllers = ProjektController.class)
public class ProjektControllerTest {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BeredareService beredareService;

    @SuppressWarnings("unused") // Behövs för CDI
    @MockBean
    private GeometristatusRepository geometristatusRepository;

    @MockBean
    private ProjektService projektService;

    @MockBean
    private FiberProjektService fiberProjektService;

    @SuppressWarnings("unused") // Behövs för CDI
    @MockBean
    private ElnatProjektService elnatProjektService;

    @MockBean
    private FastighetService fastighetService;

    @SuppressWarnings("unused") // Behövs för CDI
    @MockBean
    private IntrangService intrangService;

    @SuppressWarnings("unused") // Behövs för CDI
    @MockBean
    private AvtalRepository avtalRepository;

    @MockBean
    private DokumentGeneratorService dokumentGeneratorService;

    @MockBean
    private DokumentmallService dokumentmallService;

    @MockBean
    private MarkagareService markagareService;

    @SuppressWarnings("unused")
    @MockBean
    private AvtalUtskickZipService avtalUtskickZipService;

    @MockBean
    private LoggService loggService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjektImportService elnatProjektImportService;

    @MockBean
    private ProjektImportServiceFactory projektImportServiceFactory;

    @MockBean
    private IntrangImportService intrangImportService;

    @MockBean
    private AvtalsinstallningarService avtalsinstallningarService;

    @MockBean
    private AvtalService avtalService;

    @MockBean
    private ForteckningGeneratorService forteckningGeneratorService;

    final FastighetsfilterDto filter = new FastighetsfilterDto()
            .avtalsstatus(AvtalsstatusDto.AVTAL_JUSTERAS)
            .intrangLength(7.5)
            .excludeWithMarkagare(true)
            .fastighetsbeteckning("fastighet")
            .showSenastUppdaterade(true);

    final String sortProperty = "namn";
    final Sort.Direction sortDirection = Sort.Direction.ASC;
    final Integer pageNum = 0;
    final Integer size = 3;
    final static List<String> kunder = Arrays.asList("kund 1", "kund 2");

    @Test
    @Disabled("Vi har ändrat hur behörigheter fungerar. Testet är inaktuellt.")
    void så_ska_man_inte_kunna_skapa_projekt_om_roll_saknas() throws Exception {
        // Given
        final var projektInfo = projektInfoMultipart(mockProjektInfoDto());
        final var importFile = new MockMultipartFile("file", "test.zip", "application/octet-stream", new byte[0]);

        // When
        final var result = mvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/projekt")
                        .file(importFile)
                        .file(projektInfo)
                        .with(csrf())
        );

        // Then
        result.andExpect(status().isUnauthorized());
    }

// TODO: Porta till Fiber/El-servicearna
//    @Test
//    @WithMockUserMarkhandlaggare
//    void så_ska_markhandläggare_kunna_skapa_fiberprojekt() throws Exception {
//        // Given
//        final var fiberProjektDto = mockFiberProjektDto();
//        final var fiberProjektPart = fiberProjektMultipart(mockFiberProjektDto());
//        final var importFile = new MockMultipartFile("file", "shapefile.zip", "application/octet-stream", new byte[0]);
//
//        // When
//        when(projektService.createFiberProjekt(any())).thenReturn(fiberProjektDto);
//
//        final var result = mvc.perform(
//                MockMvcRequestBuilders
//                        .multipart("/api/projekt/fiber")
//                        .file(fiberProjektPart)
//                        .file(importFile)
//                        .with(csrf())
//        );
//
//        // Then
//        result
//                .andExpect(status().isOk())
//                .andExpect(responseBody().containsObjectAsJson(fiberProjektDto, fiberProjektDto.getClass()));
//        fiberProjektDto.getProjektInfo().setStartDatum(null);
//        verify(projektService, times(1)).createFiberProjekt(eq(fiberProjektDto));
//    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_fiberprojekt_utan_namn_vara_otillåtna() throws Exception {
        // Given
        final var fiberProjektDto = mockFiberProjektDto();
        fiberProjektDto.getProjektInfo().setNamn(null);
        final var fiberProjektPart = fiberProjektMultipart(fiberProjektDto);
        var kundId = "kundId";
        final var importFile = new MockMultipartFile("file", "shapefile.zip", "application/octet-stream", new byte[0]);

        // When
        final var result = mvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/kund/{kundId}/projekt/fiber", kundId)
                        .file(importFile)
                        .file(fiberProjektPart)
                        .with(csrf())
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_fiberprojekt_utan_projekttyp_vara_otillåtna() throws Exception {
        // Given
        final var fiberProjektDto = mockFiberProjektDto();
        fiberProjektDto.getProjektInfo().setProjektTyp(null);
        final var fiberProjektPart = fiberProjektMultipart(fiberProjektDto);
        final var importFile = new MockMultipartFile("file", "shapefile.zip", "application/octet-stream", new byte[0]);
        var kundId = "kundId";

        // When
        final var result = mvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/kund/{kundId}/projekt/fiber", kundId)
                        .file(importFile)
                        .file(fiberProjektPart)
                        .with(csrf())
        );

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_en_sida_med_projekt() throws Exception {
        // Given
        ProjektPageDto pageExpected = mockProjektPage();

        when(projektService.projektPage(anyInt(), anyInt(), anyString(), any(), any())).thenReturn(pageExpected);
        when(userService.getCurrentUser()).thenReturn(new MarkkollUserDto().kundId(kunder.get(0)));

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/page")
                .queryParam("page", pageNum.toString())
                .queryParam("size", size.toString())
                .with(csrf())
        );

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(pageExpected, pageExpected.getClass()));

        verify(projektService, times(1))
                .projektPage(eq(pageNum), eq(size), eq(sortProperty), eq(sortDirection), eq(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asc", "desc", "ASC", "DESC", "aSc", "dEsC"})
    @WithMockUserMarkhandlaggare
    void så_ska_det_vara_möjligt_att_välja_sorteringsordning_när_man_hämtar_en_sida_med_projekt(String dir)
            throws Exception
    {
        // Given
        ProjektPageDto pageExpected = mockProjektPage();

        when(projektService.projektPage(anyInt(), anyInt(), anyString(), any(), any())).thenReturn(pageExpected);
        when(userService.getCurrentUser()).thenReturn(new MarkkollUserDto().kundId(kunder.get(0)));

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/page")
                .queryParam("page", pageNum.toString())
                .queryParam("size", size.toString())
                .queryParam("sort-direction", dir)
                .with(csrf())
        );

        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(pageExpected, ProjektPageDto.class));

        verify(projektService, times(1)).projektPage(
                eq(pageNum), eq(size), eq(sortProperty), eq(Sort.Direction.valueOf(dir.toUpperCase())),
                eq(null));
        }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_markhandläggare_kunna_hämta_fiberprojekt() throws Exception {
        // Given
        final var fiberProjektDto = TestData.mockFiberProjektDto();
        var projektId = mockUUID(0);
        when(fiberProjektService.getProjektDto(any())).thenReturn(Optional.of(fiberProjektDto));

        // When
        final var result = mvc.perform(
                MockMvcRequestBuilders
                        .get("/api/projekt/fiber/" + projektId)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())
        );

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(fiberProjektDto, FiberProjektDto.class));
        verify(fiberProjektService, times(1)).getProjektDto(eq(projektId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_fel_kastas_för_ogiltig_sorteringsordning_vid_hämtning_av_sida() throws Exception {
        // Given
        var orderBy = "ascdesc";

        ProjektPageDto pageExpected = mockProjektPage();

        when(projektService.projektPage(anyInt(), anyInt(), anyString(), any(), any())).thenReturn(pageExpected);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/page")
                .queryParam("page", "0")
                .queryParam("size", "3")
                .queryParam("sort-direction", orderBy)
                .with(csrf())
        );

        // Then
        result.andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_not_found_returneras_om_fiberprojekt_inte_hittas() throws Exception {
        // Given
        final var fiberProjektDto = TestData.mockFiberProjektDto();

        when(fiberProjektService.getProjektDto(any())).thenReturn(Optional.empty());

        // When
        final var result = mvc.perform(
            MockMvcRequestBuilders
                .get("/api/projekt/fiber/" + mockUUID(0))
                .content(mapper.writeValueAsString(fiberProjektDto))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_en_lista_med_fastigheter_för_ett_projekt() throws Exception {
        // Given
        var projektId = mockUUID(2);
        var fastigheter = Arrays.asList(
                new FastighetDto().id(mockUUID(0)), new FastighetDto().id(mockUUID(1)));

        when(fastighetService.getFastigheter(any(), any())).thenReturn(fastigheter);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/" + projektId + "/fastighet/")
                .queryParam("page", pageNum.toString())
                .queryParam("size", size.toString())
                .queryParam("fastighetsbeteckning", filter.getFastighetsbeteckning())
                .queryParam("avtalsstatus", filter.getAvtalsstatus().toString())
                .queryParam("intrangLength", filter.getIntrangLength().toString())
                .queryParam("excludeWithMarkagare", filter.getExcludeWithMarkagare().toString())
                .queryParam("showSenastUppdaterade", filter.getShowSenastUppdaterade().toString())
                .with(csrf()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsListAsJson(fastigheter, FastighetDto.class));

        verify(fastighetService).getFastigheter(eq(projektId), eq(filter));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_en_sida_med_fastigheter_för_ett_projekt() throws Exception {
        // Given
        var mockPage = mockFastighetPage();
        var projektId = mockUUID(0);

        when(fastighetService.fastighetPage(any(), anyInt(), anyInt(), any())).thenReturn(mockPage);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/" + projektId + "/fastighet/page")
                .queryParam("page", pageNum.toString())
                .queryParam("size", size.toString())
                .queryParam("fastighetsbeteckning", filter.getFastighetsbeteckning())
                .queryParam("avtalsstatus", filter.getAvtalsstatus().toString())
                .queryParam("intrangLength", filter.getIntrangLength().toString())
                .queryParam("excludeWithMarkagare", filter.getExcludeWithMarkagare().toString())
                .queryParam("showSenastUppdaterade", filter.getShowSenastUppdaterade().toString())
                .with(csrf()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockPage, mockPage.getClass()));

        verify(fastighetService).fastighetPage(eq(projektId), eq(pageNum), eq(size), eq(filter));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_en_sida_med_samfälligheter_för_ett_projekt() throws Exception {
        // Given
        var mockPage = mockFastighetPage();
        var projektId = mockUUID(0);

        when(fastighetService.samfallighetPage(any(), anyInt(), anyInt(), any())).thenReturn(mockPage);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .get("/api/projekt/" + projektId + "/samf/page")
                .queryParam("page", pageNum.toString())
                .queryParam("size", size.toString())
                .queryParam("fastighetsbeteckning", filter.getFastighetsbeteckning())
                .queryParam("avtalsstatus", filter.getAvtalsstatus().toString())
                .queryParam("intrangLength", filter.getIntrangLength().toString())
                .queryParam("excludeWithMarkagare", filter.getExcludeWithMarkagare().toString())
                .queryParam("showSenastUppdaterade", filter.getShowSenastUppdaterade().toString())
                .with(csrf()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(mockPage, mockPage.getClass()));

        verify(fastighetService).samfallighetPage(eq(projektId), eq(pageNum), eq(size), eq(filter));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_skapa_ett_nytt_avtalsjobb() throws Exception {
        // Given
        var projektId = mockUUID(1);
        var jobbId = mockUUID(0);
        var filterAndTemplate = new FilterAndTemplateDto();

        when(projektService.createAvtalJob(any(), any())).thenReturn(jobbId);

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .post("/api/projekt/" + projektId + "/avtalsjobb")
                .content(mapper.writeValueAsString(filterAndTemplate))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk())
                .andExpect(content().string(jobbId.toString()));

        verify(projektService).createAvtalJob(eq(projektId), eq(filterAndTemplate));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_ta_bort_en_version() throws Exception {
        var projektId = UUID.randomUUID();
        var versionId = UUID.randomUUID();

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/projekt/" + projektId + "/version/" + versionId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(projektService).deleteVersion(eq(versionId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_återställa_en_version_av_fiberprojekt() throws Exception {
        // Given
        var projektInfoDto = TestData.mockProjektDto();
        var projektId = projektInfoDto.getId();
        var versionId = mockUUID(1);

        when(projektService.getProjektForProjekt(eq(projektId))).thenReturn(Optional.of(projektInfoDto));

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .put("/api/projekt/" + projektId + "/version/" + versionId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(projektService).restoreVersion(eq(versionId));
        verify(fiberProjektService, times(0)).updateVarderingsprotokoll(any(), any(LocalDateTime.class));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_återställa_en_version_elnätsprojekt() throws Exception {
        // Given
        var projektInfoDto = TestData.mockProjektDto();
        projektInfoDto.setProjektTyp(ProjektTypDto.LOKALNAT);
        var projektId = projektInfoDto.getId();
        var versionId = mockUUID(1);

        when(projektService.getProjektForProjekt(eq(projektId))).thenReturn(Optional.of(projektInfoDto));

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .put("/api/projekt/" + projektId + "/version/" + versionId)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(projektService).restoreVersion(eq(versionId));
        verify(elnatProjektService).updateVarderingsprotokoll(eq(projektId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandlaggare_kunna_sätta_avtalsstatus_för_alla_ägare_för_en_fastighet() throws Exception {
        // Given
        var projektId = mockUUID(1);
        var fastighetId = mockUUID(0);
        var status = AvtalsstatusDto.AVTAL_JUSTERAS;

        when(projektService.getCurrentVersionId(any())).thenReturn(mock(UUID.class));

        // When
        final var result = mvc.perform(MockMvcRequestBuilders
                .put("/api/projekt/"+projektId+"/fastighet/"+fastighetId+"/status")
                .queryParam("avtalsstatus", status.name())
                .with(csrf()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(status, status.getClass()));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_importera_ägare_från_en_lista_med_avtal() throws Exception {
        // Given
        var projektId = mockUUID(4);
        var fastighetIds = Arrays.asList(mockUUID(1), mockUUID(2), mockUUID(3));

        var kund = kunder.get(0);

        when(userService.getCurrentUser()).thenReturn(new MarkkollUserDto().kundId(kund));

        // When
        var result = mvc.perform(MockMvcRequestBuilders
                .post("/api/projekt/"+projektId+"/agare/import")
                .content(mapper.writeValueAsString(new FastighetIdsDto().ids(fastighetIds)))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(markagareService).importAgare(eq(projektId), eq(fastighetIds), eq(kund));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_en_markhandläggare_kunna_lägga_till_nya_markägare() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);
        var markagare = mockAgareInfo();
        var agareExpect = agareExpect(markagare);

        when(markagareService.addAgare(any(), any(), any())).thenReturn(agareExpect);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
                .post("/api/projekt/"+projektId+"/fastighet/"+fastighetId+"/agare")
                .content(mapper.writeValueAsString(markagare))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then
        result
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(agareExpect, MarkagareDto.class));

        verify(markagareService).addAgare(eq(projektId), eq(fastighetId), eq(markagare));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_en_sida_med_projektloggar() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var page = new ProjektLoggPageDto().number(1).totalPages(2).totalElements(3);
        Integer pageNum = 0;
        Integer size = 1;
        var filter = Arrays.asList(ProjektLoggFilterDto.OVRIGA_DOKUMENT, ProjektLoggFilterDto.SKAPAT_AV_MIG);
        var sortDirection = Sort.Direction.ASC;

        when(loggService.getProjektLoggPage(eq(projektId), eq(pageNum), eq(size), eq(filter), eq(sortDirection)))
            .thenReturn(page);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/"+projektId+"/logg/page")
            .queryParam("page", pageNum.toString())
            .queryParam("size", size.toString())
            .queryParam("filter", filter.get(0).toString(), filter.get(1).toString())
            .queryParam("sort-direction", sortDirection.toString().toLowerCase())
            .with(csrf()));

        // Then
        result
            .andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(page, page.getClass()));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_göra_haglof_export_för_ett_projekt() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var filter = new FastighetsfilterDto();
        var data = "xlsx";
        var filnamn = "fil";
        var xlsx = new FileNameAwareByteArrayResource(data.getBytes(), filnamn);

        when(dokumentGeneratorService.getVarderingSkogsmark(eq(projektId), eq(filter))).thenReturn(xlsx);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/{projektId}/haglof", projektId)
            .queryParams(ObjectMapperUtil.toQueryParams(mapper, filter))
            .with(csrf()));

        // Then
        result
            .andExpect(status().isOk())
            .andExpect(content().string(data))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM.toString()))
            .andExpect(header().string("Content-Disposition", "inline;filename=" + filnamn));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_importera_en_registerenhet_till_ett_projekt() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        when(projektService.getProjektTyp(eq(projektId))).thenReturn(ProjektTypDto.LOKALNAT);
        when(projektImportServiceFactory.getProjektImportService(eq(projektId))).thenReturn(elnatProjektImportService);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/projekt/{projektId}/fastighet", projektId)
            .content(mapper.writeValueAsString(fastighetId))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(elnatProjektImportService).importRegisterenhet(eq(projektId), eq(fastighetId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void ska_returnera_409_om_fastigheten_redar_existerar_i_projektet() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        when(projektImportServiceFactory.getProjektImportService(eq(projektId))).thenReturn(elnatProjektImportService);
        doThrow(new EntityExistsException()).when(elnatProjektImportService).importRegisterenhet(eq(projektId), eq(fastighetId));

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/projekt/{projektId}/fastighet", projektId)
            .content(mapper.writeValueAsString(fastighetId))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().is(HttpStatus.CONFLICT.value()));

        verify(elnatProjektImportService).importRegisterenhet(eq(projektId), eq(fastighetId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void ska_returnera_400_om_ingen_fastighet_med_idt_existerar() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var fastighetId = mockUUID(1);

        when(projektImportServiceFactory.getProjektImportService(eq(projektId))).thenReturn(elnatProjektImportService);
        doThrow(new IllegalArgumentException()).when(elnatProjektImportService).importRegisterenhet(eq(projektId), eq(fastighetId));

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/projekt/{projektId}/fastighet", projektId)
            .content(mapper.writeValueAsString(fastighetId))
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // Then
        result.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

        verify(elnatProjektImportService).importRegisterenhet(eq(projektId), eq(fastighetId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_hämta_en_lista_med_användare_i_ett_projekt() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var users = Arrays.asList(new MarkkollUserDto(), new MarkkollUserDto());

        when(userService.getProjektUsers(eq(projektId))).thenReturn(users);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/{projektId}/users", projektId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(users)));
    }

    @Test
    @WithMockUser
    void så_ska_det_gå_att_hämta_användare_som_får_läsa_projekt() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var users = Arrays.asList(new MarkkollUserDto(), new MarkkollUserDto());

        when(userService.getUsersWithPermission(eq(projektId), eq(ProjektEntity.class), eq(MkPermission.READ)))
            .thenReturn(users);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/{projektId}/users/read", projektId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(content().string(mapper.writeValueAsString(users)));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_importera_nya_intrång() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var intrang = Arrays.asList(new ProjektIntrangDto()
            .avtalstyp(AvtalstypDto.SERVIS)
            .geom(new GeoJsonWriter().write(GeometryUtil.createPoint(1., 2.)))
            .type(IntrangstypDto.BRUNN)
            .subtype(IntrangsSubtypDto.LUFTLEDNING)
            .status(IntrangsStatusDto.BEVARAS));
        var version = new VersionDto().filnamn("filnamn");
        var fastighetIds = Arrays.asList(mockUUID(1));

        when(intrangImportService.importIntrang(eq(projektId), eq(intrang))).thenReturn(version);
        when(fastighetService.fastigheterFromVersion(eq(version))).thenReturn(fastighetIds);
        when(projektImportServiceFactory.getProjektImportService(eq(projektId))).thenReturn(elnatProjektImportService);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/projekt/{projektId}/version/intrang", projektId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(intrang))
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(version, VersionDto.class));

        verify(intrangImportService).importIntrang(eq(projektId), eq(intrang));
        verify(elnatProjektImportService).updateProjektVersion(eq(projektId), eq(version.getId()),
            eq(fastighetIds));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void getAvtalsinstallningar() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var installningar = new AvtalsinstallningarDto()
            .berakningAbel07(BerakningAbel07Dto.ABEL07_GRUNDERSATTNING)
            .berakningRev(BerakningRevDto.ENBART_REV);

        when(avtalsinstallningarService.getAvtalsinstallningar(eq(projektId))).thenReturn(installningar);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .get("/api/projekt/{projektId}/avtal/installningar", projektId)
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(responseBody().containsObjectAsJson(installningar, AvtalsinstallningarDto.class));

        verify(avtalsinstallningarService).getAvtalsinstallningar(eq(projektId));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void updateAvtalsinstallningar() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var installningar = new AvtalsinstallningarDto()
            .berakningAbel07(BerakningAbel07Dto.ABEL07_GRUNDERSATTNING)
            .berakningRev(BerakningRevDto.ENBART_REV)
            .mappstrategi(AvtalMappstrategiDto.FLAT);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .put("/api/projekt/{projektId}/avtal/installningar", projektId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(installningar))
            .with(csrf()));

        // Then
        result.andExpect(status().isOk());

        verify(avtalsinstallningarService).updateAvtalsinstallningar(eq(projektId), eq(installningar));
    }

    @Test
    @WithMockUserMarkhandlaggare
    void generateForteckning() throws Exception {
        // Given
        var projektId = mockUUID(0);
        var filterAndTemplate = new FilterAndTemplateDto()
            .template(mockUUID(1))
            .filter(new FastighetsfilterDto());
        var filnamn = "filnamn";
        var forteckning = new FileNameAwareByteArrayResource(ByteBuffer.allocate(4).putInt(0xC0FFEE).array(), filnamn);

        when(forteckningGeneratorService.generate(any(), any())).thenReturn(forteckning);

        // When
        var result = mvc.perform(MockMvcRequestBuilders
            .post("/api/projekt/{projektId}/forteckning", projektId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(filterAndTemplate))
            .with(csrf()));

        // Then
        result.andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().bytes(forteckning.getByteArray()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_OCTET_STREAM.toString()))
            .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "inline;filename=" + filnamn));

        verify(forteckningGeneratorService).generate(eq(projektId), eq(filterAndTemplate));
    }

    private MockMultipartFile projektInfoMultipart(ProjektInfoDto projektInfo) throws Exception {
        // ObjectMapper verkar inte tycka om datum
        projektInfo.setStartDatum(null);

        return new MockMultipartFile("projektInfo", "projektInfo.dat", "application/json",
                mapper.writeValueAsString(projektInfo).getBytes());
    }

    private MockMultipartFile fiberProjektMultipart(FiberProjektDto fiberProjekt) throws Exception {
        // ObjectMapper verkar inte tycka om datum
        fiberProjekt.getProjektInfo().setStartDatum(null);
        return new MockMultipartFile("fiberProjekt", "fiberProjekt.dat", "application/json",
                mapper.writeValueAsString(fiberProjekt).getBytes());
    }
}
