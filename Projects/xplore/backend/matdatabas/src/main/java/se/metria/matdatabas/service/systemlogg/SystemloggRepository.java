package se.metria.matdatabas.service.systemlogg;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import se.metria.matdatabas.service.systemlogg.entity.SystemloggEntity;

public interface SystemloggRepository extends JpaRepository<SystemloggEntity, Long>, JpaSpecificationExecutor<SystemloggEntity>, SystemloggRepositoryCustom {
	@Modifying(clearAutomatically = true)
	@Query(value = "INSERT INTO matdatabas.systemlogg (id, loggat_datum, loggat_av_id, handelse, beskrivning) VALUES (nextval('matdatabas.systemlogg_id_seq'),:loggatDatum, :loggatAvId, :handelse, :beskrivning)", nativeQuery = true)
	void insertHandelseInloggning(@Param("loggatDatum") LocalDateTime loggatDatum, @Param("loggatAvId") Integer loggatAvId, @Param("handelse") Short handelse, @Param("beskrivning") String beskrivning);
}
