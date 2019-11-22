package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

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

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class Instituicao2 extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "instituicao_csv_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "instituicao_csv_seq", sequenceName = "instituicao_csv_seq", allocationSize = 1, initialValue = 1)
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
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = true)
    private EnumSituacaoInstituicao situacaoInstituicao;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = true)
    private EnumTipoGestao tipoGestao;

    @Getter
    @Setter
    @Length(min = 0, max = 18, message = "O limite do campo cnpj é de 18 caracteres.", groups = AmbientValidator.class)
    @Column(length = 18, nullable = true)
    private String cnpj;

    @Getter
    @Setter
    @Length(min = 0, max = 14, message = "O limite do campo cpf é de 14 caracteres.", groups = AmbientValidator.class)
    @Column(length = 14, nullable = true)
    private String cpf;
    
    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(max = 255, message = "O limite do campo razão social é de 255 caracteres.", groups = AmbientValidator.class)
    private String cnpjMantenedora;
    
    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(max = 255, message = "O limite do campo codigo unidade é de 255 caracteres.", groups = AmbientValidator.class)
    private String codigoUnidade;

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
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @NotNull(message = "Informe o endereco da instituição.", groups = AmbientValidator.class)
    private EnderecoInstituicao endereco = new EnderecoInstituicao();

    @Getter
    @Setter
    @Length(min = 0, max = 70, message = "O limite do campo telefone é de 30 caracteres.", groups = AmbientValidator.class)
    @Column(length = 70, nullable = true)
    private String telefone;

    @Getter
    @Setter
    @Length(min = 0, max = 70, message = "O limite do campo fax é de 30 caracteres.", groups = AmbientValidator.class)
    @Column(length = 70, nullable = true)
    private String fax;

    @Getter
    @Setter
    @Length(min = 0, max = 150, message = "O limite do campo email é de 150 caracteres.", groups = AmbientValidator.class)
    @Column(length = 150, nullable = true)
    private String email;

    @Getter
    @Setter
    @Length(min = 0, max = 150, message = "O limite do campo url é de 150 caracteres.", groups = AmbientValidator.class)
    @Column(length = 150, nullable = true)
    private String url;
    
    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(max = 255, message = "O limite do campo cpf diretor é de 255 caracteres.", groups = AmbientValidator.class)
    private String cpfDiretor;
    
    @Getter
    @Setter
    @Column(length = 255, nullable = true)
    @Length(max = 255, message = "O limite do campo conselho classe diretor é de 255 caracteres.", groups = AmbientValidator.class)
    private String conselhoClasseDiretor;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private TipoUnidade tipoUnidade;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private TurnoAtendimento turnoAtendimento;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private NaturezaJuridica naturezaJuridica;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private TipoInstituicao tipoInstituicao;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private Atividade atividadePrincipal;

    @Getter
    @Setter
    @ManyToOne(optional = true)
    private MotivoDesativacao motivoDesativacao;

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean instituicaoSempreAberta;

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean contratoFormalizadoSus;

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date dataAtualizacaoGeo = new Date();

    @Getter
    @Setter
    @Temporal(TemporalType.DATE)
    private Date dataAtualizacao = new Date();

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
}
