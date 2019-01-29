package com.andchaves.helpdesk.api.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.andchaves.helpdesk.api.entity.Usuario;
import com.andchaves.helpdesk.api.enums.ProfileEnum;

public class JwtUsuarioFactory {

	private JwtUsuarioFactory() {

	}

	public static JwtUsuario create(Usuario usuario) {
		return new JwtUsuario(usuario.getId(), usuario.getEmail(), usuario.getPassword(),
				mapToGrantedAuthorities(usuario.getProfile()));
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;
	}
}
