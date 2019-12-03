package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.Assunto;
import br.com.oversight.sefisca.entidade.ItemEtapa;
import br.com.oversight.sefisca.util.UtilSefisca;

@Repository("assuntoDao")
public class AssuntoDaoJpa extends PersistenciaJpa<Assunto> implements AssuntoDao {

    private static final long serialVersionUID = 1L;

    @Override
    public List<Assunto> listarPorDescricao(String descricao) throws PersistenceException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct a from Assunto a ");
            sql.append("where 1=1 ");

            if (!UtilSefisca.isNullOrEmpty(descricao)) {
                sql.append("and upper(a.descricao) like upper(:descricao) ");
            }
            TypedQuery<Assunto> query = em.createQuery(sql.toString(), Assunto.class);
            
            if (!UtilSefisca.isNullOrEmpty(descricao)) {
                query.setParameter("descricao", descricao);
            }
            return query.getResultList();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar assuntos.", e);
        }
    }

}
