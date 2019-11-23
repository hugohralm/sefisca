package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.Atividade;

@Repository("atividadeDao")
public class AtividadeDaoJpa extends PersistenciaJpa<Atividade> implements AtividadeDao {

    private static final long serialVersionUID = 1L;
}
