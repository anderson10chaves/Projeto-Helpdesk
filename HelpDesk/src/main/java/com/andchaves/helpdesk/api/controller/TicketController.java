package com.andchaves.helpdesk.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andchaves.helpdesk.api.dto.Summary;
import com.andchaves.helpdesk.api.entity.AlteracaoStatus;
import com.andchaves.helpdesk.api.entity.Ticket;
import com.andchaves.helpdesk.api.entity.Usuario;
import com.andchaves.helpdesk.api.enums.ProfileEnum;
import com.andchaves.helpdesk.api.enums.StatusEnum;
import com.andchaves.helpdesk.api.response.Response;
import com.andchaves.helpdesk.api.security.jwt.JwtTokenUtil;
import com.andchaves.helpdesk.api.service.TicketService;
import com.andchaves.helpdesk.api.service.UsuarioService;

@RestController
@RequestMapping("/api/ticket")
@CrossOrigin(origins= "*")
@Component
public class TicketController {
	
	@Autowired
	private 
	TicketService ticketService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping()
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Ticket>> create(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		
		try {
			validateCreateTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			ticket.setStatus(StatusEnum.getStatus("New"));
			ticket.setUsuario(usuarioFromRequest(request));
			ticket.setDate(new Date());
			ticket.setNumber(GenerateNumber());
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
		}catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
		
	}
	
	private void validateCreateTicket(Ticket ticket, BindingResult result) {
		if (ticket.getTitulo() == null) {
			result.addError(new ObjectError("Ticket", "Titulo não Informado !!"));
			return;
		}
	}
	
	public Usuario usuarioFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		String email = jwtTokenUtil.getUsernameFromToken(token);
		return usuarioService.findByEmail(email);
	}
	
	private Integer GenerateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}
	
	@PutMapping()
	@PreAuthorize("hasAnyRole('CLIENTE')")
	public ResponseEntity<Response<Ticket>> update(HttpServletRequest request, @RequestBody Ticket ticket,
			BindingResult result) {
		Response<Ticket> response = new Response<Ticket>();
		try {
			validateUpdateTicket(ticket, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Ticket ticketCurrent = ticketService.findById(ticket.getId());
			ticket.setStatus(ticketCurrent.getStatus());
			ticket.setUsuario(ticketCurrent.getAssinaturaUsuario());
			ticket.setDate(ticketCurrent.getDate());
			ticket.setNumber(ticketCurrent.getNumber());
			if (ticketCurrent.getAssinaturaUsuario() != null) {
				ticket.setAssinaturaUsuario(ticketCurrent.getAssinaturaUsuario());
			}
			Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticket);
			response.setData(ticketPersisted);
		}catch (Exception e) {
			response.getErrors().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
		private void validateUpdateTicket(Ticket ticket, BindingResult result) {
			if (ticket.getId() == null) {
				result.addError(new ObjectError("Ticket", "ID não Informado !!"));
				return;
			}
			if (ticket.getTitulo() == null) {
				result.addError(new ObjectError("Ticket", "Titulo não Informado !!"));
				return;
			}
		}
		
		@PutMapping(value= "{id}")
		@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
		public ResponseEntity<Response<Ticket>> findById(@PathVariable("id") String id) {
			Response<Ticket> response = new Response<Ticket>();
			Ticket ticket = ticketService.findById(id);
			if(ticket == null) {
				response.getErrors().add("Registro não ecnontrado!: " +id);
				return ResponseEntity.badRequest().body(response);
			}
			List<AlteracaoStatus> alteracoes = new ArrayList<AlteracaoStatus>();
			Iterable<AlteracaoStatus> alteracoesCurrent = ticketService.listAlteracaoStatus(ticket.getId());
			for (Iterator<AlteracaoStatus>  iterator = alteracoesCurrent.iterator(); iterator.hasNext();) {
				AlteracaoStatus alteracaoStatus = (AlteracaoStatus) iterator.next();
				alteracaoStatus.setTicket(null);
				alteracoes.add(alteracaoStatus);
			}
			ticket.setAlteracoes(alteracoes);
			response.setData(ticket);
			return ResponseEntity.ok(response);
		}
		
		@DeleteMapping(value= "{id}")
		@PreAuthorize("hasAnyRole('CLIENTE')")
		public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
			Response<String> response = new Response<String>();
			Ticket ticket = ticketService.findById(id);
			if(ticket == null) {
				response.getErrors().add("Registro não ecnontrado!: " +id);
				return ResponseEntity.badRequest().body(response);
			}
			ticketService.delete(id);
			return ResponseEntity.ok(new Response<String>());
		}
		
		@GetMapping(value = "{page}/ {count}")
		@PreAuthorize("hasAnyRole('CLIENTE'), 'TECNICO')")
		public ResponseEntity<Response<Page<Ticket>>> findAll(HttpServletRequest request, @PathVariable("page") int page,
									@PathVariable("count") int count) {
			Response<Page<Ticket>> response = new Response<Page<Ticket>>();
			Page<Ticket> tickets = null;
			Usuario usuarioRequest = usuarioFromRequest(request);
			if (usuarioRequest.getProfile().equals(ProfileEnum.ROLE_TECNICO)) {
				tickets = ticketService.listTicket(page, count);
			} else  if (usuarioRequest.getProfile().equals(ProfileEnum.ROLE_CLIENTE)); {
				tickets = ticketService.findByCurrentUsuario(page, count, usuarioRequest.getId());
			}
			response.setData(tickets);
			return ResponseEntity.ok(response);
		}
		
		@GetMapping(value = "{page}/{count}/{number}/{titulo}/{status}/{prioridade}/{assinatura}")
		@PreAuthorize("hasAnyRole('CLIENTE'), 'TECNICO')")
		public ResponseEntity<Response<Page<Ticket>>> findByParams(HttpServletRequest request,										@PathVariable("page") int page,
										@PathVariable("page") int pages,
										@PathVariable("count") int count,
										@PathVariable("count") Integer number,
										@PathVariable("count") String titulo,
										@PathVariable("count") String status,
										@PathVariable("count") String prioridade,
										@PathVariable("count") boolean assinatura){
			titulo = titulo.equals("uninforme") ? "" : titulo;
			status = status.equals("uninforme") ? "" : status;
			prioridade = prioridade.equals("uninforme") ? "" : prioridade;
			
			Response<Page<Ticket>> response = new Response<Page<Ticket>>();
			Page<Ticket> tickets = null;
			if (number > 0) {
				tickets = ticketService.findByNumber(pages, count, number);
			} else {
				Usuario usuarioRequest = usuarioFromRequest(request);
				if(usuarioRequest.getProfile().equals(ProfileEnum.ROLE_TECNICO)) {
					if(assinatura) {
						tickets = ticketService.findByParameterAndAssinaturaUsuario(pages, count, titulo, status, prioridade, usuarioRequest.getId());
					} else {
						tickets = ticketService.findByParameters(pages, count, titulo, status, prioridade);
					}
				} else if (usuarioRequest.getProfile().equals(ProfileEnum.ROLE_CLIENTE)) {
					tickets = ticketService.findByParameterAndAssinaturaUsuario(pages, count, titulo, status, prioridade, usuarioRequest.getId());
				}
			}
			response.setData(tickets);
			return ResponseEntity.ok(response);
		}
		
		@PutMapping(value= "{id}/{status}")
		@PreAuthorize("hasAnyRole('CLIENTE', 'TECNICO')")
		public ResponseEntity<Response<Ticket>> alteracaoStatus(
											@PathVariable("id") String id,
											@PathVariable("status") String status,
											HttpServletRequest request,
											@RequestBody Ticket ticket,
											BindingResult result) {
			Response<Ticket> response = new Response<Ticket>();
			try {
				validateAlteracaoStatus(id, status, result);
				if (result.hasErrors()) {
					result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
					return ResponseEntity.badRequest().body(response);
				}
				Ticket ticketCurrent = ticketService.findById(id);
				ticketCurrent.setStatus(StatusEnum.getStatus(status));
				if(status.equals("Assinatura")) {
					ticketCurrent.setAssinaturaUsuario(usuarioFromRequest(request));
				}
				Ticket ticketPersisted = (Ticket) ticketService.createOrUpdate(ticketCurrent);
				AlteracaoStatus alteracaoStatus = new AlteracaoStatus();
				alteracaoStatus.setUsuarioAlteracao(usuarioFromRequest(request));
				alteracaoStatus.setDateAlteracaoStatus(new Date());
				alteracaoStatus.setStatus(StatusEnum.getStatus(status));
				alteracaoStatus.setTicket(ticketPersisted);
				ticketService.createAlteracaoStatus(alteracaoStatus);
				response.setData(ticketPersisted);
			} catch (Exception e) {
				response.getErrors().add(e.getMessage());
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);
			
		}
		
		private void validateAlteracaoStatus(String id, String status, BindingResult result) {
			if (id == null) {
				result.addError(new ObjectError("Solicitação", "ID nao Informado!"));
				return;
			}
			if (status == null || status.equals("")) {
				result.addError(new ObjectError("Solicitação", "Titulo nao Informado!"));
				return;
				}
			}
		
		@GetMapping(value = "/summary")
		public ResponseEntity<Response<Summary>> findSummary() {
			Response<Summary>  response = new Response<Summary>();
			Summary summary = new Summary();
			 int amountNovo = 0;
			 int amountResolvido = 0;
			 int amountAprovado = 0;
			 int amountReprovado = 0;
			 int amountAtribuido = 0;
			 int amountFechado = 0;
			 
			Iterable<Ticket> tickets = ticketService.findAll();
			 if (tickets != null) {
				  for (Iterator<Ticket>  iterator = tickets.iterator(); iterator.hasNext();) {
					Ticket ticket = (Ticket) iterator.next();
					if (ticket.getStatus().equals(StatusEnum.Novo)) {
						amountNovo++;
					}
					if (ticket.getStatus().equals(StatusEnum.Resolvido)) {
						amountResolvido++;
					}
					if (ticket.getStatus().equals(StatusEnum.Aprovado)) {
						amountAprovado++;
					}
					if (ticket.getStatus().equals(StatusEnum.Reprovado)) {
						amountReprovado++;
					}
					if (ticket.getStatus().equals(StatusEnum.Atribuido)) {
						amountAtribuido++;
					}
					if (ticket.getStatus().equals(StatusEnum.Fechar)) {
						amountFechado++;
					}
				}
			 }
			 summary.setAmountNovo(amountNovo);
			 summary.setAmountResolvido(amountResolvido);
			 summary.setAmountAprovado(amountAprovado);
			 summary.setAmountReprovado(amountReprovado);
			 summary.setAmountAtribuido(amountAtribuido);
			 summary.setAmountFechado(amountFechado);
			 response.setData(summary);
			return ResponseEntity.ok(response);
		}
}
			