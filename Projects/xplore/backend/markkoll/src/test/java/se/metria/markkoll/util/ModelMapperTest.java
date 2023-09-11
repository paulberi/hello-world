package se.metria.markkoll.util;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.entity.logging.projektlogg.AvtalhandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.InfobrevhandelseEntity;
import se.metria.markkoll.entity.logging.projektlogg.ProjekthandelseEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatMarkledningEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatPunktersattningEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatSsbSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatSsbVaganlaggningEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollConfigEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollMetadataEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.testdata.LoggTestData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.testdata.VPTestData.*;

public class ModelMapperTest {
    ModelMapper modelMapper = new ModelMapper();

    @Test
    void map_vpmetadata_dto_to_entity() {
        // Given
        var entity = vpMetadataEntity();
        var dto = vpMetadataDto();

        // When
        var entityExpect = modelMapper.map(dto, ElnatVarderingsprotokollMetadataEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_vpmetadata_entity_to_dto() {
        // Given
        var entity = vpMetadataEntity();
        var dto = vpMetadataDto();

        // When
        var dtoExpect = modelMapper.map(entity, ElnatVarderingsprotokollMetadataDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_vpconfig_dto_to_entity() {
        // Given
        var entity = vpConfigEntity();
        var dto = vpConfigDto();

        // When
        var entityExpect = modelMapper.map(dto, ElnatVarderingsprotokollConfigEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_vpconfig_entity_to_dto() {
        // Given
        var entity = vpConfigEntity();
        var dto = vpConfigDto();

        // When
        var dtoExpect = modelMapper.map(entity, ElnatVarderingsprotokollConfigDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_markledning_dto_to_entity() {
        // Given
        var entity = markledningEntity(null);
        var dto = markledningDto();

        // When
        var entityExpect = modelMapper.map(dto, ElnatMarkledningEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_markledning_entity_to_dto() {
        // Given
        var entity = markledningEntity(null);
        var dto = markledningDto();

        // When
        var dtoExpect = modelMapper.map(entity, ElnatMarkledningDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_punktersattning_dto_to_entity() {
        // Given
        var entity = punktersattningEntity(null);
        var dto = punktersattningDto();

        // When
        var entityExpect = modelMapper.map(dto, ElnatPunktersattningEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_punktersattning_entity_to_dto() {
        // Given
        var entity = punktersattningEntity(null);
        var dto = punktersattningDto();

        // When
        var dtoExpect = modelMapper.map(entity, ElnatPunktersattningDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_ssb_skogsmark_entity_to_dto() {
        // Given
        var entity = ssbSkogsmarkEntity(null);
        var dto = ssbSkogsmarkDto();

        // When
        var dtoExpect = modelMapper.map(entity, ElnatSsbSkogsmarkDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_ssb_skogsmark_dto_to_entity() {
        // Given
        var entity = ssbSkogsmarkEntity(null);
        var dto = ssbSkogsmarkDto();

        // When
        var entityExpect = modelMapper.map(dto, ElnatSsbSkogsmarkEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_ssb_vaganlaggning_entity_to_dto() {
        // Given
        var entity = ssbVaganlaggningEntity(null);
        var dto = ssbVaganlaggningDto();

        // When
        var dtoExpect = modelMapper.map(entity, ElnatSsbVaganlaggningDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_ssb_vaganlaggning_dto_to_entity() {
        // Given
        var entity = ssbVaganlaggningEntity(null);
        var dto = ssbVaganlaggningDto();

        // When
        var entityExpect = modelMapper.map(dto, ElnatSsbVaganlaggningEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_projekthandelse_dto_to_entity() {
        // Given
        var dto = LoggTestData.projekthandelseDto();
        var entity = LoggTestData.projekthandelseEntity();

        // When
        var entityExpect = modelMapper.map(dto, ProjekthandelseEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_projekthandelse_entity_to_dto() {
        // Given
        var dto = LoggTestData.projekthandelseDto();
        var entity = LoggTestData.projekthandelseEntity();

        // When
        var dtoExpect = modelMapper.map(entity, ProjekthandelseDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_avtalhandelse_dto_to_entity() {
        // Given
        var dto = LoggTestData.avtalhandelseDto(mockUUID(0));
        var entity = LoggTestData.avtalhandelseEntity(mockUUID(0));

        // When
        var entityExpect = modelMapper.map(dto, AvtalhandelseEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_avtalhandelse_entity_to_dto() {
        // Given
        var dto = LoggTestData.avtalhandelseDto(mockUUID(0));
        var entity = LoggTestData.avtalhandelseEntity(mockUUID(0));

        // When
        var dtoExpect = modelMapper.map(entity, AvtalhandelseDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }

    @Test
    void map_infobrevhandelse_dto_to_entity() {
        // Given
        var dto = LoggTestData.infobrevhandelseDto(mockUUID(0));
        var entity = LoggTestData.infobrevhandelseEntity(mockUUID(0));

        // When
        var entityExpect = modelMapper.map(dto, InfobrevhandelseEntity.class);

        // Then
        assertEquals(entityExpect, entity);
    }

    @Test
    void map_infobrevhandelse_entity_to_dto() {
        // Given
        var dto = LoggTestData.infobrevhandelseDto(mockUUID(0));
        var entity = LoggTestData.infobrevhandelseEntity(mockUUID(0));

        // When
        var dtoExpect = modelMapper.map(entity, InfobrevhandelseDto.class);

        // Then
        assertEquals(dtoExpect, dto);
    }
}
