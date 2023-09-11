package se.metria.matdatabas.service.matrunda;

import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.matrunda.dto.RapporteringMatningstyp;

import javax.annotation.security.RolesAllowed;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatrundaRapporteringService {
	private MatrundaRapporteringDataService matrundaRapporteringDataService;

	public MatrundaRapporteringService(MatrundaRapporteringDataService matrundaRapporteringDataService) {
		this.matrundaRapporteringDataService = matrundaRapporteringDataService;
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<RapporteringMatningstyp> getMatningstyper(Integer matrundaId, LocalDateTime startDate) {
		return matrundaRapporteringDataService.getMatningstyper(matrundaId, startDate);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public List<RapporteringMatningstyp> getMatningstyperFromMatobjekt(Integer matobjektId, LocalDateTime startDate) {
		return matrundaRapporteringDataService.getMatningstyperFromMatobjekt(matobjektId, startDate);
	}

	/**
	 * Generera en s.k. fältrapport - en excelfil för manuell inrapportering av mätningar.
	 */
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public ByteArrayOutputStream generateForMatrunda(Integer matrundaId, LocalDateTime startDate) throws IOException {
		String matrunda = matrundaRapporteringDataService.getMatrundaName(matrundaId);
		List<RapporteringMatningstyp> matningstyper = matrundaRapporteringDataService.getMatningstyper(matrundaId, startDate);

		try(InputStream is = MatrundaRapporteringService.class.getResourceAsStream("/faltprotokoll/Fältprotokoll.xlsx")) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Context context = new Context();
			context.putVar("matrunda", matrunda);
			context.putVar("matningstyper", matningstyper);
			JxlsHelper.getInstance().processTemplate(is, os, context);

			return os;
		}
	}
}
