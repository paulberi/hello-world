package se.metria.finfo.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import se.metria.finfo.util.FastighetsinformationToSamfallighetEntityMapper;
import se.metria.finfo.util.modelmapper.converter.*;
import se.metria.finfo.util.modelmapper.propertymap.SamfallighetsforeningMap;
import se.metria.finfo.util.modelmapper.propertymap.StyrelsemedlemMap;

@Configuration
public class ModelMapperConfiguration {
    @Bean
    @Primary
    public static ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        modelMapper.addConverter(new StyrelsefunktionConverter());
        modelMapper.addConverter(new StringToUuidConverter());
        modelMapper.addConverter(new XMLGregorianCalenderConverter());
        modelMapper.addConverter(new ForeningstypConverter());
        modelMapper.addConverter(new LanConverter());
        modelMapper.addConverter(new AtgardstypConverter());
        modelMapper.addConverter(new StringToDateConverter());

        modelMapper.addMappings(new SamfallighetsforeningMap());
        modelMapper.addMappings(new StyrelsemedlemMap());

        return modelMapper;
    }

    @Bean
    public static
    FastighetsinformationToSamfallighetEntityMapper fastighetsinformationToSamfallighetEntityMapper() {

        return new FastighetsinformationToSamfallighetEntityMapper();
    }
}
