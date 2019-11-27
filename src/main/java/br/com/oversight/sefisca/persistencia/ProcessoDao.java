package br.com.oversight.sefisca.persistencia;

import javax.persistence.PersistenceException;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.Processo;

public interface ProcessoDao extends Persistencia<Processo> {

    Processo consultarPorId(Integer id) throws PersistenceException;

}
