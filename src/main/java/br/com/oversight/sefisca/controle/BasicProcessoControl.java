package br.com.oversight.sefisca.controle;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.persistencia.EtapaProcessoDao;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import lombok.Getter;
import lombok.Setter;

public abstract class BasicProcessoControl {

    @Setter
    protected MenuProcessoControl menuProcessoControl;

    @Setter
    protected ProcessoDao processoDao;

    @Getter
    @Setter
    protected EtapaProcessoDao etapaProcessoDao;

    @Getter
    @Setter
    protected EtapaProcesso etapaProcesso;

    @Getter
    protected Processo processo;

    protected abstract void salvar() throws Exception;

    public abstract String getTitulo();

    public abstract String getUrl();

    protected abstract void postConstruct();

    protected void consultarProcesso() throws PersistenciaException {
        this.processo = processoDao.consultarPorId(menuProcessoControl.getProcesso().getId());
    }
}