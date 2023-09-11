package se.metria.markkoll.matchers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.mockito.ArgumentMatcher;
import org.springframework.data.domain.PageRequest;

import static org.mockito.ArgumentMatchers.argThat;

@RequiredArgsConstructor
public class PageRequestMatcher implements ArgumentMatcher<PageRequest> {
    @NonNull
    PageRequest left;

    public static PageRequest eq(PageRequest left) {
        return argThat(new PageRequestMatcher(left));
    }

    @Override
    public boolean matches(PageRequest right) {
        return left.getPageSize() == right.getPageSize() && left.getPageNumber() == right.getPageNumber();
    }
}
