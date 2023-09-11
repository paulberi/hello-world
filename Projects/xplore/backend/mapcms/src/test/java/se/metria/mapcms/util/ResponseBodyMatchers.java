package se.metria.mapcms.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.ResultMatcher;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * Från Markkoll
 *
 * Compare objects at the web controller layer, without having to compare JSON strings.
 *
 * https://reflectoring.io/spring-boot-web-controller-test/
 */
public class ResponseBodyMatchers {
    private static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public <T> ResultMatcher containsObjectAsJson(
            Object expectedObject,
            Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).isEqualTo(expectedObject);
        };
    }

    /* Om du försöker använda containsObjectAsJson med listor så kommer du att stöta på patrull, då elementen i
    * listorna inte kommer att bli mappade. */
    public <T> ResultMatcher containsListAsJson(
            List<T> expectedList,
            Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            Object[] objects = objectMapper.readValue(json, Object[].class);
            List<T> actualList = actualList(objects, targetClass);

            assertThat(actualList).isEqualTo(expectedList);
        };
    }

    public static ResponseBodyMatchers responseBody(){
        return new ResponseBodyMatchers();
    }

    private <T> List<T> actualList(Object[] objects, Class<T> targetClass) throws Exception {
        List<T> listActual = new ArrayList<>();
        for (var obj: objects) {
            var jsonObject = objectMapper.writeValueAsBytes(obj);
            T pojo = objectMapper.readValue(jsonObject, targetClass);
            listActual.add(pojo);
        }

        return listActual;
    }
}
