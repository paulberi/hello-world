package se.metria.markkoll.service.haglof.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HaglofOwner {
    @JsonProperty("Name")
    private String namn;

    @JsonProperty("Bankaccount")
    private String bankkonto;

    @JsonProperty("Bankgiro")
    private String bankgiro;

    @JsonProperty("Email")
    private String ePost;

    @JsonProperty("OrgNo")
    private String personnummer;

    @JsonProperty("Phonehome")
    private String telefonHem;

    @JsonProperty("Phonework")
    private String telefonArbete;

    @JsonProperty("Mobilephone")
    private String mobiltelefon;

    @JsonProperty("Plusgiro")
    private String plusgiro;
}
