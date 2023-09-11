package se.metria.xplore.sok.fastighet.model;

public class GeometryRequest {
    public String wkt;
    public GeometryOperation operation;
    public Double bufferDistance;

    public GeometryRequest(String wkt, GeometryOperation operation) {
        this.wkt = wkt;
        this.operation = operation;
    }
    public GeometryRequest(String wkt, GeometryOperation operation, Double bufferDistance) {
        this.wkt = wkt;
        this.operation = operation;
        this.bufferDistance = bufferDistance;
    }
}
