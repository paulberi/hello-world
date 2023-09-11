package se.metria.mapcms.commons.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import se.metria.mapcms.entity.GeometriEntity;
import se.metria.mapcms.entity.ProjektEntity;
import se.metria.mapcms.openapi.model.GeometriReqDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GeoJsonParser {
    public static GeometriEntity parseGeometry(GeometriReqDto geometriReqDto, ProjektEntity projekt, String GeoJsonString) throws ParseException {
        GeoJsonReader reader = new GeoJsonReader();
        final Geometry geom;
        geom = reader.read(GeoJsonString);
        geom.setSRID(3006);
        GeometriEntity geometriEntity = new GeometriEntity();
        geometriEntity.setGeom(geom);
        geometriEntity.setProjekt(projekt);
        geometriEntity.setProperties(geometriReqDto.getProps());
        return geometriEntity;
    }

    public static GeometriEntity parseFeature(ProjektEntity projekt, String geoJsonString) throws ParseException {
        JsonObject feature = JsonParser.parseString(geoJsonString).getAsJsonObject();
        final String geometry = feature.getAsJsonObject().get("geometry").toString();
        final Object properties = feature.getAsJsonObject().get("properties");
        GeometriEntity geometriEntity = new GeometriEntity();
        GeoJsonReader reader = new GeoJsonReader();
        Geometry geom;
        geom = reader.read(geometry);
        geom.setSRID(3006);
        geometriEntity.setGeom(geom);
        geometriEntity.setProjekt(projekt);
        geometriEntity.setProperties(properties.toString());
        return geometriEntity;
    }

    public static List<GeometriEntity> parseFeatureCollection(ProjektEntity projekt, String geoJsonString) throws ParseException {
        JsonObject jsonObject = JsonParser.parseString(geoJsonString).getAsJsonObject();
        List<GeometriEntity> geometriEntityList = new ArrayList<>();
        final JsonArray features = (JsonArray) jsonObject.get("features");
        for (JsonElement feature : features) {
            final GeometriEntity geometriEntity = parseFeature(projekt, feature.toString());
            geometriEntityList.add(geometriEntity);
        }
        return geometriEntityList;
    }

    public static List<GeometriEntity> parseGeometryCollection(ProjektEntity projekt, String geoJsonString) throws ParseException {
        JsonObject jsonObject = JsonParser.parseString(geoJsonString).getAsJsonObject();
        List<GeometriEntity> geometriEntityList = new ArrayList<>();
        GeoJsonReader reader = new GeoJsonReader();
        String properties = jsonObject.get("properties").toString();
        Geometry geom;
        final JsonArray geometries = (JsonArray) jsonObject.get("geometries");
        for (JsonElement geometry : geometries) {
            geom = reader.read(geometry.toString());
            geom.setSRID(3006);
            GeometriEntity geometriEntity = new GeometriEntity();
            geometriEntity.setGeom(geom);
            geometriEntity.setProjekt(projekt);
            geometriEntity.setProperties(properties);
            geometriEntityList.add(geometriEntity);
        }
        return geometriEntityList;
    }

    public static boolean isValid(String json) {
        try {
            JsonParser.parseString(json);
        } catch (JsonSyntaxException e) {
            return false;
        }
        return true;
    }

    public static Geometry convertGeojsonStringToGeometry(String geoJson) throws ParseException {


        GeoJsonReader reader = new GeoJsonReader();
        Geometry geom=null;
        try{
            geoJson=geoJson.replace("'", "\"");
            if(!isValid(geoJson)){throw new IllegalStateException("Ogiltig GeoJSON format.");}
            geom= reader.read(geoJson);
            geom.setSRID(3006);
        }catch (Exception e){
            log.warn("GeoJson format är felaktigt");
        }
        return geom;
    }
}
