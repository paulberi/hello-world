package se.metria.mapcms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.metria.mapcms.entity.DialogEntity;
import se.metria.mapcms.entity.MeddelandeEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DialogRepository extends JpaRepository<DialogEntity, UUID> {

    @Query("SELECT d FROM DialogEntity d where d.id=:dialogId and d.projekt.id=:projektId and d.projekt.kund.id=:kundId")
    public Optional<DialogEntity> getDialogWithDialogIdAndProjektId(UUID kundId, UUID projektId, UUID dialogId);

    @Query("SELECT m FROM MeddelandeEntity m join DialogPartEntity dp " +
            "on m.fran = dp where dp.dialog.id=:dialogId " +
            "and dp.dialog.projekt.id=:projektId " +
            "and dp.dialog.projekt.kund.id=:kundId")
    public List<MeddelandeEntity> listMeddelandeForDialog(UUID kundId, UUID projektId, UUID dialogId);

   @Query("SELECT d" +
           "     FROM DialogEntity d" +
           "     JOIN d.dialogParter dp" +
           "    JOIN dp.personer p" +
           "    WHERE p.pnr = :pnr" +
           "    AND dp.dialog.id = d.id" +
           "      AND d.id = :dialogId" +
           "    AND d.projekt.id=:projektId" +
           "    AND d.projekt.kund.id=:kundId")
    public Optional<DialogEntity> getDialogWithDialogIdAndProjektIdAndPnr(UUID kundId, UUID projektId, UUID dialogId, String pnr);

    @Query("SELECT d " +
            "FROM DialogEntity d " +
            "JOIN d.dialogParter dp " +
            "JOIN dp.personer p ON p.pnr=:pnr " +
            "JOIN p.dialogParter pdp " +
            "where pdp.id=dp.id " +
            "and d.projekt.id=:projektId " +
            "and d.projekt.kund.id=:kundId" )
    public List<DialogEntity> listDialogForUser(UUID kundId, UUID projektId, String pnr);
}
