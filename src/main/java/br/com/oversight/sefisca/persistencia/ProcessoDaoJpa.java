package br.com.oversight.sefisca.persistencia;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.ModeloDocumento;
import br.com.oversight.sefisca.entidade.Processo;

@Repository("processoDao")
public class ProcessoDaoJpa extends PersistenciaJpa<Processo> implements ProcessoDao {

    private static final long serialVersionUID = 1L;

    @Override
    public Processo consultarPorId(Integer id) throws PersistenceException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct p from Processo p ");
            sql.append("where p.id =:id ");

            TypedQuery<Processo> query = em.createQuery(sql.toString(), Processo.class);
            query.setParameter("id", id);

            return query.getSingleResult();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar Processo por Id;", e);
        }
    }

}
