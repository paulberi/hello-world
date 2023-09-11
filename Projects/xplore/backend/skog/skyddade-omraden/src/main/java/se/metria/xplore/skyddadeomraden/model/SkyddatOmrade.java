package se.metria.xplore.skyddadeomraden.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SkyddatOmrade {
    public String skyddsform;
    public String id;
    public String namn;
    public double areaHa;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String url;
}
