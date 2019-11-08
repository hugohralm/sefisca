package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.Processo;

@Repository("processoDao")
public class ProcessoDaoJpa extends PersistenciaJpa<Processo> implements ProcessoDao{

	private static final long serialVersionUID = 1L;

}
