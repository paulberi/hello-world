package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import se.metria.finfo.fsokws.Atgardstyp;

public class AtgardstypConverter implements Converter <Atgardstyp, String>{
    @Override
    public String convert(MappingContext<Atgardstyp, String> mappingContext) {
        return mappingContext.getSource().getValue();
    }
}
