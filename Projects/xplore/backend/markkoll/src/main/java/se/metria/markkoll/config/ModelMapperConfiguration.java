package se.metria.markkoll.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.markagare.MarkagareEntity;
import se.metria.markkoll.entity.markagare.PersonEntity;
import se.metria.markkoll.entity.samfallighet.BerordAvAtgard;
import se.metria.markkoll.entity.samfallighet.SamfallighetMerInfoEntity;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.finfo.model.FinfoBerordAvAtgardDto;
import se.metria.markkoll.openapi.finfo.model.FinfoSamfallighetsforeningDto;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.openapi.model.MarkagareInfoDto;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.util.FodelsedatumEllerOrganisationsnummerConverter;
import se.metria.markkoll.util.dokument.hms_generator.Entry;
import se.metria.markkoll.util.modelmapper.AtgardConverter;
import se.metria.markkoll.util.modelmapper.DelagandeFastighetConverter;
import se.metria.markkoll.util.modelmapper.ElnatVpConverter;
import se.metria.markkoll.util.modelmapper.FiberVpConverter;

@Configuration
@Slf4j
public class ModelMapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        org.modelmapper.Converter<String, ProjektTypDto> projektTypConverter = ctx -> projektTypConverter(ctx.getSource());
        modelMapper.addConverter(projektTypConverter);

        Converter<String, String> namnConverter = ctx -> namnParser(ctx.getSource());

        modelMapper
                .typeMap(MarkagareInfoDto.class, MarkagareEntity.class)
                .addMappings(markagareInfoDtoMarkagareEntityPropertyMap());

        // ModelMapper försöker mappa avtal till inkluderaIAvtal. Så ska vi inte ha det.
        modelMapper.getConfiguration().setImplicitMappingEnabled(false);
        modelMapper
                .createTypeMap(AvtalspartEntity.class, MarkagareDto.class)
                .addMappings(avtalspartEntityMarkagareDtoPropertyMap())
                .addMappings(mapper -> mapper.using(new FodelsedatumEllerOrganisationsnummerConverter())
                    .map(AvtalspartEntity::getMarkagare, MarkagareDto::setFodelsedatumEllerOrgnummer));
        modelMapper.getConfiguration().setImplicitMappingEnabled(true);
        modelMapper.addConverter(new ElnatVpConverter());
        modelMapper.addConverter(new FiberVpConverter());

        modelMapper.addConverter(new DelagandeFastighetConverter());

        modelMapper
            .createTypeMap(FinfoSamfallighetsforeningDto.class, SamfallighetMerInfoEntity.class)
            .addMappings(samfMerInfoMap());

        modelMapper
            .createTypeMap(FinfoBerordAvAtgardDto.class, BerordAvAtgard.class)
            .addMappings(BerordAvAtgardMap());

        modelMapper.createTypeMap(PersonEntity.class, Entry.class)
            .addMappings(personEntityEntryPropertyMap());

        return modelMapper;
    }

    private PropertyMap<FinfoSamfallighetsforeningDto, SamfallighetMerInfoEntity> samfMerInfoMap() {
        return new PropertyMap<FinfoSamfallighetsforeningDto, SamfallighetMerInfoEntity>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                skip(destination.getSamfallighet());
                skip(destination.getKund());
                skip(destination.getAndamal());
                skip(destination.getForvaltandeBeteckning());
                skip(destination.getRattighet());
                skip(destination.getBerordAvAtgard());
                skip(destination.getPagaendeFastighetsbildning());
                map().setOrganisationsnummer(source.getOrgnr());
            }
        };
    }

    private PropertyMap<FinfoBerordAvAtgardDto, BerordAvAtgard> BerordAvAtgardMap() {
        return new PropertyMap<FinfoBerordAvAtgardDto, BerordAvAtgard>() {
            @Override
            protected void configure() {
                using(new AtgardConverter()).map(source.getAtgarder(), destination.getAtgard());
            }
        };
    }

    private PropertyMap<PersonEntity, Entry> personEntityEntryPropertyMap() {
        return new PropertyMap<PersonEntity, Entry>() {
            @Override
            protected void configure() {
                map().setCo(source.getCoAdress());
                map().setGatuadress(source.getAdress());
                map().setPostnr(source.getPostnummer());
                map().setTelefonArbete(source.getTelefon());
            }
        };
    }

    private PropertyMap<MarkagareInfoDto, MarkagareEntity> markagareInfoDtoMarkagareEntityPropertyMap() {
        return new PropertyMap<MarkagareInfoDto, MarkagareEntity>() {
            @Override
            protected void configure() {
                map().getPerson().setPostort(source.getPostort());
                map().getPerson().setNamn(source.getNamn());
                map().getPerson().setAdress(source.getAdress());
                map().getPerson().setBankkonto(source.getBankkonto());
                map().getPerson().setPostnummer(source.getPostnummer());
                map().getPerson().setEPost(source.getePost());
                map().getPerson().setPersonnummer(source.getFodelsedatumEllerOrgnummer());
                map().getPerson().setTelefon(source.getTelefon());
                map().setAgartyp(source.getAgartyp());
                map().setAndel(source.getAndel());
            }
        };
    }

    private PropertyMap<AvtalspartEntity, MarkagareDto> avtalspartEntityMarkagareDtoPropertyMap() {
        return new PropertyMap<AvtalspartEntity, MarkagareDto>() {
            @Override
            protected void configure() {
                map().setId(source.getId());
                map().setInkluderaIAvtal(source.isInkluderaIAvtal());
                map().setAgareStatus(source.getAvtalsstatus());

                map().setAgartyp(source.getMarkagare().getAgartyp());
                map().setAndel(source.getMarkagare().getAndel());

                map().setBankkonto(source.getMarkagare().getPerson().getBankkonto());
                map().setePost(source.getMarkagare().getPerson().getEPost());
                map().setAdress(source.getMarkagare().getPerson().getAdress());
                map().setCoAdress(source.getMarkagare().getPerson().getCoAdress());
                map().setNamn(source.getMarkagare().getPerson().getNamn());
                map().setPostnummer(source.getMarkagare().getPerson().getPostnummer());
                map().setPostort(source.getMarkagare().getPerson().getPostort());
                map().setTelefon(source.getMarkagare().getPerson().getTelefon());
                map().setUtbetalningsdatum(source.getUtbetalningsdatum());
                map().setLand(source.getMarkagare().getPerson().getLand());
            }
        };
    }

    private ProjektTypDto projektTypConverter(String s) {
        return ProjektTypDto.fromValue(s);
    }

    private String namnParser(String namn) throws MarkkollException {
        if (namn == null) {
            return "Okänt";
        }

        var efternamn_fornamn = namn.split(",");
        if (efternamn_fornamn.length != 2) {
            // Skulle kunna vara namnet på företag, stiftelse, eller något helt annat.
            return namn;
        }

        return efternamn_fornamn[1].strip() + " " + efternamn_fornamn[0].strip();
    }
}
