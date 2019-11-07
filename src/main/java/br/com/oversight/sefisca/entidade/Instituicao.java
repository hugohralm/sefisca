package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Instituicao extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Getter
	@GeneratedValue(generator = "instituicao_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "instituicao_seq", sequenceName = "instituicao_seq", allocationSize = 1, initialValue = 1)
	private Integer id;

	@Getter
	@Setter
	@Length(min = 0, max = 50, message = "O limite do campo codigo CNES é de 50 caracteres.", groups = AmbientValidator.class)
	private String codigoCNES;

	@Getter
	@Setter
	@Length(min = 0, max = 20, message = "O limite do campo cnpj é de 20 caracteres.", groups = AmbientValidator.class)
	@Column(unique = true, nullable = false)
	private String cnpj;

	@Getter
	@Setter
	@NotBlank(message = "Informe o nome fantasia.", groups = AmbientValidator.class)
	@Column(length = 255, nullable = false)
	@Length(max = 255, message = "O limite do campo RG é de 255 caracteres.", groups = AmbientValidator.class)
	private String nomeFantasia;

	@Getter
	@Setter
	@NotBlank(message = "Informe a razao social.", groups = AmbientValidator.class)
	@Column(length = 255, nullable = false)
	@Length(max = 255, message = "O limite do campo RG é de 255 caracteres.", groups = AmbientValidator.class)
	private String razaoSocial;

	@Getter
	@Setter
	@NotBlank(message = "Informe o tipo unidade.", groups = AmbientValidator.class)
	@Column(length = 255, nullable = false)
	@Length(max = 255, message = "O limite do campo RG é de 255 caracteres.", groups = AmbientValidator.class)
	private String tipoUnidade;

	@Getter
	@Setter
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@NotNull(message = "Informe o Endereço.", groups = AmbientValidator.class)
	private Endereco endereco = new Endereco();

	@Getter
	@Setter
	@ManyToOne(optional = true)
	private Usuario usuario;

	@Getter
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao = new Date();

	@PrePersist
	private void atualizarData() {
		setDataCriacao(new Date());
	}

	public void setDto(InstituicaoDTO instituicaoDTO) {
		this.codigoCNES = instituicaoDTO.getCodigoCNES();
		this.cnpj = instituicaoDTO.getCnpj();
		this.nomeFantasia = instituicaoDTO.getNomeFantasia();
		this.razaoSocial = instituicaoDTO.getRazaoSocial();
		this.tipoUnidade = instituicaoDTO.getTipoUnidade();
		this.endereco.setEndereco(instituicaoDTO.getLogradouro());
		this.endereco.setNumero(instituicaoDTO.getNumero());
		this.endereco.setComplemento(instituicaoDTO.getComplemento());
		this.endereco.setBairro(instituicaoDTO.getBairro());
		this.endereco.setCep(instituicaoDTO.getCep());
		this.endereco.setMunicipio(instituicaoDTO.getMunicipio());
	}
}
