package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.Cargo;

@Repository("cargoDaoJpa")
public class CargoDaoJpa extends PersistenciaJpa<Cargo>
        implements CargoDao {

    private static final long serialVersionUID = 1L;

}
