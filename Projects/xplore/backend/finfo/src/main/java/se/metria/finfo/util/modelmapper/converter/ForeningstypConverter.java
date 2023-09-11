package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import se.metria.finfo.fsokws.Samfallighetsforeningstyp;

public class ForeningstypConverter implements Converter<Samfallighetsforeningstyp, String>  {
    @Override
    public String convert(MappingContext<Samfallighetsforeningstyp, String> mappingContext) {
        return mappingContext.getSource().getValue();
    }
}
