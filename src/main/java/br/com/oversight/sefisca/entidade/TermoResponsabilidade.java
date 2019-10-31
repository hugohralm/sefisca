package br.com.oversight.sefisca.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ambientinformatica.util.Entidade;

@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true)
public class TermoResponsabilidade extends Entidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "termo_responsabilidade_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "termo_responsabilidade_seq", sequenceName = "termo_responsabilidade_seq", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAceite;

    @Column(nullable = false)
    private boolean aceitou;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private TermoResponsabilidadeTemplate template;

    public TermoResponsabilidade() {}

    public TermoResponsabilidade(TermoResponsabilidadeTemplate template) {
        this.template = template;
    }

    @Override
    public Object getId() {
        return this.id;
    }

    public boolean isAceitou() {
        return aceitou;
    }

    public void setAceitou(boolean aceitou) {
        this.aceitou = aceitou;
        if (aceitou) {
            this.dateAceite = new Date();
        }
    }

    public Date getDataAceite() {
        return dateAceite;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TermoResponsabilidadeTemplate getTemplate() {
        return template;
    }

}