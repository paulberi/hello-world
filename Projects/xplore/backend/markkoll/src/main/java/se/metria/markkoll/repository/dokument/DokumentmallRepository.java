package se.metria.markkoll.repository.dokument;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.markkoll.entity.DokumentmallEntity;
import se.metria.markkoll.openapi.model.DokumentTypDto;

import java.util.List;
import java.util.UUID;

@Repository
public interface DokumentmallRepository extends JpaRepository<DokumentmallEntity, UUID> {
    List<DokumentmallEntity> getAllByKundId(String kundId, Sort sort);
    List<DokumentmallEntity> getAllByKundIdAndDokumenttypAndSelectedIsTrue(String kundId, DokumentTypDto dokumenttyp);

    List<DokumentmallEntity> findByKundIdOrderBySkapadDatumDesc(String kundId);

    @Query("SELECT doc.fil.id FROM DokumentmallEntity doc WHERE doc.id = :dokumentId")
    UUID getFilId(UUID dokumentId);

    @Query("SELECT d.fil.fil FROM DokumentmallEntity d WHERE d.id = :dokumentmallId")
    byte[] getDokumentFileData(UUID dokumentmallId);

    @Query("SELECT d.namn FROM DokumentmallEntity d WHERE d.id = :dokumentmallId")
    String getDokumentmallNamn(UUID dokumentmallId);
}
