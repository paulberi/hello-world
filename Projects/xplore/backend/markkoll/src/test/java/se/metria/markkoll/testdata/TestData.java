package se.metria.markkoll.testdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.intrang.IntrangEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.fsokws.Fastighet;
import se.metria.markkoll.fsokws.Registerenhet;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalsjobbProgress;
import se.metria.xplore.maputils.GeometryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.util.CollectionUtil.asSet;

@JsonIgnoreProperties(value = { "file" })
public class TestData {

    public static MarkagareDto mockAgare(UUID id) {
        return new MarkagareDto()
                .andel("1/2")
                .agareStatus(AvtalsstatusDto.AVTAL_JUSTERAS)
                .namn("Jenny Odeblom")
                .id(id)
                .kontaktperson(true)
                .labels(new AvtalspartLabelsDto())
                .ePost("jenny@metria.se")
                .postort("Sundsvall")
                .adress("Metriagatan")
                .bankkonto("123456")
                .telefon("070-123456")
                .fodelsedatumEllerOrgnummer("")
                .agartyp(AgartypDto.LF)
                .inkluderaIAvtal(true)
                .postnummer("12345");
    }
    public static MarkagareInfoDto mockAgareInfo() {
        return new MarkagareInfoDto()
                .andel("")
                .agareStatus(AvtalsstatusDto.EJ_BEHANDLAT)
                .namn("Jenny Odeblom")
                .kontaktperson(false)
                .ePost("jenny@metria.se")
                .postort("Sundsvall")
                .adress("Metriagatan")
                .telefon("070-123456")
                .fodelsedatumEllerOrgnummer("20220101-1122")
                .agartyp(AgartypDto.OMBUD)
                .inkluderaIAvtal(true)
                .postnummer("12345");
    }

    public static MarkagareDto agareExpect(MarkagareInfoDto markagareInfoDto){
        return new MarkagareDto()
                .andel(markagareInfoDto.getAndel())
                .agareStatus(markagareInfoDto.getAgareStatus())
                .namn(markagareInfoDto.getNamn())
                .kontaktperson(markagareInfoDto.getKontaktperson())
                .ePost(markagareInfoDto.getePost())
                .postort(markagareInfoDto.getPostort())
                .adress(markagareInfoDto.getAdress())
                .bankkonto(markagareInfoDto.getBankkonto())
                .labels(new AvtalspartLabelsDto().ofullstandingInformation(true))
                .telefon(markagareInfoDto.getTelefon())
                // Vi förväntar att de fyra sista sifforna är borttagna
                .fodelsedatumEllerOrgnummer(markagareInfoDto.getFodelsedatumEllerOrgnummer()
                        .substring(0, Math.max(0, markagareInfoDto.getFodelsedatumEllerOrgnummer().length() - 5)))
                .agartyp(markagareInfoDto.getAgartyp())
                .inkluderaIAvtal(markagareInfoDto.getInkluderaIAvtal())
                .postnummer(markagareInfoDto.getPostnummer());
    }

    public static MarkagareDto mockAgareOrganisation(UUID id) {
        return new MarkagareDto()
                .andel("1/2")
                .agareStatus(AvtalsstatusDto.AVTAL_JUSTERAS)
                .namn("Jenny Odeblom")
                .id(id)
                .kontaktperson(true)
                .ePost("jenny@metria.se")
                .postort("Sundsvall")
                .adress("Metriagatan")
                .bankkonto("123456")
                .telefon("070-123456")
                .fodelsedatumEllerOrgnummer("456-789")
                .agartyp(AgartypDto.LF)
                .postnummer("12345");
    }

    public static ProjektInfoDto mockProjektInfoDto() {
        Resource file = new ClassPathResource("testData/oneco-shapetest-1.zip");
        MockMultipartFile importFile = null;
        try {
            importFile = new MockMultipartFile("file", "shapefile.zip", "application/octet-stream", file.getInputStream().readAllBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mockProjektDto();
    }

    public static ProjektInfoDto mockProjektDto() {
        return new ProjektInfoDto()
                .id(mockUUID(0))
                .namn("projekt")
                .ort("Öjebyn")
                .projektTyp(ProjektTypDto.FIBER)
                .beskrivning("Kort beskrivning");
    }

    public static FiberProjektDto mockFiberProjektDto() {
        var projektInfo = mockProjektDto();
        var fiberInfo = new FiberInfoDto()
            .bestallare("Beställar-Bosse")
            .bidragsprojekt(true)
            .ledningsstracka("Här och där")
            .ledningsagare("Lednings-Lotta")
            .bidragsprojekt(true);

        return new FiberProjektDto()
                .projektInfo(projektInfo)
                .fiberInfo(fiberInfo);
    }

    public static IntrangEntity mockIntrangEntity() {

        SimpleFeatureType TYPE = null;
        try {
            TYPE = DataUtilities.createType(
                    "Location",
                    "the_geom:Point:srid=3006,"
                            + // <- the geometry attribute: Point type
                            "name:String,"
                            + // <- a String attribute
                            "number:Integer" // a number attribute
            );
        } catch (SchemaException e) {
            e.printStackTrace();
        }

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        Point point = geometryFactory.createPoint(new Coordinate(11., 12.));
        featureBuilder.add(point);
        SimpleFeature feature = featureBuilder.buildFeature(null);
        var intrangEntity = new IntrangEntity();
        return intrangEntity
                .builder()
                .geom(GeometryUtil.getFeatureGeometry(feature))
                .type(IntrangstypDto.BRUNN.toString())
                .subtype(IntrangsSubtypDto.NONE.toString())
                .build();
    }

    public static Geometry geometry() {
        SimpleFeatureType TYPE = null;
        try {
            TYPE = DataUtilities.createType(
                    "Location",
                    "the_geom:Point:srid=3006,"
                            + // <- the geometry attribute: Point type
                            "name:String,"
                            + // <- a String attribute
                            "number:Integer" // a number attribute
            );
        } catch (SchemaException e) {
            e.printStackTrace();
        }
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
        Point point = geometryFactory.createPoint(new Coordinate(11., 12.));
        featureBuilder.add(point);
        SimpleFeature feature = featureBuilder.buildFeature(null);

        return GeometryUtil.getFeatureGeometry(feature);
    }

    public static ProjektPageDto mockProjektPage() {
        var projektList = new ArrayList<ProjektInfoDto>();
        projektList.add(mockProjektDto());
        projektList.add(mockProjektDto());
        projektList.add(mockProjektDto());

        var pageDto = new ProjektPageDto();
        pageDto.setContent(projektList);
        pageDto.setNumberOfElements(3);
        pageDto.setNumber(0);
        pageDto.setTotalElements(3);
        pageDto.setTotalPages(1);

        return pageDto;
    }

    public static FastighetPageDto mockFastighetPage() {
        var fastighetPageDto = new FastighetPageDto();
        fastighetPageDto.setNumber(2);
        fastighetPageDto.setTotalElements(125);
        fastighetPageDto.setTotalPages(13);
        List<FastighetDto> content = IntStream
                .range(1, 7)
                .mapToObj(i -> new FastighetDto().fastighetsbeteckning("Långträsk 2:" + i))
                .collect(Collectors.toList());
        fastighetPageDto.setContent(content);

        return fastighetPageDto;
    }

    public static Page<FastighetDto> mockFastighetPageEntity() {
        List<FastighetDto> entities = IntStream
                .range(1, 7)
                .mapToObj(i -> new FastighetDto().fastighetsbeteckning("Långträsk 2:" + i))
                .collect(Collectors.toList());

        return new PageImpl(entities);
    }

    public static FastighetDto mockFastighetDto(){
        var fastighetDto = new FastighetDto()
                .fastighetsbeteckning("Bägaren 4>1")
                .id(UUID.randomUUID());
        return fastighetDto;

    }

    public static AvtalsjobbEntity avtalsjobbEntity() {
        return AvtalsjobbEntity.builder()
                .generated(3)
                .total(13)
                .path("path")
                .status(AvtalsjobbStatusDto.IN_PROGRESS)
                .build();
    }

    public static String mockGeoJson(){
        return "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"Fastighetsytor_FK.4069080\",\"geometry\":{\"type\":\"MultiPolygon\",\"coordinates\":[[[[679072.37099691,7168246.15797042],[679066.49799691,7168246.96597043],[679019.64699692,7168253.48497049],[678975.97499692,7168259.48197055],[678919.61999693,7168267.32597063],[678873.48499694,7168273.68297069],[678835.32399695,7168278.91697074],[678773.80199696,7168287.44997082],[678716.36299696,7168294.53397089],[678679.37699697,7168298.11397094],[678647.95999698,7168301.26497098],[678563.76899699,7168309.56197109],[678492.532997,7168316.56197119],[678423.79099701,7168323.32397127],[678396.91499701,7168326.05497131],[678316.60099703,7168333.94497141],[678272.09499703,7168338.37197147],[678216.60799704,7168343.83997154],[678158.21399705,7168349.60597162],[678059.30799707,7168359.36397174],[677996.47599708,7168365.48997182],[677939.24299708,7168371.0529719],[677928.40699709,7168384.54297191],[677914.88199709,7168401.38097193],[677926.33399709,7168417.19997191],[678018.45599707,7168408.31097179],[678111.39499706,7168504.29297167],[678103.98699706,7168682.88797166],[678392.82999701,7168901.11197127],[678138.67599705,7169259.41097158],[678079.66499705,7169342.60397165],[678007.34899706,7169444.55697173],[678288.55199702,7169648.51997135],[678369.49999701,7169707.22897124],[678419.190997,7169734.06897118],[678493.27299699,7169784.21197108],[678528.38399698,7169809.44397103],[679034.8859969,7170181.46797033],[679540.68799682,7169797.52596967],[679470.31699683,7169742.97996977],[678631.30999697,7169121.71997094],[678503.08599699,7169026.73397112],[679072.37099691,7168246.15797042]]]]},\"geometry_name\":\"geom\",\"properties\":{\"FNR_FDS\":\"240086042\",\"EXTERNID\":\"2481>FURUVIK>1:2>2\",\"DETALJTYP\":\"FASTIGHET\",\"KOMMUNKOD\":\"2481\",\"KOMMUNNAMN\":\"LYCKSELE\",\"TRAKT\":\"FURUVIK\",\"BLOCKENHET\":\"1:2\",\"OMRNR\":2,\"FASTIGHET\":\"FURUVIK 1:2>2\",\"YTKVAL\":1,\"ADAT\":\"2012-03-28 13:45\",\"OMRTYP\":\" \",\"OBJEKT_ID\":\"909a6a86-2425-90ec-e040-ed8f66444c3f\"},\"bbox\":[677914.88199709,7168246.15797042,679540.68799682,7170181.46797033]}],\"totalFeatures\":1,\"numberMatched\":1,\"numberReturned\":1,\"timeStamp\":\"2021-02-23T12:08:34.601Z\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:EPSG::3006\"}},\"bbox\":[677914.88199709,7168246.15797042,679540.68799682,7170181.46797033]}";
    }

    public static PersonEntity personEntity() {
        return PersonEntity.builder()
                .adress("adress")
                .bankkonto("bankkonto")
                .namn("förnamn efternamn")
                .ePost("ePost")
                .personnummer("860304-8979")
                .build();
    }

    public static MarkagareEntity markagareEntity() {
        var person = personEntity();

        var markagare = MarkagareEntity.builder()
                .person(personEntity())
                .agartyp(AgartypDto.LF)
                .andel("1/2")
                .build();

        person.addMarkagare(markagare);

        return markagare;
    }

    public static AvtalspartEntity avtalspartEntity() {
        return AvtalspartEntity.builder()
                .avtalsstatus(AvtalsstatusDto.AVTAL_JUSTERAS)
                .inkluderaIAvtal(true)
                .signatar(true)
                .markagare(markagareEntity())
                .build();
    }

    public static FastighetEntity fastighetEntity() {
        var markagare = markagareEntity();

        var fastighet = FastighetEntity.builder()
                .id(mockUUID(0))
                .fastighetsbeteckning("fastighetsbeteckning")
                .markagare(asSet(markagare))
                .detaljtyp(DetaljtypDto.FASTIGHET)
                .externid("externId")
                .build();

        markagare.setFastighet(fastighet);

        return fastighet;
    }

    public static ImportVersionEntity versionEntity() {
        var version = ImportVersionEntity.builder()
                .filnamn("filnamn")
                .skapadDatum(localDateTime())
                .build();

        return version;
    }

    public static AvtalEntity avtalEntity() {
        var avtalspart = avtalspartEntity();
        var fastighet = fastighetEntity();
        fastighet.setMarkagare(asSet(avtalspart.getMarkagare()));
        avtalspart.getMarkagare().setFastighet(fastighet);

        var avtal = AvtalEntity.builder()
                .ersattning(1234)
                .anteckning("Anteckning")
                .fastighet(fastighet)
                .avtalsparter(asSet(avtalspart))
                .kontaktperson(avtalspart)
                .build();

        avtalspart.setAvtal(avtal);

        return avtal;
    }

    public static ProjektEntity projektEntity() {
        var version = versionEntity();
        var avtal = avtalEntity();

        var projekt = ProjektEntity.builder()
                .beskrivning("beskrivning")
// TODO: Mocka upp för nya strukturen
//                .ledningsagare("Ledningsägare")
                .namn("namn")
                .ort("ort")
                .projekttyp(ProjektTypDto.FIBER)
                .startDatum(localDateTime())
                .build();

        avtal.setProjekt(projekt);
        projekt.setAvtal(asSet(avtal));

        version.setProjekt(projekt);
        projekt.setVersioner(asSet(version));
        projekt.setCurrentVersion(version);
        return projekt;
    }

    public static Resource resource() {
        return new ByteArrayResource(ByteBuffer.allocate(4).putInt(0xC0FFEE).array());
    }

    public static AvtalsjobbProgress avtalsjobbProgress(Integer total, Integer generated, AvtalsjobbStatusDto status) {
        var progress = mock(AvtalsjobbProgress.class);
        when(progress.getTotal()).thenReturn(total);
        when(progress.getGenerated()).thenReturn(generated);
        when(progress.getStatus()).thenReturn(status);

        return progress;
    }

    public static VersionDto versionDto() {
        return new VersionDto()
                .id(mockUUID(0))
                .filnamn("filnamn")
                .skapadDatum(localDateTime());
    }

    public static LocalDateTime localDateTime() {
        return LocalDateTime.of(1986,3,4,5,6);
    }

    public static UUID mockUUID(Integer index) {
        if (index >= 0 && index <= 9) {
            return UUID.fromString("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx".replace("x", index.toString()));
        } else if (index <= 15) {
            char charVal = (char)(87 + index);
            return UUID.fromString("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx".replace('x', charVal));
        } else {
            // Vi håller tummarna för att man aldrig kommer behöva mer än 16 IDs...
            throw new IllegalArgumentException("För stort index: " + index);
        }
    }

    public static ArrayList<Registerenhet> samfFastighetLista() {
        var result = new ArrayList<Registerenhet>();
        var fast1 = new Fastighet();
        fast1.getGallandeBeteckning().setBlocknummer("1");
        fast1.getGallandeBeteckning().getTrakt().setTraktnamn("abc");
        fast1.getGallandeBeteckning().setTecken(":");
        fast1.getGallandeBeteckning().setEnhetsnummer(1);
        fast1.setUUID(UUID.randomUUID().toString());
        var fast2 = new Fastighet();
        fast2.getGallandeBeteckning().setBlocknummer("2");
        fast2.getGallandeBeteckning().getTrakt().setTraktnamn("abc");
        fast2.getGallandeBeteckning().setTecken(":");
        fast2.getGallandeBeteckning().setEnhetsnummer(2);
        fast2.setUUID(UUID.randomUUID().toString());

        result.add(fast1);
        result.add(fast2);
        return result;
    }
}
