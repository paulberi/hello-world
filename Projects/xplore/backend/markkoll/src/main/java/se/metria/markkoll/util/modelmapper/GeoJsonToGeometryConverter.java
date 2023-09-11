package se.metria.markkoll.util.modelmapper;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class GeoJsonToGeometryConverter implements Converter <String, Geometry> {
    @Override
    public Geometry convert(MappingContext<String, Geometry> mappingContext) {
        try {
            return new GeoJsonReader().read(mappingContext.getSource());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
