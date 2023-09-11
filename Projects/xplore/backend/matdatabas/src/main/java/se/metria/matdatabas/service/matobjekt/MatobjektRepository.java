package se.metria.matdatabas.service.matobjekt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.metria.matdatabas.service.matobjekt.entity.MatobjektEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatobjektRepository extends JpaRepository<MatobjektEntity, Integer>, JpaSpecificationExecutor<MatobjektEntity> {

	@Query("SELECT mo.namn FROM MatobjektEntity mo WHERE LOWER(mo.namn) LIKE LOWER(concat(:query, '%')) ORDER BY mo.namn")
	List<String> findAllNamnLike(String query);
	@Query("SELECT mo.namn FROM MatobjektEntity mo WHERE LOWER(mo.namn) LIKE LOWER(concat(:query, '%')) AND mo.typ = :typ ORDER BY mo.namn")
	List<String> findAllNamnLikeAndTypEquals(String query, Short typ);
	@Query("SELECT DISTINCT mo.fastighet FROM MatobjektEntity mo WHERE LOWER(mo.fastighet) LIKE LOWER(concat(:query, '%')) ORDER BY mo.fastighet")
	List<String> findAllFastighetLike(String query);

	@Query("SELECT mo.namn FROM MatobjektEntity mo where mo.id = :id")
	Optional<String> findNamnById(Integer id);
	@Query("SELECT mo.typ FROM MatobjektEntity mo where mo.id = :id")
	Optional<Short> findTypById(Integer id);

	Boolean existsByNamn(String namn);

	@Query("SELECT mo.id FROM MatobjektEntity mo where LOWER(mo.namn) = LOWER(:namn)")
	Optional<Integer> findIdByNamnIgnoreCase(String namn);

	@Query(value ="SELECT COUNT(*) > 0 FROM matdatabas.matobjekt mo" +
			" JOIN matdatabas.matningstyp mt ON mt.matobjekt_id = mo.id" +
			" JOIN matdatabas.matning m ON m.matningstyp_id = mt.id" +
			" WHERE mo.id = :id", nativeQuery = true)
	Boolean hasMatningar(Integer id);
}
