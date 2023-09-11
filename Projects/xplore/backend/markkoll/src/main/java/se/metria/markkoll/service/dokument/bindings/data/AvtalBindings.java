package se.metria.markkoll.service.dokument.bindings.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Data
@Builder
public class AvtalBindings {
    @DocProperty("MMK-Fastighetsbeteckning")
    private String fastighetsbeteckning;

    @DocProperty("MMK-Kartor-multi")
    private List<ByteArrayResource> kartor_multi;

    @DocProperty("MMK-Kommun")
    private String kommun;

    @DocProperty("MMK-Station-fran")
    private String stationFran;

    @DocProperty("MMK-Station-till")
    private String stationTill;

    @DocProperty("MMK-Stationsnamn")
    private String stationsnamn;

    @DocProperty("MMK-Markslag")
    private String markslag;

    @DocProperty("MMK-Inlöst-bredd-meter")
    private Integer inlostBreddMeter;

    @DocProperty("MMK-Inlöst-bredd-skog-meter")
    private Integer inlostBreddSkogMeter;

    @DocProperty("MMK-Inlöst-bredd-skog-luft-meter")
    private Integer inlostBreddSkogLuftMeter;

    @DocProperty("MMK-Län")
    private String lan;

    @DocProperty("MMK-Direktförlagd-kabel")
    private Boolean direktforlagdKabel;

    @DocProperty("MMK-Handläggare")
    private String handlaggare;

    @DocProperty("MMK-Rotnetto-Ägarens-Val")
    private Boolean rotnettoAgarensVal;

    @DocProperty("MMK-Egettillvaratagande-Ägarens-Val")
    private Boolean egetTillvaratagandeAgarensVal;
}