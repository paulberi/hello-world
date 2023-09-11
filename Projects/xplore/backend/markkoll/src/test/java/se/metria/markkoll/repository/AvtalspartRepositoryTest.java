package se.metria.markkoll.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.metria.markkoll.annotations.MarkkollDatabaseTest;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.openapi.model.AgartypDto;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.DetaljtypDto;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;
import se.metria.markkoll.repository.markagare.MarkagareRepository;
import se.metria.markkoll.repository.markagare.PersonRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@MarkkollDatabaseTest
@DisplayName("Givet AvtalspartRepository")
public class AvtalspartRepositoryTest {
    @Autowired
    AvtalspartRepository avtalspartRepository;

    @Autowired
    VersionRepository versionRepository;

    @Autowired
    ProjektRepository projektRepository;

    @Autowired
    FastighetRepository fastighetRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MarkagareRepository markagareRepository;

    @Autowired
    AvtalRepository avtalRepository;

    @BeforeEach
    void beforeEach() {
        projektRepository.deleteAll();
        projektRepository.flush();

        fastighetRepository.deleteAll();
        fastighetRepository.flush();
    }

    @Test
    void så_ska_ska_gå_att_hämta_id_för_kontaktpersonen_för_en_fastighet() {
        // Given
        var fastighetId = mockUUID(1);

        var projekt = createProjekt();
        createVersion(projekt);
        var fastighet = createFastighet(fastighetId);
        var avtal = createAvtal(projekt, fastighet);
        var avtalspart = createAvtalspart(avtal, fastighet, true);

        // When
        var kontaktpersonId = avtalspartRepository.findKontaktpersonId(projekt.getId(), fastighetId);

        // Then
        assertEquals(Optional.of(avtalspart.getId()), kontaktpersonId);
    }

    @Test
    void så_ska_det_gå_att_hämta_avtalsparter_från_projekt_id_och_fastighet_id() {
        // Given

        var fastighetId = mockUUID(1);
        var projekt = createProjekt();
        createVersion(projekt);
        var fastighet = createFastighet(fastighetId);
        var avtal = createAvtal(projekt, fastighet);
        var avtalspart = createAvtalspart(avtal, fastighet, true);

        // When
        List<AvtalspartEntity> avtalsparter = avtalspartRepository.findByProjektIdAndFastighetId(projekt.getId(), fastighetId);

        // Then
        assertEquals(avtalsparter, Arrays.asList(avtalspart));
    }

    @Test
    void så_ska_det_gå_att_erhålla_kontaktpersonen_för_en_fastighet_i_ett_avtal() {
        // Given
        var fastighetId = mockUUID(1);
        var avtalspart = createTestData(fastighetId, true);

        // When
        var kontaktperson = avtalspartRepository.findKontaktperson(avtalspart.getAvtal().getId());

        // Then
        assertEquals(avtalspart, kontaktperson.get());
    }

    @Test
    void så_ska_empty_returneras_om_fastigheten_saknar_kontaktperson() {
        // Given
        var fastighetId = mockUUID(1);
        var avtalspart = createTestData(fastighetId, false);

        // When
        var kontaktperson = avtalspartRepository.findKontaktperson(avtalspart.getAvtal().getId());

        // Then
        assertEquals(Optional.empty(), kontaktperson);
    }

    private AvtalspartEntity createTestData(UUID fastighetId, Boolean isKontaktperson) {
        var projekt = createProjekt();
        createVersion(projekt);
        var fastighet = createFastighet(fastighetId);
        var avtal = createAvtal(projekt, fastighet);

        return createAvtalspart(avtal, fastighet, isKontaktperson);
    }

    private ProjektEntity createProjekt() {
        // I den bästa av världar så skulle vi ha bättre designade servicar som sköter såna här CRUD-operationer...
        var projekt = new ProjektEntity();
        projekt.setNamn("projekt");
        projekt.setProjekttyp(ProjektTypDto.FIBER);
        projekt.setSkapadAv("CK");
        projekt.setSkapadDatum(LocalDateTime.of(2020, 2, 3, 4, 5));
        projekt = projektRepository.saveAndFlush(projekt);

        return projekt;
    }

    private ImportVersionEntity createVersion(ProjektEntity projektEntity) {
        var version = new ImportVersionEntity();
        version.setProjekt(projektEntity);
        version.setFilnamn("filnamn");
        version.setBuffert(0.0);
        return versionRepository.saveAndFlush(version);
    }

    private FastighetEntity createFastighet(UUID fastighetId) {
        var fastighet = new FastighetEntity();
        fastighet.setId(fastighetId);
        fastighet.setFastighetsbeteckning("fastighetsbeteckning");
        fastighet.setDetaljtyp(DetaljtypDto.FASTIGHET);
        fastighet.setExternid("externId");
        return fastighetRepository.saveAndFlush(fastighet);
    }

    private AvtalEntity createAvtal(ProjektEntity projektEntity, FastighetEntity fastighetEntity) {
        var avtal = new AvtalEntity();
        avtal.setProjekt(projektEntity);
        avtal.setFastighet(fastighetEntity);
        avtal.setErsattning(100);
        return avtalRepository.saveAndFlush(avtal);
    }

    private AvtalspartEntity
    createAvtalspart(AvtalEntity avtalEntity, FastighetEntity fastighetEntity, Boolean isKontaktperson)
    {
        var person = new PersonEntity();
        person.setNamn("person");
        person.setAdress("adress");
        person.setPostnummer("postnummer");
        person.setPostort("postort");
        person.setKundId("kundId");
        person = personRepository.saveAndFlush(person);

        var markagare = new MarkagareEntity();
        markagare.setPerson(person);
        markagare.setFastighet(fastighetEntity);
        markagare.setAgartyp(AgartypDto.LF);
        markagare.setAndel("1/2");
        markagare.setKundId("kundId");
        markagare = markagareRepository.saveAndFlush(markagare);

        var avtalspart = new AvtalspartEntity();
        avtalspart.setMarkagare(markagare);
        avtalspart.setAvtal(avtalEntity);
        avtalspart.setAvtalsstatus(AvtalsstatusDto.AVTAL_JUSTERAS);
        avtalspart.setKontaktpersonAvtal(avtalEntity);
        avtalspart = avtalspartRepository.saveAndFlush(avtalspart);

        if (isKontaktperson) {
            avtalEntity.setKontaktperson(avtalspart);
        }

        return avtalspart;
    }
}
