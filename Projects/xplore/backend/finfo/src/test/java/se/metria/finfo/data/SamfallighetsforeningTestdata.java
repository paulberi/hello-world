package se.metria.finfo.data;

import se.metria.finfo.entity.agare.ForvaltningsobjektEntity;
import se.metria.finfo.entity.samfallighetsforening.SamfallighetsforeningEntity;
import se.metria.finfo.entity.samfallighetsforening.StyrelsemedlemEntity;
import se.metria.finfo.fsokws.*;
import se.metria.finfo.openapi.model.ForvaltningsobjektDto;
import se.metria.finfo.openapi.model.SamfallighetsforeningDto;
import se.metria.finfo.openapi.model.StyrelsemedlemDto;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

public class SamfallighetsforeningTestdata {
    public static SamfallighetsforeningDto samfallighetsforeningDto() {
        return new SamfallighetsforeningDto()
            .anmarkning("anmarkning")
            .coNamn("coNamn")
            .coAdress("coAdress")
            .coPostnummer("coPostnummer")
            .coPostort("coPostort")
            .avregistreringsdatum(LocalDate.of(1986, 3, 4))
            .faststallelsedatum(LocalDate.of(2000, 1, 2))
            .firmatecknare("firmatecknare")
            .foreningsnamn("foreningsnamn")
            .foreningstyp("Samfällighetsförening")
            .lan("länsnamn")
            .orgnr("orgnr")
            .rakenskapsarFromManad(1)
            .rakenskapsarFromDag(1)
            .rakenskapsarTomManad(12)
            .rakenskapsarTomDag(31)
            .registreringsdatum(LocalDate.of(2020, 2, 2))
            .sate("säte")
            .senasteAjourforingsdatum(LocalDate.of(1234, 5, 6))
            .styrelsedatum(LocalDate.of(1991, 1, 9))
            .telefonnummer("telefonnummer")
            .underAjourforing(true)
            .uuid(UUID.fromString("536e6f6d-f1e0-4985-9243-1cbc08096de0"))
            .forvaltningsobjekt(Arrays.asList(
                new ForvaltningsobjektDto()
                    .anmarkning("anmärkning A")
                    .objektsinformation("objektsinformation A"),
                new ForvaltningsobjektDto()
                    .anmarkning("anmärkning B")
                    .objektsinformation("objektsinformation B")
            ))
            .styrelsemedlemmar(Arrays.asList(
                new StyrelsemedlemDto()
                    .namn("namn A")
                    .utdelningsadress("utdelningsadress A")
                    .postnummer("postnummer A")
                    .postort("postort A")
                    .medlemstyp("medlemstyp A")
                    .land("land A")
                    .coAdress("co A")
                    .anmarkning("anmärkning A")
                    .lansstyrelsediarienummer("Diarienummer A")
                    .datumForLansstyrelsebeslut(LocalDate.of(2022, 2, 2))
                    .addFunktionItem("styrelsefunktion 1A")
                    .addFunktionItem("styrelsefunktion 2A"),
                new StyrelsemedlemDto()
                    .namn("namn B")
                    .utdelningsadress("utdelningsadress B")
                    .postnummer("postnummer B")
                    .postort("postort B")
                    .medlemstyp("medlemstyp B")
                    .land("land B")
                    .coAdress("co B")
                    .anmarkning("anmärkning B")
                    .lansstyrelsediarienummer("Diarienummer B")
                    .datumForLansstyrelsebeslut(LocalDate.of(2022, 2, 2))
                    .addFunktionItem("styrelsefunktion 1B")
                    .addFunktionItem("styrelsefunktion 2B")
            ));
    }

    public static SamfallighetsforeningEntity samfallighetsforeningEntity() {
        var entity = new SamfallighetsforeningEntity();
        entity.setAnmarkning("anmarkning");
        entity.setAvregistreringsdatum(LocalDate.of(1986, 3, 4));
        entity.setFaststallelsedatum(LocalDate.of(2000, 1, 2));
        entity.setFirmatecknare("firmatecknare");
        entity.setForeningsnamn("foreningsnamn");
        entity.setForeningstyp("Samfällighetsförening");
        entity.setLan("länsnamn");
        entity.setOrgnr("orgnr");

        entity.setCoNamn("coNamn");
        entity.setCoAdress("coAdress");
        entity.setCoPostnummer("coPostnummer");
        entity.setCoPostort("coPostort");

        entity.setRakenskapsarFromDag(1);
        entity.setRakenskapsarFromManad(1);
        entity.setRakenskapsarTomDag(31);
        entity.setRakenskapsarTomManad(12);
        entity.setRegistreringsdatum(LocalDate.of(2020, 2, 2));
        entity.setSate("säte");
        entity.setSenasteAjourforingsdatum(LocalDate.of(1234, 5, 6));
        entity.setStyrelsedatum(LocalDate.of(1991, 1, 9));
        entity.setTelefonnummer("telefonnummer");
        entity.setUnderAjourforing(true);
        entity.setUuid(UUID.fromString("536e6f6d-f1e0-4985-9243-1cbc08096de0"));

        var forvaltningsobjektA = new ForvaltningsobjektEntity();
        forvaltningsobjektA.setAnmarkning("anmärkning A");
        forvaltningsobjektA.setObjektsinformation("objektsinformation A");
        forvaltningsobjektA.setAndamalstyp("value A");
        forvaltningsobjektA.setSamfallighetsforening(entity);
        entity.getForvaltningsobjekt().add(forvaltningsobjektA);

        var forvaltningsobjektB = new ForvaltningsobjektEntity();
        forvaltningsobjektB.setAnmarkning("anmärkning B");
        forvaltningsobjektB.setObjektsinformation("objektsinformation B");
        forvaltningsobjektB.setAndamalstyp("value B");
        forvaltningsobjektB.setSamfallighetsforening(entity);
        entity.getForvaltningsobjekt().add(forvaltningsobjektB);

        var styrelsemedlemA = new StyrelsemedlemEntity();
        styrelsemedlemA.setNamn("namn A");
        styrelsemedlemA.setUtdelningsadress("utdelningsadress A");
        styrelsemedlemA.setPostnummer("postnummer A");
        styrelsemedlemA.setPostort("postort A");
        styrelsemedlemA.setMedlemstyp("medlemstyp A");
        styrelsemedlemA.setLand("land A");
        styrelsemedlemA.setCoAdress("co A");
        styrelsemedlemA.setAnmarkning("anmärkning A");
        styrelsemedlemA.setLansstyrelsediarienummer("Diarienummer A");
        styrelsemedlemA.setDatumForLansstyrelsebeslut(LocalDate.of(2022, 2, 2));
        styrelsemedlemA.setFunktion(Arrays.asList("styrelsefunktion 1A", "styrelsefunktion 2A"));
        styrelsemedlemA.setSamfallighetsforening(entity);
        entity.getStyrelsemedlemmar().add(styrelsemedlemA);

        var styrelsemedlemB = new StyrelsemedlemEntity();
        styrelsemedlemB.setNamn("namn B");
        styrelsemedlemB.setUtdelningsadress("utdelningsadress B");
        styrelsemedlemB.setPostnummer("postnummer B");
        styrelsemedlemB.setPostort("postort B");
        styrelsemedlemB.setMedlemstyp("medlemstyp B");
        styrelsemedlemB.setLand("land B");
        styrelsemedlemB.setCoAdress("co B");
        styrelsemedlemB.setAnmarkning("anmärkning B");
        styrelsemedlemB.setLansstyrelsediarienummer("Diarienummer B");
        styrelsemedlemB.setDatumForLansstyrelsebeslut(LocalDate.of(2022, 2, 2));
        styrelsemedlemB.setFunktion(Arrays.asList("styrelsefunktion 1B", "styrelsefunktion 2B"));
        styrelsemedlemB.setSamfallighetsforening(entity);
        entity.getStyrelsemedlemmar().add(styrelsemedlemB);

        return entity;
    }

    public static Samfallighetsforening samfallighetsforening() throws DatatypeConfigurationException {
        var samfallighetsforening = new Samfallighetsforening();
        samfallighetsforening.setAnmarkning("anmarkning");
        samfallighetsforening.setAdress("coAdress");
        samfallighetsforening.setAvregistreringsdatum(calendar(LocalDate.of(1986, 3, 4)));
        samfallighetsforening.setCoAdress("coNamn");
        samfallighetsforening.setFaststallelsedatum(calendar(LocalDate.of(2000, 1, 2)));
        samfallighetsforening.setFirmatecknare("firmatecknare");
        samfallighetsforening.setForeningsnamn("foreningsnamn");
        samfallighetsforening.setForeningstyp(foreningstyp("SAMF", "Samfällighetsförening"));
        samfallighetsforening.setLan(lan("länsnamn", "länskod"));
        samfallighetsforening.setOrgnr("orgnr");
        samfallighetsforening.setPostnummer("coPostnummer");
        samfallighetsforening.setPostort("coPostort");
        samfallighetsforening.setRakenskapsarFrom("0101");
        samfallighetsforening.setRakenskapsarTom("3112");
        samfallighetsforening.setRegistreringsdatum(calendar(LocalDate.of(2020, 2, 2)));
        samfallighetsforening.setSate("säte");
        samfallighetsforening.setSenasteAjourforingsdatum(calendar(LocalDate.of(1234, 5, 6)));
        samfallighetsforening.setStyrelsedatum(calendar(LocalDate.of(1991, 1, 9)));
        samfallighetsforening.setTelefonnummer("telefonnummer");
        samfallighetsforening.setUnderAjourforing(true);
        samfallighetsforening.setUUID("536e6f6d-f1e0-4985-9243-1cbc08096de0");

        var forvaltningsobjektA = forvaltningsobjekt(
            "anmärkning A", "A", "objektsinformation A", "A");
        var forvaltningsobjektB = forvaltningsobjekt(
            "anmärkning B", "B", "objektsinformation B", "B");

        samfallighetsforening.getForvaltningsobjekt().add(forvaltningsobjektA);
        samfallighetsforening.getForvaltningsobjekt().add(forvaltningsobjektB);

        var styrelsemedlemA = new Styrelsemedlem();
        styrelsemedlemA.setNamn("namn A");
        styrelsemedlemA.setUtdelningsadress("utdelningsadress A");
        styrelsemedlemA.setPostnummer("postnummer A");
        styrelsemedlemA.setPostort("postort A");
        styrelsemedlemA.setMedlemstyp("medlemstyp A");
        styrelsemedlemA.setLand("land A");
        styrelsemedlemA.setCoAdress("co A");
        styrelsemedlemA.setAnmarkning("anmärkning A");
        styrelsemedlemA.setLansstyrelseDiarieNummmer("Diarienummer A");
        styrelsemedlemA.setDatumForLanstyrelseBeslut(calendar(LocalDate.of(2022, 2, 2)));
        styrelsemedlemA.getFunktion().add(styrelsefunktion(1, "styrelsefunktion 1A"));
        styrelsemedlemA.getFunktion().add(styrelsefunktion(2, "styrelsefunktion 2A"));

        var styrelsemedlemB = new Styrelsemedlem();
        styrelsemedlemB.setNamn("namn B");
        styrelsemedlemB.setUtdelningsadress("utdelningsadress B");
        styrelsemedlemB.setPostnummer("postnummer B");
        styrelsemedlemB.setPostort("postort B");
        styrelsemedlemB.setMedlemstyp("medlemstyp B");
        styrelsemedlemB.setLand("land B");
        styrelsemedlemB.setCoAdress("co B");
        styrelsemedlemB.setAnmarkning("anmärkning B");
        styrelsemedlemB.setLansstyrelseDiarieNummmer("Diarienummer B");
        styrelsemedlemB.setDatumForLanstyrelseBeslut(calendar(LocalDate.of(2022, 2, 2)));
        styrelsemedlemB.getFunktion().add(styrelsefunktion(1, "styrelsefunktion 1B"));
        styrelsemedlemB.getFunktion().add(styrelsefunktion(2, "styrelsefunktion 2B"));

        samfallighetsforening.getStyrelsemedlem().add(styrelsemedlemA);
        samfallighetsforening.getStyrelsemedlem().add(styrelsemedlemB);

        var forvaltningsobjektsandamalA = new ForvaltningsobjektsAndamal();
        forvaltningsobjektsandamalA.setAndamalstyp(andamalstyp("kod A", "value A"));
        forvaltningsobjektsandamalA.setObjektslopnummer("A");
        samfallighetsforening.getForvaltningsobjektsAndamal().add(forvaltningsobjektsandamalA);

        var forvaltningsobjektsandamalB = new ForvaltningsobjektsAndamal();
        forvaltningsobjektsandamalB.setAndamalstyp(andamalstyp("kod B", "value B"));
        forvaltningsobjektsandamalB.setObjektslopnummer("B");
        samfallighetsforening.getForvaltningsobjektsAndamal().add(forvaltningsobjektsandamalB);

        return samfallighetsforening;
    }

    private static Andamalstyp andamalstyp(String kod, String value) {
        var andamalstyp = new Andamalstyp();
        andamalstyp.setKod(kod);
        andamalstyp.setValue(value);
        return andamalstyp;
    }

    private static Styrelsefunktion styrelsefunktion(long kod, String value) {
        var styrelsefunktion = new Styrelsefunktion();
        var styrelseFunktionTyp = new Styrelsefunktiontyp();
        styrelseFunktionTyp.setKod(kod);
        styrelseFunktionTyp.setValue(value);
        styrelsefunktion.setStyrelsefunktionTyp(styrelseFunktionTyp);
        return styrelsefunktion;
    }

    private static XMLGregorianCalendar calendar(LocalDate localDate) throws DatatypeConfigurationException {
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(localDate.toString());
    }

    private static Forvaltning
    forvaltningsobjekt(String anmarkning, String nyckel, String objektsinformation, String objektlopnummer) {

        var forvaltningsobjekt = new SamfallighetsEllerGAforvaltning();
        forvaltningsobjekt.setAnmarkning(anmarkning);
        forvaltningsobjekt.setFastighetsnyckelForSamfEllerGA(nyckel);
        forvaltningsobjekt.setObjektsinformation(objektsinformation);
        forvaltningsobjekt.setObjektslopnummer(objektlopnummer);
        return forvaltningsobjekt;
    }

    private static Samfallighetsforeningstyp foreningstyp(String kod, String value) {
        var foreningstyp = new Samfallighetsforeningstyp();
        foreningstyp.setKod(kod);
        foreningstyp.setValue(value);
        return foreningstyp;
    }

    private static Lan lan(String lansnamn, String lanskod) {
        var lan = new Lan();
        lan.setLanskod(lanskod);
        lan.setLansnamn(lansnamn);
        return lan;
    }
}
