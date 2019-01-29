package com.andchaves.helpdesk.api.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.andchaves.helpdesk.api.entity.AlteracaoStatus;
import com.andchaves.helpdesk.api.entity.Ticket;

@Component
public interface TicketService {
	
	Ticket createOrUpdate(Ticket ticket);
	
	Ticket findById(String id);
	
	void delete(String id);
	
	Page<Ticket> listTicket(int page, int count);
	
	AlteracaoStatus createAlteracaoStatus(AlteracaoStatus alteracaoStatus);
	
	Iterable<AlteracaoStatus> listAlteracaoStatus(String ticketId);
	
	Page<Ticket> findByCurrentUsuario(int page, int count, String usuarioId);
	
	Page<Ticket> findByParameters(int page, int count, String titulo, String status, String prioridade);
	
	Page<Ticket> findByParametersAndCurrentUsuario(int page, int count, String titulo, String status, String prioridade, String usuarioId);
	
	Page<Ticket> findByNumber(int page, int count, Integer number);
	
	Iterable<Ticket> findAll();
	
	Page<Ticket> findByParameterAndAssinaturaUsuario(int page, int count, String titulo, String status, String prioridade, String assinaturaUsuario);
}
