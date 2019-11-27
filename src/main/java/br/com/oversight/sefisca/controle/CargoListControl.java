package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.Cargo;
import br.com.oversight.sefisca.entidade.TermoResponsabilidadeTemplate;
import br.com.oversight.sefisca.persistencia.CargoDao;
import br.com.oversight.sefisca.persistencia.TermoResponsabilidadeTemplateDao;
import br.com.oversight.sefisca.util.UtilMessages;
import lombok.Getter;

@Scope("conversation")
@Controller("CargoListControl")
public class CargoListControl implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Autowired
    private CargoDao cargoDao;

    @Getter
    private List<Cargo> cargos = new ArrayList<>();

    @PostConstruct
    public void init() {
        try {
            this.cargos = cargoDao.listar();
        } catch (Exception e) {
        	e.printStackTrace();
            UtilMessages.addMessage(e);
        }
    }

}
