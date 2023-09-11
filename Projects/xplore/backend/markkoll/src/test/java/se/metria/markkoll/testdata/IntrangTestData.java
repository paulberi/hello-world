package se.metria.markkoll.testdata;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.locationtech.jts.geom.*;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;

import static se.metria.markkoll.testdata.TestData.mockUUID;
import static se.metria.xplore.maputils.GeometryUtil.getPointGeometry;


public class IntrangTestData {
    public static OmradesintrangEntity markstrakEntity() {
        return OmradesintrangEntity.builder()
            .id(mockUUID(0))
            .fastighetomrade(fastighetsOmradeEntity())
            .geom(getLineStringGeometry())
            .type(IntrangstypDto.STRAK.toString())
            .subtype(IntrangsSubtypDto.MARKSTRAK.toString())
            .status(IntrangsStatusDto.NY.toString())
            .build();
    }

    public static OmradesintrangEntity lagspanningMarkledningOmradesintrangEntity() {
        return OmradesintrangEntity.builder()
                .id(mockUUID(0))
                .fastighetomrade(fastighetsOmradeEntity())
                .geom(getLineStringGeometry())
                .type(IntrangstypDto.LAGSPANNINGSLEDNING.toString())
                .subtype(IntrangsSubtypDto.MARKLEDNING.toString())
                .status(IntrangsStatusDto.NY.toString())
                .build();
    }

    public static OmradesintrangEntity lagspanningLuftledningOmradesintrangEntity() {
        return OmradesintrangEntity.builder()
                .id(mockUUID(0))
                .fastighetomrade(fastighetsOmradeEntity())
                .geom(getLineStringGeometry())
                .type(IntrangstypDto.LAGSPANNINGSLEDNING.toString())
                .subtype(IntrangsSubtypDto.LUFTLEDNING.toString())
                .status(IntrangsStatusDto.NY.toString())
                .build();
    }

    public static OmradesintrangEntity hogspanningMarkledningOmradesintrangEntity() {
        return OmradesintrangEntity.builder()
                .id(mockUUID(0))
                .fastighetomrade(fastighetsOmradeEntity())
                .geom(getLineStringGeometry())
                .type(IntrangstypDto.HOGSPANNINGSLEDNING.toString())
                .subtype(IntrangsSubtypDto.MARKLEDNING.toString())
                .status(IntrangsStatusDto.NY.toString())
                .build();
    }

    public static OmradesintrangEntity hogspanningLuftledningOmradesintrangEntity() {
        return OmradesintrangEntity.builder()
                .id(mockUUID(0))
                .fastighetomrade(fastighetsOmradeEntity())
                .geom(getLineStringGeometry())
                .type(IntrangstypDto.HOGSPANNINGSLEDNING.toString())
                .subtype(IntrangsSubtypDto.LUFTLEDNING.toString())
                .status(IntrangsStatusDto.NY.toString())
                .build();
    }

    public static OmradesintrangEntity kabelskapOmradesintrangEntity() {
        return OmradesintrangEntity.builder()
                .id(mockUUID(0))
                .fastighetomrade(fastighetsOmradeEntity())
                .geom(getPointGeometry(3, 4))
                .type(IntrangstypDto.KABELSKAP.toString())
                .subtype(IntrangsSubtypDto.NONE.toString())
                .status(IntrangsStatusDto.NY.toString())
                .build();
    }

    public static OmradesintrangEntity natstationOmradesintrangEntity() {
        return OmradesintrangEntity.builder()
                .id(mockUUID(0))
                .fastighetomrade(fastighetsOmradeEntity())
                .geom(getPointGeometry(3, 4))
                .type(IntrangstypDto.NATSTATION.toString())
                .subtype(IntrangsSubtypDto.NONE.toString())
                .status(IntrangsStatusDto.NY.toString())
                .build();
    }

    public static FastighetOmradeEntity fastighetsOmradeEntity() {
        return FastighetOmradeEntity.builder()
                .fastighetId(mockUUID(0))
                .omradeNr(1L)
                // .geom()  Fixa geometri
                .build();
    }

    public static Geometry getLineStringGeometry() {
        GeometryFactory gm = getGeometryFactory();

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("LineString");
        typeBuilder.add("geom", LineString.class);

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(typeBuilder.buildFeatureType());
        Coordinate[] coordinates = { new Coordinate(3, 4), new Coordinate(15, 16)};
        builder.add(gm.createLineString(coordinates));

        return (LineString)builder.buildFeature("pointFeature").getDefaultGeometry();
    }

    public static GeometryFactory getGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), 3006);
    }
}
