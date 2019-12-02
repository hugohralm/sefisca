package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.jpa.util.CpfCnpj;
import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import br.com.oversight.sefisca.controle.dto.ProfissionalDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Profissional extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Getter
	@GeneratedValue(generator = "profissional_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "profissional_seq", sequenceName = "profissional_seq", allocationSize = 1, initialValue = 1)
	private Integer id;	
	
	@Getter
	@Setter
	@NotBlank(message = "Informe o nome do profissional", groups = AmbientValidator.class)
	@Length(min = 0, max = 255, message = "O limite do campo nome é de 255 caracteres.", groups = AmbientValidator.class)
	private String nome;
	
	@Getter
	@Setter
	@NotBlank(message = "Informe o cpf do profissional", groups = AmbientValidator.class)
	@CpfCnpj(message = "CPF inválido.", groups = AmbientValidator.class)
	private String cpf;
	
	@Getter
	@Setter
	@Length(min = 0, max = 100, message = "O limite do campo cns é de 100 caracteres.", groups = AmbientValidator.class)
	private String cns;
	
	@Getter
	@Setter
	@Length(min = 0, max = 100, message = "O limite do campo cbo é de 100 caracteres.", groups = AmbientValidator.class)
	private String cbo;

	@Getter
	@Setter
	@ManyToOne(optional = true)
	private Usuario usuario;

	@Getter
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;

	@PrePersist
	private void atualizarData() {
		setDataCriacao(new Date());
	}
	
	public Profissional(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Profissional(ProfissionalDTO profissionalDTO) {
		this.nome = profissionalDTO.getNome();
		this.cpf = profissionalDTO.getCpf();
		this.cns = profissionalDTO.getCns();
		this.cbo = profissionalDTO.getCbo();
	}
}
