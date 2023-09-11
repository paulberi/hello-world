package se.metria.markkoll.service.indata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.entity.indata.KallfilEntity;
import se.metria.markkoll.openapi.model.IndataTypDto;
import se.metria.markkoll.repository.indata.KallfilRepository;
import se.metria.markkoll.service.FilService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class KallfilServiceTest {
    @InjectMocks
    KallfilService kallfilService;

    @Mock
    FilService filService;

    @Mock
    KallfilRepository kallfilRepository;

    @Test
    void så_ska_det_gå_att_spara_en_källfil() throws Exception {
        // Given
        var kundId = "kundId";
        var indataTyp = IndataTypDto.DPCOM;
        var shapeFile = new FileNameAwareByteArrayResource(new byte[0], "filnamn");

        var fil = new FilEntity();
        var kallfilId = mockUUID(0);

        when(filService.create(eq(shapeFile), eq("application/zip"), eq(kundId))).thenReturn(fil);
        when(kallfilRepository.save(any())).thenReturn(kallfilEntity(kallfilId, null, null));

        // When
        var idActual = kallfilService.create(shapeFile, indataTyp, kundId);

        // Then
        var entityExpect = kallfilEntity(null, indataTyp, fil);

        assertEquals(kallfilId, idActual);
        verify(kallfilRepository).save(eq(entityExpect));
    }

    KallfilEntity kallfilEntity(UUID id, IndataTypDto indataTyp, FilEntity filEntity) {
        var entity = new KallfilEntity();
        entity.setId(id);
        entity.setIndataTyp(indataTyp);
        entity.setFil(filEntity);
        return entity;
    }
}