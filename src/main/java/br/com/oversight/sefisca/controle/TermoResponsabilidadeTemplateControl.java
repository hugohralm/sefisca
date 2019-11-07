package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.TermoResponsabilidadeTemplate;
import br.com.oversight.sefisca.persistencia.TermoResponsabilidadeTemplateDao;
import br.com.oversight.sefisca.util.UtilMessages;

@Scope("conversation")
@Controller("TermoResponsabilidadeTemplateControl")
public class TermoResponsabilidadeTemplateControl implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Autowired
    private TermoResponsabilidadeTemplateDao termoResponsabilidadeTemplateDao;

    @Autowired
    private UsuarioLogadoControl usuarioLogadoControl;

    private TermoResponsabilidadeTemplate termoResponsabilidade;

    boolean editavel;

    @PostConstruct
    private void init() {
        this.editavel = true;
        this.termoResponsabilidade = new TermoResponsabilidadeTemplate(usuarioLogadoControl.getUsuario());
    }

    public void confirmar() {
        try {
            atualizarEditavel();
            termoResponsabilidadeTemplateDao.validarEditavel(this.termoResponsabilidade);
            this.termoResponsabilidade = termoResponsabilidadeTemplateDao.alterar(this.termoResponsabilidade);
            UtilMessages.addMessage("Sucesso!",  "Registro confirmado!");
        } catch (Exception e) {
        	e.printStackTrace();
            UtilFaces.addMensagemFaces(e);
        }
    }

    public TermoResponsabilidadeTemplate getTermoResponsabilidade() {
        return termoResponsabilidade;
    }

    public void setTermoResponsabilidade(TermoResponsabilidadeTemplate termoResponsabilidade) {
        this.termoResponsabilidade = termoResponsabilidade;
        atualizarEditavel();
    }

    private void atualizarEditavel() {
        try {
            this.editavel = termoResponsabilidadeTemplateDao.isEditavel(termoResponsabilidade);
        } catch (Exception e) {
        	e.printStackTrace();
            UtilFaces.addMensagemFaces(e);
        }
    }

    public boolean isEditavel() {
        return editavel;
    }

}
