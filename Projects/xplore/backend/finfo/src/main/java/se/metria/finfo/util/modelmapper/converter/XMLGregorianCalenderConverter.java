package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

public class XMLGregorianCalenderConverter implements Converter<XMLGregorianCalendar, LocalDate> {
    @Override
    public LocalDate convert(MappingContext<XMLGregorianCalendar, LocalDate> mappingContext) {
        var calendar = mappingContext.getSource();

        var localDate = LocalDate.of(calendar.getYear(), calendar.getMonth(), calendar.getDay());

        return localDate;
    }
}
