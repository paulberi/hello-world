package se.metria.finfo.data;

import se.metria.finfo.entity.registerenhet.*;
import se.metria.finfo.openapi.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class SamfallighetTestdata {
    public static RegisterenhetEntity samfallighetEntity() {
        var samfallighetEntity = new RegisterenhetEntity();

        samfallighetEntity.setTyp(RegisterenhetTyp.SAMFALLIGHET);
        samfallighetEntity.setAndamal("LÅNG-OCH TVÄRGATOR");
        samfallighetEntity.setForvaltandeBeteckning("Beteckning");
        samfallighetEntity.setUuid(UUID.fromString("909a6a83-5b69-90ec-e040-ed8f66444c3f"));

        var arendePagar = new PagaendeFastighetsbildningEntity();
        arendePagar.setArendeDagboksnummer("210691");
        arendePagar.setArendestatus("Lantmäteriförrättning pågår");
        samfallighetEntity.getPagaendeFastighetsbildning().add(arendePagar);
        arendePagar.setRegisterenhet(samfallighetEntity);

        var rattighetEntity1 = new RattighetEntity();
        rattighetEntity1.setAktbeteckning("2361-03/9.1");
        rattighetEntity1.setAndamal("VÄG");
        rattighetEntity1.setRattighetstyp("Officialservitut");
        rattighetEntity1.setRattsforhallande("Last");
        rattighetEntity1.setRattighetsbeskrivning("RÄTT ATT ANVÄNDA DEN SAMFÄLLDA VÄGEN YTTERBERG S:3, VARI STAMFASTIGHETEN HAR DEL, FÖR UTFART TILL DEN ALLMÄNNA VÄGEN.");
        samfallighetEntity.getRattighet().add(rattighetEntity1);
        rattighetEntity1.setRegisterenhet(samfallighetEntity);

        var rattighetEntity2 = new RattighetEntity();
        rattighetEntity2.setAktbeteckning("2361-96/129.3");
        rattighetEntity2.setAndamal("VÄG");
        rattighetEntity2.setRattighetstyp("Officialservitut");
        rattighetEntity2.setRattsforhallande("Last");
        rattighetEntity2.setRattighetsanmarkning("ANDELSSERVITUT");
        samfallighetEntity.getRattighet().add(rattighetEntity2);
        rattighetEntity2.setRegisterenhet(samfallighetEntity);

        var atgard1 = new BerordAvAtgardEntity();
        atgard1.setAktbeteckning("23-SVJ-1302");
        atgard1.setRegistreringsdatum(LocalDate.of(1980, 3, 28));
        atgard1.setAtgardstyp(AtgardstypDto.FASTIGHETSRATTLIG);
        atgard1.setAtgarder(Arrays.asList("Fastighetsbestämning"));
        samfallighetEntity.getBerordAvAtgard().add(atgard1);
        atgard1.setRegisterenhet(samfallighetEntity);

        var atgard2 = new BerordAvAtgardEntity();
        atgard2.setAktbeteckning("23-SVJ-68");
        atgard2.setRegistreringsdatum(LocalDate.of(1980, 3, 28));
        atgard2.setAtgardstyp(AtgardstypDto.FASTIGHETSRATTLIG);
        atgard2.setAtgarder(Arrays.asList("Laga skifte S:1"));
        samfallighetEntity.getBerordAvAtgard().add(atgard2);
        atgard2.setRegisterenhet(samfallighetEntity);

        var atgard3 = new BerordAvAtgardEntity();
        atgard3.setAktbeteckning("2361-03/9");
        atgard3.setRegistreringsdatum(LocalDate.of(2003, 3, 24));
        atgard3.setAtgardstyp(AtgardstypDto.FASTIGHETSRATTLIG);
        atgard3.setAtgarder(Arrays.asList("Avstyckning"));
        samfallighetEntity.getBerordAvAtgard().add(atgard3);
        atgard3.setRegisterenhet(samfallighetEntity);

        var atgard4 = new BerordAvAtgardEntity();
        atgard4.setAktbeteckning("1234-56/7");
        atgard4.setRegistreringsdatum(LocalDate.of(2001, 2, 3));
        atgard4.setAtgardstyp(AtgardstypDto.TEKNISK);
        atgard4.setAtgarder(Arrays.asList("Avstyckning"));
        samfallighetEntity.getBerordAvAtgard().add(atgard4);
        atgard4.setRegisterenhet(samfallighetEntity);

        return samfallighetEntity;
    }

    public static SamfallighetDto samfallighetDto() {
        var registerenhet = new SamfallighetDto()
            .andamal("LÅNG-OCH TVÄRGATOR")
            .forvaltandeBeteckning("Beteckning")
            .uuid(UUID.fromString("909a6a83-5b69-90ec-e040-ed8f66444c3f"))
            .addPagaendeFastighetsbildningItem(new PagaendeFastighetsbildningDto() //pågående fastighetsbildning???
                .arendeDagboksnummer("210691")
                .arendestatus("Lantmäteriförrättning pågår")
            )
            .addRattighetItem(new RattighetDto()
                .aktbeteckning("2361-03/9.1")
                .andamal("VÄG")
                .rattighetstyp("Officialservitut")
                .rattsforhallande("Last")
                .rattighetsbeskrivning("RÄTT ATT ANVÄNDA DEN SAMFÄLLDA VÄGEN YTTERBERG S:3, VARI STAMFASTIGHETEN HAR DEL, FÖR UTFART TILL DEN ALLMÄNNA VÄGEN.")
                .rattighetsanmarkning("")
            )
            .addRattighetItem(new RattighetDto()
                .aktbeteckning("2361-96/129.3")
                .andamal("VÄG")
                .rattighetstyp("Officialservitut")
                .rattsforhallande("Last")
                .rattighetsbeskrivning("")
                .rattighetsanmarkning("ANDELSSERVITUT")
            )
            .addBerordAvAtgardItem(new BerordAvAtgardDto()
                .aktbeteckning("23-SVJ-1302")
                .registreringsdatum(LocalDate.of(1980, 3, 28))
                .atgarder(Arrays.asList("Fastighetsbestämning"))
                .atgardstyp(AtgardstypDto.FASTIGHETSRATTLIG)
            )
            .addBerordAvAtgardItem(new BerordAvAtgardDto()
                .aktbeteckning("23-SVJ-68")
                .registreringsdatum(LocalDate.of(1980, 3, 28))
                .atgarder(Arrays.asList("Laga skifte S:1"))
                .atgardstyp(AtgardstypDto.FASTIGHETSRATTLIG)
            )
            .addBerordAvAtgardItem(new BerordAvAtgardDto()
                .aktbeteckning("2361-03/9")
                .registreringsdatum(LocalDate.of(2003, 3, 24))
                .atgarder(Arrays.asList("Avstyckning"))
                .atgardstyp(AtgardstypDto.FASTIGHETSRATTLIG)
            )
            .addBerordAvAtgardItem(new BerordAvAtgardDto()
                .aktbeteckning("1234-56/7")
                .registreringsdatum(LocalDate.of(2001, 2, 3))
                .atgarder(Arrays.asList("Avstyckning"))
                .atgardstyp(AtgardstypDto.TEKNISK)
            );

        return registerenhet;
    }
}
