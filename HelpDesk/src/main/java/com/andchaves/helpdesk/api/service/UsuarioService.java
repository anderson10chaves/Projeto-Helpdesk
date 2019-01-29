package com.andchaves.helpdesk.api.service;

import org.springframework.data.domain.Page;

import com.andchaves.helpdesk.api.entity.Usuario;

public interface UsuarioService {
	
	Usuario findById(String id);
 
	Usuario findByEmail(String email);
	
	Usuario createOrUpdate(Usuario usuario);
	
	Usuario findOne(String id);
	
	void delete(String id);
	
	Page<Usuario> findAll(int page, int count);

	
}
