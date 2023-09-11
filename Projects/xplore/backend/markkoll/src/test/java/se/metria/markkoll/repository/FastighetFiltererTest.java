package se.metria.markkoll.repository;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import se.metria.markkoll.annotations.MarkkollDatabaseTestJooq;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.GeometristatusEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.fastighet.FastighetOmradeEntity;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningAnledning;
import se.metria.markkoll.entity.fastighetsforteckning.FastighetsforteckningEntity;
import se.metria.markkoll.entity.intrang.FastighetsintrangEntity;
import se.metria.markkoll.entity.intrang.OmradesintrangEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.geometristatus.GeometristatusRepository;
import se.metria.markkoll.repository.fastighet.FastighetOmradeRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.fastighet.jooq.FastighetFilterer;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.repository.version.entity.VersionRepository;
import se.metria.xplore.maputils.GeometryUtil;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.markkoll.db.tables.AvtalGeometristatus.AVTAL_GEOMETRISTATUS;
import static se.metria.markkoll.db.tables.Fastighet.FASTIGHET;
import static se.metria.xplore.maputils.GeometryUtil.getPointGeometry;

@MarkkollDatabaseTestJooq
@DisplayName("Givet FastighetFilterer")
public class FastighetFiltererTest {
    @Autowired
    DSLContext create;

    @Autowired
    FastighetsintrangRepository fastighetsintrangRepository;

    @Autowired
    OmradesintrangRepository omradesintrangRepository;

    @Autowired
    FastighetRepository fastighetRepository;

    @Autowired
    FastighetOmradeRepository fastighetOmradeRepository;

    @Autowired
    VersionRepository versionRepository;

    @Autowired
    ProjektRepository projektRepository;

    @Autowired
    AvtalRepository avtalRepository;

    @Autowired
    GeometristatusRepository geometristatusRepository;

    @Autowired
    FastighetsforteckningRepository fastighetsforteckningRepository;

    FastighetFilterer fastighetFilterer;

    @BeforeEach
    void beforeEach() {
        fastighetFilterer = new FastighetFilterer(create);
    }

    @AfterEach
    void tearDown() {
        projektRepository.deleteAll();
        fastighetRepository.deleteAll();
    }

    @Test
    void så_ska_det_gå_att_filtrera_efter_fastigheter_med_luftledningar() {
        // Given
        var projekt = createProjekt();
        var fastighet = createFastighet(projekt, projekt.getCurrentVersion(), IntrangsSubtypDto.LUFTLEDNING);
        var fastighet2 = createFastighet(projekt, projekt.getCurrentVersion(), IntrangsSubtypDto.MARKLEDNING);
        var filter = new FastighetsfilterDto().luftledningar(true);

        // When
        var records = fastighetFilterer.fastigheterRecordsFiltered(projekt.getId(), filter).fetch();

        // Then
        assertEquals(1, records.size());
        assertFastighet(fastighet, projekt.getId(), records.get(0));
    }

    private void assertFastighet(FastighetEntity fastighet, UUID projektId, Record record) {
        assertEquals(fastighet.getFastighetsbeteckning(), record.get(FASTIGHET.FASTIGHETSBETECKNING));
        assertEquals(fastighet.getId(), record.get(FASTIGHET.ID));
        assertEquals(fastighet.getDetaljtyp(), DetaljtypDto.fromValue(record.get(FASTIGHET.DETALJTYP)));
        assertEquals(
            geometristatusRepository.findByFastighetIdAndProjektId(fastighet.getId(), projektId).get().getGeometristatus(),
            GeometristatusDto.valueOf(record.get(AVTAL_GEOMETRISTATUS.GEOMETRISTATUS))
        );
    }

    private ProjektEntity createProjekt() {
        var projekt = projektRepository.saveAndFlush(ProjektEntity.builder()
            .namn("namn")
            .projekttyp(ProjektTypDto.FIBER)
            .build()
        );

        var version = versionRepository.saveAndFlush(ImportVersionEntity.builder()
            .id(UUID.randomUUID())
            .projekt(projekt)
            .filnamn("filnamn")
            .buffert(0.0)
            .build()
        );

        projekt.setCurrentVersion(version);
        return projektRepository.saveAndFlush(projekt);
    }

    private FastighetEntity
    createFastighet(ProjektEntity projekt, ImportVersionEntity version, IntrangsSubtypDto subtyp)
    {
        var fastighet = fastighetRepository.saveAndFlush(FastighetEntity.builder()
            .id(UUID.randomUUID())
            .fastighetsbeteckning("fastighet")
            .detaljtyp(DetaljtypDto.FASTIGHET)
            .externid("extern_id")
            .build()
        );

        var avtal = avtalRepository.saveAndFlush(AvtalEntity.builder()
            .id(UUID.randomUUID())
            .projekt(projekt)
            .fastighet(fastighet)
            .ersattning(0)
            .build()
        );

        geometristatusRepository.saveAndFlush(GeometristatusEntity.builder()
            .avtal(avtal)
            .version(version)
            .geometristatus(GeometristatusDto.OFORANDRAD)
            .build()
        );

        var omrade = fastighetOmradeRepository.saveAndFlush(FastighetOmradeEntity.builder()
            .geom(GeometryUtil.getPointGeometry(3, 4))
            .fastighetId(fastighet.getId())
            .omradeNr(Long.valueOf(0))
            .build()
        );

        omradesintrangRepository.saveAndFlush(OmradesintrangEntity.builder()
            .version(version)
            .fastighetomrade(omrade)
            .type(IntrangstypDto.NATSTATION.toString())
            .subtype(IntrangsSubtypDto.MARKSTRAK.toString())
            .geom(GeometryUtil.getPointGeometry(3, 4))
            .avtalstyp(AvtalstypDto.INTRANG)
            .status("NY")
            .build()
        );

        fastighetsintrangRepository.saveAndFlush(FastighetsintrangEntity.builder()
            .fastighet(fastighet)
            .version(version)
            .geom(getPointGeometry(3, 4))
            .status("NY")
            .typ(IntrangstypDto.NATSTATION.toString())
            .subtyp(subtyp.getValue())
            .avtalstyp(AvtalstypDto.INTRANG)
            .build()
        );

        fastighetsforteckningRepository.saveAndFlush(FastighetsforteckningEntity.builder()
            .anledning(FastighetsforteckningAnledning.MANUELLT_TILLAGD)
            .avtal(avtalRepository.getReferenceById(avtal.getId()))
            .fastighet(fastighetRepository.getReferenceById(fastighet.getId()))
            .projekt(projektRepository.getReferenceById(projekt.getId()))
            .build()
        );

        return fastighetRepository.findById(fastighet.getId()).get();
    }
}
