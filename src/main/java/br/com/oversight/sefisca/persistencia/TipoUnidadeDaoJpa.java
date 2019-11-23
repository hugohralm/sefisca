package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.TipoUnidade;

@Repository("tipoUnidadeDao")
public class TipoUnidadeDaoJpa extends PersistenciaJpa<TipoUnidade> implements TipoUnidadeDao {

    private static final long serialVersionUID = 1L;
}
