package se.metria.xplore.sok.ort.model;

import org.apache.commons.lang3.StringUtils;
import org.locationtech.jts.geom.Coordinate;
import se.metria.xplore.maputils.CqlEscape;
import se.metria.xplore.sok.wfs.WfsRequest;

import java.util.List;
import java.util.Objects;

public class OrtRequest extends WfsRequest {

    private static final String QUERY_TEMPLATE = "service=WFS&version=1.1.0&request=GetFeature&typeName=metria:GSD_ORTNAMN_LNKN_UCASE&cql_filter={cql_filter}&maxFeatures=100";

    protected String kommun;
    protected String lan;
    protected Coordinate coordinate;
    List<String> detaljtyper;

    public OrtRequest(String query, String kommun, String lan, Double x, Double y) {
        this(query, kommun, lan, x, y, null);
    }

    public OrtRequest(String query, String kommun, String lan, Double x, Double y, List<String> detaljtyper) {
        setQuery(query);
        setKommun(kommun);
        setLan(lan);
        setCoordinate(x, y);
        setDetaljtyper(detaljtyper);
    }

    public String getKommun() {
        return kommun;
    }

    public void setKommun(String kommun) {
        if (kommun != null) {
            this.kommun = kommun.trim();

            if (this.kommun.isEmpty()) {
                throw new IllegalArgumentException("Invalid kommun");
            }
        }
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        if (lan != null) {
            this.lan = lan.trim();

            if (this.lan.isEmpty()) {
                throw new IllegalArgumentException("Invalid län");
            }
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Double x, Double y) {
        try {
            if (x != null || y != null) {
                this.coordinate = new Coordinate(x, y);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
    }

    public List<String> getDetaljtyper() {
        return detaljtyper;
    }

    public void setDetaljtyper(List<String> detaljtyper) {
        if (detaljtyper != null) {
            detaljtyper.replaceAll(String::toUpperCase);
            this.detaljtyper = detaljtyper;
        }
    }

    public String getWfsQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public String getCqlFilter() {
        String cql_filter = String.format("ORTNAMN LIKE '%s%s'", CqlEscape.cqlEscape(query.toUpperCase()), (query.length() > 2 ? "%%" : ""));

        if (!StringUtils.isEmpty(kommun)) {
            if (kommun.contains(",")) {
                String[] kommuner = CqlEscape.cqlEscape(kommun.toUpperCase()).split(",");

                cql_filter += String.format(" AND KOMMUNNAMN IN ('%s')", String.join("','", kommuner));
            } else {
                cql_filter += String.format(" AND KOMMUNNAMN LIKE '%s%%'", CqlEscape.cqlEscape(kommun.toUpperCase()));
            }
        }

        if (!StringUtils.isEmpty(lan)) {
            cql_filter += String.format(" AND LAN LIKE '%s%%'", CqlEscape.cqlEscape(lan.toUpperCase()));
        }

        if (detaljtyper != null && !detaljtyper.isEmpty()) {
            cql_filter += String.format(" AND DETALJTYP IN ('%s')", String.join("','", detaljtyper));
        }

        return cql_filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrtRequest that = (OrtRequest) o;

        if (!Objects.equals(query, that.query)) return false;
        if (!Objects.equals(kommun, that.kommun)) return false;
        if (!Objects.equals(lan, that.lan)) return false;
        if (!Objects.equals(coordinate, that.coordinate)) return false;
        return Objects.equals(detaljtyper, that.detaljtyper);
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + (kommun != null ? kommun.hashCode() : 0);
        result = 31 * result + (lan != null ? lan.hashCode() : 0);
        result = 31 * result + (coordinate != null ? coordinate.hashCode() : 0);
        result = 31 * result + (detaljtyper != null ? detaljtyper.hashCode() : 0);
        return result;
    }
}
