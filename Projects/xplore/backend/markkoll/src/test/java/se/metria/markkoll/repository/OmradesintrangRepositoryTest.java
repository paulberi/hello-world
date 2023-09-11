package se.metria.markkoll.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import se.metria.markkoll.annotations.MarkkollDatabaseTest;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.repository.intrang.OmradesintrangRepository;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@MarkkollDatabaseTest
@DisplayName("Givet OmradesintrangRepository")
@Sql({"/data/markkoll_elnat_projekt.sql"})
public class OmradesintrangRepositoryTest {
    @Autowired
    OmradesintrangRepository omradesintrangRepository;

    @Test
    void så_ska_det_gå_att_hämta_alla_områdesintrång_för_en_fastighet() {
        // Given
        var fastighetId = UUID.fromString("00ac1d50-861c-4e34-acad-8fa34d9502e4");

        // When
        var intrang = omradesintrangRepository.findByFastighetId(fastighetId);

        // Then
        assertEquals(6, intrang.size());
    }

    @Test
    void så_ska_det_gå_att_hämta_alla_områdesintrång_för_ett_avtal() {
        // Given
        var avtalId = UUID.fromString("3245ed8b-4480-4cef-822f-138f5bb104ff");
        var versionId = UUID.fromString("851fd90a-ac2c-4579-adeb-c94080a4718d");

        // When
        var intrang = omradesintrangRepository.findByAvtalIdAndAvtalstyper(avtalId,
            versionId, Arrays.asList(AvtalstypDto.INTRANG));

        // Then
        var nya = intrang.stream()
                .filter(intr -> intr.getStatus().equals(IntrangsStatusDto.NY.toString())).collect(Collectors.toList());
        var bevaras = intrang.stream()
                .filter(intr -> intr.getStatus().equals(IntrangsStatusDto.BEVARAS.toString())).collect(Collectors.toList());
        var raseras = intrang.stream()
                .filter(intr -> intr.getStatus().equals(IntrangsStatusDto.RASERAS.toString())).collect(Collectors.toList());

        assertEquals(4, nya.size());
        assertEquals(1, bevaras.size());
        assertEquals(1, raseras.size());
    }
}
