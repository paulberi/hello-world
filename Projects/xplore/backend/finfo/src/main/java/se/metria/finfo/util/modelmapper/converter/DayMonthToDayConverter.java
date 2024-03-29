package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class DayMonthToDayConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(MappingContext<String, Integer> mappingContext) {
        return Integer.valueOf(mappingContext.getSource().substring(0, 2));
    }
}
