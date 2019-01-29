package com.andchaves.helpdesk.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.andchaves.helpdesk.api.entity.Usuario;
import com.andchaves.helpdesk.api.repository.UsuarioRepository;
import com.andchaves.helpdesk.api.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository; 
	
	@Override
	public Usuario findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public Usuario createOrUpdate(Usuario usuario) {
		return this.usuarioRepository.save(usuario);
	}
	
	public Usuario findById(String id) {
		return this.usuarioRepository.findOne(id);
	}

	public void delete(String id) {
		this.usuarioRepository.delete(id);
	}

	@Override
	public Page<Usuario> findAll(int page, int count) {
		PageRequest pages = new PageRequest(page, count);
		return this.usuarioRepository.findAll(pages);
	}

	@Override
	public Usuario findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}


}
