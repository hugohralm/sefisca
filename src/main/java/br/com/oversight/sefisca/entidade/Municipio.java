package br.com.oversight.sefisca.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Municipio extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    private Integer id;

    @Getter @Setter
    @Length(min = 0, max = 255, message = "O limite do campo descrição é de 255 caracteres.", groups = AmbientValidator.class)
    @Column(length = 255, nullable = false)
    private String descricao;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private EnumUf uf;

    @Getter @Setter
    private Integer codigoIbge;

    @Override
    public String toString() {
        return String.format("%s (%s)", descricao, uf);
    }
}
