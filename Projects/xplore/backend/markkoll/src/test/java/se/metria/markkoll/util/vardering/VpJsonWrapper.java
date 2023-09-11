package se.metria.markkoll.util.vardering;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;

@Data
public class VpJsonWrapper {
    @JsonIgnoreProperties(ignoreUnknown = true)
    ElnatVarderingsprotokollDto varderingsprotokoll;
}