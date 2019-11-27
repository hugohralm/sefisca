package br.com.oversight.sefisca.persistencia;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.DadosProfissionais;
import br.com.oversight.sefisca.entidade.Pessoa;

@Repository("dadosProfissionaisDaoJpa")
public class DadosProfissionaisDaoJpa extends PersistenciaJpa<DadosProfissionais> implements DadosProfissionaisDao {

	private static final long serialVersionUID = 1L;

	@Override
	public DadosProfissionais consultarDadosPorPessoa(Pessoa pessoa) throws PersistenceException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select distinct dp from DadosProfissionais dp ");
			sql.append("left join fetch dp.pessoa ps ");
			sql.append("left join fetch dp.cargo c ");
			sql.append("where ps = :pessoa ");

			TypedQuery<DadosProfissionais> query = em.createQuery(sql.toString(), DadosProfissionais.class);
			query.setParameter("pessoa", pessoa);
			return query.getSingleResult();

		} catch (NoResultException ne) {
			return new DadosProfissionais();
		} catch (Exception e) {
			UtilLog.getLog().error(e.getMessage(), e);
			throw new PersistenciaException("Erro ao consultar dados profissionais.", e);
		}
	}

}
