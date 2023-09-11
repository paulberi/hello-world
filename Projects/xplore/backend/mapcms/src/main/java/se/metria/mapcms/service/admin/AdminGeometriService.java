package se.metria.mapcms.service.admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.locationtech.jts.io.ParseException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.entity.GeometriEntity;
import se.metria.mapcms.entity.ProjektEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.metria.mapcms.openapi.model.GeometriReqDto;
import se.metria.mapcms.openapi.model.GeometriRspDto;
import se.metria.mapcms.repository.GeometriRepository;
import se.metria.mapcms.repository.ProjektRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static se.metria.mapcms.commons.utils.GeoJsonParser.*;

@Service
@RequiredArgsConstructor
public class AdminGeometriService {
    @NonNull
    private final GeometriRepository geometriRepository;
    @NonNull
    private final ModelMapper modelMapper;
    @NonNull
    private final ProjektRepository projektRepository;

    @Transactional
    public GeometriRspDto createGeometri(UUID kundId, UUID projektId, GeometriReqDto geometriReqDto) {
        ProjektEntity projekt;
        try {
            projekt = projektRepository.getProjektForKund(kundId, projektId).orElseThrow(EntityNotFoundException::new);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Det finns ingen projekt med projekt ID " +
                projektId + " och kund ID "+
                kundId + " i databasen.");
        }

        String geoJson = geometriReqDto.getGeom().replace("'", "\"");
        if(!isValid(geoJson)){throw new IllegalStateException("Ogiltig GeoJSON format.");}

        JsonObject jsonObject = JsonParser.parseString(geoJson).getAsJsonObject();
        final String geojsonType = jsonObject.get("type").getAsString();
        final String geojsonString = jsonObject.toString();

        try {
            switch (geojsonType) {
                case "FeatureCollection":
                    List<GeometriEntity> geometriEntityList = parseFeatureCollection(projekt, geojsonString);
                    final List<GeometriEntity> savedFeatureCollection = geometriRepository.saveAll(geometriEntityList);
                    final GeometriRspDto featureCollectionRspDto = modelMapper.map(geometriReqDto, GeometriRspDto.class);
                    featureCollectionRspDto.setId(savedFeatureCollection.get(0).getId());
                    return featureCollectionRspDto;
                case "GeometryCollection":
                    List<GeometriEntity> geometryCollection = parseGeometryCollection(projekt, geojsonString);
                    final List<GeometriEntity> savedGeometryCollection = geometriRepository.saveAll(geometryCollection);
                    final GeometriRspDto geometryCollectionRspDto = modelMapper.map(geometriReqDto, GeometriRspDto.class);
                    geometryCollectionRspDto.setId(savedGeometryCollection.get(0).getId());
                    geometryCollectionRspDto.setProps(savedGeometryCollection.get(0).getProperties());
                    return geometryCollectionRspDto;
                case "Feature":
                    final GeometriEntity featureGeometry = parseFeature(projekt, geojsonString);
                    final GeometriEntity savedFeatureGeometry = geometriRepository.save(featureGeometry);
                    final GeometriRspDto featureRspDto = modelMapper.map(savedFeatureGeometry, GeometriRspDto.class);
                    featureRspDto.setProps(featureGeometry.getProperties());
                    return featureRspDto;

                case "Point":
                    case "MultiPoint":
                        case "LineString":
                            case "MultiLineString":
                                case "Polygon":
                                    case "MultiPolygon":
                                        GeometriEntity geometriEntity = parseGeometry(geometriReqDto, projekt, geoJson);
                                        final GeometriEntity savedGeometri = geometriRepository.save(geometriEntity);
                                        final GeometriRspDto geometriRspDto = modelMapper.map(savedGeometri, GeometriRspDto.class);
                                        geometriRspDto.setProps(savedGeometri.getProperties());
                                        return geometriRspDto;
                default:
                    throw new IllegalStateException("Ogiltig GeoJSON format.");
            }
        } catch (ParseException e) {
            throw new IllegalStateException("Ogiltig GeoJSON format.");
        }
    }

    @Transactional
    public void deleteGeometri(UUID kundId, UUID projektId, UUID geometriId) {
        final GeometriEntity geometri = geometriRepository.findGeometri(kundId, projektId, geometriId);
        try {
            geometriRepository.delete(geometri);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new EntityNotFoundException("Geometri finns inte i databasen.");
        }
    }

    @Transactional
    public GeometriRspDto getGeometri(UUID kundId, UUID projektId, UUID geometriId) {
        try {
            GeometriEntity geom = geometriRepository.findGeometri(kundId, projektId, geometriId);
            return modelMapper.map(geom, GeometriRspDto.class);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Det finns ingen geometri med id " + geometriId + " i databasen.");
        }
    }

    @Transactional
    public List<GeometriRspDto> listGeometrier(UUID kundId, UUID projektId) {
        final List<GeometriEntity> geometriEntityList = geometriRepository.findAll(kundId, projektId);
        final List<GeometriRspDto> geometriRspDtoList = new ArrayList<>();
        for (GeometriEntity geometriEntity : geometriEntityList) {
            final GeometriRspDto geometriRspDto = modelMapper.map(geometriEntity, GeometriRspDto.class);
            geometriRspDtoList.add(geometriRspDto);
            }
            return geometriRspDtoList;
        }
}
