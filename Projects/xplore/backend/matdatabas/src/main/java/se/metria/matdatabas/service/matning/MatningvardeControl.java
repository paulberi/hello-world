package se.metria.matdatabas.service.matning;

import org.springframework.stereotype.Component;
import se.metria.matdatabas.openapi.model.GransvardeDto;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.Larmstatus;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.larm.dto.SaveLarm;
import se.metria.matdatabas.service.matning.dto.Matning;
import se.metria.matdatabas.service.matningstyp.dto.Matningstyp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MatningvardeControl {

	private GransvardeService gransvardeService;
	private LarmService larmService;


	public MatningvardeControl(GransvardeService gransvardeService, LarmService larmService) {
		this.gransvardeService = gransvardeService;
		this.larmService = larmService;
	}

	public List<Larm> controlMatning(Matningstyp matningstyp, Matning matning) {
		List<Larm> larms = new ArrayList<>();

		if (!matning.hasFelkod()) {
			List<GransvardeDto> gransvarden = this.gransvardeService.getGransvardenListForMatningstyp(matningstyp.getId());

			var gransvardenLarm = gransvarden.stream()
											 .filter(GransvardeDto::getAktiv)
											 .filter(g -> this.isGransvardeLarm(g.getId()))
											 .filter(g -> g.getLarmTillAnvandargruppId() != null)
											 .collect(Collectors.toList());

			largestMaxGransvardeLarm(gransvardenLarm, matning)
					.ifPresent(g -> larms.add(this.larmOnGransvarde(matningstyp, matning, g)));

			smallestMinGransvardeLarm(gransvardenLarm, matning)
					.ifPresent(g -> larms.add(this.larmOnGransvarde(matningstyp, matning, g)));
		}

		return larms;
	}

	private Optional<GransvardeDto> largestMaxGransvardeLarm(List<GransvardeDto> gransvarden, Matning matning) {
		return gransvarden.stream()
						  .filter(g -> g.getTypAvKontroll() == TypAvKontroll.MAX)
						  .filter(g -> getVarde(matning) > g.getGransvarde())
						  .sorted(new GransvardeLarmComparator(TypAvKontroll.MAX))
						  .findFirst();
	}

	private Optional<GransvardeDto> smallestMinGransvardeLarm(List<GransvardeDto> gransvarden, Matning matning) {
		return gransvarden.stream()
				.filter(g -> g.getTypAvKontroll() == TypAvKontroll.MIN)
				.filter(g -> getVarde(matning) < g.getGransvarde())
				.sorted(new GransvardeLarmComparator(TypAvKontroll.MIN))
				.findFirst();
	}

	private Larm larmOnGransvarde(Matningstyp matningstyp, Matning matning, GransvardeDto gransvarde)  {
		SaveLarm saveLarm = SaveLarm.builder()
				.status((short) 0)
				.gransvarde(gransvarde.getGransvarde())
				.gransvardeId(gransvarde.getId())
				.matningId(matning.getId())
				.matobjektId(matningstyp.getMatobjektId())
				.varde(getVarde(matning))
				.larmnivaId(gransvarde.getLarmnivaId())
				.larmnivaNamn(gransvarde.getLarmnivaNamn())
				.anvandargruppId(gransvarde.getLarmTillAnvandargruppId())
				.typAvKontroll(Short.valueOf(gransvarde.getTypAvKontroll().toString()))
				.enhet(matningstyp.getEnhet())
				.avlastDatum(matning.getAvlastDatum())
				.build();
		return this.larmService.createLarm(saveLarm);
	}

	private Double getVarde(Matning matning) {
		return matning.getBeraknatVarde() == null ? matning.getAvlastVarde() : matning.getBeraknatVarde();
	}

	private boolean isGransvardeLarm(Integer gransvardeId) {
		return this.larmService.countByStatusAndGransvardeId(Larmstatus.LARM, gransvardeId) == 0;
	}
}
