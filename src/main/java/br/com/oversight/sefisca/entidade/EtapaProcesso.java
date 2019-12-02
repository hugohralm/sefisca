package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class EtapaProcesso extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "etapaProcesso_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "etapaProcesso_seq", sequenceName = "etapaProcesso_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnumEtapaProcesso etapaProcesso;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @NotNull(message = "Informe o processo.", groups = AmbientValidator.class)
    private Processo processo;

    @Getter
    @Setter
    @ManyToOne
    private Profissional profissionalResponsavel;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date dataFim;

    @Getter
    @Setter
    private int diasPrazo;

    @Getter
    @Setter
    private int pagina;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    private Usuario usuario;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @PrePersist
    private void setDataCriacao() {
        setDataCriacao(new Date());
    }
}
