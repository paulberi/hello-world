package se.metria.markkoll.testdata;

import se.metria.markkoll.entity.vardering.elnat.ElnatLedningSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatMarkledningEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatPunktersattningEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatSsbSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.elnat.ElnatSsbVaganlaggningEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollConfigEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollMetadataEntity;
import se.metria.markkoll.openapi.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static se.metria.markkoll.testdata.TestData.mockUUID;

public class VPTestData {
    public static ElnatVarderingsprotokollEntity varderingsprotokollEntity(UUID id) {
        return ElnatVarderingsprotokollEntity.builder()
            .id(id)
            .config(vpConfigEntity())
            .metadata(vpMetadataEntity())
            .markledning(Arrays.asList(markledningEntity(mockUUID(0))))
            .punktersattning(Arrays.asList(punktersattningEntity(mockUUID(1))))
            .ssbSkogsmark(Arrays.asList(ssbSkogsmarkEntity(mockUUID(2))))
            .ssbVaganlaggning(Arrays.asList(ssbVaganlaggningEntity(mockUUID(3))))
            .ledningSkogsmark(Arrays.asList(ledningSkogsmarkEntity(mockUUID(4))))
            .rotnetto(0.)
            .prisomrade(ElnatPrisomradeDto.NORRLANDS_KUSTLAND)
            .build();
    }

    public static ElnatVarderingsprotokollMetadataEntity vpMetadataEntity() {
        return ElnatVarderingsprotokollMetadataEntity.builder()
            .koncessionslopnr("löpnummer")
            .ledning("ledning")
            .varderingsmanOchForetag("värderingsman")
            .varderingstidpunkt(LocalDateTime.of(2002, 1, 1, 1, 1))
            .fastighetsnummer("Fastighetsnummer")
            .spanningsniva("10kV")
            .build();
    }

    public static ElnatVarderingsprotokollMetadataEntity vpMetadataEntity2() {
        return ElnatVarderingsprotokollMetadataEntity.builder()
            .koncessionslopnr("löpnummer 2")
            .ledning("ledning 2")
            .varderingsmanOchForetag("företag")
            .varderingstidpunkt(LocalDateTime.of(1234, 5, 6, 7, 8))
            .build();
    }

    public static ElnatVarderingsprotokollConfigEntity vpConfigEntity() {
        return ElnatVarderingsprotokollConfigEntity.builder()
            .forhojdMinimumersattning(true)
            .ingenGrundersattning(false)
            .lagspanning(true)
            .storskogsbruksavtalet(false)
            .build();
    }

    public static ElnatVarderingsprotokollConfigEntity vpConfigEntity2() {
        return ElnatVarderingsprotokollConfigEntity.builder()
            .forhojdMinimumersattning(false)
            .ingenGrundersattning(true)
            .lagspanning(false)
            .storskogsbruksavtalet(true)
            .build();
    }

    public static ElnatVarderingsprotokollDto varderingsprotokollDto(UUID id) {
        return new ElnatVarderingsprotokollDto()
            .metadata(vpMetadataDto())
            .markledning(Arrays.asList(markledningDto()))
            .punktersattning(Arrays.asList(punktersattningDto()))
            .ssbSkogsmark(Arrays.asList(ssbSkogsmarkDto()))
            .ssbVaganlaggning(Arrays.asList(ssbVaganlaggningDto()))
            .ledningSkogsmark(Arrays.asList(ledningSkogsmarkDto()))
            .ovrigtIntrang(new ArrayList<>())
            .config(vpConfigDto())
            .rotnetto(0.)
            .prisomrade(ElnatPrisomradeDto.NORRLANDS_KUSTLAND)
            .id(id);
    }

    public static ElnatVarderingsprotokollConfigDto vpConfigDto() {
        return new ElnatVarderingsprotokollConfigDto()
            .storskogsbruksavtalet(false)
            .lagspanning(true)
            .ingenGrundersattning(false)
            .forhojdMinimumersattning(true);
    }

    public static ElnatVarderingsprotokollMetadataDto vpMetadataDto() {
        return new ElnatVarderingsprotokollMetadataDto()
            .koncessionslopnr("löpnummer")
            .ledning("ledning")
            .varderingsmanOchForetag("värderingsman")
            .varderingstidpunkt(LocalDateTime.of(2002, 1, 1, 1, 1))
            .spanningsniva("10kV")
            .fastighetsnummer("Fastighetsnummer");
    }

    public static ElnatLedningSkogsmarkDto ledningSkogsmarkDto() {
        return new ElnatLedningSkogsmarkDto()
            .beskrivning("beskrivning")
            .ersattning(1234);
    }

    public static ElnatPunktersattningDto punktersattningDto() {
        return new ElnatPunktersattningDto()
            .antal(1)
            .beskrivning("beskrivning")
            .typ(ElnatPunktersattningTypDto.KABELSKAP_JORDBRUKSIMPEDIMENT);
    }

    public static ElnatPunktersattningEntity punktersattningEntity(UUID id) {
        return ElnatPunktersattningEntity.builder()
            .antal(1)
            .id(id)
            .beskrivning("beskrivning")
            .typ(ElnatPunktersattningTypDto.KABELSKAP_JORDBRUKSIMPEDIMENT)
            .build();
    }

    public static ElnatPunktersattningEntity punktersattningEntity2(UUID id) {
        return ElnatPunktersattningEntity.builder()
            .antal(2)
            .id(id)
            .beskrivning("beskrivning 2")
            .typ(ElnatPunktersattningTypDto.NATSTATION_JORDBRUKSIMPEDIMENT_6X6M)
            .build();
    }

    public static ElnatMarkledningDto markledningDto() {
        return new ElnatMarkledningDto()
            .beskrivning("beskrivning")
            .bredd(1)
            .langd(2);
    }

    public static ElnatLedningSkogsmarkEntity ledningSkogsmarkEntity(UUID id) {
        return ElnatLedningSkogsmarkEntity.builder()
            .id(id)
            .beskrivning("beskrivning")
            .ersattning(1234)
            .build();
    }

    public static ElnatMarkledningEntity markledningEntity(UUID id) {
        return ElnatMarkledningEntity.builder()
            .beskrivning("beskrivning")
            .bredd(1)
            .langd(2)
            .id(id)
            .build();
    }

    public static ElnatMarkledningEntity markledningEntity2(UUID id) {
        return ElnatMarkledningEntity.builder()
            .beskrivning("en annan beskrivning")
            .bredd(13)
            .langd(37)
            .id(id)
            .build();
    }

    public static ElnatSsbSkogsmarkDto ssbSkogsmarkDto() {
        return new ElnatSsbSkogsmarkDto()
            .beskrivning("beskrivning")
            .bredd(1)
            .langd(2);
    }

    public static ElnatSsbSkogsmarkEntity ssbSkogsmarkEntity(UUID id) {
        return ElnatSsbSkogsmarkEntity.builder()
            .beskrivning("beskrivning")
            .bredd(1)
            .langd(2)
            .id(id)
            .build();
    }

    public static ElnatSsbSkogsmarkEntity ssbSkogsmarkEntity2(UUID id) {
        return ElnatSsbSkogsmarkEntity.builder()
            .beskrivning("ytterligare beskrivning")
            .bredd(90)
            .langd(3)
            .id(id)
            .build();
    }

    public static ElnatSsbVaganlaggningDto ssbVaganlaggningDto() {
        return new ElnatSsbVaganlaggningDto()
            .beskrivning("beskrivning")
            .langd(1)
            .zon(ElnatZonDto.ZON_1);
    }

    public static ElnatSsbVaganlaggningEntity ssbVaganlaggningEntity(UUID id) {
        return ElnatSsbVaganlaggningEntity.builder()
            .beskrivning("beskrivning")
            .langd(1)
            .zon(ElnatZonDto.ZON_1)
            .id(id)
            .build();
    }

    public static ElnatSsbVaganlaggningEntity ssbVaganlaggningEntity2(UUID id) {
        return ElnatSsbVaganlaggningEntity.builder()
            .beskrivning("O_o")
            .langd(9)
            .zon(ElnatZonDto.ZON_2)
            .id(id)
            .build();
    }
}
