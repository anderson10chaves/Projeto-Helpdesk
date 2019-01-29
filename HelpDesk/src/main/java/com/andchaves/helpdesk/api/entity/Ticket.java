package com.andchaves.helpdesk.api.entity;


import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andchaves.helpdesk.api.enums.PrioridadeEnum;
import com.andchaves.helpdesk.api.enums.StatusEnum;

@Document
public class Ticket {

	@Id
	private String id;
	
	@DBRef(lazy = true)
	private Usuario usuario;
	
	private Date date;
	
	private String titulo;
	
	private Integer number;
	
	private StatusEnum status;
	
	private PrioridadeEnum prioridade;
	
	@DBRef(lazy = true)
	private Usuario assinaturaUsuario;
	
	private String descricao;
	
	private String imagem;
	
	@Transient
	private List<AlteracaoStatus> alteracoes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public PrioridadeEnum getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(PrioridadeEnum prioridade) {
		this.prioridade = prioridade;
	}

	public Usuario getAssinaturaUsuario() {
		return assinaturaUsuario;
	}

	public void setAssinaturaUsuario(Usuario assinaturaUsuario) {
		this.assinaturaUsuario = assinaturaUsuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public List<AlteracaoStatus> getAlteracoes() {
		return alteracoes;
	}

	public void setAlteracoes(List<AlteracaoStatus> alteracoes) {
		this.alteracoes = alteracoes;
	}
	
	
}
