package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class DadosProfissionais extends Entidade implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Getter
	@GeneratedValue(generator = "dados_profissionais_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "dados_profissionais_seq", sequenceName = "dados_profissionais_seq", allocationSize = 1, initialValue = 1)
	private Integer id;

	@Getter
	@Setter
	@Column(length = 255, nullable = true)
	@Length(max = 255, message = "O limite do campo profissão é de 255 caracteres.", groups = AmbientValidator.class)
	private String profissao;
	
	@Getter
	@Setter
	@Column(unique = true, nullable = true)
	private String registroProfissional;
	
	@Getter
	@Setter
	@NotNull(message = "Informe o cargo.", groups = AmbientValidator.class)
	@ManyToOne
	private Cargo cargo;
	
	@Getter
	@Setter
	@OneToOne
	private Pessoa pessoa;
}
