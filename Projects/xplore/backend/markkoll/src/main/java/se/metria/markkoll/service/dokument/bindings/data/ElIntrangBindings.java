package se.metria.markkoll.service.dokument.bindings.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElIntrangBindings {
    @Builder.Default
    @DocProperty("MMK-Transformatorstation-ja-nej")
    private Boolean hasTransformatorstation = false;

    @Builder.Default
    @DocProperty("MMK-Transformatorstation-namn")
    private String transformatorstationNamn = "";

    @Builder.Default
    @DocProperty("MMK-Transformatorstation-bredd-a-meter")
    private Integer transformatorstationBreddAMeter = 0;

    @Builder.Default
    @DocProperty("MMK-Transformatorstation-bredd-b-meter")
    private Integer transformatorstationBreddBMeter = 0;

    @Builder.Default
    @DocProperty("MMK-Transformatorstation-yta")
    private String transformatorstationYta = "0x0m";

    @DocProperty("MMK-Ledningslittera")
    private String ledningslittera;

    @DocProperty("MMK-Hogsta-spanning")
    private String hogstaSpanning;
}
