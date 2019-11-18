package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.TermoResponsabilidadeTemplate;
import br.com.oversight.sefisca.persistencia.TermoResponsabilidadeTemplateDao;
import lombok.Getter;

@Scope("conversation")
@Controller("TermoResponsabilidadeTemplateListControl")
public class TermoResponsabilidadeTemplateListControl implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

    @Autowired
    private TermoResponsabilidadeTemplateDao termoResponsabilidadeTemplateDao;

    @Getter
    private List<TermoResponsabilidadeTemplate> termosResponsabilidade = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            this.termosResponsabilidade = termoResponsabilidadeTemplateDao.listar();
        } catch (Exception e) {
        	e.printStackTrace();
            UtilFaces.addMensagemFaces(e);
        }
    }

}
