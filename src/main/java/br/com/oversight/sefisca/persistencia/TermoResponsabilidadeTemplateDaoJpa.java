package br.com.oversight.sefisca.persistencia;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.exception.ValidacaoException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.TermoResponsabilidadeTemplate;

@Repository("termoResponsabilidadeTemplateDao")
public class TermoResponsabilidadeTemplateDaoJpa extends PersistenciaJpa<TermoResponsabilidadeTemplate>
        implements TermoResponsabilidadeTemplateDao {

    private static final long serialVersionUID = 1L;

    @Override
    public TermoResponsabilidadeTemplate consultarUltimo() throws PersistenciaException {
        try {
            String sql = "select t from TermoResponsabilidadeTemplate t order by t.id desc";
            TypedQuery<TermoResponsabilidadeTemplate> query = em.createQuery(sql, TermoResponsabilidadeTemplate.class);
            return query.setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            throw new PersistenciaException("Não há template de termo de responsabilidade cadastrado.", e);
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao consultar último template do termo de responsabilidade.", e);
        }
    }

    @Override
    public boolean isEditavel(TermoResponsabilidadeTemplate template) throws PersistenciaException {
        try {
            if (template.isNovo()) {
                return true;
            }
            TypedQuery<Boolean> query = em.createQuery(
                    "select case when count(t) > 0 then true else false end from TermoResponsabilidade t where t.template = :template",
                    Boolean.class);
            query.setParameter("template", template);
            return !query.getSingleResult();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException(
                    "Erro ao consultar se o template do termo de responsabilidade pode ser editado.", e);
        }
    }

    @Override
    public TermoResponsabilidadeTemplate alterar(TermoResponsabilidadeTemplate template) throws PersistenciaException {
        validarEditavel(template);
        return super.alterar(template);
    }

    public void validarEditavel(TermoResponsabilidadeTemplate template) throws PersistenciaException {
        if (!isEditavel(template)) {
            throw new ValidacaoException(
                    "Este template de termo de responsabilidade não pode ser editado pois já foi utilizado.");
        }
    }

}
