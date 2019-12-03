package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.ItemEtapa;

@Repository("itemEtapaDao")
public class ItemEtapaDaoJpa extends PersistenciaJpa<ItemEtapa> implements ItemEtapaDao {

    private static final long serialVersionUID = 1L;

    @Override
    public List<ItemEtapa> listarPorEtapa(EtapaProcesso etapaProcesso) throws PersistenceException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct i from ItemEtapa i ");
            sql.append("where i.etapaProcesso =:etapaProcesso ");
            sql.append("order by i.dataCriacao");

            TypedQuery<ItemEtapa> query = em.createQuery(sql.toString(), ItemEtapa.class);
            query.setParameter("etapaProcesso", etapaProcesso);

            return query.getResultList();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar Item etapa.", e);
        }
    }

}
