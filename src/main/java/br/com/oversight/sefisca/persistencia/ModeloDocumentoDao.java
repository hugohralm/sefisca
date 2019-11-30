package br.com.oversight.sefisca.persistencia;

import java.util.List;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.ModeloDocumento;

public interface ModeloDocumentoDao extends Persistencia<ModeloDocumento> {

    List<ModeloDocumento> listarPorDescricao(String descricao) throws PersistenciaException;
}
