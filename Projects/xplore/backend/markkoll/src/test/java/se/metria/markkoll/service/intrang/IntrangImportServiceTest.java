package se.metria.markkoll.service.intrang;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.AvtalstypEvaluatorService;
import se.metria.markkoll.service.VersionService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.indata.IndataService;
import se.metria.markkoll.service.indata.KallfilService;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.ConstantAvtalstypEvaluator;
import se.metria.xplore.maputils.GeometryUtil;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class IntrangImportServiceTest {
    @InjectMocks
    IntrangImportService intrangImportService;

    @Mock
    AvtalstypEvaluatorService avtalstypEvaluatorService;

    @Mock
    IndataService indataService;

    @Spy
    IntrangReader intrangReader;

    @Mock
    KallfilService kallfilService;

    @Mock
    ProjektRepository projektRepository;

    @Mock
    UserService userService;

    @Mock
    VersionService versionService;

    LocalDate date = LocalDate.of(2022, 8, 15);

    @Spy
    Clock clock = Clock.fixed(date.atStartOfDay(ZoneId.systemDefault()).toInstant(),ZoneId.systemDefault());

    @Test
    void så_ska_det_gå_att_importera_intrångsobjekt() {
        // Given
        var projektId = mockUUID(0);
        var intrang = Arrays.asList(
            new ProjektIntrangDto().avtalstyp(AvtalstypDto.SERVIS),
            new ProjektIntrangDto().avtalstyp(AvtalstypDto.INTRANG)
        );
        var version = new VersionDto()
            .filnamn("christoffer_karlsson_2022-08-15_00_00.zip")
            .buffert(0.)
            .skapadDatum(LocalDateTime.now(clock));

        when(userService.getCurrentUser()).thenReturn(new MarkkollUserDto().fornamn("Christoffer").efternamn("Karlsson"));

        // When
        var v = intrangImportService.importIntrang(projektId, intrang);

        // Then
        verify(versionService).create(eq(projektId), eq(version), eq(intrang));
    }

    @Test
    void så_ska_det_gå_att_importera_en_shapefil() throws IOException {
        // Given
        var projektId = mockUUID(0);
        var shapeFile = new ClassPathResource("shape/shape.zip");
        var indataTyp = IndataTypDto.DPCOM;
        var kundId = "kundId";
        var buffert = 1.0;
        var kallfilId = mockUUID(0);

        when(projektRepository.getKundId(eq(projektId))).thenReturn(kundId);
        when(kallfilService.create(eq(shapeFile), eq(indataTyp), eq(kundId))).thenReturn(kallfilId);
        when(avtalstypEvaluatorService.getAvtalstypEvaluator(eq(kundId), eq(indataTyp)))
            .thenReturn(new ConstantAvtalstypEvaluator(AvtalstypDto.INTRANG));

        // When
        intrangImportService.importShape(projektId, shapeFile, indataTyp, 1.);

        // Then
        var version = new VersionDto()
            .filnamn(shapeFile.getFilename())
            .buffert(buffert)
            .skapadDatum(LocalDateTime.now(clock));

        var intrang = Arrays.asList(
            new ProjektIntrangDto()
                .avtalstyp(AvtalstypDto.INTRANG)
                .status(IntrangsStatusDto.NY)
                .type(IntrangstypDto.MARKSKAP)
                .subtype(IntrangsSubtypDto.NONE)
                .geom(GeometryUtil.toGeoJSON(GeometryUtil.createPoint(694909.89561475,6645522.87819333)))
        );

        verify(kallfilService).create(eq(shapeFile), eq(indataTyp), eq(kundId));
        verify(indataService).update(eq(projektId), eq(kallfilId), any());
        verify(versionService).create(eq(projektId), eq(version), eq(intrang));
    }
}