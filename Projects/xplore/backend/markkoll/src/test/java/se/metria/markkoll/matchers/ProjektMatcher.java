package se.metria.markkoll.matchers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import se.metria.markkoll.openapi.model.ProjektInfoDto;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;

@RequiredArgsConstructor
public class ProjektMatcher implements ArgumentMatcher<ProjektInfoDto>  {
    @NonNull
    ProjektInfoDto left;

    public static ProjektInfoDto eq(ProjektInfoDto left) {
        return argThat(new ProjektMatcher(left));
    }

    @Override
    public boolean matches(ProjektInfoDto right) {
        // Jämför alla fält förutom file. Något med blobs gör att jämförelser visst inte fungerar.
        return  Objects.equals(left.getNamn(), right.getNamn()) &&
                Objects.equals(left.getOrt(), right.getOrt()) &&
                Objects.equals(left.getProjektTyp(), right.getProjektTyp()) &&
                Objects.equals(left.getBeskrivning(), right.getBeskrivning());
    }
}
