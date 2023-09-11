package se.metria.markkoll.service;

import org.geotools.geojson.geom.GeometryJSON;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.locationtech.jts.geom.Geometry;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.markkoll.annotations.MarkkollServiceTest;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.exception.MarkkollError;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.matchers.PageRequestMatcher;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.IntrangRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.markkoll.security.MkPermission;
import se.metria.markkoll.service.admin.AclService;
import se.metria.markkoll.service.admin.AdminService;
import se.metria.markkoll.service.admin.PermissionService;
import se.metria.markkoll.service.admin.UserService;
import se.metria.markkoll.service.avtal.AvtalService;
import se.metria.markkoll.service.fastighet.FastighetService;
import se.metria.markkoll.service.fastighet.RegisterenhetImportService;
import se.metria.markkoll.service.fastighet.SamfService;
import se.metria.markkoll.service.intrang.IntrangService;
import se.metria.markkoll.service.logging.AvtalsloggService;
import se.metria.markkoll.service.logging.LoggService;
import se.metria.markkoll.service.projekt.ProjektService;
import se.metria.markkoll.testdata.TestData;
import se.metria.markkoll.util.CollectionUtil;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.*;
import static se.metria.markkoll.util.CollectionUtil.asSet;

@SuppressWarnings("NonAsciiCharacters") // Krockar med namngivningsmönstret med underscores
@MarkkollServiceTest
@DisplayName("Givet ProjektService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
public class ProjektServiceTest {
    static final String basePath = "src/test/resources/testData/versionDiff/";

    EntityManager mockEntityManager;
    ProjektService projektService;

    AclService mockAclService;
    FastighetService mockFastighetService;
    FastighetsforteckningService mockFastighetsforteckningService;
    SamfService mockSamfService;
    RegisterenhetImportService mockRegisterenhetImportService;
    ProjektRepository mockProjektRepository;
    VersionRepository mockVersionRepository;
    AvtalService mockAvtalService;
    InfobrevService mockInfobrevService;
    AvtalRepository mockAvtalRepository;
    AvtalsloggService mockAvtalsloggService;
    IntrangRepository mockIntrangRepository;
    OmradesintrangRepository mockOmradesintrangRepository;
    FastighetsintrangRepository mockFastighetsintrangRepository;
    KundConfigService mockKundConfigService;
    IntrangService mockIntrangService;
    LoggService mockLoggService;
    FastighetRepository mockFastighetRepository;
    UserService mockUserService;
    AdminService mockAdminService;
    PermissionService mockPermissionService;

    private final int pageNum = 1;
    private final int size = 3;
    private final String sortProperty = "namn";
    private final Sort.Direction sortDirection = Sort.Direction.ASC;
    private final String status = "Pågående";
    private final String name = "Projekt 456";
    private final List<UUID> projektfilter = Arrays.asList(mockUUID(0), mockUUID(1));

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ModelMapper modelMapper;

    @BeforeEach
    void before() {
        mockAclService = mock(AclService.class);
        mockFastighetsforteckningService = mock(FastighetsforteckningService.class);
        mockEntityManager = mock(EntityManager.class);
        mockFastighetService = mock(FastighetService.class);
        mockSamfService = mock(SamfService.class);
        mockRegisterenhetImportService = mock(RegisterenhetImportService.class);
        mockProjektRepository = mock(ProjektRepository.class);
        mockAvtalService = mock(AvtalService.class);
        mockVersionRepository = mock(VersionRepository.class);
        mockAvtalRepository = mock(AvtalRepository.class);
        mockAvtalsloggService = mock(AvtalsloggService.class);
        mockIntrangRepository = mock(IntrangRepository.class);
        mockOmradesintrangRepository = mock(OmradesintrangRepository.class);
        mockFastighetsintrangRepository = mock(FastighetsintrangRepository.class);
        mockKundConfigService = mock(KundConfigService.class);
        mockIntrangService = mock(IntrangService.class);
        mockInfobrevService = mock(InfobrevService.class);
        mockLoggService = mock(LoggService.class);
        mockFastighetRepository = mock(FastighetRepository.class);
        mockUserService = mock(UserService.class);
        mockAdminService = mock(AdminService.class);
        mockPermissionService = mock(PermissionService.class);

        projektService = new ProjektService(mockAclService, mockFastighetService, mockFastighetsforteckningService,
            mockProjektRepository, mockVersionRepository, modelMapper, mockAvtalService, mockInfobrevService,
            mockIntrangService, mockAvtalRepository, mockLoggService, mockAdminService, mockPermissionService,
            mockUserService);
    }

    // Slå av test tills man har refaktorerat till något som är mer testbart
    @Disabled
    @Test
    void så_ska_det_gå_att_skapa_ett_nytt_projekt_utan_shapefil() throws Exception {
        // Given
        final var saveProjektDto = TestData.mockProjektInfoDto();
        var file = new FileSystemResource(basePath + "Versioner_v1.zip");

        var versionEntity = versionEntity();
        versionEntity.setId(mockUUID(0));

        when(mockProjektRepository.saveAndFlush(any())).then(AdditionalAnswers.returnsFirstArg());
        //when(mockFastighetService.fastigheterFromIntrang(any())).thenReturn(asSet(fastighet1()));

        // When
        //final var projectActual = projektService.createProjekt(saveProjektDto);

        // Then
        //final var projectExpect = modelMapper.map(saveProjektDto, ProjektDto.class);

        //assertEquals(projectExpect, projectActual);
    }

    private Integer countGeometristatus(ImportVersionEntity importVersionEntity, GeometristatusDto geometristatus) {
        var countLong = importVersionEntity
                .getGeometristatus()
                .stream()
                .filter(version -> version.getGeometristatus() == geometristatus)
                .count();

        return Long.valueOf(countLong).intValue();
    }

    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_projekt() {
        // Given
        var pageRequest = PageRequest.of(pageNum, size, sortDirection, sortProperty);
        var user = new MarkkollUserDto();
        var projektIdRead = mockUUID(0);
        var projektIdRead2 = mockUUID(1);
        var kundId = "kkuuuuuund";
        var projektFilter = CollectionUtil.asSet(projektIdRead, projektIdRead2);

        var pageEntity = projektPage();

        when(mockProjektRepository.pageFiltered(any(), any(), eq(projektFilter))).thenReturn(pageEntity);
        when(mockUserService.getCurrentUser()).thenReturn(user);
        when(mockPermissionService.<UUID>filterObjectsByPermission(eq(user.getId()), eq(ProjektEntity.class),
            eq(MkPermission.READ))).thenReturn(projektFilter);

        // When
        var pageDto = projektService.projektPage(pageNum, size, sortProperty, sortDirection, name);

        // Then
        Mockito
                .verify(mockProjektRepository, times(1))
                .pageFiltered(PageRequestMatcher.eq(pageRequest), eq(name), eq(projektFilter));

        assertEquals(modelMapper.map(pageEntity, ProjektPageDto.class), pageDto);
    }

    @Test
    void så_ska_exception_kastas_om_en_sida_med_projekt_inte_gick_att_hämta() {
        // Given
        when(mockProjektRepository.findAll((Pageable) any())).thenReturn(null);

        when(mockUserService.getCurrentUser()).thenReturn(new MarkkollUserDto());

        assertThrows(MarkkollException.class,
                () -> projektService.projektPage(pageNum, size, sortProperty, sortDirection, null));
    }

    @Test
    void så_ska_markkoll_exception_kastas_för_ogiltig_property() {
        doThrow(PropertyReferenceException.class).when(mockProjektRepository).findAll((Pageable) any());

        when(mockUserService.getCurrentUser()).thenReturn(new MarkkollUserDto());

        assertThrows(MarkkollException.class,
                () -> projektService.projektPage(pageNum, size, sortProperty, sortDirection,  null));
    }

    @Test
    void så_ska_det_gå_att_skapa_ett_avtalsjobb() {
        // Given
        var projektId = mock(UUID.class);
        var jobbIdExpect = mock(UUID.class);
        var filterAndTemplate = new FilterAndTemplateDto().template(mockUUID(1));

        when(mockAvtalService.createAvtalsjobb(any(), any())).thenReturn(jobbIdExpect);

        // When
        var jobbIdActual = projektService.createAvtalJob(projektId, filterAndTemplate);

        // Then
        assertEquals(jobbIdActual, jobbIdExpect);
        verify(mockAvtalService).createAvtalsjobb(eq(projektId), eq(filterAndTemplate));
        verify(mockAvtalService).runAvtalsjobb(eq(jobbIdExpect), eq(filterAndTemplate.getTemplate()));
    }

    Page<ProjektEntity> projektPage() {
        var projektList = new ArrayList<ProjektEntity>();
        var projekt1 = modelMapper.map(mockProjektInfoDto(), ProjektEntity.class);
        var projekt2 = modelMapper.map(mockProjektInfoDto(), ProjektEntity.class);
        var projekt3 = modelMapper.map(mockProjektInfoDto(), ProjektEntity.class);
        projektList.add(projekt1);
        projektList.add(projekt2);
        projektList.add(projekt3);

        return new PageImpl<>(projektList);
    }

    @Test
    void så_ska_det_gå_att_hämta_ett_projekt() {
        // Given
        var projektId = mock(UUID.class);
        final ProjektEntity expectProjektEntity = projektEntity();
        final ProjektInfoDto expectProjektInfoDto = this.projektService.createProjektInfoDto(expectProjektEntity);
        when(mockProjektRepository.findById(any())).thenReturn(Optional.of(expectProjektEntity));

        // When
        final var projectActual = projektService.getProjektForProjekt(projektId);

        // Then
        Mockito.verify(mockProjektRepository, times(1)).findById(projektId);
        assertEquals(expectProjektInfoDto, projectActual.get());
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en_tidigare_version() {
        // Given
        var versionId = mockUUID(0);;

        when(mockVersionRepository.isCurrentVersion(any())).thenReturn(false);

        // When
        projektService.deleteVersion(versionId);

        // Then
        verify(mockVersionRepository).isCurrentVersion(eq(versionId));
        verify(mockVersionRepository).deleteById(versionId);
    }

    @Test
    void så_ska_det_inte_gå_att_ta_bort_nuvarande_version() {
        // Given
        var versionId = mockUUID(0);

        when(mockVersionRepository.isCurrentVersion(any())).thenReturn(true);

        // When
        var exception = assertThrows(MarkkollException.class,
                () -> projektService.deleteVersion(versionId));
        assertEquals(exception.getMessage(), MarkkollError.IMPORT_ERROR_NEW_VERSION.getMessage());
        verify(mockVersionRepository).isCurrentVersion(eq(versionId));
    }

    @Test
    void så_ska_det_gå_att_återställa_till_en_tidigare_version() {
        // Given
        var numOfVersions = 5;
        var versionsToDelete = 2;

        var versioner = versionerWithDate(numOfVersions);
        var versionerExpect = versionerWithDate(numOfVersions - versionsToDelete);
        var versionIdToRestore =  versionerExpect.get(versionerExpect.size() - 1).getId();

        var projektEntity = projektEntity().withVersioner(new HashSet<>());
        for (var version: versioner) {
            projektEntity.addVersion(version);
        }

        var versionExpected = modelMapper.map(
                versionerExpect.get(versionerExpect.size() - 1), VersionDto.class
        );

        when(mockVersionRepository.getOne(any())).thenReturn(versioner.get(numOfVersions - versionsToDelete));

        // When
        var versionActual = projektService.restoreVersion(versionIdToRestore);

        // Then
        assertEquals(versionExpected, versionActual);
    }

    private List<ImportVersionEntity> versionerWithDate(int amount) {
        var versionList = new ArrayList<ImportVersionEntity>();
        var baseDate = LocalDateTime.of(1986,3,4,5,6);

        for (int i = 0; i < amount; i++) {
            var version = new ImportVersionEntity();

            var fastighet = fastighetEntity();
            fastighet.setFastighetsbeteckning("fastighet " + i);
            fastighet.setId(mockUUID(i));

            version.setId(mockUUID(i));
            version.setSkapadDatum(baseDate.plusDays(i));
            versionList.add(version);
        }

        return versionList;
    }

    private static Stream<Arguments> versionTestData() {
        return Stream.of(
                Arguments.of("Versioner_v1.zip", "Versioner_v2.zip", 1, 0, 1, 1),
                Arguments.of("Versioner_v2.zip", "Versioner_v3.zip", 0, 0, 2, 1),
                Arguments.of("Versioner_v3.zip", "Versioner_v4.zip", 0, 1, 1, 1)
        );
    }

    private Set<FastighetEntity> fastigheterInShapefile(String shapefile) {
        List<String> fastighetsbeteckningar;
        switch (shapefile) {
            case "Versioner_v1.zip":
            case "Versioner_v4.zip":
                fastighetsbeteckningar = Arrays.asList("HÖLJES 1:129", "HÖLJES 1:312");
                break;
            case "Versioner_v2.zip":
            case "Versioner_v3.zip":
                fastighetsbeteckningar = Arrays.asList("HÖLJES 1:129", "HÖLJES 1:312", "HÖLJES 1:338");
                break;
            default:
                throw new RuntimeException();
        }

        return fastighetsbeteckningar.stream().map(this::testFastighet).collect(Collectors.toSet());
    }

    private FastighetEntity testFastighet(String fastighetsbeteckning) {
        try {
            switch (fastighetsbeteckning) {
                case "HÖLJES 1:129":
                    return fastighet1();
                case "HÖLJES 1:312":
                    return fastighet2();
                case "HÖLJES 1:338":
                    return fastighet3();
                default:
                    throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private FastighetEntity fastighet1() throws IOException {
        var geojson = "{\"type\":\"Polygon\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:3006\"}},\"coordinates\":[[[369074.14499998,6754272.07899927],[369002.15099998,6754301.66699926],[369008.12599998,6754309.54399926],[369072.35299998,6754394.51899927],[369074.77199998,6754397.71999927],[369125.35599998,6754464.16399927],[369158.32699998,6754451.48999927],[369191.96199998,6754440.64499927],[369230.18899998,6754435.25999927],[369270.79699998,6754435.50499928],[369311.57299998,6754464.21699928],[369328.61499998,6754500.78399928],[369295.96399998,6754515.98699928],[369290.62399998,6754514.06299928],[369280.11499998,6754492.00299928],[369258.78699998,6754476.41499928],[369228.83499998,6754475.31599927],[369219.85099998,6754475.01999928],[369201.48599998,6754479.64799927],[369183.20599998,6754484.17099927],[369174.16099998,6754488.13999927],[369150.61199998,6754497.57199927],[369124.67299998,6754515.60099927],[369100.07899998,6754532.64099927],[369075.41699998,6754549.70399927],[369053.69499998,6754518.59299927],[369025.59199998,6754539.64999926],[369049.01299998,6754568.10299926],[369102.64599998,6754629.95999927],[369115.00799998,6754620.41699927],[369148.40999998,6754597.57399927],[369184.15799998,6754579.37099927],[369217.64699998,6754564.36799927],[369225.88099998,6754567.24099927],[369237.85699998,6754590.06999928],[369235.16399998,6754597.66899928],[369212.11299998,6754607.64299927],[369201.67599998,6754612.70099927],[369188.04899998,6754620.35299927],[369176.58899998,6754628.17799927],[369171.76499998,6754632.13999927],[369163.95599998,6754638.81699927],[369142.81899998,6754657.38699927],[369127.24199998,6754671.07099927],[369132.87099998,6754678.13599927],[369149.84599998,6754663.19299927],[369169.99299998,6754645.45799927],[369182.09399998,6754635.37899927],[369193.09599998,6754627.88999927],[369201.81799998,6754622.72099927],[369216.05899998,6754615.72799928],[369234.60799998,6754607.78299927],[369253.91999998,6754601.04199928],[369264.72099998,6754639.91999928],[369231.86999998,6754648.00599927],[369209.09099998,6754660.30199927],[369190.31399998,6754674.59799927],[369172.71699998,6754690.02299927],[369162.12399998,6754699.35599927],[369226.03999998,6754773.68599927],[369501.31499998,6754651.60299929],[369460.08999998,6754568.44899929],[369447.60599998,6754575.33999929],[369416.65199998,6754592.43499928],[369384.67399998,6754610.26499928],[369370.68799998,6754613.71799928],[369331.91799998,6754623.29399928],[369298.95799998,6754631.42399928],[369288.31999998,6754589.47899928],[369321.29499998,6754581.19799928],[369360.10999998,6754571.43899928],[369366.86299998,6754569.74199928],[369397.50199998,6754551.93899929],[369424.59099998,6754536.19999929],[369437.39699998,6754528.75699928],[369440.95399998,6754524.94999929],[369418.21699998,6754466.68399929],[369395.83299998,6754472.83199928],[369388.96799998,6754438.56799928],[369386.27399998,6754399.19499928],[369373.09599998,6754368.94299928],[369365.78399998,6754351.58199928],[369360.17199998,6754326.14199928],[369283.89099998,6754328.49699928],[369226.73199998,6754329.11199928],[369201.16799998,6754334.72899927],[369187.09199998,6754338.78999927],[369200.17299998,6754360.04799927],[369136.02999998,6754399.87199927],[369110.27099998,6754357.91199927],[369134.73499998,6754343.08199927],[369124.97099998,6754324.23299927],[369074.14499998,6754272.07899927]],[[369129.87899998,6754522.85099927],[369154.55999998,6754505.71699927],[369182.81799998,6754494.69299927],[369187.27299998,6754492.28199927],[369221.66799998,6754484.01999927],[369255.64399998,6754485.19899928],[369263.29199998,6754490.70299928],[369273.05499998,6754497.79399928],[369282.40699998,6754517.97299928],[369280.39299998,6754523.29699928],[369254.97199998,6754535.02599928],[369249.43099998,6754522.78399928],[369226.16499998,6754524.06099927],[369200.92999998,6754529.94999927],[369176.70499998,6754538.93399927],[369152.73099998,6754555.61299927],[369128.09099998,6754572.76699927],[369103.41299998,6754589.93999927],[369080.65799998,6754557.05499927],[369105.24799998,6754539.97299927],[369129.87899998,6754522.85099927]]]}";
        var geometry = getGeometryFromGeoJSON(geojson);

        var fastighetOmrade = FastighetOmradeEntity
                .builder()
                .geom(geometry)
                .build();

        var fastighet = new FastighetEntity();
        fastighet.setId(mockUUID(0));
        fastighet.setFastighetsbeteckning("HÖLJES 1:129");
        fastighet.setFastighetOmraden(asSet(fastighetOmrade));
        return fastighet;
    }

    private FastighetEntity fastighet2() throws IOException {
        var geojson = "{\"type\":\"Polygon\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:3006\"}},\"coordinates\":[[[369184.15799998,6754579.37099927],[369217.64699998,6754564.36799927],[369225.88099998,6754567.24099927],[369237.85699998,6754590.06999928],[369235.16399998,6754597.66899928],[369212.11299998,6754607.64299927],[369201.67599998,6754612.70099927],[369184.15799998,6754579.37099927]]]}";
        var geometry = getGeometryFromGeoJSON(geojson);

        var fastighetOmrade = FastighetOmradeEntity
                .builder()
                .fastighetId(mockUUID(1))
                .geom(geometry)
                .build();

        var fastighet = new FastighetEntity();
        fastighet.setId(mockUUID(1));
        fastighet.setFastighetsbeteckning("HÖLJES 1:312");
        fastighet.setFastighetOmraden(asSet(fastighetOmrade));
        return fastighet;
    }

    private FastighetEntity fastighet3() throws IOException {
        var geojson = "{\"type\":\"Polygon\",\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:3006\"}},\"coordinates\":[[[369231.86999998,6754648.00599927],[369216.05899998,6754615.72799928],[369234.60799998,6754607.78299927],[369253.91999998,6754601.04199928],[369264.72099998,6754639.91999928],[369231.86999998,6754648.00599927]]]}";
        var geometry = getGeometryFromGeoJSON(geojson);

        var fastighetOmrade = FastighetOmradeEntity
                .builder()
                .fastighetId(mockUUID(2))
                .geom(geometry)
                .build();

        var fastighet = new FastighetEntity();
        fastighet.setId(mockUUID(2));
        fastighet.setFastighetsbeteckning("HÖLJES 1:338");
        fastighet.setFastighetOmraden(asSet(fastighetOmrade));
        return fastighet;
    }

    private Geometry getGeometryFromGeoJSON(String geojson) throws IOException {
        GeometryJSON geometryJSON = new GeometryJSON();
        var g = geometryJSON.read(new ByteArrayInputStream(geojson.getBytes()));
        return g;
    }
}
