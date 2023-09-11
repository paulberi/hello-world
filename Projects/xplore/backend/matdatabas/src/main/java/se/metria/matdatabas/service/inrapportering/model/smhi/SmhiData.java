package se.metria.matdatabas.service.inrapportering.model.smhi;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SmhiData {
    long from;
    long to;
    long date;
    String ref;
    double value;
    String quality;
}
