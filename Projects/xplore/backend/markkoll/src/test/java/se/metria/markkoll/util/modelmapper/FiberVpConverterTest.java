package se.metria.markkoll.util.modelmapper;

import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;
import se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.openapi.model.FiberPunktersattningTypDto.MARKSKAP_OVRIGMARK;
import static se.metria.markkoll.openapi.model.FiberPunktersattningTypDto.MARKSKAP_SKOG;

public class FiberVpConverterTest {
    @Test
    void convert() {
        // Given
        var converter = new FiberVpConverter();
        var dto = fiberDto();
        var entity = fiberEntity();

        var context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(dto);
        when(context.getDestination()).thenReturn(new FiberVarderingsprotokollEntity());

        // When
        var entityActual = converter.convert(context);

        // Then
        assertEquals(entity, entityActual);
    }

    public static FiberVarderingsprotokollDto fiberDto() {
        return new FiberVarderingsprotokollDto()
            .config(new FiberVarderingsprotokollConfigDto().sarskildErsattning(1234.))
            .metadata(new FiberVarderingsprotokollMetadataDto()
                .varderingsmanOchForetag("Värderingsman")
                .varderingstidpunkt(LocalDateTime.of(1234, 5, 6, 7, 8))
            )
            .punktersattning(Arrays.asList(
                new FiberPunktersattningDto().beskrivning("punktersattning1").antal(1).typ(MARKSKAP_SKOG),
                new FiberPunktersattningDto().beskrivning("punktersattning2").antal(2).typ(MARKSKAP_OVRIGMARK)
            ))
            .markledning(Arrays.asList(
                new FiberMarkledningDto().beskrivning("markledning1").langd(1.).bredd(2),
                new FiberMarkledningDto().beskrivning("markledning2").langd(3.).bredd(4)
            ))
            .intrangAkerOchSkogsmark(Arrays.asList(
                new FiberIntrangAkerOchSkogsmarkDto().beskrivning("ÅkerOchSkogsmark1").ersattning(200),
                new FiberIntrangAkerOchSkogsmarkDto().beskrivning("ÅkerOchSkogsmark2").ersattning(300)
            ))
            .ovrigIntrangsersattning(Arrays.asList(
                new FiberOvrigIntrangsersattningDto().beskrivning("ÖvrigtIntrång1").ersattning(400),
                new FiberOvrigIntrangsersattningDto().beskrivning("ÖvrigtIntrång2").ersattning(500)
            ));
    }

    public static FiberVarderingsprotokollEntity fiberEntity() {
        var entity = new FiberVarderingsprotokollEntity();

        entity.getConfig().setSarskildErsattning(1234.);
        entity.getMetadata().setVarderingsmanOchForetag("Värderingsman");
        entity.getMetadata().setVarderingstidpunkt(LocalDateTime.of(1234, 5, 6, 7, 8));
        entity.addPunktersattning(new FiberPunktersattningEntity("punktersattning1", 1, MARKSKAP_SKOG));
        entity.addPunktersattning(new FiberPunktersattningEntity("punktersattning2", 2, MARKSKAP_OVRIGMARK));
        entity.addMarkledning(new FiberMarkledningEntity("markledning1", 1, 2));
        entity.addMarkledning(new FiberMarkledningEntity("markledning2", 3, 4));
        entity.addIntrangAkerOchSkogsmark(new FiberIntrangAkerOchSkogsmarkEntity("ÅkerOchSkogsmark1", 200));
        entity.addIntrangAkerOchSkogsmark(new FiberIntrangAkerOchSkogsmarkEntity("ÅkerOchSkogsmark2", 300));
        entity.addOvrigIntrangsersattning(new FiberOvrigIntrangsersattningEntity("ÖvrigtIntrång1", 400));
        entity.addOvrigIntrangsersattning(new FiberOvrigIntrangsersattningEntity("ÖvrigtIntrång2", 500));

        return entity;
    }
}