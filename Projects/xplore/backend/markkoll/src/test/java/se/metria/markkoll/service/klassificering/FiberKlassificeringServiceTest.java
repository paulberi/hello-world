package se.metria.markkoll.service.klassificering;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.VaghallareService;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.openapi.model.AvtalstypDto.*;
import static se.metria.markkoll.openapi.model.IntrangsStatusDto.BEVARAS;
import static se.metria.markkoll.openapi.model.IntrangsStatusDto.NY;
import static se.metria.markkoll.openapi.model.IntrangsSubtypDto.*;
import static se.metria.markkoll.openapi.model.IntrangstypDto.*;
import static se.metria.markkoll.service.klassificering.FiberKlassificeringService.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.markkoll.util.AccountUtil.MARKKOLL_SYSTEM_USER_FRIENDLY_NAME;

class FiberKlassificeringServiceTest {
    FiberKlassificeringService fiberKlassificeringService;
    OmradesintrangRepository mockOmradesintrangRepository;
    AvtalRepository mockAvtalRepository;
    ProjektRepository mockProjektRepository;
    VaghallareService mockVaghallareService;

    @BeforeEach
    void beforeEach() {
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockVaghallareService = mock(VaghallareService.class);

        fiberKlassificeringService = new FiberKlassificeringService(mockOmradesintrangRepository, mockAvtalRepository,
            mockProjektRepository, mockVaghallareService);
    }

    @Test
    void klassificera() {
        // Given
        var avtalId = mockUUID(0);
        var versionId = mockUUID(1);
        
        var omradesintrang = Arrays.asList(
            omradesintrangEntity(LAGSPANNINGSLEDNING, MARKSTRAK, NY, INTRANG, 1.1, "L"),
            omradesintrangEntity(LAGSPANNINGSLEDNING, MARKSTRAK, NY, INTRANG, 2.2, "I"),

            omradesintrangEntity(LAGSPANNINGSLEDNING, LUFTSTRAK, NY, INTRANG, 3.3, "T"),
            omradesintrangEntity(LAGSPANNINGSLEDNING, LUFTSTRAK, NY, INTRANG, 4.8, "T"),

            omradesintrangEntity(MARKSKAP, NONE, NY, INTRANG, 0., "E"),
            omradesintrangEntity(MARKSKAP, NONE, NY, INTRANG, 0., "R"),

            omradesintrangEntity(BRUNN, NONE, NY, INTRANG, 0., "A"),
            omradesintrangEntity(BRUNN, NONE, NY, INTRANG, 0., null),

            omradesintrangEntity(TEKNIKBOD, NONE, NY, INTRANG, 0., null),
            omradesintrangEntity(TEKNIKBOD, NONE, NY, INTRANG, 0., null),

            omradesintrangEntity(MARKSKAP, MARKLEDNING, BEVARAS, INTRANG, 4., "!!"),
            omradesintrangEntity(MARKSKAP, MARKLEDNING, BEVARAS, SERVIS, 20., "??"),
            omradesintrangEntity(MARKSKAP, MARKLEDNING, BEVARAS, REV, 6., "!?")
        );

        when(mockVaghallareService.vaghallareAvtalstyperInVp(any())).thenReturn(Arrays.asList(REV));

        when(mockOmradesintrangRepository.findByAvtalIdAndAvtalstyper(eq(avtalId), eq(versionId), eq(Arrays.asList(INTRANG, REV))))
            .thenReturn(omradesintrang);

        // When
        var klassificering = fiberKlassificeringService.klassificera(avtalId, versionId);

        // Then
        var klassificeringExpect = fiberKlassificering();

        assertEquals(klassificeringExpect, klassificering);
    }

    @Test
    void getKlassificeratVp() {
        // Given
        var klassificering = fiberKlassificering();

        var avtalId = mockUUID(0);
        var varderingstidpunkt = LocalDateTime.of(1234, 12, 12, 1, 2);

        var vp = new FiberVarderingsprotokollDto()
            .markledning(Arrays.asList(
                new FiberMarkledningDto()
                    .langd(klassificering.getLangdMarkledning())
                    .bredd(MARKLEDNING_STANDARD_BREDD)
                    .beskrivning(MARKLEDNING_FRITEXT)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME)
            ))
            .punktersattning(Arrays.asList(
                new FiberPunktersattningDto()
                    .antal(klassificering.getAntalMarkskap())
                    .beskrivning(MARKSKAP_FRITEXT)
                    .typ(FiberPunktersattningTypDto.MARKSKAP_EJ_KLASSIFICERAD)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME),
                new FiberPunktersattningDto()
                    .antal(klassificering.getAntalOptobrunn())
                    .beskrivning(OPTOBRUNN_FRITEXT)
                    .typ(FiberPunktersattningTypDto.OPTOBRUNN_EJ_KLASSIFICERAD)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME),
                new FiberPunktersattningDto()
                    .antal(klassificering.getAntalTeknikbod())
                    .beskrivning(TEKNIKBOD_FRITEXT)
                    .typ(FiberPunktersattningTypDto.SITE_EJ_KLASSIFICERAD)
                    .skapadAv(MARKKOLL_SYSTEM_USER_FRIENDLY_NAME)
            ))
            .metadata(new FiberVarderingsprotokollMetadataDto()
                .varderingsmanOchForetag("")
                .varderingstidpunkt(varderingstidpunkt));

        // When
        var vpActual = fiberKlassificeringService
            .getKlassificeratVarderingsprotokoll(klassificering, varderingstidpunkt);

        // Then
        assertEquals(vp, vpActual);
    }

    private OmradesintrangEntity
    omradesintrangEntity(IntrangstypDto intrangstyp,
                         IntrangsSubtypDto subtype,
                         IntrangsStatusDto status,
                         AvtalstypDto avtalstyp,
                         double geomLength,
                         String littera) {

        var geom = mock(Geometry.class);
        when(geom.getLength()).thenReturn(geomLength);

        var omradesintrang = new OmradesintrangEntity();
        omradesintrang.setType(intrangstyp.toString());
        omradesintrang.setSubtype(subtype.toString());
        omradesintrang.setStatus(status.toString());
        omradesintrang.setAvtalstyp(avtalstyp);
        omradesintrang.setGeom(geom);
        omradesintrang.setLittera(littera);

        return omradesintrang;
    }

    private FiberKlassificeringDto fiberKlassificering() {
        var klassificering = new FiberKlassificeringDto();
        klassificering.setAntalMarkskap(2);
        klassificering.setAntalOptobrunn(2);
        klassificering.setAntalTeknikbod(2);
        klassificering.setLangdMarkledning(4.);
        klassificering.setLangdLuftledning(9.);
        klassificering.setLittera("L I T T E R A");
        return klassificering;
    }
}