package se.metria.matdatabas.security;

import static java.util.stream.Collectors.toSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Responsible for linking the identity in the SAMLCredential to a user in the local database.
 */
@Service
public class MatdatabasUserDetailsService {

	// Logger
	private static final Logger LOG = LoggerFactory.getLogger(MatdatabasUserDetailsService.class);

	private JdbcTemplate jdbcTemplate;

	public MatdatabasUserDetailsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public MatdatabasUser getMatdatabasUser(String userID) {
		return jdbcTemplate.queryForObject(
				"SELECT id, aktiv, behorighet, namn, foretag, default_kartlager_id, inloggad_senast  FROM matdatabas.anvandare WHERE lower(inloggnings_namn) = lower(?) AND aktiv = TRUE",
				(rs, rowNum) -> {
					Timestamp ts = rs.getTimestamp("inloggad_senast");
					return new MatdatabasUser(
							userID, "",
							rs.getBoolean("aktiv"),
							true, true, true,
							MatdatabasRole.getRoles(rs.getInt("behorighet")).stream().map(SimpleGrantedAuthority::new).collect(toSet()),
							rs.getString("namn"),
							rs.getString("foretag"),
							(Integer) rs.getObject("default_kartlager_id"),
							rs.getInt("id"),
							ts != null ? ts.toLocalDateTime() : null
					);
				},
				userID);
	}
}
