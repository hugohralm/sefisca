package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.Processo;

public interface EtapaProcessoDao extends Persistencia<EtapaProcesso> {

    List<EtapaProcesso> listarPorProcesso(Processo processo) throws PersistenceException;

}
