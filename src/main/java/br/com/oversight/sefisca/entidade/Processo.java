package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
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
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Processo extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "processo_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "processo_seq", sequenceName = "processo_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnumTipoProcesso tipoProcesso;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnumStatusProcesso statusProcesso;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnumEtapaProcesso etapaProcesso;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Informe a data de abertura do processo", groups = AmbientValidator.class)
    private Date dataAbertura = new Date();

    @Getter
    @Setter
    @NotBlank(message = "Informe o número do processo", groups = AmbientValidator.class)
    @Length(min = 0, max = 100, message = "O limite do campo numero é de 100 caracteres.", groups = AmbientValidator.class)
    private String numero;

    @Getter
    @Setter
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @NotNull(message = "Informe a instituição", groups = AmbientValidator.class)
    private Instituicao instituicao;

    @Getter
    @Setter
    @ManyToOne
    private Usuario profissionalResponsavel;

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

    public Processo(Usuario usuario) {
        this.usuario = usuario;
        this.statusProcesso = EnumStatusProcesso.ATIVO;
        this.etapaProcesso = EnumEtapaProcesso.ABERTURA;
        this.numero = UUID.randomUUID().toString();
    }

    public String getDataAberturaFormatada() {
        return UtilSefisca.getDataStringFormatadaMask(this.dataAbertura, "dd/MM/yyyy");
    }
}
