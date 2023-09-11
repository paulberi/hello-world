package se.metria.markkoll;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import se.metria.markkoll.config.ModelMapperConfiguration;
import se.metria.markkoll.config.WebMvcConfiguration;

@TestConfiguration
@RequiredArgsConstructor
@Import({ModelMapperConfiguration.class, WebMvcConfiguration.class})
public class MarkkollTestConfiguration {
}
