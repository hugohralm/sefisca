package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.ambientinformatica.jpa.exception.PersistenciaException;

@Controller("EtapaProcessoControl")
@Scope("conversation")
public class EtapaProcessoControl extends BasicJEControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void salvar() throws Exception {
        etapaProcessoDao.alterar(this.etapaProcesso);
        processoDao.alterar(this.processo);
    }

    @Override
    public String getTitulo() {
        return getEtapaProcesso().getEtapaProcesso().getDescricao().toUpperCase();
    }

    @Override
    public String getUrl() {
        return "etapaProcesso";
    }

    @Override
    protected void postConstruct() {
        try {
            consultarProcesso();
        } catch (PersistenciaException e) {
            UtilFaces.addMensagemFaces(e);
        }
    }
}
