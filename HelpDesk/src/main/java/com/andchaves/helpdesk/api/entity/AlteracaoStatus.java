package com.andchaves.helpdesk.api.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andchaves.helpdesk.api.enums.StatusEnum;

@Document
public class AlteracaoStatus {

	@Id
	private String id;
	
	@DBRef
	private Ticket ticket;
	
	@DBRef
	private Usuario usuarioAlteracao;
	
	private Date dateAlteracaoStatus;
	
	private StatusEnum status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public Date getDateAlteracaoStatus() {
		return dateAlteracaoStatus;
	}

	public void setDateAlteracaoStatus(Date dateAlteracaoStatus) {
		this.dateAlteracaoStatus = dateAlteracaoStatus;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}
	
	
}
