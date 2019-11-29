package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@org.hibernate.annotations.Entity
public class MotivoDesativacao extends Entidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Getter
	@GeneratedValue(generator = "motivo_desativacao_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "motivo_desativacao_seq", sequenceName = "motivo_desativacao_seq", allocationSize = 1, initialValue = 1)
	private Integer id;
	
	@Getter
	@Setter
	@NotBlank(message = "Informe o nome.", groups = AmbientValidator.class)
	@Length(min = 0, max = 255, message = "O limite do campo nome é de 255 caracteres.", groups = AmbientValidator.class)
	@Column(nullable = false, length = 255)
	private String nome;
	
	@Override
	public String toString() {
		return this.nome;
	}
}
