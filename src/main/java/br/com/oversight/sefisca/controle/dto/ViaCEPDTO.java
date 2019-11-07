package br.com.oversight.sefisca.controle.dto;

import br.com.oversight.sefisca.entidade.Municipio;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ViaCEPDTO {

   private String cep;
   private String logradouro;
   private String complemento;
   private String bairro;
   private String localidade;
   private Municipio municipio;
   private String uf;
   private Integer ibge;
   private String gia;
   private String enderecoCompleto;
}