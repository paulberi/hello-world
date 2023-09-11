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
public class ErsattningBindings {
    @DocProperty("MMK-Ersättningsbelopp")
    private int ersattningsbelopp;

    @DocProperty("MMK-Rotnetto")
    private double rotnetto;

    @DocProperty("MMK-Rotnetto-Moms")
    private double rotnettoMoms;

    @DocProperty("MMK-Rubrik-Utanordning-Självfaktura")
    @Builder.Default
    private String rubrikUtanordningSjalvfaktura = "Utanordning";
}
