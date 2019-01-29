package com.andchaves.helpdesk.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.andchaves.helpdesk.api.entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
	Usuario findByEmail(String email);
}
