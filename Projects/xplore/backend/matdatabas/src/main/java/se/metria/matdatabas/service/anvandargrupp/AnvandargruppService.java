package se.metria.matdatabas.service.anvandargrupp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import se.metria.matdatabas.security.MatdatabasRole;
import se.metria.matdatabas.service.anvandargrupp.dto.Anvandargrupp;
import se.metria.matdatabas.service.anvandargrupp.dto.EditAnvandargrupp;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@Service
public class AnvandargruppService {
	private JdbcTemplate jdbcTemplate;
	private AnvandargruppRepository repository;

	public AnvandargruppService(JdbcTemplate jdbcTemplate, AnvandargruppRepository repository) {
		this.jdbcTemplate = jdbcTemplate;
		this.repository = repository;
	}

	@RolesAllowed(MatdatabasRole.MATRAPPORTOR)
	public Page<Anvandargrupp> getAnvandargrupper(Integer page, Integer pageSize, String sortProperty, Sort.Direction sortDirection) {
		if ("antalAnvandare".equalsIgnoreCase(sortProperty)) {
			PageRequest pageRequest = PageRequest.of(page, pageSize);
			if (sortDirection.equals(Sort.Direction.ASC)) {
				return repository.findAllByAnvandareCountAsc(pageRequest).map(Anvandargrupp::fromEntity);
			} else {
				return repository.findAllByAnvandareCountDesc(pageRequest).map(Anvandargrupp::fromEntity);
			}
		} else {
			PageRequest pageRequest = PageRequest.of(page, pageSize, sortDirection, sortProperty);
			return repository.findAll(pageRequest).map(Anvandargrupp::fromEntity);
		}
	}

	@RolesAllowed(MatdatabasRole.TILLSTANDSHANDLAGGARE)
	public Optional<Anvandargrupp> getAnvandargrupp(Integer id) {
		return repository.findById(id).map(Anvandargrupp::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public void deleteAnvandargrupp(Integer id) {
		repository.deleteById(id);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Boolean exists(String namn) {
		return repository.existsByNamn(namn);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Optional<Anvandargrupp> editAnvandargrupp(Integer id, EditAnvandargrupp editAnvandargrupp) {
		return repository.findById(id)
				.map(e -> editAnvandargrupp.copyToEntity(e))
				.map(e -> repository.saveAndFlush(e))
				.map(Anvandargrupp::fromEntity);
	}

	@RolesAllowed(MatdatabasRole.ADMINISTRATOR)
	public Anvandargrupp addAnvandargrupp(EditAnvandargrupp editAnvandargrupp) {
		return Anvandargrupp.fromEntity(repository.saveAndFlush(editAnvandargrupp.toEntity()));
	}

	public List<Integer> getAnvandargrupperForAnvandare(Integer id) {
		return jdbcTemplate.queryForList(
				String.format("SELECT anvandargrupp_id FROM matdatabas.anvandargrupp_anvandare WHERE anvandar_id = %d", id),
				Integer.class);
	}
}
