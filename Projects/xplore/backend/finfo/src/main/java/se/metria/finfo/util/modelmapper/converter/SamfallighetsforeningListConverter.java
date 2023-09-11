package se.metria.finfo.util.modelmapper.converter;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.data.util.Pair;
import se.metria.finfo.entity.agare.ForvaltningsobjektEntity;
import se.metria.finfo.fsokws.Forvaltning;
import se.metria.finfo.fsokws.ForvaltningsobjektsAndamal;
import se.metria.finfo.fsokws.Samfallighetsforening;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SamfallighetsforeningListConverter
    implements Converter <Samfallighetsforening, List<ForvaltningsobjektEntity>> {

    @Override
    public List<ForvaltningsobjektEntity> convert(MappingContext<Samfallighetsforening, List<ForvaltningsobjektEntity>> mappingContext) {
        return foo(mappingContext.getSource());
    }

    private List<ForvaltningsobjektEntity> foo(Samfallighetsforening samf) {
        var forvaltningEntityList = new ArrayList<ForvaltningsobjektEntity>();

        for (var forvaltningPair: getForvaltningPair(samf)) {
            var forvaltning = forvaltningPair.getFirst();
            var andamal = forvaltningPair.getSecond();

            var forvaltningEntity = new ForvaltningsobjektEntity();
            if (andamal.isPresent()) {
                forvaltningEntity.setAndamalstyp(andamal.get().getAndamalstyp().getValue());
            }
            forvaltningEntity.setAnmarkning(forvaltning.getAnmarkning());
            forvaltningEntity.setObjektsinformation(forvaltning.getObjektsinformation());

            forvaltningEntityList.add(forvaltningEntity);
        }

        return forvaltningEntityList;
    }

    /* Förvaltningsobjekt och förvaltningsobjektsändamål representerar samma objekt om de har samma
       objektslöpnummer.

       Jag vet inte (än) om alla förvaltningsobjekt alltid har ett motsvarande ändamål, och/eller om de alltid
       är sorterade efter objektlöpnummer, så jag gör linjära sökningar på objektslöpnumret och parar ihop objekten. */
    private List<Pair<Forvaltning, Optional<ForvaltningsobjektsAndamal>>>
    getForvaltningPair(Samfallighetsforening samfallighetsforening) {
        return samfallighetsforening.getForvaltningsobjekt().stream()
            .map(forvaltningsobjekt -> {
                var andamal = samfallighetsforening.getForvaltningsobjektsAndamal().stream()
                    .filter(a -> a.getObjektslopnummer().equals(forvaltningsobjekt.getObjektslopnummer()))
                    .findAny();
                return Pair.of(forvaltningsobjekt, andamal);
            })
            .collect(Collectors.toList());
    }
}
