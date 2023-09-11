package se.metria.markkoll.service.haglof.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HaglofEvaluation {
    @JsonProperty("Rotvalue")
    private Integer rotvalue;

    @JsonProperty("Landvalue")
    private Integer landvalue;

    @JsonProperty("Earlycutvalue")
    private Integer earlycutvalue;

    @JsonProperty("Stormdryvalue")
    private Integer stormdryvalue;

    @JsonProperty("Bordertreevalue")
    private Integer bordertreevalue;

    @JsonProperty("Highcutvalue")
    private int highcutvalue;
}
