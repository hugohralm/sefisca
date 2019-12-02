package br.com.oversight.sefisca.persistencia;

import java.util.List;

import javax.persistence.PersistenceException;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;

public interface ProcessoDao extends Persistencia<Processo> {

    Processo consultarPorId(Object id) throws PersistenceException;

    List<Processo> listarPorUsuario(Usuario usuario) throws PersistenciaException;
}
