package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Arquivo extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "arquivo_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "arquivo_seq", sequenceName = "arquivo_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @Getter
    @Setter
    @NotEmpty(message = "Informe a chave do arquivo.", groups = AmbientValidator.class)
    @Column(unique = true, length = 255, nullable = false)
    @Length(min = 0, max = 255, message = "O limite do campo chave é de 255 caracteres.", groups = AmbientValidator.class)
    private String chave;

    @Getter
    @Setter
    @NotEmpty(message = "Informe o nome do arquivo.", groups = AmbientValidator.class)
    @Column(length = 255, nullable = false)
    @Length(min = 0, max = 255, message = "O limite do nome do arquivo é de 255 caracteres.", groups = AmbientValidator.class)
    private String nome;

    @PrePersist
    private void prePersist() {
        this.dataCadastro = new Date();
    }
}
