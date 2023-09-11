package se.metria.matdatabas.service.matrunda;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import se.metria.matdatabas.service.matrunda.entity.MatrundaEntity;

import java.util.List;

@Repository
interface MatrundaRepository extends JpaRepository<MatrundaEntity, Integer> {
	List<MatrundaEntity> findByMatrundaMatningstyperMatningstypId(Integer matningstypId);
	Page<MatrundaEntity> findAllByAktivTrue(Pageable pageable);
	Boolean existsByNamn(String namn);
}
