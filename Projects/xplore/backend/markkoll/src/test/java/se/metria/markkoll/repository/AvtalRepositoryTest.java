package se.metria.markkoll.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import se.metria.markkoll.annotations.MarkkollDatabaseTest;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@MarkkollDatabaseTest
@DisplayName("Givet AvtalRepository")
@Sql({"/data/markkoll_elnat_projekt.sql"})
public class AvtalRepositoryTest {
    @Autowired
    AvtalRepository avtalRepository;

    @Autowired
    ProjektRepository projektRepository;

    @Autowired
    FastighetRepository fastighetRepository;

    @AfterEach
    void afterEach() {
        fastighetRepository.deleteAll();
        projektRepository.deleteAll();
    }

    @Test
    void så_ska_det_gå_att_hämta_avtal_med_ny_geometristatus_för_ett_projekt() {
        // Given
        var projektId = UUID.fromString("ab1db75b-caac-4350-a0ff-fa0c97993ad6");

        // When
        var nyaAvtal = avtalRepository.findNewByProjektId(projektId);

        // Then
        assertEquals(1, nyaAvtal.size());
        assertEquals(GeometristatusDto.NY, nyaAvtal.stream().findFirst().get()
                .getGeometristatus().stream().findFirst().get().getGeometristatus());
    }

    @Test
    void så_ska_det_gå_att_hämta_avtal_med_uppdaterad_geometristatus_för_ett_projekt() {
        // Given
        var projektId = UUID.fromString("ab1db75b-caac-4350-a0ff-fa0c97993ad6");

        // When
        var nyaAvtal = avtalRepository.findUpdatedByProjektId(projektId);

        // Then
        assertEquals(1, nyaAvtal.size());
        assertEquals(GeometristatusDto.UPPDATERAD, nyaAvtal.stream().findFirst().get()
                .getGeometristatus().stream().findFirst().get().getGeometristatus());
    }
}
