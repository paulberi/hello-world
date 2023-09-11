package se.metria.markkoll.service.intrang.converters.featuretointrang;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opengis.feature.simple.SimpleFeature;
import se.metria.markkoll.openapi.model.AvtalstypDto;
import se.metria.markkoll.openapi.model.IntrangsStatusDto;
import se.metria.markkoll.openapi.model.IntrangsSubtypDto;
import se.metria.markkoll.openapi.model.IntrangstypDto;
import se.metria.markkoll.service.intrang.converters.avtalstypevaluator.AvtalstypEvaluator;
import se.metria.markkoll.util.trimble.HarjeanTrimbleMotsvarighetsfilCSVReader;
import se.metria.markkoll.util.trimble.HarjeanTrimbleMotsvarighetsfilRow;
import se.metria.xplore.maputils.GeometryUtil;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
public class TrimbleFeatureToIntrangConverter implements FeatureToIntrangConverter {
	@NonNull
	private final AvtalstypEvaluator avtalstypEvaluator;

	@Override
	public IntrangsStatusDto getIntrangStatus(SimpleFeature feature) {

		var status = getStatusFromFeature(feature);
		var operationS = getOperationSFromFeature(feature);
		var featureType = getTypeNameFromFeature(feature);

		if (featureType.equals("Lägeskarta%20Element") || featureType.equals("Lägeskarta Element")) {
			return IntrangsStatusDto.NY;
		}

		if (status == 32 && operationS.toLowerCase().equals("i bruk")) {
			return IntrangsStatusDto.NY;
		}

		return IntrangsStatusDto.BEVARAS;
	}

    @Override
	public IntrangstypDto getIntrangsTyp(SimpleFeature feature) {

		var classId = getClassIdFromFeature(feature);
		var featureType = getTypeNameFromFeature(feature);

		switch (featureType) {
			case "Lägeskarta%20Element":
			case "Lägeskarta Element":
				return IntrangstypDto.HOGSPANNINGSLEDNING;
			case "LSP%20Element":
			case "LSP Element":
				if (isLuftledning(classId)) {
					return IntrangstypDto.LAGSPANNINGSLEDNING;
				} else {
					log.warn("Okänt ClassID (layer={}, classId={}", feature.getFeatureType().getTypeName(), classId);
					return IntrangstypDto.OKAND;
				}
			case "HSP%20Element":
			case "HSP Element":
				if (isLuftledning(classId)) {
					return IntrangstypDto.HOGSPANNINGSLEDNING;
				} else {
					log.warn("Okänt ClassId (layer={}, classId={}", feature.getFeatureType().getTypeName(), classId);
					return IntrangstypDto.OKAND;
				}
			case "LSP%20Kabelskåp":
			case "LSP Kabelskåp":
				return IntrangstypDto.KABELSKAP;
			case "Nätstation":
				return IntrangstypDto.NATSTATION;
			default:
				log.warn("Okänd Trimble-feature (layer={}, classId={}", feature.getFeatureType().getTypeName(), classId);
				return IntrangstypDto.OKAND;
		}
	}

	@Override
	public IntrangsSubtypDto getIntrangsSubtyp(SimpleFeature feature) {

		var classId = getClassIdFromFeature(feature);
		var featureType = getTypeNameFromFeature(feature);

		switch (featureType) {
			case "Lägeskarta%20Element":
			case "Lägeskarta Element":
				return  IntrangsSubtypDto.MARKLEDNING;
			case "LSP%20Element":
			case "LSP Element":
			case "HSP%20Element":
			case "HSP Element":
				if (isLuftledning(classId)) {
					return IntrangsSubtypDto.LUFTLEDNING;
				} else {
					return IntrangsSubtypDto.NONE;
				}
			default:
				return IntrangsSubtypDto.NONE;
		}
	}

	private boolean isLuftledning(long classId) {

		var motsvarighetsfil = getMotsvarighetsfil();

		if(motsvarighetsfil == null || !motsvarighetsfil.containsKey(classId)) {
			return false;
		}

		var row = motsvarighetsfil.get(classId);
		return row.isLuftledning();
	}

	@Override
	public Double getSpanningsniva(SimpleFeature feature) {

		var classId = getClassIdFromFeature(feature);
		var motsvarighetsfil = getMotsvarighetsfil();

		if(motsvarighetsfil == null || !motsvarighetsfil.containsKey(classId)) {
			return 24.0;
		}

		var row = motsvarighetsfil.get(classId);
		return  row.getSpanningsniva();
	}

	@Override
	public AvtalstypDto getAvtalstyp(SimpleFeature feature) {
		return avtalstypEvaluator.getAvtalstyp(feature, "remark");
	}

	private long getClassIdFromFeature(SimpleFeature feature) {
		return GeometryUtil.extractAttribute(feature, "ClassId", Long.class, -1L).intValue();
	}

	private long getStatusFromFeature(SimpleFeature feature) {
		return GeometryUtil.extractAttribute(feature, "Status", Long.class, -1L);
	}

	private String getOperationSFromFeature(SimpleFeature feature) {
		return GeometryUtil.extractAttribute(feature, "OperationS", String.class, "");
	}

	private String getTypeNameFromFeature(SimpleFeature feature) {
		return feature.getFeatureType().getTypeName();
	}

	private HashMap<Long, HarjeanTrimbleMotsvarighetsfilRow> motsvarighetsfil = null;
	private  HashMap<Long, HarjeanTrimbleMotsvarighetsfilRow> getMotsvarighetsfil() {
		if(motsvarighetsfil == null) {
			try {
				motsvarighetsfil = HarjeanTrimbleMotsvarighetsfilCSVReader.ReadCSVFile();
			}
			catch(IOException exception) {
				log.error("Kunde inte läsa Harjeån Trimble Motsvarighetsfil");
			}
		}
		return  motsvarighetsfil;
	}
}
