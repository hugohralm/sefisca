package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.Length;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Instituicao extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "instituicao_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "instituicao_seq", sequenceName = "instituicao_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    @Length(min = 0, max = 7, message = "O limite do campo cnes é de 7 caracteres.", groups = AmbientValidator.class)
    @Column(length = 7, nullable = true)
    private String cnes;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 2, nullable = true)
    private EnumTipoPessoa tipoPessoa;

    @Getter
    @Setter
    @Length(min = 0, max = 19, message = "O limite do campo cpf/cnpj é de 18 caracteres.", groups = AmbientValidator.class)
    @Column(length = 19, nullable = true)
    private String cpfCnpj;

    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(max = 255, message = "O limite do campo nome fantasia é de 255 caracteres.", groups = AmbientValidator.class)
    private String nomeFantasia;

    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(max = 255, message = "O limite do campo razão social é de 255 caracteres.", groups = AmbientValidator.class)
    private String razaoSocial;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private TipoUnidade tipoUnidade;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private Usuario usuario;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao = new Date();

    @PrePersist
    private void atualizarData() {
        setDataCriacao(new Date());
    }

    public Instituicao(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isPessoaFisica() {
        return this.tipoPessoa.equals(EnumTipoPessoa.PF);
    }

    @Override
    public String toString() {
        return nomeFantasia;
    }
}
