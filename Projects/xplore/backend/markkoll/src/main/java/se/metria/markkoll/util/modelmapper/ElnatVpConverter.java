package se.metria.markkoll.util.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.BeanUtils;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollConfigEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollMetadataEntity;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollDto;
import se.metria.markkoll.util.CollectionUtil;

public final class ElnatVpConverter implements Converter<ElnatVarderingsprotokollDto, ElnatVarderingsprotokollEntity> {
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public ElnatVarderingsprotokollEntity
    convert(MappingContext<ElnatVarderingsprotokollDto, ElnatVarderingsprotokollEntity> context) {
        var dto = context.getSource();
        var entity = context.getDestination() == null ? new ElnatVarderingsprotokollEntity() :
            context.getDestination();

        BeanUtils.copyProperties(dto, entity);

        if (dto.getConfig() != null) {
            entity.setConfig(modelMapper.map(dto.getConfig(), ElnatVarderingsprotokollConfigEntity.class));
        }
        if (dto.getMetadata() != null) {
            entity.setMetadata(modelMapper.map(dto.getMetadata(), ElnatVarderingsprotokollMetadataEntity.class));
        }

        CollectionUtil.emptyCollection(entity.getPunktersattning());
        if (dto.getPunktersattning() != null) {
            for (var punktersattning : dto.getPunktersattning()) {
                entity.addPunktersattning(punktersattning.getBeskrivning(), punktersattning.getAntal(), punktersattning.getTyp());
            }
        }

        CollectionUtil.emptyCollection(entity.getLedningSkogsmark());
        if (dto.getLedningSkogsmark() != null) {
            for (var ledningSkogsmark : dto.getLedningSkogsmark()) {
                entity.addLedningSkogsmark(ledningSkogsmark.getBeskrivning(), ledningSkogsmark.getErsattning());
            }
        }

        CollectionUtil.emptyCollection(entity.getSsbSkogsmark());
        if (dto.getSsbSkogsmark() != null) {
            for (var ssbSkogsmark : dto.getSsbSkogsmark()) {
                entity.addSsbSkogsmark(ssbSkogsmark.getBeskrivning(), ssbSkogsmark.getLangd(), ssbSkogsmark.getBredd());
            }
        }

        CollectionUtil.emptyCollection(entity.getSsbVaganlaggning());
        if (dto.getSsbVaganlaggning() != null) {
            for (var ssbVaganlaggning : dto.getSsbVaganlaggning()) {
                entity.addSsbVaganlaggning(ssbVaganlaggning.getBeskrivning(), ssbVaganlaggning.getLangd(),
                    ssbVaganlaggning.getZon());
            }
        }

        CollectionUtil.emptyCollection(entity.getMarkledning());
        if (dto.getMarkledning() != null) {
            for (var markledning : dto.getMarkledning()) {
                entity.addMarkledning(markledning.getBeskrivning(), markledning.getLangd(), markledning.getBredd());
            }
        }

        CollectionUtil.emptyCollection(entity.getOvrigtIntrang());
        if (dto.getOvrigtIntrang() != null) {
            for (var ovrigtIntrang : dto.getOvrigtIntrang()) {
                entity.addOvrigtIntrang(ovrigtIntrang.getBeskrivning(), ovrigtIntrang.getErsattning());
            }
        }

        CollectionUtil.emptyCollection(entity.getHinderAkermark());
        if (dto.getHinderAkermark() != null) {
            for (var hinderAkermark: dto.getHinderAkermark()) {
                entity.addHinderAkermark(hinderAkermark.getBeskrivning(), hinderAkermark.getErsattning());
            }
        }

        return entity;
    }
}
