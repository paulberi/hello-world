package se.metria.markkoll.util.modelmapper;

import org.junit.jupiter.api.Test;
import org.modelmapper.spi.MappingContext;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollConfigEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollMetadataEntity;
import se.metria.markkoll.openapi.model.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.openapi.model.ElnatPunktersattningTypDto.KABELSKAP_EJ_KLASSIFICERAD;
import static se.metria.markkoll.openapi.model.ElnatPunktersattningTypDto.KABELSKAP_OVRIGMARK;
import static se.metria.markkoll.testdata.TestData.mockUUID;

public class ElnatVpConverterTest {
    @Test
    void convert() {
        // Given
        var converter = new ElnatVpConverter();
        var dto = elnatDto();
        var entity = elnatEntity();

        var context = mock(MappingContext.class);
        when(context.getSource()).thenReturn(dto);
        when(context.getDestination()).thenReturn(new ElnatVarderingsprotokollEntity());

        // When
        var entityActual = converter.convert(context);

        // Then
        assertEquals(entity, entityActual);
    }

    public static ElnatVarderingsprotokollDto elnatDto() {
        var dto = new ElnatVarderingsprotokollDto()
            .punktersattning(Arrays.asList(
                new ElnatPunktersattningDto().beskrivning("punktersattning1").antal(1).typ(KABELSKAP_OVRIGMARK),
                new ElnatPunktersattningDto().beskrivning("punktersattning2").antal(2).typ(KABELSKAP_EJ_KLASSIFICERAD)
            ))
            .ledningSkogsmark(Arrays.asList(
                new ElnatLedningSkogsmarkDto().beskrivning("ledningSkogsmark1").ersattning(300),
                new ElnatLedningSkogsmarkDto().beskrivning("ledningSkogsmark2").ersattning(400)
            ))
            .ssbSkogsmark(Arrays.asList(
                new ElnatSsbSkogsmarkDto().beskrivning("ssbSkogsmark1").langd(1).bredd(2),
                new ElnatSsbSkogsmarkDto().beskrivning("ssbSkogsmark2").langd(3).bredd(4)
            ))
            .ssbVaganlaggning(Arrays.asList(
                new ElnatSsbVaganlaggningDto().beskrivning("vaganlaggning1").langd(5).zon(ElnatZonDto.ZON_1),
                new ElnatSsbVaganlaggningDto().beskrivning("vaganlaggning2").langd(6).zon(ElnatZonDto.ZON_2)
            ))
            .markledning(Arrays.asList(
                new ElnatMarkledningDto().beskrivning("markledning1").langd(1).bredd(2),
                new ElnatMarkledningDto().beskrivning("markledning2").langd(3).bredd(4)
            ))
            .ovrigtIntrang(Arrays.asList(
                new ElnatOvrigtIntrangDto().beskrivning("övrigt intrång1").ersattning(100),
                new ElnatOvrigtIntrangDto().beskrivning("övrigt intrång2").ersattning(200)
            ))
            .hinderAkermark(Arrays.asList(
                new ElnatHinderAkermarkDto().beskrivning("hinder åkermark1").ersattning(300),
                new ElnatHinderAkermarkDto().beskrivning("hinder åkermark2").ersattning(400)
            ))
            .config(new ElnatVarderingsprotokollConfigDto()
                .forhojdMinimumersattning(true)
                .ingenGrundersattning(true)
                .lagspanning(true)
                .storskogsbruksavtalet(true)
            )
            .metadata(new ElnatVarderingsprotokollMetadataDto()
                .ledning("ledning")
                .koncessionslopnr("lopnr")
                .varderingsmanOchForetag("vmf")
                .varderingstidpunkt(LocalDateTime.of(2222,2,2, 2, 2, 2))
                .fastighetsnummer("Fastighetsnummer")
                .spanningsniva("10kV")
            )
            .rotnetto(1234.)
            .id(mockUUID(0));

        return dto;
    }

    public static ElnatVarderingsprotokollEntity elnatEntity() {
        var entity = new ElnatVarderingsprotokollEntity();

        entity.addPunktersattning("punktersattning1", 1, KABELSKAP_OVRIGMARK);
        entity.addPunktersattning("punktersattning2", 2, KABELSKAP_EJ_KLASSIFICERAD);
        entity.addLedningSkogsmark("ledningSkogsmark1", 300);
        entity.addLedningSkogsmark("ledningSkogsmark2", 400);
        entity.addSsbSkogsmark("ssbSkogsmark1", 1, 2);
        entity.addSsbSkogsmark("ssbSkogsmark2", 3, 4);
        entity.addSsbVaganlaggning("vaganlaggning1", 5, ElnatZonDto.ZON_1);
        entity.addSsbVaganlaggning("vaganlaggning2", 6, ElnatZonDto.ZON_2);
        entity.addMarkledning("markledning1", 1, 2);
        entity.addMarkledning("markledning2", 3, 4);
        entity.addOvrigtIntrang("övrigt intrång1", 100);
        entity.addOvrigtIntrang("övrigt intrång2", 200);
        entity.addHinderAkermark("hinder åkermark1", 300);
        entity.addHinderAkermark("hinder åkermark2", 400);

        entity.setConfig(new ElnatVarderingsprotokollConfigEntity(true, true, true, true));
        entity.setMetadata(new ElnatVarderingsprotokollMetadataEntity("ledning", "lopnr", "vmf", LocalDateTime.of(2222,2,2, 2, 2, 2), "Fastighetsnummer", "10kV"));
        entity.setRotnetto(1234.);
        entity.setId(mockUUID(0));

        return entity;
    }
}