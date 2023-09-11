package se.metria.markkoll.util.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import se.metria.markkoll.entity.samfallighet.SamfallighetIngaendeFastighetEntity;

public class DelagandeFastighetConverter implements Converter<SamfallighetIngaendeFastighetEntity, String> {
    @Override
    public String convert(MappingContext<SamfallighetIngaendeFastighetEntity, String> mappingContext) {
        return mappingContext.getSource().getFastighetsbeteckning();
    }
}
