package se.metria.markkoll.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import se.metria.markkoll.annotations.MarkkollDatabaseTestJooq;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.FastighetsfilterDto;
import se.metria.markkoll.openapi.model.IntrangDto;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.fastighet.SamfallighetFastighetRepository;
import se.metria.markkoll.repository.fastighet.SamfallighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.projekt.ProjektService;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@MarkkollDatabaseTestJooq
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("Givet Fastighetrepository")
@Sql({"/data/markkoll_fastigheter.sql"})
public class FastighetRepositoryTest {
    @Autowired
    ProjektService projektService;

    @Autowired
    FastighetRepository fastighetRepository;

    @Autowired
    ProjektRepository projektRepository;

    @Autowired
    SamfallighetFastighetRepository samfallighetFastighetRepository;

    @Autowired
    SamfallighetRepository samfallighetRepository;

    static UUID projektId = UUID.fromString("ab1db75b-caac-4350-a0ff-fa0c97993ad6");
    static UUID fastighetId = UUID.fromString("00ac1d50-861c-4e34-acad-8fa34d9502e4");

    @AfterEach
    void tearDown() {
        projektRepository.deleteById(projektId);
        samfallighetRepository.deleteAll();
        samfallighetFastighetRepository.deleteAll();
        fastighetRepository.deleteAll();
    }

    @Test
    void så_ska_det_gå_att_hämta_en_fastighets_extent() {
        Optional<Geometry> extent = fastighetRepository.getExtent(fastighetId);
        Assertions.assertTrue(extent.isPresent());
        Assertions.assertFalse(extent.get().isEmpty());
    }

    @Test
    void så_hanterar_getExtent_ogiltiga_fastighets_idn() {
        var id = UUID.fromString("7e131579-93e2-42b8-8044-8d8f3e4d7e48");
        Optional<Geometry> extent = fastighetRepository.getExtent(id);
        Assertions.assertTrue(extent.isEmpty());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_lista_med_fastigheter() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(null)
                .excludeOutreddaFastigheter(false)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("");

        // When
        var filtered = fastighetRepository.fastigheterFiltered(projektId, fastighetsfilter);

        // Then
        assertEquals(4, filtered.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_fastighetslista_filtrerad_på_fastighetsbeteckning() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(null)
                .excludeOutreddaFastigheter(false)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("HÖLJES 1:");

        // When
        var filtered = fastighetRepository.fastigheterFiltered(projektId, fastighetsfilter);

        // Then
        assertEquals(2, filtered.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_fastighetslista_filtrerad_på_avtalsstatus() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(AvtalsstatusDto.AVTAL_SIGNERAT)
                .excludeOutreddaFastigheter(false)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("");

        // When
        var filtered = fastighetRepository.fastigheterFiltered(projektId, fastighetsfilter);

        // Then
        assertEquals(1, filtered.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_fastighetslista_filtrerad_på_försenade_avtal() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .showForsenade(true);

        // When
        var filtered = fastighetRepository.fastigheterFiltered(projektId, fastighetsfilter);

        // Then
        assertEquals(1, filtered.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_fastighetslista_filtrerad_på_inga_outredda_fastigheter() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(null)
                .excludeOutreddaFastigheter(true)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("");

        // When
        var filtered = fastighetRepository.fastigheterFiltered(projektId, fastighetsfilter);

        // Then
        assertEquals(3, filtered.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_fastighetslista_filtrerad_på_fastigheter_som_saknar_markägare() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(null)
                .excludeOutreddaFastigheter(true)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("");

        // When
        var filtered = fastighetRepository.fastigheterFiltered(projektId, fastighetsfilter);

        // Then
        assertEquals(3, filtered.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_fastigheter() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(null)
                .excludeOutreddaFastigheter(false)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("");

        var pageRequest = PageRequest.of(0, 2, Sort.Direction.DESC, "fastighetsbeteckning");

        // When
        var filtered = fastighetRepository.fastighetPageFiltered(projektId, pageRequest,
                fastighetsfilter);

        // Then
        assertEquals(2, filtered.getContent().size());
        assertEquals("HÖLJES 3:557", filtered.getContent().get(0).getFastighetsbeteckning());
        assertEquals("HÖLJES 3:457", filtered.getContent().get(1).getFastighetsbeteckning());
    }

    @Test
    void så_ska_det_gå_att_hämta_en_sida_med_samfälligheter() {
        // Given
        var fastighetsfilter = new FastighetsfilterDto()
                .avtalsstatus(null)
                .excludeOutreddaFastigheter(false)
                .excludeWithMarkagare(false)
                .fastighetsbeteckning("");

        var pageRequest = PageRequest.of(0, 2, Sort.Direction.ASC, "fastighetsbeteckning");

        // When
        var filtered = fastighetRepository.samfallighetPageFiltered(projektId, pageRequest,
                fastighetsfilter);

        // Then
        assertEquals(2, filtered.getContent().size());
        assertEquals("SAMF 1", filtered.getContent().get(0).getFastighetsbeteckning());
        assertEquals("SAMF 2", filtered.getContent().get(1).getFastighetsbeteckning());
    }

    @Test
    void så_ska_det_gå_att_hämta_ingående_fastighetsbeteckningar_för_en_samfällighet() {
        // Given
        var samfallighetId = UUID.fromString("109a6a72-387e-90ec-e040-ed8f66444c3f");

        // When
        var beteckningar = fastighetRepository.getIngaendeFastigheterBeteckningar(samfallighetId);

        // Then
        assertEquals(Arrays.asList("FAST 1", "FAST 2", "FAST 3"), beteckningar);
    }

    @Test
    void så_ska_det_gå_att_hämta_information_om_intrången_i_en_fastighet() {
        // Given
        var fastighetId = UUID.fromString("00ac1d50-861c-4e34-acad-8fa34d9502e4");

        // When
        var omraden = fastighetRepository.findIntrangInfoForFastighet(fastighetId, projektId);

        // Then
        var expect = new IntrangDto()
                .typ("MARKSKAP")
                .subtyp("NONE")
                .langd(11.57627898029922);

        assertEquals(Arrays.asList(expect), omraden);
    }

    @Test
    void så_ska_det_gå_att_hämta_avtalsstatus() {
        // When
        var status = fastighetRepository.fastighetAvtalsstatus(fastighetId, projektId);

        // Then
        assertEquals(AvtalsstatusDto.AVTAL_SIGNERAT, status);
    }

    @Test
    void så_ska_det_gå_att_hämta_fastigheter_för_ett_projekt() {
        // Given
        var fastighetIdsExpect = Arrays.asList(
            UUID.fromString("00ac1d50-861c-4e34-acad-8fa34d9502e4"),
            UUID.fromString("109a6a72-25bb-90ec-e040-ed8f66444c3f"),
            UUID.fromString("109a6a72-384c-90ec-e040-ed8f66444c3f"),
            UUID.fromString("109a6a72-387e-90ec-e040-ed8f66444c3f"),
            UUID.fromString("64ac1d50-861c-4e34-acad-8fa34d9502a1"),
            UUID.fromString("909a6a72-25bb-90ec-e040-ed8f66444c3f"),
            UUID.fromString("909a6a72-38a7-90ec-e040-ed8f66444c3f")
        );

        // When
        var skogsfastigheter = fastighetRepository.getFastigheter(projektId);

        // Then
        var fastighetIds = skogsfastigheter.stream()
            .map(FastighetEntity::getId)
            .collect(Collectors.toList());

        assertEquals(fastighetIdsExpect, fastighetIds);
    }
}
