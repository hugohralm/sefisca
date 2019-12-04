package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.EtapaProcesso;
import br.com.oversight.sefisca.entidade.Processo;

@Repository("etapaProcessoDao")
public class EtapaProcessoDaoJpa extends PersistenciaJpa<EtapaProcesso> implements EtapaProcessoDao {

    private static final long serialVersionUID = 1L;

    @Override
    public List<EtapaProcesso> listarPorProcesso(Processo processo) throws PersistenceException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct e from EtapaProcesso e ");
            sql.append("where e.processo =:processo ");
            sql.append("order by e.dataCriacao");

            TypedQuery<EtapaProcesso> query = em.createQuery(sql.toString(), EtapaProcesso.class);
            query.setParameter("processo", processo);

            return query.getResultList();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar Etapa por Id;", e);
        }
    }
}
