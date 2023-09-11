package se.metria.markkoll.util.trimble;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class HarjeanTrimbleMotsvarighetsfilCSVReader  {
    public static HashMap<Long, HarjeanTrimbleMotsvarighetsfilRow> ReadCSVFile() throws IOException {
        var inputStream = new ClassPathResource("harjean_trimble_csv/harjean_trimble_motsvarighetsfil.csv").getInputStream();
        return HarjeanTrimbleMotsvarighetsfilParser.ParseStream(inputStream);
    }
}

