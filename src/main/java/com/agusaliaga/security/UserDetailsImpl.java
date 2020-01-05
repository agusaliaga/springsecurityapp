package com.agusaliaga.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.agusaliaga.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String userName;

	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String userName, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	/* Builder Method to build a UserDetailsImplementation, returning an Object with User id, name, email, passwd
	 * and authorities (roles).
	 * we convert Set<Role> into List<GrantedAuthority>. 
	 * It is important to work with Spring Security and Authentication object later.*/
	/* user.getRoles() returns a Set<Role>, we convert this Set to a Stream of Role. 
	 * Imagine that the stream is a collection of Role that releases items in turn.
	 * map() changes every Role item to a SimpleGrantedAuthority object, the parameter for the 
	 * constructor could be one of these Strings: ‘ROLE_USER’, ‘ROLE_MODERATOR’, ‘ROLE_ADMIN’.
	 * So, what does role.getName().name() do?
	 * role.getName() returns ERole enum object, let’s call it erole_object.
	 * */
	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getUserName(), 
				user.getEmail(),
				user.getPassword(), 
				authorities);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
