package se.metria.markkoll.service.dokument;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.DokumentmallEntity;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.openapi.model.DokumentmallDto;
import se.metria.markkoll.openapi.model.DokumentTypDto;
import se.metria.markkoll.repository.dokument.DokumentmallRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.service.admin.AclService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
@DisplayName("Givet DokumentService")
public class DokumentmallServiceTest {

    DokumentmallService dokumentmallService;

    AclService mockAclService;
    DokumentmallRepository mockDokumentmallRepository;
    FilService mockFilService;
    ModelMapper mockModelMapper;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockDokumentmallRepository = mock(DokumentmallRepository.class);
        mockFilService = mock(FilService.class);
        mockModelMapper = mock(ModelMapper.class);
        dokumentmallService = new DokumentmallService(mockAclService, mockDokumentmallRepository, mockFilService, mockModelMapper);
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_dokument() {
        // Given
        var dokumentId = mockUUID(0);
        var dokumentEntity = new DokumentmallEntity();
        var dokumentDto = new DokumentmallDto();

        when(mockDokumentmallRepository.findById(any())).thenReturn(Optional.of(dokumentEntity));
        when(mockModelMapper.map(any(), any())).thenReturn(dokumentDto);

        // When
        var dokument = dokumentmallService.get(dokumentId);

        // Then
        assertEquals(dokumentDto, dokument);

        verify(mockDokumentmallRepository).findById(eq(dokumentId));
        verify(mockModelMapper).map(eq(dokumentEntity), eq(DokumentmallDto.class));
    }

    @Test
    void så_ska_det_gå_att_hämta_alla_dokument_tillhörandes_ett_projekt() {
        // Given
        var kundId = "kundId";
        var dokumentEntityNoId = DokumentmallEntity.builder().id(null);
        var dokumentListExpect = Arrays.asList(dokumentEntityNoId.build(), dokumentEntityNoId.build());

        when(mockDokumentmallRepository.findByKundIdOrderBySkapadDatumDesc(any())).thenReturn(dokumentListExpect);
        when(mockModelMapper.map(any(), any())).thenReturn(dokumentEntityNoId.build());

        // When
        var dokumentList = dokumentmallService.getKundDokumentmallar(kundId);

        // Then
        assertEquals(dokumentListExpect, dokumentList);

        verify(mockDokumentmallRepository).findByKundIdOrderBySkapadDatumDesc(eq(kundId));
        verify(mockModelMapper, times(dokumentListExpect.size())).map(any(), eq(DokumentmallDto.class));
    }

    @Test
    void så_ska_det_gå_att_hämta_innehållet_i_ett_dokument() {
        // Given
        var dokumentId = mockUUID(0);
        var filId = mockUUID(1);
        var dataExpect = new ByteArrayResource("bytes".getBytes());

        var filEntity = FilEntity.builder()
                .fil(dataExpect.getByteArray())
                .build();
        when(mockFilService.getFilEntity(any())).thenReturn(filEntity);

        var dokumentEntity = new DokumentmallEntity();
        dokumentEntity.setFil(filEntity);
        dokumentEntity.setDokumenttyp(DokumentTypDto.MARKUPPLATELSEAVTAL);
        dokumentEntity.setId(dokumentId);
        dokumentEntity.setKundId("kundId");
        dokumentEntity.setNamn("namn");
        dokumentEntity.setSelected(true);

        when(mockDokumentmallRepository.findById(any())).thenReturn(Optional.of(dokumentEntity));

        // When
        var data = dokumentmallService.getFileData(dokumentId);

        // Then
        assertEquals(dataExpect, data);
    }

    @Test
    void så_ska_det_gå_att_hämta_det_dokumentet_som_är_valt_som_default_för_en_dokumenttyp() {
        // Given
        var kundId = "kundId";
        var dokumenttyp = DokumentTypDto.INFOBREV;
        var dokumentExpect = new DokumentmallDto();
        var dokumentEntity = new DokumentmallEntity();

        when(mockDokumentmallRepository.getAllByKundIdAndDokumenttypAndSelectedIsTrue(eq(kundId), eq(dokumenttyp)))
            .thenReturn(Arrays.asList(dokumentEntity));
        when(mockModelMapper.map(eq(dokumentEntity), eq(DokumentmallDto.class))).thenReturn(dokumentExpect);

        // When
        var dokumentActual = dokumentmallService.getSelected(kundId, dokumenttyp);

        // Then
        verify(mockDokumentmallRepository).getAllByKundIdAndDokumenttypAndSelectedIsTrue(eq(kundId), eq(dokumenttyp));
        verify(mockModelMapper).map(eq(dokumentEntity), eq(DokumentmallDto.class));

        assertEquals(dokumentExpect, dokumentActual);
    }
}
