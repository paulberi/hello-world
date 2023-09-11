package se.metria.markkoll.util;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.argThat;

@RequiredArgsConstructor
public class ResourceByteMatcher implements ArgumentMatcher<Resource> {
    @NonNull
    Resource left;

    @Override
    public boolean matches(Resource right) {
        try {
            return Arrays.equals(left.getInputStream().readAllBytes(), right.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Resource eq(Resource resource) {
        return argThat(new ResourceByteMatcher(resource));
    }
}
