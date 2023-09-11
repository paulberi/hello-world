package se.metria.matdatabas.service.inrapportering;

public class ConversionTestParam {
    Double avlastVarde;
    Double convertedVarde;

    public ConversionTestParam(Double avlastVarde, Double convertedVarde) {
        this.avlastVarde = avlastVarde;
        this.convertedVarde = convertedVarde;
    }

    public Double getAvlastVarde() {
        return avlastVarde;
    }

    public Double getConvertedVarde() {
        return convertedVarde;
    }
}
