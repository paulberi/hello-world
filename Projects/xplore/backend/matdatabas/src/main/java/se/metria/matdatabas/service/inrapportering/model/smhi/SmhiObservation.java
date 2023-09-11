package se.metria.matdatabas.service.inrapportering.model.smhi;

import lombok.Data;
import lombok.ToString;

import java.util.Collection;

/**
 * Kapslar in ett data-element från SMHIs metobs-API.
 */
@Data
@ToString
public class SmhiObservation {
    Collection<SmhiData> value;
    SmhiStation station;
    SmhiParameter parameter;
}
