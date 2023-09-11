package se.metria.markkoll.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import se.metria.markkoll.openapi.model.AvtalsstatusDto;
import se.metria.markkoll.openapi.model.BilagaTypDto;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        addFormatter(registry, AvtalsstatusDto.class, s -> AvtalsstatusDto.fromValue(s.toUpperCase()));
        addFormatter(registry, ProjektTypDto.class, s -> ProjektTypDto.fromValue(s.toUpperCase()));
        addFormatter(registry, Sort.Direction.class, s -> Sort.Direction.valueOf(s.toUpperCase()));
        addFormatter(registry, BilagaTypDto.class, s -> BilagaTypDto.fromValue(s.toUpperCase()));
        addFormatter(registry, RoleTypeDto.class, s -> RoleTypeDto.fromValue(s.toUpperCase()));
    }

    private <T> void
    addFormatter(FormatterRegistry registry, Class<T> clazz, Converter<String, T> converter)
    {
        registry.addConverter(String.class, clazz, converter);
    }
}