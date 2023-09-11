package se.metria.xplore.samrad.commons.modelMapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;


/*

* Har en dependency i pommes
**/
public class ModelMapping {

    public static <T> void mapAndMerge(T source, T target){
        ModelMapper mapper= new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.map(source, target);
    }
}
