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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Assunto extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "assunto_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "assunto_seq", sequenceName = "assunto_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    @NotBlank(message = "Informe o nome do documento.", groups = AmbientValidator.class)
    @Column(length = 512)
    @Length(min = 0, max = 512, message = "O limite do campo descrição é de 512 caracteres.", groups = AmbientValidator.class)
    private String descricao;

}
