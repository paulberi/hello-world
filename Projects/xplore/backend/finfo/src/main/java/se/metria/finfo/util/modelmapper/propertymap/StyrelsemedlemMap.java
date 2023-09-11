package se.metria.finfo.util.modelmapper.propertymap;

import org.modelmapper.PropertyMap;
import se.metria.finfo.entity.samfallighetsforening.StyrelsemedlemEntity;
import se.metria.finfo.fsokws.Styrelsemedlem;
import se.metria.finfo.util.modelmapper.converter.XMLGregorianCalenderConverter;

public class StyrelsemedlemMap extends PropertyMap<Styrelsemedlem, StyrelsemedlemEntity> {
    @Override
    protected void configure() {
        skip(destination.getSamfallighetsforening());
        skip(destination.getId());
        map(source.getLansstyrelseDiarieNummmer(), destination.getLansstyrelsediarienummer());
        using(new XMLGregorianCalenderConverter()).map(source.getDatumForLanstyrelseBeslut(), destination.getDatumForLansstyrelsebeslut());
    }
}
