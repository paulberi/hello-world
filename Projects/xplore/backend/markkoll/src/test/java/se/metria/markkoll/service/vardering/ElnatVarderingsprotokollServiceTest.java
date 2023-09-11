package se.metria.markkoll.service.vardering;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.AvtalsinstallningarRepository;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.vardering.elnat.ElnatVarderingsprotokollRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.klassificering.ElnatKlassificeringService;
import se.metria.markkoll.service.vardering.elnat.bilaga.ElnatBilagaService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService;
import se.metria.markkoll.util.modelmapper.ElnatVpConverterTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollService.ELEKTRISK_STARKSTROMSLEDNING;
import static se.metria.markkoll.testdata.TestData.avtalEntity;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.testdata.VPTestData.*;

@MarkkollServiceTest
@DisplayName("Givet ElnatVarderingsprotokollService")
public class ElnatVarderingsprotokollServiceTest {

    ElnatVarderingsprotokollService varderingsprotokollService;

    AclService mockAclService;
    ElnatBilagaService mockBilagaService;
    ElnatKlassificeringService mockElnatKlassificeringService;
    FilService mockFilService;
    ModelMapper mockModelMapper;
    ElnatVarderingsprotokollRepository mockVarderingsprotokollRepository;
    AvtalRepository mockAvtalRepository;
    OmradesintrangRepository mockOmradesintrangRepository;
    AvtalsinstallningarRepository mockAvtalsinstallningarRepository;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockBilagaService = mock(ElnatBilagaService.class);
        mockFilService = mock(FilService.class);
        mockElnatKlassificeringService = mock(ElnatKlassificeringService.class);
        mockModelMapper = mock(ModelMapper.class);
        mockVarderingsprotokollRepository = mock(ElnatVarderingsprotokollRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockAvtalsinstallningarRepository = mock(AvtalsinstallningarRepository.class);

        varderingsprotokollService = new ElnatVarderingsprotokollService(mockAvtalsinstallningarRepository,
            mockAclService, mockBilagaService, mockElnatKlassificeringService, mockFilService,
            mockVarderingsprotokollRepository, mockAvtalRepository, mockModelMapper, mockOmradesintrangRepository);
    }

    @Test
    void så_ska_det_gå_att_skapa_ett_värderingsprotokoll() {
        // Given
        var vpDto = varderingsprotokollDto(mockUUID(0));
        var vpEntity = varderingsprotokollEntity(mockUUID(0));
        var avtalEntity = avtalEntity();

        var avtalId = mockUUID(1);

        when(mockModelMapper.map(any(), eq(ElnatVarderingsprotokollEntity.class))).thenReturn(vpEntity);
        when(mockAvtalRepository.findById(any())).thenReturn(Optional.of(avtalEntity));
        when(mockVarderingsprotokollRepository.save(any())).then(returnsFirstArg());

        // When
        var idExpect = varderingsprotokollService.create(vpDto, avtalId);

        // Then
        assertEquals(idExpect, vpEntity.getId());
        assertEquals(vpEntity.getAvtal(), avtalEntity);

        verify(mockVarderingsprotokollRepository).save(eq(vpEntity));
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_värderingsprotokoll() {
        // Given
        var vpId = mockUUID(0);
        var vpDto = varderingsprotokollDto(mockUUID(0));
        var vpEntity = varderingsprotokollEntity(mockUUID(0));

        when(mockModelMapper.map(any(), any())).thenReturn(vpDto);
        when(mockVarderingsprotokollRepository.findById(any())).thenReturn(Optional.of(vpEntity));

        // When
        var vpExpect = varderingsprotokollService.get(vpId);

        // Then
        assertEquals(vpExpect, vpDto);
    }

    @Test
    void så_ska_det_gå_att_uppdatera_varderingsprotokollets_metadata() {
        // Given
        var vpId = mockUUID(0);
        var metaData = vpMetadataDto();

        var entity = new ElnatVarderingsprotokollEntity().builder()
            .metadata(vpMetadataEntity2())
            .build();

        var metadataEntity = vpMetadataEntity();

        when(mockModelMapper.map(any(), any())).thenReturn(metadataEntity);
        when(mockVarderingsprotokollRepository.findById(any())).thenReturn(Optional.of(entity));

        // When
        varderingsprotokollService.updateMetadata(vpId, metaData);

        // Then
        assertEquals(metadataEntity, entity.getMetadata());
    }

    @Test
    void så_ska_det_gå_att_uppdatera_värderingsprotokollets_beräkningskonfiguration() {
        // Given
        var vpId = mockUUID(0);
        var config = vpConfigDto();

        var entity = new ElnatVarderingsprotokollEntity().builder()
            .config(vpConfigEntity2())
            .build();

        var configEntity = vpConfigEntity();

        when(mockModelMapper.map(any(), any())).thenReturn(configEntity);
        when(mockVarderingsprotokollRepository.findById(any())).thenReturn(Optional.of(entity));

        // When
        varderingsprotokollService.updateConfig(vpId, config);

        // Then
        assertEquals(configEntity, entity.getConfig());
    }

    @Test
    void så_ska_det_gå_att_uppdatera_prisområde() {
        // Given
        var vpId = mockUUID(0);
        var prisomrade = ElnatPrisomradeDto.TILLVAXTOMRADE_3;

        var entity = new ElnatVarderingsprotokollEntity();
        assertNotEquals(entity.getPrisomrade(), prisomrade);

        when(mockVarderingsprotokollRepository.findById(any())).thenReturn(Optional.of(entity));

        // When
        varderingsprotokollService.updatePrisomrade(vpId, prisomrade);

        // Then
        assertEquals(entity.getPrisomrade(), prisomrade);
    }

    @Test
    void så_ska_det_gå_att_lägga_till_en_bilaga_till_ett_värderingsprotokoll() throws Exception {
        // Given
        var vpId = mockUUID(0);
        var bilagaTyp = BilagaTypDto.ENSTAKA_TRAD;
        var file = new MockMultipartFile("file", "file".getBytes());
        var bilagaExpect = new ElnatBilagaDto();
        var kundId = "kund";

        var filId = mockUUID(0);
        var filEntity = new FilEntity();
        filEntity.setId(filId);

        when(mockVarderingsprotokollRepository.getKundId(eq(vpId))).thenReturn(kundId);
        when(mockFilService.create(eq(file), eq(kundId))).thenReturn(filEntity);
        when(mockBilagaService.create(eq(filId), eq(vpId), eq(bilagaTyp))).thenReturn(bilagaExpect);

        // When
        var bilagaActual = varderingsprotokollService.addBilaga(file, bilagaTyp, vpId);

        // Then
        assertEquals(bilagaExpect, bilagaActual);
        verify(mockFilService).create(eq(file), eq(kundId));
        verify(mockBilagaService).create(eq(filId), eq(vpId), eq(bilagaTyp));
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_avtals_värderingsprotokoll() {
        // Given
        var avtalId = mockUUID(0);
        var vpEntity = varderingsprotokollEntity(mockUUID(1));
        var vpDto = varderingsprotokollDto(mockUUID(1));

        when(mockVarderingsprotokollRepository.getWithAvtalId(eq(avtalId))).thenReturn(Optional.of(vpEntity));
        when(mockModelMapper.map(eq(vpEntity), eq(ElnatVarderingsprotokollDto.class))).thenReturn(vpDto);

        // When
        var vpOpt = varderingsprotokollService.getWithAvtalId(avtalId);

        // Then
        assertEquals(Optional.of(vpDto), vpOpt);
    }

    @Test
    void så_ska_det_gå_att_uppdatera_ett_värderingsprotokoll() {
        // Given
        var vpId = mockUUID(0);
        var vpDto = new ElnatVarderingsprotokollDto().id(vpId);
        var vpEntity = new ElnatVarderingsprotokollEntity();

        when(mockVarderingsprotokollRepository.findById(eq(vpId))).thenReturn(Optional.of(vpEntity));

        // When
        varderingsprotokollService.update(vpDto);

        // Then
        verify(mockModelMapper).map(eq(vpDto), eq(vpEntity));
        verify(mockVarderingsprotokollRepository).save(eq(vpEntity));
    }

    @Test
    void så_ska_inget_returneras_om_ett_avtal_saknar_värderingsprotokoll() {
        // Given
        var avtalId = mockUUID(0);

        when(mockVarderingsprotokollRepository.getWithAvtalId(eq(avtalId))).thenReturn(Optional.empty());

        // When
        var vpOpt = varderingsprotokollService.getWithAvtalId(avtalId);

        // Then
        assertEquals(Optional.empty(), vpOpt);
    }

    @Test
    void så_ska_det_gå_att_sätta_markvärde() {
        // Given
        var vpId = mockUUID(0);
        var ersattning = 1234;

        var entity = new ElnatVarderingsprotokollEntity();
        entity.addLedningSkogsmark("placeholder", 1234);
        entity.getMetadata().setVarderingstidpunkt(LocalDateTime.of(1, 2, 3, 4, 5));

        when(mockVarderingsprotokollRepository.findById(eq(vpId))).thenReturn(Optional.of(entity));

        // When
        varderingsprotokollService.setMarkvarde(vpId, ersattning);

        // Then
        var entityExpect = new ElnatVarderingsprotokollEntity();
        entityExpect.addLedningSkogsmark(ELEKTRISK_STARKSTROMSLEDNING, ersattning);
        entityExpect.getMetadata().setVarderingstidpunkt(LocalDateTime.of(1, 2, 3, 4, 5));

        assertEquals(entityExpect, entity);
    }

    @Test
    void så_ska_det_gå_att_uppdatera_littera() {
        // Given
        var vpId = mockUUID(0);
        var littera = "littera";
        var entity = new ElnatVarderingsprotokollEntity();

        when(mockVarderingsprotokollRepository.findById(eq(vpId))).thenReturn(Optional.of(entity));

        // When
        varderingsprotokollService.updateLittera(vpId, littera);

        // Then
        assertEquals(littera, entity.getMetadata().getLedning());
    }

    @Test
    void så_ska_det_gå_att_uppdatera_ett_värderingsprotokoll_för_ett_visst_avtal() {
        // Given
        var avtalId = mockUUID(0);

        var vpDto = ElnatVpConverterTest.elnatDto();
        var vpEntity = new ElnatVarderingsprotokollEntity();
        vpEntity.setId(mockUUID(1));

        assertNotEquals(vpDto.getId(), vpEntity.getId());

        when(mockVarderingsprotokollRepository.findByAvtalId(eq(avtalId))).thenReturn(Optional.of(vpEntity));

        // When
        varderingsprotokollService.update(vpDto, avtalId);

        // Then
        var vpEntityExpect = ElnatVpConverterTest.elnatEntity();
        vpEntityExpect.setId(mockUUID(1));
        verify(mockVarderingsprotokollRepository).save(eq(vpEntity));
    }
}
