package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.NaturezaJuridica;
import br.com.oversight.sefisca.util.UtilSefisca;

@Repository("naturezaJuridicaDao")
public class NaturezaJuridicaDaoJpa extends PersistenciaJpa<NaturezaJuridica> implements NaturezaJuridicaDao {

    private static final long serialVersionUID = 1L;

    @Override
    public List<NaturezaJuridica> naturezaJuridicaPorCodigo(String codigo) throws PersistenciaException {
        try {
            String sql = "select distinct n from NaturezaJuridica n where 1 = 1 ";
            if (!UtilSefisca.isNullOrEmpty(codigo)) {
                sql += " and n.codigo = :codigo";
            }
            TypedQuery<NaturezaJuridica> query = em.createQuery(sql, NaturezaJuridica.class);
            if (!UtilSefisca.isNullOrEmpty(codigo)) {
                query.setParameter("codigo", codigo);
            }
            return query.getResultList();
        } catch (NoResultException e) {
            throw new PersistenciaException("Não foi encontrada natureza jurídica cadastrada.", e);
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar natureza jurídica.", e);
        }
    }
}
