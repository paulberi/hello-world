package se.metria.matdatabas.service.bifogadfil;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.matdatabas.common.ImageMetadataReader;
import se.metria.matdatabas.security.MatdatabasUser;
import se.metria.matdatabas.service.bifogadfil.dto.BifogadfilInfo;
import se.metria.matdatabas.service.bifogadfil.dto.SaveBifogadfil;
import se.metria.matdatabas.service.bifogadfil.entity.BifogadfilEntity;
import se.metria.matdatabas.service.bifogadfil.exception.BifogadfilNotFoundException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Givet bifogadfil tjänst med filer")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureEmbeddedDatabase
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class BifogadfilServiceIT {
	
	private static final String FILE_NAME_0 = "bild0.png"; 
	private static final String FILE_NAME_1 = "bild1.png"; 
	private static final String FILE_NAME_2 = "bild2.png";
	private static final String THUMBNAIL_NAME = "thumbnail.jpg";
	private static final String MIME_TYPE = "image/png";

	@Autowired
	private BifogadfilRepository repository;
	
	@Autowired
	private BifogadfilService service;
	
	@MockBean
	private MatdatabasUser user;
	
	private UUID entityId0;
	private UUID entityId1;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@BeforeEach
	void setUp() throws Exception {
		var authentication = mock(Authentication.class);
		var securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getDetails()).thenReturn(user);
		SecurityContextHolder.setContext(securityContext);

		var entity0 = createEntity(FILE_NAME_0);
		entityId0 = repository.save(entity0).getId();
		var entity1 = createEntity(FILE_NAME_1);
		entityId1 = repository.save(entity1).getId();
	}

	@Test
	void när_en_fil_hämtas_med_id_skall_filen_hämtas() {
		var optFil = service.getBifogadfil(entityId0);
		assertTrue(optFil.isPresent());
		var fil = optFil.get();
		assertEquals(FILE_NAME_0, fil.getFilnamn());
		assertEquals(MIME_TYPE, fil.getMimeTyp());
		assertNotNull(fil.getSkapadDatum());
		var now = LocalDateTime.now().plusSeconds(1);
		assertTrue(now.isAfter(fil.getSkapadDatum()));
		assertEquals(FILE_NAME_0, fil.getFilnamn());
		assertNotNull(fil.getFil());
		assertNotNull(fil.getThumbnail());
	}

	@Test
	void när_filinfo_hämtas_med_id_skall_filinfo_hämtas() {
		var optFil = service.getBifogadfilInfo(entityId0);
		assertTrue(optFil.isPresent());
		var fil = optFil.get();
		assertEquals(FILE_NAME_0, fil.getFilnamn());
		assertEquals(MIME_TYPE, fil.getMimeTyp());
		assertNotNull(fil.getSkapadDatum());
		var now = LocalDateTime.now().plusSeconds(1);
		assertTrue(now.isAfter(fil.getSkapadDatum()));
		assertEquals(FILE_NAME_0, fil.getFilnamn());
	}

	@Test
	void när_filinfo_hämtas_med_flera_id_skall_flera_filinfo_hämtas() {
		var filInfos = service.getBifogadfilInfos(Set.of(entityId0, entityId1, UUID.fromString("00000000-0000-0000-0000-000000000000")));
		assertEquals(2, filInfos.size());
		assertTrue(filInfos.stream().map(BifogadfilInfo::getFilnamn).anyMatch(FILE_NAME_0::equals));
		assertTrue(filInfos.stream().map(BifogadfilInfo::getFilnamn).anyMatch(FILE_NAME_1::equals));
	}

	@Test
	void när_en_filinfo_hämtas_med_felaktigt_id_skall_inte_filinfo_hämtas() {
		var optFil = service.getBifogadfilInfo(UUID.fromString("00000000-0000-0000-0000-000000000000"));
		assertFalse(optFil.isPresent());
	}

	@Test
	void när_en_fil_hämtas_med_felaktigt_id_skall_ingen_filen_hämtas() {
		var optFil = service.getBifogadfil(UUID.fromString("00000000-0000-0000-0000-000000000000"));
		assertFalse(optFil.isPresent());
	}
	
	@Test
	void när_en_fil_sparas_skall_filen_sparas_och_kunna_hämtas_med_id() throws IOException, ImageReadException {
		var newFil = createEditBifogadfil(FILE_NAME_2);
		var saved = service.createBifogadfil(newFil);
		assertNotNull(saved.getId());
		var optFil = service.getBifogadfil(saved.getId());
		assertTrue(optFil.isPresent());
		var fil = optFil.get();
		assertEquals(FILE_NAME_2, fil.getFilnamn());
		assertEquals(MIME_TYPE, fil.getMimeTyp());
		assertNotNull(fil.getSkapadDatum());
		var now = LocalDateTime.now().plusSeconds(1);
		assertTrue(now.isAfter(fil.getSkapadDatum()));
		assertEquals(FILE_NAME_2, fil.getFilnamn());
		assertNotNull(fil.getFil());
		assertNotNull(fil.getThumbnail());
	}
	
	@Test
	void när_en_bild_sparas_utan_thumbnail_skall_bilden_sparas_och_thumbnail_genereras() throws IOException, ImageReadException {
		var fileName = FILE_NAME_2;

		var newFil = createEditBifogadfil(fileName);
		newFil.setThumbnail(null);
		var saved = service.createBifogadfil(newFil);
		assertNotNull(saved.getId());

		var optFil = service.getBifogadfil(saved.getId());
		assertTrue(optFil.isPresent());

		var fil = optFil.get();
		assertEquals(fileName, fil.getFilnamn());
		assertEquals(MIME_TYPE, fil.getMimeTyp());
		assertNotNull(fil.getSkapadDatum());

		var now = LocalDateTime.now().plusSeconds(1);
		assertTrue(now.isAfter(fil.getSkapadDatum()));
		assertEquals(fileName, fil.getFilnamn());
		assertNotNull(fil.getFil());
		assertNotNull(fil.getThumbnail());
	}

	@ParameterizedTest
	@CsvSource({
			"orientation-landscape-0.jpg, 0, 200, 133",
			"orientation-landscape-1.jpg, 1, 200, 133",
			"orientation-landscape-2.jpg, 2, 200, 133",
			"orientation-landscape-3.jpg, 3, 200, 133",
			"orientation-landscape-4.jpg, 4, 200, 133",
			"orientation-landscape-5.jpg, 5, 200, 133",
			"orientation-landscape-6.jpg, 6, 200, 133",
			"orientation-landscape-7.jpg, 7, 200, 133",
			"orientation-landscape-8.jpg, 8, 200, 133",
			"orientation-portrait-0.jpg, 0, 133, 200",
			"orientation-portrait-1.jpg, 1, 133, 200",
			"orientation-portrait-2.jpg, 2, 133, 200",
			"orientation-portrait-3.jpg, 3, 133, 200",
			"orientation-portrait-4.jpg, 4, 133, 200",
			"orientation-portrait-5.jpg, 5, 133, 200",
			"orientation-portrait-6.jpg, 6, 133, 200",
			"orientation-portrait-7.jpg, 7, 133, 200",
			"orientation-portrait-8.jpg, 8, 133, 200"})
	void när_en_thumbnail_genereras_ska_bilden_roteras_och_orientering_tas_bort(String fileName,
																				Integer orientation,
																				Integer width,
																				Integer height) throws IOException, ImageReadException {
		var input = getFileStream(fileName);
		boolean writeOutputImages = false;

		var created = createEditBifogadfil(fileName);
		created.setThumbnail(null);
		var saved = service.createBifogadfil(created);
		assertNotNull(saved.getId());

		var attachedFile = service.getBifogadfil(saved.getId());
		assertTrue(attachedFile.isPresent());

		var file = attachedFile.get();
		assertNotNull(file.getThumbnail());

		if (writeOutputImages) {
			var thumbnailInputStream = new ByteArrayInputStream(file.getThumbnail());
			IOUtils.copy(thumbnailInputStream, new FileOutputStream("output/" + fileName));
		}

		var reader = new ImageMetadataReader(new ByteArrayInputStream(file.getThumbnail()));
		assertEquals(0, reader.getOrientation());
		assertEquals(width, reader.getImageWidth());
		assertEquals(height, reader.getImageHeight());
	}

	@Test
	void ta_bort_bifogad_fil_som_finns() throws Exception {
		var bifogadfil = service.createBifogadfil(createEditBifogadfil(FILE_NAME_0));
		UUID id = bifogadfil.getId();
		assertFalse(service.getBifogadfil(id).isEmpty());

		service.deleteBifogadefiler(List.of(id));
		assertTrue(service.getBifogadfil(id).isEmpty());
	}

	@Test
	void ta_bort_bifogade_filer_som_finns() throws Exception {
		var bifogadfil1 = service.createBifogadfil(createEditBifogadfil(FILE_NAME_0));
		var bifogadfil2 = service.createBifogadfil(createEditBifogadfil(FILE_NAME_1));
		var bifogadfil3 = service.createBifogadfil(createEditBifogadfil(FILE_NAME_2));

		Set<UUID> ids = Stream.of(
				bifogadfil1.getId(),
				bifogadfil2.getId(),
				bifogadfil3.getId()).
				collect(Collectors.toSet());

		for (UUID id : ids) {
			assertTrue(service.getBifogadfilInfo(id).isPresent());
		}

		service.deleteBifogadefiler(new ArrayList<>(ids));

		for (UUID id2 : ids) {
			assertTrue(service.getBifogadfilInfo(id2).isEmpty());
		}
	}

	@Test
	void ta_bort_bifogad_fil_som_inte_finns() throws Exception {
		assertThrows(BifogadfilNotFoundException.class, () -> service.deleteBifogadefiler(List.of(UUID.fromString("00000000-0000-0000-0000-000000000000"))));

		var bifogadfil = service.createBifogadfil(createEditBifogadfil(FILE_NAME_0));

		assertThrows(BifogadfilNotFoundException.class, () -> service.deleteBifogadefiler(List.of(bifogadfil.getId(), UUID.fromString("00000000-0000-0000-0000-000000000000"))));

		assertTrue(service.getBifogadfilInfo(bifogadfil.getId()).isPresent());
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
	}
	
	private BifogadfilEntity createEntity(String fileName) throws IOException {
		return createEditBifogadfil(fileName).toEntity();
	}
	
	private SaveBifogadfil createEditBifogadfil(String fileName) throws IOException {
		return SaveBifogadfil.builder()
				.filnamn(fileName)
				.mimeTyp(MIME_TYPE)
				.fil(getFileContent(fileName))
				.thumbnail(getFileContent(THUMBNAIL_NAME))
				.build();
	}

	private byte[] getFileContent(String fileName) throws IOException {
		try (var in = this.getClass().getResourceAsStream("/images/" + fileName)) {
			return in.readAllBytes();
		}
	}

	private InputStream getFileStream(String fileName) throws IOException {
		try (var in = this.getClass().getResourceAsStream("/images/" + fileName)) {
			return in;
		}
	}

	private String getFilePath(String fileName) throws IOException {
		var url = this.getClass().getResource(fileName);
		return url.getPath();
	}
}
