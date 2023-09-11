package se.metria.xplore.sok.ort.model;

import org.locationtech.jts.geom.Coordinate;

import java.util.Objects;

public class OrtSokResultat {
    private String typ;
    private String ort;
    private String lan;
    private String kommun;
	private Double x;
	private Double y;

	public OrtSokResultat() {

	}

    public OrtSokResultat(String typ, String ort, String kommun, String lan, Coordinate coordinate) {
        this.typ = typ;
        this.ort = ort;
        this.lan = lan;
        this.kommun = kommun;
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getKommun() {
        return kommun;
    }

    public void setKommun(String kommun) {
        this.kommun = kommun;
    }

    public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrtSokResultat that = (OrtSokResultat) o;

        if (!Objects.equals(typ, that.typ)) return false;
        if (!Objects.equals(ort, that.ort)) return false;
        if (!Objects.equals(lan, that.lan)) return false;
        if (!Objects.equals(kommun, that.kommun)) return false;
        if (!Objects.equals(x, that.x)) return false;
        return Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        int result = typ != null ? typ.hashCode() : 0;
        result = 31 * result + (ort != null ? ort.hashCode() : 0);
        result = 31 * result + (lan != null ? lan.hashCode() : 0);
        result = 31 * result + (kommun != null ? kommun.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}