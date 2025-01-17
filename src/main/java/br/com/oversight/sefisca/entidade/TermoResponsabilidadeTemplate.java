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
import org.hibernate.validator.constraints.NotEmpty;

import br.com.ambientinformatica.util.AmbientValidator;
import br.com.ambientinformatica.util.Entidade;
import lombok.Getter;
import lombok.Setter;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class TermoResponsabilidadeTemplate extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @GeneratedValue(generator = "termo_responsabilidade_template_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "termo_responsabilidade_template_seq", sequenceName = "termo_responsabilidade_template_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Getter @Setter
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @Getter @Setter
    @ManyToOne(optional = false)
    private Usuario usuario;

    @Getter @Setter
    @NotEmpty(message = "Informe o texto", groups = AmbientValidator.class)
    @Length(min = 0, max = 5000, message = "O limite do campo texto é de 5000 caracteres.", groups = AmbientValidator.class)
    @Column(nullable = false, length = 5000)
    private String texto;

    public TermoResponsabilidadeTemplate() {}

    public TermoResponsabilidadeTemplate(Usuario usuario) {
        this.usuario = usuario;
    }

    @PrePersist
    private void insereData() {
        this.dataCadastro = new Date();
    }

    public boolean isNovo() {
        return this.id == null;
    }

    public String getPreviaTexto() {
    	String textoLocal = texto.replaceAll("\\<[^>]*>","");;
        return (textoLocal != null && textoLocal.length() > 50 ? textoLocal.substring(0, 50) + "..." : textoLocal).replaceAll("\\<[^>]*>","");
    }
}