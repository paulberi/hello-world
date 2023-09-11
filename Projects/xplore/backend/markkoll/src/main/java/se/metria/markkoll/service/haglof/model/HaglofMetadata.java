package se.metria.markkoll.service.haglof.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.metria.markkoll.service.haglof.HaglofDateDeserializer;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HaglofMetadata {
    @JsonProperty("Date")
    @JsonDeserialize(using = HaglofDateDeserializer.class)
    private LocalDateTime varderingstidpunkt;

    @JsonProperty("PerformedBy")
    private String varderingsmanOchForetag = "";

    @JsonProperty("Nr")
    private String projektnummer = "";
}
