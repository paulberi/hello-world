package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import se.metria.finfo.fsokws.Styrelsefunktion;

public class StyrelsefunktionConverter implements Converter<Styrelsefunktion, String> {
    @Override
    public String convert(MappingContext<Styrelsefunktion, String> mappingContext) {
        var styrelsefunktion = mappingContext.getSource();

        return styrelsefunktion.getStyrelsefunktionTyp().getValue();
    }
}
