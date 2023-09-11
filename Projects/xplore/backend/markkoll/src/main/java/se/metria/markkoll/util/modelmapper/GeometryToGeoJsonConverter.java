package se.metria.markkoll.util.modelmapper;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonWriter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class GeometryToGeoJsonConverter implements Converter<Geometry, String> {
    @Override
    public String convert(MappingContext<Geometry, String> mappingContext) {
        return new GeoJsonWriter().write(mappingContext.getSource());
    }
}
