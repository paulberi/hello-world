package se.metria.matdatabas.service.inrapportering.model.smhi;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SmhiParameter {
    String key;
    String name;
    String summary;
    String unit;
}