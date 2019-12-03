package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.ItemEtapa;

public interface ItemEtapaDao extends Persistencia<ItemEtapa> {

    List<ItemEtapa> listarPorEtapa(EtapaProcesso etapaProcesso) throws PersistenceException;

}
