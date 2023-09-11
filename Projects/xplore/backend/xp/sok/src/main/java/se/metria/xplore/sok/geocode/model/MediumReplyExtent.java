package se.metria.xplore.sok.geocode.model;

public class MediumReplyExtent {
	private String address;
	private String mismatch;
	private Double rank;
	private Double similarity;
	private String subtype;
	private String type;
	private Double x;
	private Double y;

	public MediumReplyExtent() {

	}

	public String getAddress() {
		return address;
	}

	public String getMismatch() {
		return mismatch;
	}

	public Double getRank() {
		return rank;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public String getSubtype() {
		return subtype;
	}

	public String getType() {
		return type;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}
}