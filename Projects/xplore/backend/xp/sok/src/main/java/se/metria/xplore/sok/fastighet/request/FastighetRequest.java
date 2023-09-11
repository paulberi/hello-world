package se.metria.xplore.sok.fastighet.request;

import se.metria.xplore.sok.wfs.WfsRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class FastighetRequest extends WfsRequest {

    protected List<String> kommun;

    public FastighetRequest(String query, String[] kommun) {
        setQuery(query);
        setKommun(kommun);
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

    public String getKommunCqlFormatted() {
        return kommun.stream().collect(Collectors.joining("','", "'", "'"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FastighetRequest that = (FastighetRequest) o;

        if (!Objects.equals(query, that.query)) return false;
        return Objects.equals(kommun, that.kommun);
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + (kommun != null ? kommun.hashCode() : 0);
        return result;
    }

}