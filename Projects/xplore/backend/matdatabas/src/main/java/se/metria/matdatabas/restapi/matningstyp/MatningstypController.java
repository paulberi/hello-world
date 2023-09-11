package se.metria.matdatabas.restapi.matningstyp;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.metria.matdatabas.openapi.api.MatningstyperApi;
import se.metria.matdatabas.openapi.model.*;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.gransvarde.GransvardeService;
import se.metria.matdatabas.service.larm.LarmService;
import se.metria.matdatabas.service.larm.dto.Larm;
import se.metria.matdatabas.service.matning.MatningService;
import se.metria.matdatabas.service.matning.Matningstatus;
import se.metria.matdatabas.service.matning.dto.MatningDataSeries;
import se.metria.matdatabas.service.matning.query.MatningSearchFilter;
import se.metria.matdatabas.service.matobjekt.MatobjektService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(value = "/api")
@RestController
public class MatningstypController implements MatningstyperApi {
	private MatningService matningService;
	private MatobjektService matobjektService;
	private GransvardeService gransvardeService;
	private LarmService larmService;
	private ModelMapper mapper;

	public MatningstypController(MatningService matningService,
								 MatobjektService matobjektService,
								 GransvardeService gransvardeService,
								 LarmService larmService,
								 ModelMapper mapper) {
		this.matningService = matningService;
		this.matobjektService = matobjektService;
		this.gransvardeService = gransvardeService;
		this.larmService = larmService;
		this.mapper = mapper;
	}

	@Override
	public ResponseEntity<MatningDataSeriesDto> matningstypDataSeries(Integer matningstypId,
																	  @Valid LocalDate fromDatum,
																	  @Valid LocalDate sattningReferensdatum,
																	  @Valid Boolean filterGodkanda,
																	  @Valid Boolean filterFelkodOk) {
		// Mätrapportör eller bättre får se icke godkända mätningar
		boolean matrapportorEllerBattre = false;

		for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if (authority.getAuthority().equalsIgnoreCase(MatdatabasRole.MATRAPPORTOR)) {
				matrapportorEllerBattre = true;
			}
		}

		if (!matrapportorEllerBattre) {
			if (filterGodkanda == null || filterGodkanda != true) {
				throw new AccessDeniedException("Access denied");
			}
		}

		var filter = new MatningSearchFilter();

		if (fromDatum != null) {
			filter.setFrom(fromDatum.atStartOfDay());
		}

		if (filterGodkanda != null && filterGodkanda) {
			filter.setStatus(Matningstatus.GODKANT);
		}

		if (filterFelkodOk != null) {
			filter.setFilterFelkodOk(filterFelkodOk);
		}

		MatningDataSeries matningDataSeries = matningService.getMatningDataSeries(matningstypId, filter, sattningReferensdatum);

		List<MatningDataSeriesDataDto> dataDto = new ArrayList<>();

		for (MatningDataSeries.MatningData d : matningDataSeries.getData()) {
			MatningDataSeriesDataDto dDto = new MatningDataSeriesDataDto();
			dDto.setAvlastDatum(new BigDecimal(d.getAvlastDatum()));
			dDto.setId(Math.toIntExact(d.getId()));
			dDto.setStatus(d.getStatus()==null ? null : Integer.valueOf(d.getStatus()));
			dDto.setVarde(d.getVarde());
			dDto.setFelkod(d.getFelkod());

			dataDto.add(dDto);
		}

		MatningDataSeriesDto matningDataSeriesDto = new MatningDataSeriesDto();
		matningDataSeriesDto.setMatobjektId(matningDataSeries.getMatobjektId());
		matningDataSeriesDto.setMatobjektNamn(matningDataSeries.getMatobjektNamn());
		matningDataSeriesDto.setMatningstypId(matningDataSeries.getMatningstypId());
		matningDataSeriesDto.setMatningstypNamn(matningDataSeries.getMatningstypNamn());
		matningDataSeriesDto.setBeraknadEnhet(matningDataSeries.getBeraknadEnhet());
		matningDataSeriesDto.setBeraknadStorhet(matningDataSeries.getBeraknadStorhet());
		matningDataSeriesDto.setDecimaler(matningDataSeries.getDecimaler() == null ? null : matningDataSeries.getDecimaler().intValue());
		matningDataSeriesDto.setStorhet(matningDataSeries.getStorhet());
		matningDataSeriesDto.setEnhet(matningDataSeries.getEnhet());
		matningDataSeriesDto.setyAxisLabel(matningDataSeries.getYAxisLabel());
		matningDataSeriesDto.setGraftyp(matningDataSeries.getGraftyp().intValue());
		matningDataSeriesDto.setData(dataDto);

		return ResponseEntity.ok(matningDataSeriesDto);
	}

	@Override
	@RolesAllowed(MatdatabasRole.OBSERVATOR)
	public ResponseEntity<List<GransvardeDto>> matningstypGransvarden(Integer matningstypId) {
		List<GransvardeDto> gransvarden = gransvardeService.getGransvardenListForMatningstyp(matningstypId);
		return ResponseEntity.ok(gransvarden);
	}

	@Override
	public ResponseEntity<List<LarmDto>> matningstypLarmdetaljer(Integer matningstypId) {
		List<Larm> larm = larmService.getLarmForMatningstyp(matningstypId);
		return ResponseEntity.ok(larm.stream().map(e -> (LarmDto) e).collect(Collectors.toList()));
	}
}
