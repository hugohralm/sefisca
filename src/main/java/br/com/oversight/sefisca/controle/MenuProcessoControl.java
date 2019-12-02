package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.persistencia.EtapaProcessoDao;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import lombok.Getter;
import lombok.Setter;

@Controller("MenuProcessoControl")
@Scope("conversation")
public class MenuProcessoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ProcessoDao processoDao;

    @Autowired
    private EtapaProcessoDao etapaProcessoDao;

    private BasicJEControl controlador;

    @Getter
    @Setter
    private int menu = 0;

    @Getter
    @Setter
    private Processo processo;

    @Getter
    public List<EtapaProcesso> etapas = new ArrayList<>();

    public String iniciar(Processo processo) {
        try {
            this.processo = processoDao.consultarPorId(processo.getId());
            this.etapas = etapaProcessoDao.listarPorProcesso(processo);
            this.controlador = (BasicJEControl) UtilFaces.getManagedBean("EtapaProcessoControl");
            this.controlador.setMenuProcessoControl(this);
            this.controlador.setProcessoDao(processoDao);
            this.controlador.setEtapaProcessoDao(etapaProcessoDao);
            this.controlador.setEtapaProcesso(this.etapas.get(0));
            this.controlador.postConstruct();
            return "/andamentoProcesso/" + abrirAba(0);
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }

    public String abrirAba(int index) {
        this.menu = index;
        this.controlador.setEtapaProcesso(etapas.get(index));
        return this.controlador.getUrl() + "?faces-redirect=true";
    }

    public String voltar() {
        return abrirAba(menu > 0 ? menu - 1 : menu);
    }

    public String avancar() {
        try {
            this.controlador.salvar();
            return abrirAba(menu < this.etapas.size() - 1 ? menu + 1 : menu);
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }
    
    public boolean isVoltarEnable() {
        return menu != 0;
    }
}
