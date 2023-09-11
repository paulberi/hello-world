package se.metria.xplore.skogligagrunddata.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Grunddata {
    public double volSum;
    public double volMedel;
    public double hgvMedel;
    public double gyMedel;
    public double dgvMedel;
    public double areaHa;
    public double areaProduktivHa;
    public double areaSkogHa;
    public int referensAr;
    public boolean lov;
    public String referensArIntervall;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double volSumExklAvv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double volMedelExklAvv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double hgvMedelExklAvv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double gyMedelExklAvv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double dgvMedelExklAvv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double areaProduktivHaExklAvv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Double areaSkogHaExklAvv;
}
