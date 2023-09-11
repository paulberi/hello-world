package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.DialogPartEntity;

import java.util.Optional;
import java.util.UUID;

public interface DialogPartRepository extends JpaRepository<DialogPartEntity, UUID> {

    @Query("SELECT dp FROM DialogPartEntity dp WHERE dp.epost=:epost and dp.dialog.id=:dialogId")
    public Optional<DialogPartEntity> getPartWithEpostAndDialogId(String epost, UUID dialogId);

    @Query("SELECT dp" +
            "     FROM PersonEntity p " +
            "     JOIN p.dialogParter dp" +
            "    WHERE p.pnr = :pnr" +
            "      AND dp.id = :dialogPartId")
    public Optional<DialogPartEntity> getPartWithPnr(String pnr, UUID dialogPartId);

    @Query("SELECT dp" +
            "     FROM MeddelandeEntity m " +
            "     JOIN m.fran dp" +
            "   JOIN dp.personer p" +
            "     WHERE dp.id = m.fran.id" +
            "      AND p.pnr=:pnr" +
            " AND  m.id=:meddelandeId" )
    public Optional<DialogPartEntity> getPartWithPnrForUser(String pnr, UUID meddelandeId);

    @Query("SELECT dp" +
            "     FROM DialogEntity d" +
            "     JOIN d.dialogParter dp ON dp.dialog.id=:dialogId" +
            "    JOIN dp.personer p " +
            "   JOIN p.dialogParter pdp" +
            "     WHERE pdp.id = dp.id" +
            "      AND p.pnr=:pnr" +
            "   AND d.projekt.id=:projektId" +
            "   AND d.projekt.kund.id=:kundId" )
    public Optional<DialogPartEntity> getPartWithPnrAndDialogId(UUID kundId, UUID projektId, String pnr, UUID dialogId);
}