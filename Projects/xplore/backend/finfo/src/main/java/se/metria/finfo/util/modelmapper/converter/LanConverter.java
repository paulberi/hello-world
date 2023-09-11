package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import se.metria.finfo.fsokws.Lan;

public class LanConverter implements Converter<Lan, String> {
    @Override
    public String convert(MappingContext<Lan, String> mappingContext) {
        return mappingContext.getSource().getLansnamn();
    }
}
