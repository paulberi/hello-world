package se.metria.markkoll.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.metria.markkoll.annotations.MarkkollDatabaseTest;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@MarkkollDatabaseTest
@DisplayName("Givet VersionRepository")
public class VersionRepositoryTest {
    @Autowired
    VersionRepository versionRepository;

    @Autowired
    ProjektRepository projektRepository;

    @BeforeEach
    void beforeEach() {
        projektRepository.deleteAll();
    }

    @Test
    void isCurrentVersion_ska_returnera_true_för_nuvarande_versioner() {
        // Given
        var projekt = saveProjekt();
        var version = saveVersion(projekt);

        // When
        var isCurrentVersion = versionRepository.isCurrentVersion(version.getId());

        // Then
        assertEquals(isCurrentVersion, true);
    }

    @Test
    void isCurrentVersion_ska_returnera_false_för_icke_nuvarande_versioner() {
        // Given
        var projekt = saveProjekt();
        var oldVersion = saveVersion(projekt);
        saveVersion(projekt);

        projektRepository.saveAndFlush(projekt);

        var oldVersionId = oldVersion.getId();

        // When
        var isCurrentVersion = versionRepository.isCurrentVersion(oldVersionId);

        // Then
        assertEquals(isCurrentVersion, false);
    }

    @Test
    void så_ska_det_gå_att_hämta_nuvarande_version_för_ett_projekt() {
        // Given
        var projekt = saveProjekt();
        saveVersion(projekt);

        // When
        var version = versionRepository.getCurrentVersion(projekt.getId());

        // Then
        assertEquals(projekt.getCurrentVersion(), version);
    }

    @Test
    void så_ska_det_gå_att_hämta_nuvarande_version_id_för_ett_projekt() {
        // Given
        var projekt = saveProjekt();
        saveVersion(projekt);

        // When
        var versionId = versionRepository.getCurrentVersionId(projekt.getId());

        // Then
        assertEquals(projekt.getCurrentVersion().getId(), versionId);
    }

    private ProjektEntity saveProjekt() {
        var projekt = new ProjektEntity();
        projekt.setNamn("projekt");
        projekt.setProjekttyp(ProjektTypDto.FIBER);
        projekt.setSkapadAv("CK");
        projekt.setSkapadDatum(LocalDateTime.of(2020, 2, 3, 4, 5));
        projekt = projektRepository.saveAndFlush(projekt);

        return projekt;
    }

    private ImportVersionEntity saveVersion(ProjektEntity projekt) {
        var version = new ImportVersionEntity();
        version.setProjekt(projekt);
        version.setFilnamn("filnamn");
        version.setBuffert(0.0);
        version = versionRepository.saveAndFlush(version);
        projekt.addVersion(version);
        projekt.setCurrentVersion(version);

        return version;
    }
}