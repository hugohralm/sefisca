package br.com.oversight.sefisca.persistencia;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.DadosInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

@Repository("dadosInstituicaoDaoJpa")
public class DadosInstituicaoDaoJpa extends PersistenciaJpa<DadosInstituicao> implements DadosInstituicaoDao {

    private static final long serialVersionUID = 1L;

    @Override
    public DadosInstituicao dadosInstituicaoPorInstituicao(Instituicao instituicao) throws PersistenciaException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct d from DadosInstituicao d ");
            sql.append("where d.instituicao = :instituicao ");

            TypedQuery<DadosInstituicao> query = em.createQuery(sql.toString(), DadosInstituicao.class);
            query.setParameter("instituicao", instituicao);
            return query.getSingleResult();
        } catch (NoResultException ne) {
            return null;
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar dados da instituição por instituição.", e);
        }
    }

}
