package br.com.oversight.sefisca.controle.dto;

import br.com.oversight.sefisca.entidade.Municipio;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstabelecimentoDTO {

	private String codigoCNES;
	private String cnpj;
	private String nomeFantasia;
	private String nomeEmpresarial;
	private String logradouro;
	private String tipoUnidade;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private Municipio municipio;
	private String uf;
	private String longitude;
	private String latitude;
	private String geoJson;
}