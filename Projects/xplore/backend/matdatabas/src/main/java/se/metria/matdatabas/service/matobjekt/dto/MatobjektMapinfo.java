package se.metria.matdatabas.service.matobjekt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static se.metria.matdatabas.db.tables.Grupp.GRUPP;
import static se.metria.matdatabas.db.tables.Matobjekt.MATOBJEKT;
import static se.metria.matdatabas.db.tables.Matningstyp.MATNINGSTYP;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatobjektMapinfo {
    private Integer id;
    private Short typ;
    private String typNamn;
    private String namn;
    private Boolean aktiv;
    private BigDecimal posN;
    private BigDecimal posE;
    private String fastighet;
    private String lage;
    private Short kartsymbol;
    private UUID bifogadBildId;
    private List<Integer> matningstypIds;

    public MatobjektMapinfo(Record source) {
        id = source.get(MATOBJEKT.ID);
        typ = source.get(MATOBJEKT.TYP);
        typNamn = source.get("typNamn",String.class);
        namn = source.get(MATOBJEKT.NAMN);
        aktiv = source.get(MATOBJEKT.AKTIV);
        fastighet = source.get(MATOBJEKT.FASTIGHET);
        lage = source.get(MATOBJEKT.LAGE);
        posN = source.get(MATOBJEKT.POS_N);
        posE = source.get(MATOBJEKT.POS_E);
        kartsymbol = source.get(GRUPP.KARTSYMBOL);
        bifogadBildId = source.get(MATOBJEKT.BIFOGAD_BILD_ID);
        matningstypIds = Collections.singletonList(source.get(MATNINGSTYP.ID));
    }
}
