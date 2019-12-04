package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class ItemEtapa extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "itemEtapa_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "itemEtapa_seq", sequenceName = "itemEtapa_seq", allocationSize = 1, initialValue = 1)
    private Integer id;
    
    @Getter
    @Setter
    @NotBlank(message = "Informe a descrição do item.", groups = AmbientValidator.class)
    @Length(min = 0, max = 255, message = "O limite do campo numero é de 255 caracteres.", groups = AmbientValidator.class)
    private String descricao;
    
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EnumTipoDocumento tipoDocumento;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @NotNull(message = "Informe a etapa do processo.", groups = AmbientValidator.class)
    private EtapaProcesso etapaProcesso;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "modelodocumento_id")
    private ModeloDocumento modeloDocumento;

    @Getter
    @Setter
    private int pagina;
    
    @Getter
    @Setter
    @ManyToMany
    private Set<Pessoa> profissionaisEnvolvidos;
    
    @Getter
    @Setter
    @ManyToMany
    private Set<Assunto> assuntos;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    private Usuario usuario;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;
    
    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date dataConclusao = new Date();

    @PrePersist
    private void setDataCriacao() {
        setDataCriacao(new Date());
    }
}
