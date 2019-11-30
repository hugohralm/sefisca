package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.ambientinformatica.util.UtilLog;
import br.com.oversight.sefisca.entidade.ModeloDocumento;

@Repository("modeloDocumentoDao")
public class ModeloDocumentoDaoJpa extends PersistenciaJpa<ModeloDocumento> implements ModeloDocumentoDao {

    private static final long serialVersionUID = 1L;

    public List<ModeloDocumento> listarPorDescricao(String descricao) throws PersistenciaException {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("select distinct md from ModeloDocumento md ");
            sql.append("where upper(md.descricao) like upper(:descricao) ");
            
            TypedQuery<ModeloDocumento> query = em.createQuery(sql.toString(), ModeloDocumento.class);
            query.setParameter("descricao", "%" + descricao + "%");
            
            return query.getResultList();
        } catch (Exception e) {
            UtilLog.getLog().error(e.getMessage(), e);
            throw new PersistenciaException("Erro ao listar modelos de Documentos.", e);
        }
    }
}
