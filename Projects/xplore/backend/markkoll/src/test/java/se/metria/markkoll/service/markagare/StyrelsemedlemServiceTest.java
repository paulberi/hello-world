package se.metria.markkoll.service.markagare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.fastighet.FastighetEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.entity.markagare.StyrelsemedlemEntity;
import se.metria.markkoll.openapi.finfo.model.FinfoStyrelsemedlemDto;
import se.metria.markkoll.openapi.model.*;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.markagare.AvtalspartRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
public class StyrelsemedlemServiceTest {
    @InjectMocks
    StyrelsemedlemService styrelsemedlemService;

    @Mock
    AvtalspartRepository avtalspartRepository;

    @Mock
    AvtalRepository avtalRepository;

    @Mock
    FastighetRepository fastighetRepository;

    @Mock
    ModelMapper modelMapper;

    @Captor
    ArgumentCaptor<List<AvtalspartEntity>> avtalspartEntityCaptor;

    @Test
    void addStyrelsemedlemmar() {
        // Given
        var avtal = new AvtalEntity();
        var avtalId = mockUUID(0);

        var fastighet = new FastighetEntity();
        var samfId = mockUUID(1);

        var kundId = "kundId";

        List<FinfoStyrelsemedlemDto> styrelsemedlemmar = Arrays.asList(
            new FinfoStyrelsemedlemDto()
                .funktion(Arrays.asList("Kassör"))
                .namn("Styrelsemedlemmen")
                .utdelningsadress("Adress")
                .postnummer("Postnummer")
                .postort("Postort")
        );

        when(fastighetRepository.getReferenceById(eq(samfId))).thenReturn(fastighet);
        when(avtalRepository.getReferenceById(eq(avtalId))).thenReturn(avtal);

        // When
        styrelsemedlemService.addStyrelsemedlemmar(avtalId, samfId, kundId, styrelsemedlemmar);

        // Then
        verify(avtalspartRepository).saveAll(avtalspartEntityCaptor.capture());
        var avtalsparter = avtalspartEntityCaptor.getValue();

        assertEquals(avtalsparter.size(), styrelsemedlemmar.size());
        for (int i = 0; i < styrelsemedlemmar.size(); i++) {
            var avtalspart = avtalsparter.get(i);
            var styrelsemedlem = styrelsemedlemmar.get(i);
            verifyStyrelsemedlem(avtalspart, styrelsemedlem, kundId, fastighet, avtal);
        }
    }

    @Test
    void getStyrelsemedlemmar() {
        // Given
        var kundId = "kundId";
        var samfId = mockUUID(0);
        List<AvtalspartEntity> avtalsparter = Arrays.asList(avtalspartEntity());

        when(avtalspartRepository.findStyrelsemedlemmar(eq(kundId), eq(samfId))).thenReturn(avtalsparter);

        // When
        var styrelsemedlemmarActual = styrelsemedlemService.getStyrelsemedlemmar(kundId, samfId);

        // Then
        var styrelsemedlemmarExpect = Arrays.asList(styrelsemedlem());
        assertEquals(styrelsemedlemmarExpect, styrelsemedlemmarActual);
    }

    private StyrelsemedlemDto styrelsemedlem() {
        var markagare = new MarkagareDto()
            .namn("Namn")
            .adress("Adress")
            .postnummer("Postnummer")
            .postort("Postort")
            .telefon("Telefon")
            .bankkonto("Bankkonto")
            .ePost("Epost")
            .fodelsedatumEllerOrgnummer("")
            .andel("Andel")
            .kontaktperson(false)
            .inkluderaIAvtal(false)
            .agartyp(AgartypDto.STYRELSEMEDLEM)
            .labels(new AvtalspartLabelsDto()
                .ofullstandingInformation(false)
                .avtalsstatusGammal(false)
                .ofullstandingInformation(false)
            )
            .agareStatus(AvtalsstatusDto.AVTAL_SIGNERAT);

        return new StyrelsemedlemDto()
            .funktion("Styrelsefunktion")
            .markagare(markagare);
    }

    private AvtalspartEntity avtalspartEntity() {
        var person = new PersonEntity();
        person.setNamn("Namn");
        person.setAdress("Adress");
        person.setPostnummer("Postnummer");
        person.setPostort("Postort");
        person.setTelefon("Telefon");
        person.setBankkonto("Bankkonto");
        person.setEPost("Epost");

        var styrelsemedlem = new StyrelsemedlemEntity();
        styrelsemedlem.setFunktion("Styrelsefunktion");

        var markagare = new MarkagareEntity();
        markagare.setAndel("Andel");
        markagare.setAgartyp(AgartypDto.STYRELSEMEDLEM);
        markagare.setPerson(person);
        markagare.setStyrelsemedlem(styrelsemedlem);

        var avtalspart = new AvtalspartEntity();
        avtalspart.setAvtalsstatus(AvtalsstatusDto.AVTAL_SIGNERAT);
        avtalspart.setMarkagare(markagare);

        return avtalspart;
    }

    private void
    verifyStyrelsemedlem(AvtalspartEntity avtalspart,
                         FinfoStyrelsemedlemDto styrelsemedlem,
                         String kundId,
                         FastighetEntity fastighet,
                         AvtalEntity avtal) {

        assertEquals(styrelsemedlem.getFunktion().get(0), avtalspart.getMarkagare().getStyrelsemedlem().getFunktion());
        assertEquals(styrelsemedlem.getNamn(), avtalspart.getMarkagare().getPerson().getNamn());
        assertEquals(styrelsemedlem.getUtdelningsadress(), avtalspart.getMarkagare().getPerson().getAdress());
        assertEquals(styrelsemedlem.getPostnummer(), avtalspart.getMarkagare().getPerson().getPostnummer());
        assertEquals(styrelsemedlem.getPostort(), avtalspart.getMarkagare().getPerson().getPostort());
        assertEquals(AgartypDto.STYRELSEMEDLEM, avtalspart.getMarkagare().getAgartyp());
        assertEquals(AvtalsstatusDto.EJ_BEHANDLAT, avtalspart.getAvtalsstatus());

        assertEquals(kundId, avtalspart.getMarkagare().getKundId());
        assertEquals(kundId, avtalspart.getMarkagare().getPerson().getKundId());
        assertSame(fastighet, avtalspart.getMarkagare().getFastighet());
        assertSame(avtal, avtalspart.getAvtal());
        assertSame(avtalspart.getMarkagare(), avtalspart.getMarkagare().getStyrelsemedlem().getMarkagare());
    }
}
