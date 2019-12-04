package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ModeloDocumento extends Entidade implements Serializable, Comparable<ModeloDocumento> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "modelo_documento_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "modelo_documento_seq", sequenceName = "modelo_documento_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    @NotBlank(message = "Informe o nome do documento.", groups = AmbientValidator.class)
    @Column(length = 512)
    @Length(min = 0, max = 512, message = "O limite do campo descrição é de 512 caracteres.", groups = AmbientValidator.class)
    private String descricao;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    @NotNull(message = "Informe o tamanho do documento.", groups = AmbientValidator.class)
    private EnumTamanhoDocumento tamanhoDocumento;

    @Getter
    @Setter
    private boolean procuracao = false;

    @Getter
    @Setter
    private boolean opcional = false;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public int compareTo(ModeloDocumento a) {
        int descricao = this.descricao.compareTo(a.getDescricao());
        if (descricao != 0) {
            return descricao;
        } else {
            return descricao;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ModeloDocumento) {
            ModeloDocumento p = (ModeloDocumento) obj;
            return this.descricao.equals(p.getDescricao());
        } else
            return false;
    }
}
