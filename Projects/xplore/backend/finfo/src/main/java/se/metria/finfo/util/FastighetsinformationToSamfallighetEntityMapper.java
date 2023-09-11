package se.metria.finfo.util;

import org.apache.commons.lang3.NotImplementedException;
import se.metria.finfo.entity.registerenhet.*;
import se.metria.finfo.fsokws.*;
import se.metria.finfo.openapi.model.AtgardstypDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/* Jag hade föredragit att låta ModelMapper sköta hela mappningen, men det är knepigt att få mappningen att fungera
* när klasshierariker är involverade. Jag gav upp och sköter det manuellt istället. */
public class FastighetsinformationToSamfallighetEntityMapper {
    public RegisterenhetEntity map(Fastighetsinformation fastighetsinformation) {
        var samfEntity = new RegisterenhetEntity();

        samfEntity.setUuid(UUID.fromString(fastighetsinformation.getRegisterenhet().getUUID()));

        if (fastighetsinformation.getRegisterenhet() instanceof Samfallighet) {
            mapSamfallighet((Samfallighet) fastighetsinformation.getRegisterenhet(), samfEntity);
        }
        else {
            throw new NotImplementedException("Ej stödd registerenhetstyp: " +
                fastighetsinformation.getRegisterenhet().getClass());
        }

        mapPagaendeFastighetsbildning(fastighetsinformation.getRegisterenhet().getArendePagar(), samfEntity);
        if (fastighetsinformation.getRefereradeRattigheter() != null) {
            mapRattigheter(fastighetsinformation.getRefereradeRattigheter().getRattighet(), samfEntity);
        }
        mapFastighetsrattligAtgard(fastighetsinformation.getRegisterenhet().getBerordAvFastighetsrattsligAtgard(),
            samfEntity);
        mapTekniskAtgard(fastighetsinformation.getRegisterenhet().getBerordAvTekniskAtgard(), samfEntity);

        return samfEntity;
    }

    private void mapSamfallighet(Samfallighet samfallighet, RegisterenhetEntity samfEntity) {
        samfEntity.setAndamal(samfallighet.getAndamal());
        samfEntity.setForvaltandeBeteckning(samfallighet.getForvaltandeForening());
        samfEntity.setTyp(RegisterenhetTyp.SAMFALLIGHET);
    }

    private void
    mapPagaendeFastighetsbildning(List<PagaendeFastighetsbildning> pagaendeFastighetsbildning,
                                  RegisterenhetEntity samfEntity) {

        for (var arendePagar: pagaendeFastighetsbildning) {
            var pagaendeFastighetsbildningEntity = new PagaendeFastighetsbildningEntity();
            pagaendeFastighetsbildningEntity.setArendeDagboksnummer(arendePagar.getArendeDagboksnummer());
            pagaendeFastighetsbildningEntity.setArendestatus(arendePagar.getArendestatus().getValue());

            samfEntity.addPagaendeFastighetsbildning(pagaendeFastighetsbildningEntity);
        }
    }

    private void
    mapRattigheter(List<Rattighet> rattigheter, RegisterenhetEntity samfEntity) {

        for (var rattighet: rattigheter) {
            var rattighetEntity = new RattighetEntity();
            rattighetEntity.setAktbeteckning(rattighet.getAktbeteckning() + "." + rattighet.getLopnummerInomAkt());
            rattighetEntity.setAndamal(rattighet.getAndamal());
            rattighetEntity.setRattighetstyp(rattighet.getTypAvRattighet().getValue());
            rattighetEntity.setRattsforhallande("Last");
            rattighetEntity.setRattighetsbeskrivning(rattighet.getRattighetsbeskrivning());
            rattighetEntity.setRattighetsanmarkning(rattighet.getRattighetsanmarkning());

            samfEntity.addRattighet(rattighetEntity);
        }
    }

    private void
    mapFastighetsrattligAtgard(List<FastighetsrattsligAtgard> fastighetsrattsligAtgards,
                               RegisterenhetEntity samfEntity) {

        for (var atgard: fastighetsrattsligAtgards) {
            var atgardEntity = new BerordAvAtgardEntity();
            atgardEntity.setAktbeteckning(atgard.getAktbeteckning());
            atgardEntity.setRegistreringsdatum(LocalDate.parse(atgard.getRegistreringsdatum()));
            atgardEntity.setAtgardstyp(AtgardstypDto.FASTIGHETSRATTLIG);
            setAtgarder(atgardEntity, atgard.getLittera(), atgard.getAtgard());

            samfEntity.addBerordAvAtgard(atgardEntity);
        }
    }

    private void mapTekniskAtgard(List<TekniskAtgard> tekniskAtgards, RegisterenhetEntity samfEntity) {
        for (var atgard: tekniskAtgards) {
            var atgardEntity = new BerordAvAtgardEntity();
            atgardEntity.setAktbeteckning(atgard.getAktbeteckning());
            atgardEntity.setRegistreringsdatum(LocalDate.parse(atgard.getRegistreringsdatum()));
            atgardEntity.setAtgardstyp(AtgardstypDto.TEKNISK);
            setAtgarder(atgardEntity, atgard.getLittera(), atgard.getAtgard());

            samfEntity.addBerordAvAtgard(atgardEntity);
        }
    }

    private void
    setAtgarder(BerordAvAtgardEntity atgardEntity,
                String littera,
                List<Atgardstyp> atgardstyp) {

        var atgardValues = getAtgardValues(littera, atgardstyp);
        atgardEntity.setAtgarder(atgardValues);
    }

    private List<String> getAtgardValues(String littera, List<Atgardstyp> atgardstyper) {
        if (littera == null) {
            return atgardstyper.stream()
                .map(a -> a.getValue())
                .collect(Collectors.toList());
        }
        else {
            return atgardstyper.stream()
                .map(a -> a.getValue() + " " + littera)
                .collect(Collectors.toList());
        }
    }
}
