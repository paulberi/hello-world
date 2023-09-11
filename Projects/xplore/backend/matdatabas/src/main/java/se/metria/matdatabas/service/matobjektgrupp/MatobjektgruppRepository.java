package se.metria.matdatabas.service.matobjektgrupp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.matobjektgrupp.entity.MatobjektgruppEntity;

@Repository
public interface MatobjektgruppRepository extends JpaRepository<MatobjektgruppEntity, Integer> {
	Page<MatobjektgruppEntity> findAllByKategoriEquals(Short kategori, Pageable pageable);

	@Query(value = "SELECT mg FROM MatobjektgruppEntity mg WHERE kategori = :kategori ORDER BY mg.matobjektCount ASC",
			countQuery = "SELECT COUNT(mg) FROM MatobjektgruppEntity mg WHERE kategori = :kategori")
	Page<MatobjektgruppEntity> findAllByKategoriEqualsAndMatobjektCountAsc(@Param("kategori") Short kategori, Pageable pageable);

	@Query(value = "SELECT mg FROM MatobjektgruppEntity mg WHERE kategori = :kategori ORDER BY mg.matobjektCount DESC",
			countQuery = "SELECT COUNT(mg) FROM MatobjektgruppEntity mg WHERE kategori = :kategori ")
	Page<MatobjektgruppEntity> findAllByKategoriEqualsAndMatobjektCountDesc(@Param("kategori") Short kategori, Pageable pageable);

	Boolean existsByNamn(String namn);
}
