package se.metria.matdatabas.restapi.importctrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.matdatabas.security.mock.MockWebSecurityConfig;
import se.metria.matdatabas.security.mock.WithMockUserMatrapportor;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.kallsystem.KallsystemService;
import se.metria.matdatabas.service.kallsystem.StandardKallsystem;
import se.metria.matdatabas.service.kallsystem.dto.Kallsystem;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportError;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportMatning;
import se.metria.matdatabas.service.matning.dto.csvImport.ImportFormat;
import se.metria.matdatabas.service.matningstyp.MatningstypService;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;
import se.metria.matdatabas.service.matobjekt.MatobjektService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = ImportController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MockWebSecurityConfig.class})
)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class ImportControllerIT {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MatobjektService matobjektService;

    @MockBean
    private MatningstypService matningstypService;

    @MockBean
    private KallsystemService kallsystemService;

    @MockBean
    private MatningService matningService;

    @MockBean
    private DefinitionMatningstypService definitionMatningstypService;

    @Autowired
    private ObjectMapper objectMapper;

    private final ImportMatning importMatning = ImportMatning.builder()
            .avlastDatum("idag")
            .avlastVarde("42,3")
            .enhetAvlast("km")
            .felkod("Ok")
            .inomDetektionsomrade(">")
            .instrument("mandolin")
            .kommentar("qwerty")
            .matningstyp("Nivå")
            .matningstypId(1)
            .matobjekt("ABC123")
            .matobjektId(2)
            .importFel(List.of(ImportError.MATOBJEKT_MISSING))
            .build();

    private final Kallsystem createKallsystem(String kallsystemNamn) {
        return Kallsystem
                .builder()
                .namn(kallsystemNamn)
                .beskrivning("beskrivning")
                .defaultGodkand(false)
                .manuellImport(true)
                .tips("tips")
                .build();
    }

    @Test
    @WithMockUserMatrapportor
    void en_mätrapportör_ska_kunna_läsa_in_en_CSV_fil_standard_med_mätningar() throws Exception {
        final var kallsystemNamn = StandardKallsystem.RH2000_GRANSKAT.getNamn();

        var kallsystem = createKallsystem(kallsystemNamn);

        when(matobjektService.parseCsvImport(any(), eq(ImportFormat.STANDARD), eq(kallsystem))).thenReturn(List.of(importMatning));
        when(kallsystemService.findById(kallsystemNamn)).thenReturn(Optional.of(kallsystem));
        when(definitionMatningstypService.findByNamn(any())).thenReturn(new DefinitionMatningstyp());

        MockMultipartFile csvFile = new MockMultipartFile(
                "file",
                "standard.csv",
                "text/csv",
                new byte[] {1,2,3});
        mvc.perform(multipart("/api/import/matningar/parseimport?format=STANDARD").file(csvFile)
                .contentType("multipart/form-data")
                .param("kallsystem", kallsystemNamn)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUserMatrapportor
    void en_mätrapportör_ska_kunna_läsa_in_en_CSV_fil_instrument_med_mätningar() throws Exception {
        final var kallsystemNamn = StandardKallsystem.RH2000_GRANSKAT.getNamn();

        var kallsystem = createKallsystem(kallsystemNamn);

        when(matobjektService.parseCsvImport(any(), eq(ImportFormat.INSTRUMENT), eq(kallsystem))).thenReturn(List.of(importMatning));
        when(kallsystemService.findById(kallsystemNamn)).thenReturn(Optional.of(kallsystem));
        when(definitionMatningstypService.findByNamn(any())).thenReturn(new DefinitionMatningstyp());

        MockMultipartFile csvFile = new MockMultipartFile(
                "file",
                "instrument.csv",
                "text/csv",
                new byte[] {1,2,3});
        mvc.perform(multipart("/api/import/matningar/parseimport?format=INSTRUMENT")
                .file(csvFile)
                .param("kallsystem", kallsystemNamn)
                .contentType("multipart/form-data")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUserMatrapportor
    void en_mätrapportör_ska_kunna_läsa_in_en_CSV_fil_med_tidpunkt_i_sekunder() throws Exception {
        final var kallsystemNamn = StandardKallsystem.RH2000_GRANSKAT.getNamn();

        var kallsystem = createKallsystem(kallsystemNamn);

        when(matobjektService.parseCsvImport(any(), eq(ImportFormat.STANDARD), eq(kallsystem))).thenReturn(List.of(importMatning));
        when(kallsystemService.findById(kallsystemNamn)).thenReturn(Optional.of(kallsystem));
        when(definitionMatningstypService.findByNamn(any())).thenReturn(new DefinitionMatningstyp());

        MockMultipartFile csvFile = new MockMultipartFile(
                "file",
                "standard_with_seconds.csv",
                "text/csv",
                new byte[] {1,2,3});
        mvc.perform(multipart("/api/import/matningar/parseimport?format=STANDARD").file(csvFile)
                        .contentType("multipart/form-data")
                        .param("kallsystem", kallsystemNamn)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUserMatrapportor
    void när_en_felaktig_fil_läses_in_skall_400_returneras() throws Exception {
        when(matobjektService.parseCsvImport(any(), any(), any())).thenThrow(IOException.class);

        MockMultipartFile csvFile = new MockMultipartFile(
                "file",
                "bad.csv",
                "text/csv",
                new byte[] {1,2,3});
        mvc.perform(multipart("/api/import/matningar/parseimport?format=STANDARD").file(csvFile)
                .contentType("multipart/form-data")
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUserMatrapportor
    void en_mätrapportör_ska_kunna_spara_en_import() throws Exception {
        ImportMatning importMatning1 = ImportMatning.builder()
                .avlastDatum("2020-01-02 12:00")
                .avlastVarde("42,3")
                .enhetAvlast("km")
                .felkod("Ok")
                .inomDetektionsomrade(">")
                .instrument("mandolin")
                .kommentar("qwerty")
                .matningstyp("Nivå")
                .matningstypId(10)
                .matobjekt("ABC123")
                .matobjektId(2)
                .build();

        ImportMatning importMatning2 = ImportMatning.builder()
                .avlastDatum("2020-01-02 12:00")
                .avlastVarde("42,3")
                .enhetAvlast("km")
                .felkod("Ok")
                .inomDetektionsomrade(">")
                .instrument("mandolin")
                .kommentar("qwerty")
                .matningstyp("Nivå2")
                .matningstypId(11)
                .matobjekt("ABC123")
                .matobjektId(2)
                .build();


        ImportMatning matningar[] = {importMatning1, importMatning2};

        when(matobjektService.getMatobjektIdByNamn("ABC123")).thenReturn(Optional.of(1));
        when(matningstypService.getMatningstyperForMatobjekt(1))
                .thenReturn(List.of(
                        Matningstyp.builder().id(10).typ("Nivå").build(),
                        Matningstyp.builder().id(11).typ("Nivå2").build()
                        ));

        when(kallsystemService.findById("Annat (ej granskade mätningar)")).thenReturn(
                Optional.of(new Kallsystem("Annat (ej granskade mätningar)",
                        "En beskrivning",
                        false,true, "tips")));

        mvc.perform(post("/api/import/matningar/import?matrapportor=Metria AB&kallsystem=Annat (ej granskade mätningar)")
                .content(objectMapper.writeValueAsString(matningar))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(matningService, Mockito.times(2)).create(any(), any(),eq(true), eq("Annat (ej granskade mätningar)"));
    }

    @Test
    @WithMockUserMatrapportor
    void en_mätrapportör_ska_kunna_spara_en_import_med_tidpunkt_i_sekunder() throws Exception {
        ImportMatning importMatning1 = ImportMatning.builder()
                .avlastDatum("2020-01-02 12:00:00")
                .avlastVarde("42,3")
                .enhetAvlast("km")
                .felkod("Ok")
                .inomDetektionsomrade(">")
                .instrument("mandolin")
                .kommentar("qwerty")
                .matningstyp("Nivå")
                .matningstypId(10)
                .matobjekt("ABC123")
                .matobjektId(2)
                .build();

        ImportMatning importMatning2 = ImportMatning.builder()
                .avlastDatum("2020-01-02 12:00:00")
                .avlastVarde("42,3")
                .enhetAvlast("km")
                .felkod("Ok")
                .inomDetektionsomrade(">")
                .instrument("mandolin")
                .kommentar("qwerty")
                .matningstyp("Nivå2")
                .matningstypId(11)
                .matobjekt("ABC123")
                .matobjektId(2)
                .build();


        ImportMatning matningar[] = {importMatning1, importMatning2};

        when(matobjektService.getMatobjektIdByNamn("ABC123")).thenReturn(Optional.of(1));
        when(matningstypService.getMatningstyperForMatobjekt(1))
                .thenReturn(List.of(
                        Matningstyp.builder().id(10).typ("Nivå").build(),
                        Matningstyp.builder().id(11).typ("Nivå2").build()
                ));

        when(kallsystemService.findById("Annat (ej granskade mätningar)")).thenReturn(
                Optional.of(new Kallsystem("Annat (ej granskade mätningar)",
                        "En beskrivning",
                        false,true, "tips")));

        mvc.perform(post("/api/import/matningar/import?matrapportor=Metria AB&kallsystem=Annat (ej granskade mätningar)")
                        .content(objectMapper.writeValueAsString(matningar))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(matningService, Mockito.times(2)).create(any(), any(),eq(true), eq("Annat (ej granskade mätningar)"));
    }
}
