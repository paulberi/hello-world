package se.metria.markkoll.util.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.List;

public class AtgardConverter implements Converter <List<String>, String>{
    @Override
    public String convert(MappingContext<List<String>, String> mappingContext) {
        var source = mappingContext.getSource();
        return String.join(" ", source);
    }
}
