package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;

@Entity
public class Endereco extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "endereco_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "endereco_seq", sequenceName = "endereco_seq", allocationSize = 1, initialValue = 1)
	private Long id;

	@NotBlank(message = "Informe o endereço.", groups = AmbientValidator.class)
	@Column(nullable = false, length = 255)
	@Length(min = 0, max = 255, message = "O limite do campo endereço é de 255 caracteres.", groups = AmbientValidator.class)
	private String endereco;

	@ManyToOne(optional = false)
	@NotNull(message = "Informe o município.", groups = AmbientValidator.class)
	private Municipio municipio;

	@Column(nullable = false)
	@NotBlank(message = "Informe o CEP.", groups = AmbientValidator.class)
	private String cep;

	@NotBlank(message = "Informe o bairro.", groups = AmbientValidator.class)
	@Column(length = 255, nullable = false)
	@Length(min = 0, max = 255, message = "O limite do campo bairro é de 255 caracteres.", groups = AmbientValidator.class)
	private String bairro;

	@NotBlank(message = "Informe o número.", groups = AmbientValidator.class)
	@Column(length = 20, nullable = false)
	@Length(min = 0, max = 20, message = "O limite do campo número é de 20 caracteres.", groups = AmbientValidator.class)
	private String numero;

	@Column(length = 512)
	@Length(min = 0, max = 512, message = "O limite do campo complemento é de 512 caracteres.", groups = AmbientValidator.class)
	private String complemento;

	public Endereco() {
		super();
	}
	
	public Endereco(String endereco, Municipio municipio, String cep, String bairro, String numero) {
		super();
		this.endereco = endereco;
		this.municipio = municipio;
		this.cep = cep;
		this.bairro = bairro;
		this.numero = numero;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s, %s - %s", this.endereco, this.numero,
				this.complemento != null && !this.complemento.isEmpty() ? this.complemento + "" : "", this.bairro,
				this.municipio);
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@Override
	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}
}