package br.com.oversight.sefisca.controle.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfissionalDTO {
	private String nome;
	private String cpf;
	private String cns;
	private String cbo;
	private String dataAtualizacao;
	
	@Override
	public String toString() {
		return "ProfissionalDTO [nome=" + nome + ", cpf=" + cpf + ", cns=" + cns + ", cbo=" + cbo + ", dataAtualizacao="
				+ dataAtualizacao + "]";
	}
}
