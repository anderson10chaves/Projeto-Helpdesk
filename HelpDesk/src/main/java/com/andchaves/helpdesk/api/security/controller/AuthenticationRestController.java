package com.andchaves.helpdesk.api.security.controller;

import javax.naming.AuthenticationException;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andchaves.helpdesk.api.entity.Usuario;
import com.andchaves.helpdesk.api.security.jwt.JwtAuthenticationRequest;
import com.andchaves.helpdesk.api.security.jwt.JwtTokenUtil;
import com.andchaves.helpdesk.api.security.model.CurrentUsuario;
import com.andchaves.helpdesk.api.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping(value="/api/auth")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
	
	final Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(),
					authenticationRequest.getPassword()
					)
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
			final String token = jwtTokenUtil.generateToken(userDetails);
			final Usuario usuario = usuarioService.findByEmail(authenticationRequest.getEmail());
			usuario.setPassword(null);
			return ResponseEntity.ok(new CurrentUsuario(token, usuario));
	}
	
	@PostMapping(value="/api/refresh")
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String username = jwtTokenUtil.getUsernameFromToken(token);
		final Usuario usuario = usuarioService.findByEmail(username);
		
		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new CurrentUsuario(refreshedToken, usuario));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
