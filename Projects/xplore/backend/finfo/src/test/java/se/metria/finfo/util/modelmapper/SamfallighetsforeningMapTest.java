package se.metria.finfo.util.modelmapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.finfo.config.ModelMapperConfiguration;
import se.metria.finfo.entity.samfallighetsforening.SamfallighetsforeningEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.finfo.data.SamfallighetsforeningTestdata.samfallighetsforening;
import static se.metria.finfo.data.SamfallighetsforeningTestdata.samfallighetsforeningEntity;

@ExtendWith(SpringExtension.class)
@Import(ModelMapperConfiguration.class)
class SamfallighetsforeningMapTest {
    @Autowired
    ModelMapper modelMapper;

    @Test
    void mapTest() throws Exception {
        // Given
        var samfallighetsforening = samfallighetsforening();

        // When
        var entityActual = modelMapper.map(samfallighetsforening, SamfallighetsforeningEntity.class);

        // Then
        modelMapper.validate();
        var entityExpect = samfallighetsforeningEntity();
        assertEquals(entityExpect.toString(), entityActual.toString());
    }
}