package se.metria.markkoll.util.trimble;

import lombok.*;

@Data
@AllArgsConstructor
public class HarjeanTrimbleMotsvarighetsfilRow {

    private long classId;
    private String ledningskonstruktion;
    private double spanningsniva;
    private String kostnadskod;

    public boolean isLuftledning()
    {
        return (ledningskonstruktion.equals("aercbl") || ledningskonstruktion.equals("overh")  || ledningskonstruktion.equals("PAS"));
    }
}
