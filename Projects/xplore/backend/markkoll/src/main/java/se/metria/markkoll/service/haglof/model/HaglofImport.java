package se.metria.markkoll.service.haglof.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "Object")
@NoArgsConstructor
public class HaglofImport {
    @JsonProperty("Properties")
    private List<HaglofFastighet> fastigheter = new ArrayList<>();

    @JsonUnwrapped
    HaglofMetadata metadata = new HaglofMetadata();
}
