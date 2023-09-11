package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.UUID;

public class StringToUuidConverter implements Converter <String, UUID> {
    @Override
    public UUID convert(MappingContext<String, UUID> mappingContext) {
        return UUID.fromString(mappingContext.getSource());
    }
}
