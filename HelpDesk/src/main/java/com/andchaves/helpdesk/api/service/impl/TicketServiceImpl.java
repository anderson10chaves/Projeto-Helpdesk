package com.andchaves.helpdesk.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andchaves.helpdesk.api.entity.AlteracaoStatus;
import com.andchaves.helpdesk.api.entity.Ticket;
import com.andchaves.helpdesk.api.repository.AlteracaoStatusRepository;
import com.andchaves.helpdesk.api.repository.TicketRepository;
import com.andchaves.helpdesk.api.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private AlteracaoStatusRepository alteracaoStatusRepository;
	
	@Override
	public Ticket createOrUpdate(Ticket ticket) {
		return this.ticketRepository.save(ticket);
	}

	@Override
	public Ticket findById(String id) {
		return this.ticketRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		this.ticketRepository.delete(id);
	}

	@Override
	public Page<Ticket> listTicket(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findAll(pages);
	}

	@Override
	public AlteracaoStatus createAlteracaoStatus(AlteracaoStatus alteracaoStatus) {		
		return this.alteracaoStatusRepository.save(alteracaoStatus);
	}

	@Override
	public Iterable<AlteracaoStatus> listAlteracaoStatus(String ticketId) {
		return this.alteracaoStatusRepository.findByTicketIdOrderByDateAlteracaoStatusDesc(ticketId);
	}

	@Override
	public Page<Ticket> findByCurrentUsuario(int page, int count, String usuarioId) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByUsuarioIdOrderByDateDesc(pages, usuarioId);
	}

	@Override
	public Page<Ticket> findByParameters(int page, int count, String titulo, String status, String prioridade) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusIgnoreCaseContainingAndPrioridadeOrderByDateDesc(titulo, status, prioridade, pages);
	}

	@Override
	public Page<Ticket> findByParametersAndCurrentUsuario(int page, int count, String titulo, String status,
			String prioridade, String usuarioId) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndUsuarioIdOrderByDateDesc(titulo, status, prioridade, pages);
	}

	@Override
	public Page<Ticket> findByNumber(int page, int count, Integer number) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByNumber(number, pages);
	}

	@Override
	public Iterable<Ticket> findAll() {
		return this.ticketRepository.findAll();
	}

	@Override
	public Page<Ticket> findByParameterAndAssinaturaUsuario(int page, int count, String titulo, String status,
			String prioridade, String assinaturaUsuario) {
		Pageable pages = new PageRequest(page, count);
		return this.ticketRepository.findByTituloIgnoreCaseContainingAndStatusAndPrioridadeAndAssinaturaUsuarioOrderByDateDesc(titulo, status, prioridade, pages);
	}
	

}
