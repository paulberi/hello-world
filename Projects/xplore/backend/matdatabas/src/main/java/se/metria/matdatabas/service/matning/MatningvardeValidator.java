package se.metria.matdatabas.service.matning;


import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.dto.DefinitionMatningstyp;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;

import static se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp.*;

@Component
public class MatningvardeValidator {
	
	private static BiFunction<Matningstyp, Double, Boolean> alwaysTrue = (mt, av) -> true;
	private static BiFunction<Matningstyp, Double, Boolean> greaterOrEqualsToZero = (mt, av) -> av >= 0d;
	
	Map<Berakningstyp, BiFunction<Matningstyp, Double, Boolean>> valFunctions = Map.of(
			NIVA_NEDMATNING, (mt, av) -> 0d < av && av <= mt.getMaxPejlbartDjup(),
			NIVA_PORTRYCK, (mt, av) -> av < 200d,
			INFILTRATION_MOMENTANT_FLODE, greaterOrEqualsToZero,
			INFILTRATION_MEDEL_FLODE, greaterOrEqualsToZero,
			TUNNELVATTEN_MOMENTANT_FLODE, greaterOrEqualsToZero
	);
	
	public boolean validate(DefinitionMatningstyp definitionMatningstyp, Matningstyp matningstyp, Double avlastVarde) {
		if (avlastVarde == null) {
			return false;
		}

		if (definitionMatningstyp.getBerakningstyp() == null) {
			return true;
		}

		return valFunctions.getOrDefault(definitionMatningstyp.getBerakningstyp(), alwaysTrue).apply(matningstyp, avlastVarde);
	}
}
