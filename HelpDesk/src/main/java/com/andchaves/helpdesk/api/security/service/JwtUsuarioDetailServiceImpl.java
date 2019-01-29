package com.andchaves.helpdesk.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andchaves.helpdesk.api.entity.Usuario;
import com.andchaves.helpdesk.api.security.jwt.JwtUsuarioFactory;
import com.andchaves.helpdesk.api.service.UsuarioService;



@Service
public class JwtUsuarioDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioService.findByEmail(email);
		if (usuario == null) {
			throw new UsernameNotFoundException(String.format("Não há usuário com esse email '%s'.", email));
		} else {
			return JwtUsuarioFactory.create(usuario);
		}
	}
	

}
