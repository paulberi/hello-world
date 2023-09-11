package se.metria.xplore.sok.geocode.request;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class GeocodeRequest {

    protected String query;
    protected Double x;
    protected Double y;
    protected List<String> kommun;
    protected String maskUrl;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        if (query != null) {
            this.query = query.trim();
        }

        if (StringUtils.isEmpty(query)) {
            throw new IllegalArgumentException("Invalid query");
        }
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

    public List<String> getKommun() {
        return kommun;
    }

    public void setKommun(String[] kommun) {
        if (kommun != null) {
            this.kommun = Arrays.stream(kommun)
                    .map(String::trim)
					.map(String::toUpperCase)
                    .collect(Collectors.toList());
        }
    }

    public boolean hasKommun() {
        return kommun != null && !kommun.isEmpty();
    }
    public String getMaskUrl() {
        return maskUrl;
    }

    public void setMaskUrl(String maskUrl) {
        this.maskUrl = maskUrl;
    }

    public abstract String getQueryTemplate();

    public abstract Object[] getQueryParameters();

    public String getAddress() {
		if (hasKommun()) {
			String paddedQuery = " " + query.toUpperCase().replaceAll(",", " , ") + " ";
			String paddedKommun = " " + kommun.get(0) + " ";

			if (paddedQuery.contains(paddedKommun)) {
				paddedQuery = paddedQuery.replaceAll(paddedKommun, "");
			}
			paddedQuery = paddedQuery.replaceAll("\\s*,\\s+", ",");
			paddedQuery = paddedQuery.trim();

			// 1119 är koden för kommuner enligt geokodningstjänstens API
			return kommun.get(0) + "|1119," + paddedQuery;
		}

		return query;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeocodeRequest request = (GeocodeRequest) o;

        if (!Objects.equals(query, request.query)) return false;
        if (!Objects.equals(x, request.x)) return false;
        if (!Objects.equals(y, request.y)) return false;
        if (!Objects.equals(kommun, request.kommun)) return false;
        return Objects.equals(maskUrl, request.maskUrl);
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (kommun != null ? kommun.hashCode() : 0);
        result = 31 * result + (maskUrl != null ? maskUrl.hashCode() : 0);
        return result;
    }
}