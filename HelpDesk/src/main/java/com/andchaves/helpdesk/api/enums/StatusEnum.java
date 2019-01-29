package com.andchaves.helpdesk.api.enums;

public enum StatusEnum {
	
	Novo,
	Atribuido,
	Resolvido,
	Aprovado,
	Reprovado,
	Fechar;

	public static StatusEnum getStatus(String status) {
		switch (status) {
		case "Novo" : return Novo;
		case "Atribuido" : return Atribuido;
		case "Resolvido" : return Resolvido;
		case "Aprovado" : return Aprovado;
		case "Reprovado" : return Reprovado;
		case "Fechar" : return Fechar;
		default : return Novo;
	}

  }

}