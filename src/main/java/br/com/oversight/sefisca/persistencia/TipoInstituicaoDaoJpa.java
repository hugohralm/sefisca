package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.TipoInstituicao;

@Repository("tipoInstituicaoDao")
public class TipoInstituicaoDaoJpa extends PersistenciaJpa<TipoInstituicao> implements TipoInstituicaoDao {

    private static final long serialVersionUID = 1L;
}
