package se.metria.markkoll.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.xplore.maputils.GeometryUtil;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {
    @Mock
    AclService mockAclService;

    @Mock
    VersionRepository mockVersionRepository;

    @Mock
    ProjektRepository mockProjektRepository;

    @InjectMocks
    VersionService versionService;

    @Test
    void så_ska_det_gå_att_skapa_en_ny_importversion() {
        // Given
        var projektId = mockUUID(0);

        var versionDto = versionDto();
        var intrangDtos = intrangDtos();

        var projektEntity = new ProjektEntity();
        when(mockProjektRepository.findById(eq(projektId))).thenReturn(Optional.of(projektEntity));

        // When
        versionService.create(projektId, versionDto, intrangDtos);

        // Then
        var versionEntity = versionEntity(projektEntity, false);

        verify(mockVersionRepository).save(eq(versionEntity));
    }

    private VersionDto versionDto() {
        var versionDto = new VersionDto();
        versionDto.setBuffert(10.);
        versionDto.setFilnamn("filnamn");
        versionDto.setSkapadDatum(LocalDateTime.of(1986, 3 ,4, 1, 2));
        return versionDto;
    }

    public Collection<ProjektIntrangDto> intrangDtos() {
        return Arrays.asList(
            intrangDto(
                mockUUID(0),
                AvtalstypDto.SERVIS,
                GeometryUtil.createPoint(1., 2.),
                0.5,
                IntrangsStatusDto.BEVARAS,
                IntrangstypDto.BRUNN,
                IntrangsSubtypDto.NONE
            ),
            intrangDto(
                mockUUID(1),
                AvtalstypDto.INTRANG,
                GeometryUtil.createPoint(3., 4.),
                null,
                IntrangsStatusDto.NY,
                IntrangstypDto.STRAK,
                IntrangsSubtypDto.MARKSTRAK
            )
        );
    }

    private ImportVersionEntity versionEntity(ProjektEntity projektEntity, boolean hasIntrangIds) {
        var entity = new ImportVersionEntity();
        entity.setSkapadDatum(LocalDateTime.of(1986, 3 ,4, 1, 2));
        entity.setProjekt(projektEntity);
        entity.setBuffert(10.);
        entity.setFilnamn("filnamn");
        entity.setIntrang(intrangEntities(entity, hasIntrangIds));

        return entity;
    }

    private Set<IntrangEntity> intrangEntities(ImportVersionEntity versionEntity, boolean hasId) {
        return CollectionUtil.asSet(
            IntrangEntity.builder()
                .id(hasId ? mockUUID(0) : null)
                .avtalstyp(AvtalstypDto.SERVIS)
                .geom(GeometryUtil.createPoint(1., 2.))
                .spanningsniva(0.5)
                .status(IntrangsStatusDto.BEVARAS.toString())
                .type(IntrangstypDto.BRUNN.toString())
                .subtype(IntrangsSubtypDto.NONE.toString())
                .version(versionEntity)
                .build(),
            IntrangEntity.builder()
                .id(hasId ? mockUUID(1) : null)
                .avtalstyp(AvtalstypDto.INTRANG)
                .geom(GeometryUtil.createPoint(3., 4.))
                .spanningsniva(null)
                .status(IntrangsStatusDto.NY.toString())
                .type(IntrangstypDto.STRAK.toString())
                .subtype(IntrangsSubtypDto.MARKSTRAK.toString())
                .version(versionEntity)
                .build()
        );
    }
    private ProjektIntrangDto
    intrangDto(UUID id,
               AvtalstypDto avtalstyp,
               Geometry geom,
               Double spanningsniva,
               IntrangsStatusDto status,
               IntrangstypDto type,
               IntrangsSubtypDto subtype) {

        var intrang = new ProjektIntrangDto();
        intrang.setId(id);
        intrang.setAvtalstyp(avtalstyp);
        intrang.setGeom(new GeoJsonWriter().write(geom));
        intrang.setSpanningsniva(spanningsniva);
        intrang.setStatus(status);
        intrang.setType(type);
        intrang.setSubtype(subtype);
        return intrang;
    }
}