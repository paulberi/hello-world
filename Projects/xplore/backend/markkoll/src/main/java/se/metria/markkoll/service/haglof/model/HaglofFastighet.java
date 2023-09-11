package se.metria.markkoll.service.haglof.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HaglofFastighet {
    @JsonProperty("Name")
    String fastighetsbeteckning;

    String kommun;

    @JsonGetter("Municipal")
    public String getKommun()
    {
        return kommun;
    }

    // Make sure Municipal is uppercase to so that we can match it with the data from finfo.
    @JsonSetter("Municipal")
    public void setKommun(String kommun)
    {
        this.kommun = kommun.toUpperCase();
    }

    @JsonProperty("Evaluation")
    HaglofEvaluation evaluation = new HaglofEvaluation();

    @JsonProperty("TypeOfAction")
    Tillvaratagande tillvaratagande = Tillvaratagande.EJ_VALT;

    @JsonProperty("Owners")
    List<HaglofOwner> owners = new ArrayList<>();

    @JsonProperty("Nr")
    String fastighetsnummer;
}