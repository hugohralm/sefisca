package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@org.hibernate.annotations.Entity
public class Cargo extends Entidade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Cargo() {}
	public Cargo(Usuario usuario) {
		this.usuario = usuario;
	}
	
    @PrePersist
    private void insereData() {
        this.dataCadastro = new Date();
    }
	
	@Id
	@Getter
	@GeneratedValue(generator = "cargo_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "cargo_seq", sequenceName = "cargo_seq", allocationSize = 1, initialValue = 1)
	private Integer id;

	@Getter
	@Setter
	@NotBlank(message = "Informe o nome.", groups = AmbientValidator.class)
	@Length(min = 0, max = 255, message = "O limite do campo nome é de 255 caracteres.", groups = AmbientValidator.class)
	@Column(nullable = false, length = 255)
	private String nome;

	@Getter
	@Setter
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Getter
	@Setter
	@ManyToOne(optional = false)
	private Usuario usuario;

	@Override
	public String toString() {
		return this.nome;
	}
}
