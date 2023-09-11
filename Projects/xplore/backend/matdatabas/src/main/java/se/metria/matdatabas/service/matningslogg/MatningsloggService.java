package se.metria.matdatabas.service.matningslogg;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.definitionmatningstyp.Berakningstyp;
import se.metria.matdatabas.service.definitionmatningstyp.DefinitionMatningstypService;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matningslogg.dto.Matningslogg;
import se.metria.matdatabas.service.matningslogg.entity.MatningsloggEntity;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;

@Service
public class MatningsloggService {

	private MatningsloggRepository repository;
	private DefinitionMatningstypService definitionMatningstypService;

	public MatningsloggService(MatningsloggRepository repository,
							   DefinitionMatningstypService definitionMatningstypService) {
		this.repository = repository;
		this.definitionMatningstypService = definitionMatningstypService;
	}

	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public Page<Matningslogg> getMatningslogg(Long matningId, Integer page, Integer pageSize,
													String sortProperty, Sort.Direction sortDirection) {
		PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
		return repository.findAllByMatningId(matningId, pageRequest).map(Matningslogg::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)	
	public Matningslogg addHandelseRapporterad(Matningstyp matningstyp, Long matningId, Double uppmattVarde, String kommentar) {
		var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

		if (Berakningstyp.SATTNING.equals(definitionMatningstyp.getBerakningstyp())) {
			var matvardeString = vardeString(uppmattVarde, matningstyp.getEnhet());
			var beskrivning = "Uppmätt värde: " + matvardeString;

			beskrivning += ", fixpunkt: " + matningstyp.getFixpunkt();

			if (kommentar != null) {
				beskrivning = beskrivning + ", kommentar: " + kommentar;
			}

			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.RAPPORTERAT, beskrivning)));
		} else {
			var matvardeString = vardeString(uppmattVarde, matningstyp.getEnhet());
			var beskrivning = "Uppmätt värde: " + matvardeString;

			if (kommentar != null) {
				beskrivning = beskrivning + ", kommentar: " + kommentar;
			}

			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.RAPPORTERAT, beskrivning)));
		}
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Matningslogg addHandelseRapporteradFelkod(Long matningId, String felkod, String kommentar) {
		var beskrivning = "Felkod: " + felkod;

		if (kommentar != null) {
			beskrivning = beskrivning + ", kommentar: " + kommentar;
		}

		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.RAPPORTERAT, beskrivning)));
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Matningslogg addHandelseImporterad(Matningstyp matningstyp, Long matningId, Double uppmattVarde,
											  Double beraknatVarde, String beraknatVardeEnhet, String kommentar) {
		var matvardeString = vardeString(uppmattVarde, matningstyp.getEnhet());
		var beraknatVardeString = vardeString(beraknatVarde, beraknatVardeEnhet);
		var beskrivning = "Uppmätt värde: " + matvardeString + ", beräknat värde: " + beraknatVardeString;

		var definitionMatningstyp = definitionMatningstypService.findById(matningstyp.getDefinitionMatningstypId()).orElseThrow();

		if (Berakningstyp.SATTNING.equals(definitionMatningstyp.getBerakningstyp())) {
			beskrivning = beskrivning + ", fixpunkt: " + matningstyp.getFixpunkt();
		}

		if (kommentar != null) {
			beskrivning = beskrivning + ", kommentar: " + kommentar;
		}

		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.IMPORTERAT, beskrivning)));
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Matningslogg addHandelseImporteradFelkod(Long matningId, String felkod, String kommentar) {
		var beskrivning = "Felkod: " + felkod;

		if (kommentar != null) {
			beskrivning = beskrivning + ", kommentar: " + kommentar;
		}

		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.IMPORTERAT, beskrivning)));
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)	
	public Matningslogg addHandelseBeraknadFromParams(boolean ok, Matningstyp matningstyp, Long matningId, String refnivaNamn) {
		var refniva = String.format("%1$s: %2$s", refnivaNamn, matningstyp.getBerakningReferensniva() != null ? matningstyp.getBerakningReferensniva() + " m" : "saknas"); 
		var konst = String.format("Gradningskonstant: %1$s", matningstyp.getBerakningKonstant() != null ? matningstyp.getBerakningKonstant() : "saknas"); 
		var beskrivning = String.format("%1$s%2$s, %3$s, %4$s", matningstyp.getTyp(), ok ? "" : " misslyckades", refniva, konst);
		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.BERAKNING, beskrivning)));
	}
	
	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)	
	public Matningslogg addHandelseBeraknadFromPrev(boolean ok, Matningstyp matningstyp, Long matningId, Optional<Matning> prev) {
		var prevstr = prev.isEmpty() ? "Föregående mätvärde saknas": 
			String.format("Föregående mätvärde (%1$s): %2$.4f m3", prev.get().getAvlastDatum().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), prev.get().getAvlastVarde()); 
		var beskrivning = String.format("%1$s%2$s, %3$s", matningstyp.getTyp(), ok ? "" : " misslyckades", prevstr);
		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.BERAKNING, beskrivning)));
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)	
	public Matningslogg addHandelseBeraknadFromOther(boolean ok, Matningstyp matningstyp, Long matningId, Optional<Matning> other, String otherName) {
		var konst = String.format("Beräkningskonstant: %1$s", matningstyp.getBerakningKonstant() != null ? matningstyp.getBerakningKonstant() : "saknas");		
		var otherstr = other.isEmpty() ? otherName + " saknas": 
			String.format("%1$s (%2$s): %3$.4f hPa", otherName, other.get().getAvlastDatum().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), other.get().getAvlastVarde());
		var beskrivning = String.format("%1$s%2$s, %3$s, %4$s", matningstyp.getTyp(), ok ? "" : " misslyckades", konst, otherstr);
		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.BERAKNING, beskrivning)));
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matningslogg addHandelseKorrigerad(Long matningId, Double oldVarde, Double newVarde) {
		var beskrivning = String.format("Från %1$.4f till %2$.4f",  oldVarde, newVarde);
		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.KORRIGERAT, beskrivning)));
	}

	public Matningslogg addHandelseKorrigeradKommentar(long matningId, String oldKommentar, String newKommentar) {
		var beskrivning = "Kommentar från \"" + oldKommentar + "\" till \"" + newKommentar + "\"";

		return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.KORRIGERAT, beskrivning)));
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Matningslogg addHandelseAndrat(Long matningId,
										  Double oldVarde, Double newVarde,
										  String oldKommentar, String newKommentar,
										  LocalDateTime oldAvlastDatum, LocalDateTime newAvlastDatum,
										  String oldFelkod, String newFelkod) {
		String beskrivning = "";

		if (!Objects.equals(oldVarde, newVarde)) {
			beskrivning += String.format("Värde från %1$.4f till %2$.4f, ", oldVarde, newVarde);
		}

		if (!Objects.equals(oldKommentar, newKommentar)) {
			beskrivning += "Kommentar från \"" + oldKommentar + "\" till \"" + newKommentar + "\", ";
		}

		if (!Objects.equals(oldAvlastDatum, newAvlastDatum)) {
			beskrivning += "Avläst datum från " + oldAvlastDatum + " till " + newAvlastDatum + ", ";
		}

		if (!Objects.equals(oldFelkod, newFelkod)) {
			beskrivning += "Felkod från " + oldFelkod + " till " + newFelkod + ", ";
		}

		if (beskrivning.length() > 2) {
			beskrivning = beskrivning.substring(0, beskrivning.length() - 2);
		}

		if (beskrivning.equals("")) {
			return null;
		} else {
			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.ANDRAT, beskrivning)));
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matningslogg addHandelseAutomatisktGranskat(Long matningId, Short status) {
		var beskrivning = String.format("Automatiskt granskat");
		if (status == Matningstatus.GODKANT) {
			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.GODKANT, beskrivning)));
		} else {
			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.FELMARKERAT, beskrivning)));
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Matningslogg addHandelseStatus(Long matningId, Short status) {
		if (status == Matningstatus.GODKANT) {
			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.GODKANT)));
		} else {
			return Matningslogg.fromEntity(repository.save(new MatningsloggEntity(matningId, MatningsloggHandelse.FELMARKERAT)));
		}
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void deleteLogsForMatning(Long matningId) {
		for (var log: repository.findAllByMatningId(matningId, Pageable.unpaged())) {
			repository.deleteById(log.getId());
		}
	}

	private String vardeString(Double varde, String enhet) {
		return (varde == null) ? "N/A" : varde + enhet;
	}
}
