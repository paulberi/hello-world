package se.metria.markkoll.util.dokument.forteckninggenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForteckningTemplateData {
    @JsonProperty("mMk-mOtTaGaRe-OrGaNiSaTiOnSnUmMeR") // Case insensitive
    private String mottagareOrgnummer;

    @JsonProperty("MMK-Mottagare-Namn")
    private String mottagareNamn;

    @JsonProperty("MMK-Bank-Land")
    private String bankLand;

    // Testa så att Excelgeneratorn läser properties istället för fält
    public String getMottagareOrgnummer() {
        return "<<" + mottagareOrgnummer + ">>";
    }
}
