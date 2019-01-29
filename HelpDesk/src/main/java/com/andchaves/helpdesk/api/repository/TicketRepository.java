package com.andchaves.helpdesk.api.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.andchaves.helpdesk.api.entity.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String> {
	
	Page<Ticket> findByUsuarioIdOrderByDateDesc(Pageable pages, String usuarioId);
	
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPrioridadeOrderByDateDesc(
		String titulo, String status, String prioridade, Pageable pages);
	
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDateDesc(
			String titulo, String status, String prioridade, Pageable pages);
	
	Page<Ticket> findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndAssinaturaUsuarioOrderByDateDesc(
			String titulo, String status, String prioridade, Pageable pages);
	
	Page<Ticket> findByNumber(Integer number, Pageable pages);
	
	
}
