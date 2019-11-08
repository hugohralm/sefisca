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
import lombok.Getter;
import lombok.Setter;

@Entity
public class Endereco extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "endereco_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "endereco_seq", sequenceName = "endereco_seq", allocationSize = 1, initialValue = 1)
	@Getter
	private Long id;

	@Getter	@Setter
	@NotBlank(message = "Informe o endereço.", groups = AmbientValidator.class)
	@Column(nullable = false, length = 255)
	@Length(min = 0, max = 255, message = "O limite do campo endereço é de 255 caracteres.", groups = AmbientValidator.class)
	private String endereco;

	@Getter	@Setter
	@ManyToOne(optional = false)
	@NotNull(message = "Informe o município.", groups = AmbientValidator.class)
	private Municipio municipio;

	@Getter	@Setter
	@Column(nullable = false)
	@NotBlank(message = "Informe o CEP.", groups = AmbientValidator.class)
	private String cep;

	@Getter	@Setter
	@NotBlank(message = "Informe o bairro.", groups = AmbientValidator.class)
	@Column(length = 255, nullable = false)
	@Length(min = 0, max = 255, message = "O limite do campo bairro é de 255 caracteres.", groups = AmbientValidator.class)
	private String bairro;

	@Getter	@Setter
	@NotBlank(message = "Informe o número.", groups = AmbientValidator.class)
	@Column(length = 20, nullable = false)
	@Length(min = 0, max = 20, message = "O limite do campo número é de 20 caracteres.", groups = AmbientValidator.class)
	private String numero;

	@Getter	@Setter
	@Column(length = 512)
	@Length(min = 0, max = 512, message = "O limite do campo complemento é de 512 caracteres.", groups = AmbientValidator.class)
	private String complemento;
	
	@Getter	@Setter
	private String longitude;
	
	@Getter	@Setter
	private String latitude;
	
	@Getter	@Setter
	private String geoJson;

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
}