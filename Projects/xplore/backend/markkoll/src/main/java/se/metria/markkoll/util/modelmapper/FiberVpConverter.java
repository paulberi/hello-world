package se.metria.markkoll.util.modelmapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.FiberVarderingsprotokollDto;
import se.metria.markkoll.util.CollectionUtil;

import java.util.Collection;
import java.util.function.Consumer;

public final class FiberVpConverter implements Converter<FiberVarderingsprotokollDto, FiberVarderingsprotokollEntity> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public FiberVarderingsprotokollEntity
    convert(MappingContext<FiberVarderingsprotokollDto, FiberVarderingsprotokollEntity> context) {

        var dto = context.getSource();
        var entity = context.getDestination() == null ? new FiberVarderingsprotokollEntity() :
            context.getDestination();

        if (dto.getConfig() != null) {
            modelMapper.map(dto.getConfig(), entity.getConfig());
        }

        if (dto.getMetadata() != null) {
            modelMapper.map(dto.getMetadata(), entity.getMetadata());
        }

        addDtosToCollection(dto.getPunktersattning(), entity.getPunktersattning(), FiberPunktersattningEntity.class,
            p -> entity.addPunktersattning(p));

        addDtosToCollection(dto.getMarkledning(), entity.getMarkledning(), FiberMarkledningEntity.class,
            m -> entity.addMarkledning(m));

        addDtosToCollection(dto.getIntrangAkerOchSkogsmark(), entity.getIntrangAkerOchSkogsmark(),
            FiberIntrangAkerOchSkogsmarkEntity.class, ias -> entity.addIntrangAkerOchSkogsmark(ias));

        addDtosToCollection(dto.getOvrigIntrangsersattning(), entity.getOvrigIntrangsersattning(),
            FiberOvrigIntrangsersattningEntity.class, oie -> entity.addOvrigIntrangsersattning(oie));

        return entity;
    }

    private <D, E> void
    addDtosToCollection(Collection<D> dtoCollection, Collection<E> entityCollection, Class<E> entityClass, Consumer<E> addEntity) {
        CollectionUtil.emptyCollection(entityCollection);
        if (dtoCollection != null) {
            for (var dto: dtoCollection) {
                addEntity.accept(modelMapper.map(dto, entityClass));
            }
        }
    }
}
