package se.metria.markkoll.repository.vardering.elnat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.openapi.model.ElnatVarderingsprotokollMetadataDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ElnatVarderingsprotokollRepository extends JpaRepository<ElnatVarderingsprotokollEntity, UUID> {
    @Query("SELECT p.kundId " +
             "FROM ProjektEntity p " +
             "JOIN AvtalEntity av ON av.projekt.id = p.id " +
             "JOIN ElnatVarderingsprotokollEntity vp ON vp.avtal.id = av.id " +
            "WHERE vp.id = :varderingsprotokollId")
    String getKundId(UUID varderingsprotokollId);

    @Query("SELECT vp FROM ElnatVarderingsprotokollEntity vp WHERE vp.avtal.id = :avtalId")
    Optional<ElnatVarderingsprotokollEntity> getWithAvtalId(UUID avtalId);

    @Query("SELECT b.varderingsprotokoll.id FROM BilagaEntity b WHERE b.id = :bilagaId")
    Optional<UUID> getIdWithBilagaId(UUID bilagaId);

    Optional<ElnatVarderingsprotokollEntity> findByAvtalId(UUID avtalId);

    List<ElnatVarderingsprotokollEntity> findAllByAvtalIdIn(Iterable<UUID> avtalIds);

    @Modifying
    @Query("UPDATE ElnatVarderingsprotokollEntity vp" +
        "      SET vp.metadata.fastighetsnummer = :#{#metadata.fastighetsnummer}," +
        "          vp.metadata.koncessionslopnr = :#{#metadata.koncessionslopnr}," +
        "          vp.metadata.ledning = :#{#metadata.ledning}," +
        "          vp.metadata.spanningsniva = :#{#metadata.spanningsniva}," +
        "          vp.metadata.varderingsmanOchForetag = :#{#metadata.varderingsmanOchForetag}," +
        "          vp.metadata.varderingstidpunkt = :#{#metadata.varderingstidpunkt}" +
        "    WHERE vp.avtal.id = :avtalId")
    void updateMetadata(UUID avtalId, ElnatVarderingsprotokollMetadataDto metadata);
}
