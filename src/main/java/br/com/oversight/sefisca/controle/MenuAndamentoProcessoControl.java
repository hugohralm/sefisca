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

@Controller("MenuAndamentoProcessoControl")
@Scope("conversation")
public class MenuAndamentoProcessoControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ProcessoDao processoDao;

    @Autowired
    private EtapaProcessoDao etapaProcessoDao;

    @Getter
    @Setter
    private int menu = 0;

    @Getter
    @Setter
    private Processo processo;

    private static final List<String> NOMES_CONTROLADORES = new ArrayList<>();

    @Getter
    public List<BasicJEControl> controladores = new ArrayList<>();

    @Getter
    public List<EtapaProcesso> etapas = new ArrayList<>();

    public String iniciar(Processo processo) {
        try {
            this.controladores = new ArrayList<>();
            this.processo = processoDao.consultarPorId(processo.getId());
            this.etapas = etapaProcessoDao.listarPorProcesso(processo);
            for (EtapaProcesso etapaProcesso : this.etapas) {
                BasicJEControl controlador = (BasicJEControl) UtilFaces.getManagedBean("EtapaProcessoControl");
                NOMES_CONTROLADORES.add("EtapaProcessoControl");
                controlador.setMenuAndamentoProcessoControl(this);
                controlador.setProcessoDao(processoDao);
                controlador.setEtapaProcessoDao(etapaProcessoDao);
                controlador.setEtapaProcesso(etapaProcesso);
                controlador.postConstruct();
                this.controladores.add(controlador);
            }
            return "/andamentoProcesso/" + abrirAba(0);
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }

    public String abrirAba(int index) {
        this.menu = index;
        return controladores.get(index).getUrl() + "?faces-redirect=true";
    }

    public String voltar() {
        return abrirAba(menu > 0 ? menu - 1 : menu);
    }

    public String avancar() {
        try {
            controladores.get(menu).salvar();
            return abrirAba(menu < NOMES_CONTROLADORES.size() - 1 ? menu + 1 : menu);
        } catch (Exception e) {
            UtilFaces.addMensagemFaces(e);
        }
        return null;
    }
}
