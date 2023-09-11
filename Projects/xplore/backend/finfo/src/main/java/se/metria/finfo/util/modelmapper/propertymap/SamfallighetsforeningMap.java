package se.metria.finfo.util.modelmapper.propertymap;

import org.modelmapper.PropertyMap;
import se.metria.finfo.entity.samfallighetsforening.SamfallighetsforeningEntity;
import se.metria.finfo.fsokws.Samfallighetsforening;
import se.metria.finfo.util.modelmapper.converter.*;

public class SamfallighetsforeningMap extends PropertyMap<Samfallighetsforening, SamfallighetsforeningEntity> {

    @Override
    protected void configure() {
        using(new DayMonthToDayConverter()).map(source.getRakenskapsarFrom(), destination.getRakenskapsarFromDag());
        using(new DayMonthToMonthConverter()).map(source.getRakenskapsarFrom(), destination.getRakenskapsarFromManad());
        using(new DayMonthToDayConverter()).map(source.getRakenskapsarTom(), destination.getRakenskapsarTomDag());
        using(new DayMonthToMonthConverter()).map(source.getRakenskapsarTom(), destination.getRakenskapsarTomManad());
        using(new SamfallighetsforeningListConverter()).map(source, destination.getForvaltningsobjekt());
        map(source.getStyrelsemedlem(), destination.getStyrelsemedlemmar());

        // Variabeln CoAdress är namngiven fel. Egentligen så är det ett namn, inte en adress, i den variablen.
        map(source.getCoAdress(), destination.getCoNamn());
        map(source.getAdress(), destination.getCoAdress());
        map(source.getPostnummer(), destination.getCoPostnummer());
        map(source.getPostort(), destination.getCoPostort());

        skip(destination.getId());
        skip(destination.getImporteradDatum());
    }
}
