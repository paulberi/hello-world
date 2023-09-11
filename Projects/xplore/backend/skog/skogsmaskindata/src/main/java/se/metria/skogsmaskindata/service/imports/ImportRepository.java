package se.metria.skogsmaskindata.service.imports;

import se.metria.skogsmaskindata.service.imports.model.Import;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepository extends JpaRepository<Import, Long> {

	Import findByObjektnummerAndOrganisationAndPakettyp(String objektnummer, String organisation, String pakettyp);
}
