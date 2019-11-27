package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.TurnoAtendimento;

@Repository("turnoAtendimentoDao")
public class TurnoAtendimentoDaoJpa extends PersistenciaJpa<TurnoAtendimento> implements TurnoAtendimentoDao {

    private static final long serialVersionUID = 1L;
}
