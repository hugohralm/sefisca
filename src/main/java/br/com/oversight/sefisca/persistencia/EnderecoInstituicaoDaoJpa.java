package br.com.oversight.sefisca.persistencia;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.EnderecoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

@Repository("enderecoInstituicaoDaoJpa")
public class EnderecoInstituicaoDaoJpa extends PersistenciaJpa<EnderecoInstituicao> implements EnderecoInstituicaoDao {

    private static final long serialVersionUID = 1L;

    @Override
    public EnderecoInstituicao enderecoInstituicaoPorInstituicao(Instituicao instituicao) throws PersistenciaException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct e from EnderecoInstituicao e ");
            sql.append("where e.instituicao = :instituicao ");

            TypedQuery<EnderecoInstituicao> query = em.createQuery(sql.toString(), EnderecoInstituicao.class);
            query.setParameter("instituicao", instituicao);
            return query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar endereço da instituição por instituição.", e);
        }
    }

}
