package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;

@Repository("processoDao")
public class ProcessoDaoJpa extends PersistenciaJpa<Processo> implements ProcessoDao {

    private static final long serialVersionUID = 1L;

    @Override
    public Processo consultarPorId(Object id) throws PersistenceException {
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

    @Override
    public List<Processo> listarPorUsuario(Usuario usuario) throws PersistenciaException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct p from Processo p ");
            if(!usuario.isPodeConsultar()) {
                sql.append("where p.profissionalResponsavel =:profissionalResponsavel ");
                sql.append("or p.usuario =:usuario ");
            }
            sql.append("ORDER BY p.dataCriacao");
            
            TypedQuery<Processo> query = em.createQuery(sql.toString(), Processo.class);
            if(!usuario.isPodeConsultar()) {
                query.setParameter("profissionalResponsavel", usuario);
                query.setParameter("usuario", usuario);
            }
            return query.getResultList();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao listar processos.", e);
        }
    }
}
