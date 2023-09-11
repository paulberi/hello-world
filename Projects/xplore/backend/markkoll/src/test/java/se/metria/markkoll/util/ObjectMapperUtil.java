package se.metria.markkoll.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class ObjectMapperUtil {
    public static MultiValueMap toQueryParams(ObjectMapper objectMapper, Object object) {
        MultiValueMap valueMap = new LinkedMultiValueMap<String, Object>();
        Map<String, String> fieldMap = objectMapper.convertValue(object, new TypeReference<Map<String, String>>() {});
        valueMap.setAll(fieldMap);

        return valueMap;
    }
}
