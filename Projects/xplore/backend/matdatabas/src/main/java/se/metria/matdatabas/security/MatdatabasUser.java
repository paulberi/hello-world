package se.metria.matdatabas.security;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MatdatabasUser extends User {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String fullname;
	private String company;
	private Integer defaultKartlagerId;
	private LocalDateTime inloggadSenast;

	public MatdatabasUser(
			String username,
			String password,
			boolean enabled,
			boolean accountNonExpired,
			boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			String fullname,
			String company,
			Integer defaultKartlagerId,
			Integer id,
			LocalDateTime inloggadSenast) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.fullname = fullname;
		this.company = company;
		this.defaultKartlagerId = defaultKartlagerId;
		this.id = id;
		this.inloggadSenast = inloggadSenast;
	}

	public Integer getId() {
		return id;
	}

	public String getFullname() {
		return fullname;
	}

	public String getCompany() {
		return company;
	}

	public Integer getDefaultKartlagerId() {
		return defaultKartlagerId;
	}

	public LocalDateTime getInloggadSenast() {
		return inloggadSenast;
	}
}
