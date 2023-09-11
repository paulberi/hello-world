package se.metria.markkoll.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.openapi.model.FilDto;
import se.metria.markkoll.repository.fil.FilRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.SkipIdMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
@DisplayName("Givet BifogadFilService")
public class FilServiceTest {
    FilService filService;

    AclService mockAclService;
    FilRepository mockFilRepository;
    ModelMapper mockModelMapper;

    private final String kundId1 = "kund1";
    private final String skapadAv1 = "Skapad Av Namn1";

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockFilRepository = mock(FilRepository.class);
        mockModelMapper = mock(ModelMapper.class);

        filService = new FilService(mockAclService, mockFilRepository, mockModelMapper);
    }

    @Test
    void så_ska_det_gå_att_skapa_en_fil() {
        // Given
        var bytes = "min testfil".getBytes();
        var filename = "filename";
        var mimeType = "application";
        var idExpect = mockUUID(0);

        var entity = new FilEntity();
        entity.setId(idExpect);
        entity.setMimeTyp(mimeType);
        entity.setFil(bytes);
        entity.setFilnamn(filename);
        entity.setSkapadAv(skapadAv1);
        entity.setKundId(kundId1);

        when(mockFilRepository.saveAndFlush(any())).thenReturn(entity.withId(idExpect));

        // When
        var fil = filService.create(bytes, filename, mimeType, skapadAv1, kundId1);

        // Then
        assertEquals(idExpect, fil.getId());
        verify(mockFilRepository).saveAndFlush(SkipIdMatcher.eq(entity));
    }

    @Test
    void så_ska_det_gå_att_hämta_innehållet_i_en_fil() {
        // Given
        var filId = mockUUID(0);
        var dataExpect = new ByteArrayResource("data".getBytes());
        var filEntity = FilEntity.builder()
            .fil(dataExpect.getByteArray())
            .build();

        when(mockFilRepository.findById(any())).thenReturn(Optional.of(filEntity));

        // When
        var data = filService.getFilEntity(filId).getByteArrayResource();

        // Then
        assertEquals(data, dataExpect);
    }

    @Test
    void så_ska_det_gå_att_hämta_filinformation() {
        // Given
        var filId = mockUUID(0);
        var filEntity = new FilEntity();
        var filExpect = new FilDto();

        when(mockFilRepository.findById(eq(filId))).thenReturn(Optional.of(filEntity));
        when(mockModelMapper.map(eq(filEntity), eq(FilDto.class))).thenReturn(filExpect);

        // When
        var filActual = filService.get(filId);

        // Then
        assertEquals(filExpect, filActual);
    }

    @Test
    void så_ska_det_gå_att_hämta_filer_till_bilagor_som_hör_till_värderingsprotokollets_avtal() {
        // Given
        var avtalId = mockUUID(0);
        List<FilDto> filerExpect = Arrays.asList(new FilDto());
        Set<FilEntity> entities = CollectionUtil.asSet(new FilEntity());

        when(mockFilRepository.getFiler(eq(avtalId))).thenReturn(entities);
        when(mockModelMapper.map(any(), eq(FilDto.class))).thenReturn(new FilDto());

        // When
        var filerActual = filService.getBilagorFiler(avtalId);

        // Then
        assertEquals(filerExpect, filerActual);
    }

    @Test
    void så_ska_det_gå_att_hämta_fildata() {
        // Given
        var filId = mockUUID(0);
        var dataExpect = new FileNameAwareByteArrayResource("data".getBytes(), "filnamn");
        var filEntity = FilEntity.builder().filnamn("filnamn").fil("data".getBytes()).build();

        when(mockFilRepository.findById(eq(filId))).thenReturn(Optional.of(filEntity));

        // When
        var dataActual = filService.getData(filId);

        // Then
        assertEquals(dataExpect, dataActual);
    }
}
