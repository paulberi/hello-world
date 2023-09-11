package se.metria.matdatabas.service.matning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.matning.entity.MatningEntity;

@Repository
public interface MatningRepository extends JpaRepository<MatningEntity, Long>, JpaSpecificationExecutor<MatningEntity>, MatningRepositoryCustom {
	Page<MatningEntity> findAllByMatningstypId(Integer matningstypId, Pageable pageable);
	Optional<MatningEntity> findByMatningstypIdAndAvlastDatum(Integer matningstypId, LocalDateTime avlastDatum);
	List<MatningEntity> findByMatningstypIdAndFelkodAndStatusNotAndAvlastDatumBefore(Integer matningstypId, String felkod, Short status, LocalDateTime avlastDatum, Pageable pageable);

	Stream<MatningEntity> findAllByMatningstypIdIn(List<Integer> ids, Sort sort);
	Stream<MatningEntity> findAllBy(Sort sort);

	boolean existsByMatningstypId(Integer matningstypId);
	boolean existsByMatningstypIdAndAvlastDatum(Integer matningstypId, LocalDateTime avlastDatum);
	boolean existsByMatningstypIdAndId(Integer matningstypID, Long matningId);
	Integer countByMatningstypIdAndStatus(Integer matningstypId, Short status);
}
