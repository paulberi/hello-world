package se.metria.xplore.kundconfig.service;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import se.metria.xplore.kundconfig.entity.PermissionsEntity;
import se.metria.xplore.kundconfig.openapi.model.PermissionsDto;
import se.metria.xplore.kundconfig.openapi.model.ProduktDto;
import se.metria.xplore.kundconfig.repository.PermissionsRepository;
import se.metria.xplore.kundconfig.util.CollectionUtil;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Givet PermissionsService")
public class PermissionsServiceTest {
    PermissionsService permissionsService;
    PermissionsRepository mockPermissionsRepository;
    ModelMapper mockModelMapper;

    @BeforeEach
    void before() {
        mockPermissionsRepository = mock(PermissionsRepository.class);
        mockModelMapper = mock(ModelMapper.class);
        permissionsService = new PermissionsService(mockPermissionsRepository, mockModelMapper);
    }

    @Test
    void så_ska_det_gå_att_hämta_användarbehörigheter() {
        var user = "chka";
        var produkt = ProduktDto.MARKKOLL;
        var permissioneEntity = CollectionUtil.objectsAsList(
                mock(PermissionsEntity.class),
                mock(PermissionsEntity.class)
        );
        var permissionDto = mock(PermissionsDto.class);

        var expect = permissioneEntity.stream()
                .map(p -> permissionDto)
                .collect(Collectors.toList());

        when(mockPermissionsRepository.findByUserIdAndProdukt(any(), any())).thenReturn(permissioneEntity);
        when(mockModelMapper.map(any(), any())).thenReturn(permissionDto);

        // When
        var permissions = permissionsService.getPermissions(user, produkt);

        // Then
        assertEquals(expect, permissions);
        verify(mockPermissionsRepository).findByUserIdAndProdukt(eq(user), eq(produkt.toString()));
    }
}
