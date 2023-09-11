package se.metria.markkoll.service.haglof;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HaglofDateDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        var date = jsonParser.getText();

        if (date.length() < 16) {
            return null;
        }

        var dateMin = date.substring(0, 16);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return LocalDateTime.parse(dateMin, formatter);
    }
}
