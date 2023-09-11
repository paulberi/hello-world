package se.metria.markkoll.util;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.mockito.ArgumentMatcher;

import static org.mockito.ArgumentMatchers.argThat;

/*
 * Custom matcher för att jämföra alla fält i en klass förutom IDt. Används då dessa IDs är slumpgenererade
 *
 * Något av ett problem med att använda den här matchern är att alla fält i klassen måste implementera CompareTo...
 */
@AllArgsConstructor
public class SkipIdMatcher<T> implements ArgumentMatcher<T> {
    private T left;

    public static<T> T eq(T right) {
        return (T)argThat(new SkipIdMatcher(right));
    }

    @Override
    public boolean matches(T right) {
        return CompareToBuilder.reflectionCompare(left, right, "id") == 0;
    }
}
