package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;

public class StringToDateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
        return LocalDate.parse(mappingContext.getSource());
    }
}
