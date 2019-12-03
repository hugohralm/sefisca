package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.Assunto;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.ItemEtapa;

public interface AssuntoDao extends Persistencia<Assunto> {

    List<Assunto> listarPorDescricao(String descricao) throws PersistenceException;

}
