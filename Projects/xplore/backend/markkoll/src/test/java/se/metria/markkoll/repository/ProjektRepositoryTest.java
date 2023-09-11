package se.metria.markkoll.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import se.metria.markkoll.annotations.MarkkollDatabaseTestJooq;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@MarkkollDatabaseTestJooq
@DisplayName("Givet ProjektRepository")
public class ProjektRepositoryTest {
    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private FastighetRepository fastighetRepository;

    @BeforeEach
    void before() {
        projektRepository.deleteAll();
        fastighetRepository.deleteAll();
    }

    @Test
    @WithMockUserMarkhandlaggare
    public void så_ska_det_gå_att_hämta_en_sida_med_projekt() {
        // Given
        // Given
        final var projekt1 = ProjektEntity.builder()
                .namn("Projekt 1")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 1")
                .build();

        final var projekt2 = ProjektEntity.builder()
                .namn("Projekt 2")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 2")
                .build();

        final var projekt3 = ProjektEntity
                .builder()
                .namn("Projekt 23")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 3")
                .build();

        projektRepository.saveAndFlush(projekt1);
        projektRepository.saveAndFlush(projekt2);
        projektRepository.saveAndFlush(projekt3);

        var pageRequest = PageRequest.of(0, 2);

        // When
        var page = projektRepository.pageFiltered(pageRequest, null, null);

        // Then
        var projektnamn = page
                .getContent()
                .stream()
                .map(ProjektEntity::getNamn)
                .collect(Collectors.toList());

        assertArrayEquals(Arrays.asList(projekt1, projekt2).toArray(), page.getContent().toArray());
    }

    @Test
    @WithMockUserMarkhandlaggare
    public void så_ska_det_gå_att_hämta_en_sida_med_projekt_filtrerat_efter_projektnamn() {
        // Given
        final var projekt1 = ProjektEntity.builder()
                .namn("Projekt 1")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 1")
                .build();

        final var projekt2 = ProjektEntity.builder()
                .namn("Projekt 2")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 2")
                .build();

        final var projekt3 = ProjektEntity
                .builder()
                .namn("Projekt 23")
                .kundId("kund 3")
                .projekttyp(ProjektTypDto.FIBER)
                .build();

        projektRepository.saveAndFlush(projekt1);
        projektRepository.saveAndFlush(projekt2);
        projektRepository.saveAndFlush(projekt3);

        var pageRequest = PageRequest.of(0, 2);

        // When
        var page = projektRepository.pageFiltered(pageRequest, "Projekt 2", null);

        // Then
        var projektnamn = page
                .getContent()
                .stream()
                .map(ProjektEntity::getNamn)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(projekt2, projekt3), page.getContent());
    }

    @Test
    @WithMockUserMarkhandlaggare
    void så_ska_det_gå_att_filtrera_efter_projekt() {
        // Given
        final var projekt1 = ProjektEntity.builder()
                .namn("Projekt 1")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 1")
                .build();

        final var projekt2 = ProjektEntity.builder()
                .namn("Projekt 2")
                .projekttyp(ProjektTypDto.FIBER)
                .kundId("kund 2")
                .build();

        projektRepository.saveAndFlush(projekt1);
        projektRepository.saveAndFlush(projekt2);

        var pageRequest = PageRequest.of(0, 2);

        // When
        var page = projektRepository.pageFiltered(pageRequest, null, Arrays.asList(projekt1.getId()));

        // Then
        assertEquals(Arrays.asList(projekt1), page.getContent());
    }
}
