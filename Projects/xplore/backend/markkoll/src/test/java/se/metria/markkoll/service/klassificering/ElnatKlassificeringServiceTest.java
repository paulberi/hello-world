package se.metria.markkoll.service.klassificering;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import se.metria.markkoll.annotations.MarkkollServiceTest;
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
import static se.metria.markkoll.service.klassificering.ElnatKlassificeringService.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollServiceTest
@DisplayName("Givet ElnatKlassificeringServiceTest")
public class ElnatKlassificeringServiceTest {
    ElnatKlassificeringService elnatKlassificeringService;
    OmradesintrangRepository mockOmradesintrangRepository;
    VaghallareService mockVaghallareService;
    AvtalRepository mockAvtalRepository;
    ProjektRepository mockProjektRepository;


    @BeforeEach
    public void beforeEach() {
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockVaghallareService = mock(VaghallareService.class);

        elnatKlassificeringService = new ElnatKlassificeringService(mockOmradesintrangRepository,
            mockAvtalRepository, mockProjektRepository, mockVaghallareService);
    }

    @Test
    void klassificera() {
        // Given
        var avtalId = mockUUID(0);
        var versionId = mockUUID(1);

        var omradesintrang = Arrays.asList(
            omradesintrangEntity(LAGSPANNINGSLEDNING, MARKLEDNING, NY, INTRANG, 11, "L", null),
            omradesintrangEntity(LAGSPANNINGSLEDNING, MARKLEDNING, NY, INTRANG, 22, "I", 2.),

            omradesintrangEntity(LAGSPANNINGSLEDNING, LUFTLEDNING, NY, INTRANG, 33, "T", 10.),
            omradesintrangEntity(LAGSPANNINGSLEDNING, LUFTLEDNING, NY, INTRANG, 48, "T", 100.),

            omradesintrangEntity(LAGSPANNINGSLEDNING, OSAKERT_LAGE, NY, INTRANG, 57, "E", null),
            omradesintrangEntity(LAGSPANNINGSLEDNING, OSAKERT_LAGE, NY, INTRANG, 69, "R", 37.),

            omradesintrangEntity(LAGSPANNINGSLEDNING, INMATT_STRAK, NY, INTRANG, 57, "A", null),
            omradesintrangEntity(LAGSPANNINGSLEDNING, INMATT_STRAK, NY, INTRANG, 69, "!", 32.),

            omradesintrangEntity(KABELSKAP, NONE, NY, INTRANG, 0., "!", null),
            omradesintrangEntity(KABELSKAP, NONE, NY, INTRANG, 0., "!", 41.),

            omradesintrangEntity(NATSTATION, NONE, NY, INTRANG, 0., null, 0.001),
            omradesintrangEntity(NATSTATION, NONE, NY, INTRANG, 0., null, 97.),

            omradesintrangEntity(TEKNIKBOD, NONE, NY, INTRANG, 0., null, null),
            omradesintrangEntity(TEKNIKBOD, NONE, NY, INTRANG, 0., null, 58.),

            omradesintrangEntity(MARKSKAP, MARKLEDNING, BEVARAS, INTRANG, 4., "!!", 71.),
            omradesintrangEntity(MARKSKAP, MARKLEDNING, BEVARAS, SERVIS, 20., "??", null),
            omradesintrangEntity(MARKSKAP, MARKLEDNING, BEVARAS, REV, 6., "!?", 5.)
        );

        when(mockVaghallareService.vaghallareAvtalstyperInVp(any())).thenReturn(Arrays.asList(REV));

        when(mockOmradesintrangRepository.findByAvtalIdAndAvtalstyper(eq(avtalId), eq(versionId), eq(Arrays.asList(INTRANG, REV))))
            .thenReturn(omradesintrang);

        // When
        var klassificering = elnatKlassificeringService.klassificera(avtalId, versionId);

        // Then
        var klassificeringExpect = elnatKlassificering();

        assertEquals(klassificeringExpect, klassificering);
    }

    @Test
    public void så_ska_vp_kunna_skapas_utifrån_en_klassificering() {
        // Given
        var klassificering = ElnatKlassificeringDto.builder()
            .langdMarkledning(6.)
            .langdMarkstrak(8.)
            .antalKabelskap(1)
            .antalNatstationer(2)
            .hogstaSpanningsniva(42.01)
            .build();

        var tidpunkt = LocalDateTime.of(1, 2, 3, 4, 5);
        var vpDto = new ElnatVarderingsprotokollDto()
            .prisomrade(ElnatPrisomradeDto.NORRLANDS_INLAND)
            .metadata(
                new ElnatVarderingsprotokollMetadataDto()
                    .varderingstidpunkt(tidpunkt)
                    .spanningsniva("42,01kV")
            )
            .markledning(Arrays.asList(
                new ElnatMarkledningDto()
                    .langd(6)
                    .bredd(MARKLEDNING_STANDARD_BREDD)
                    .beskrivning(MARKLEDNING_FRITEXT),
                new ElnatMarkledningDto()
                    .langd(8)
                    .bredd(MARKLEDNING_STANDARD_BREDD)
                    .beskrivning(STRAK_FRITEXT)
            ))
            .punktersattning(Arrays.asList(
                new ElnatPunktersattningDto()
                    .antal(1)
                    .beskrivning(KABELSKAP_FRITEXT)
                    .typ(ElnatPunktersattningTypDto.KABELSKAP_EJ_KLASSIFICERAD),
                new ElnatPunktersattningDto()
                    .antal(2)
                    .beskrivning(NATSTATION_FRITEXT)
                    .typ(ElnatPunktersattningTypDto.NATSTATION_EJ_KLASSIFICERAD)
            ));

        // When
        var vpActual = elnatKlassificeringService.getKlassificeratVarderingsprotokoll(klassificering,
            tidpunkt);

        // Then
        assertEquals(vpDto, vpActual);
    }

    private ElnatKlassificeringDto elnatKlassificering() {
        var klassificering = new ElnatKlassificeringDto();
        klassificering.setAntalKabelskap(2);
        klassificering.setAntalNatstationer(2);
        klassificering.setLangdMarkledning(33.);
        klassificering.setLangdLuftledning(81.);
        klassificering.setLangdMarkstrak(252.);
        klassificering.setLittera("L I T T E R A ! ! !");
        klassificering.setHogstaSpanningsniva(100.);
        return klassificering;
    }

    private OmradesintrangEntity
    omradesintrangEntity(IntrangstypDto intrangstyp,
                         IntrangsSubtypDto subtype,
                         IntrangsStatusDto status,
                         AvtalstypDto avtalstyp,
                         double geomLength,
                         String littera,
                         Double spanningsniva) {

        var geom = mock(Geometry.class);
        when(geom.getLength()).thenReturn(geomLength);

        var omradesintrang = new OmradesintrangEntity();
        omradesintrang.setType(intrangstyp.toString());
        omradesintrang.setSubtype(subtype.toString());
        omradesintrang.setStatus(status.toString());
        omradesintrang.setAvtalstyp(avtalstyp);
        omradesintrang.setGeom(geom);
        omradesintrang.setLittera(littera);
        omradesintrang.setSpanningsniva(spanningsniva);

        return omradesintrang;
    }
}
