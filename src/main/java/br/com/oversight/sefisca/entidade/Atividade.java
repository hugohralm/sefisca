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
@org.hibernate.annotations.Entity
public class Atividade extends Entidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Getter
	@GeneratedValue(generator = "atividade_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "atividade_seq", sequenceName = "atividade_seq", allocationSize = 1, initialValue = 1)
	private Integer id;
	
	@Getter
    @Setter
    @NotNull(message = "Informe o grupo da atividade.", groups = AmbientValidator.class)
    @ManyToOne
    private GrupoAtividade grupoAtividade;
	
	@Getter
	@Setter
	@NotBlank(message = "Informe o nome.", groups = AmbientValidator.class)
	@Length(min = 0, max = 255, message = "O limite do campo nome é de 255 caracteres.", groups = AmbientValidator.class)
	@Column(nullable = false, length = 255)
	private String nome;
	
	@Getter
    @Setter
    @NotBlank(message = "Informe o conceito.", groups = AmbientValidator.class)
    @Length(min = 0, max = 2048, message = "O limite do campo conceito é de 2048 caracteres.", groups = AmbientValidator.class)
    @Column(nullable = false, length = 2048)
    private String conceito;
	
	@Override
	public String toString() {
		return this.nome;
	}
}
