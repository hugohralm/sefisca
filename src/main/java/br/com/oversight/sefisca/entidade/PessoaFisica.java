package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.jpa.util.CpfCnpj;
import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class PessoaFisica extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Getter
	@GeneratedValue(generator = "pessoaFisica_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "pessoaFisica_seq", sequenceName = "pessoaFisica_seq", allocationSize = 1, initialValue = 1)
	private Integer id;

	@Getter
	@Setter
	@NotBlank(message = "Informe o Nome.", groups = AmbientValidator.class)
	@Length(min = 0, max = 255, message = "O limite do campo nome é de 255 caracteres.", groups = AmbientValidator.class)
	@Column(nullable = false, length = 255)
	private String nome;

	@Getter
	@Setter
	@NotBlank(message = "Informe o CPF.", groups = AmbientValidator.class)
	@CpfCnpj(message = "CPF inválido.", groups = AmbientValidator.class)
	@Column(unique = true, nullable = false)
	private String cpf;

	@Getter
	@Setter
	@Column(length = 255, nullable = true)
	@Length(max = 255, message = "O limite do campo RG é de 255 caracteres.", groups = AmbientValidator.class)
	private String rg;

	@Getter
	@Setter
	@Column(unique = true, length = 255, nullable = false)
	@NotBlank(message = "Informe o E-mail.", groups = AmbientValidator.class)
	@Email(message = "Informe um endereço de e-mail válido.", groups = AmbientValidator.class)
	@Length(min = 0, max = 255, message = "O limite do campo e-mail é de 255 caracteres.", groups = AmbientValidator.class)
	private String email;

	@Getter
	@Setter
	@Temporal(TemporalType.DATE)
	@NotNull(message = "Informe a Data de Nascimento.", groups = AmbientValidator.class)
	private Date dataNascimento;

	@Getter
	@Setter
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@NotNull(message = "Informe o Endereço.", groups = AmbientValidator.class)
	private Endereco endereco = new Endereco();

	@Getter
	@Setter
	@Column(nullable = true)
	@Length(min = 10, max = 16, message = "Informe corretamente o Telefone (incluindo o prefixo).", groups = AmbientValidator.class)
	private String telefone;

	@Getter
	@Setter
	@Column(nullable = true)
	@Length(min = 10, max = 16, message = "Informe corretamente o Celular (incluindo o prefixo).", groups = AmbientValidator.class)
	private String celular;

	@Getter
	@Setter
	@Column(length = 255, nullable = true)
	@Length(max = 255, message = "O limite do campo profissão é de 255 caracteres.", groups = AmbientValidator.class)
	private String profissao;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = true)
	private EnumEstadoCivil estadoCivil;

	@Getter
	@Setter
	@Column(length = 255, nullable = true)
	@Length(max = 255, message = "O limite do campo nacionalidade é de 255 caracteres.", groups = AmbientValidator.class)
	private String nacionalidade;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @NotNull(message = "Informe o sexo.", groups = AmbientValidator.class)
    private EnumSexo sexo;
	
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

	public PessoaFisica() {
		super();
	}

	public PessoaFisica(String nome, String cpf, String rg, EnumSexo sexo, String email, Date dataNascimento, EnumEstadoCivil estadoCivil,
			String telefone, String celular, Endereco endereco) {
		super();
		this.nome = nome;
		this.cpf = cpf;
		this.rg = rg;
		this.sexo = sexo;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.estadoCivil = estadoCivil;
		this.telefone = telefone;
		this.celular = celular;
		this.endereco = endereco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((rg == null) ? 0 : rg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaFisica other = (PessoaFisica) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (dataCriacao == null) {
			if (other.dataCriacao != null)
				return false;
		} else if (!dataCriacao.equals(other.dataCriacao))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (rg == null) {
			if (other.rg != null)
				return false;
		} else if (!rg.equals(other.rg))
			return false;
		return true;
	}
}
