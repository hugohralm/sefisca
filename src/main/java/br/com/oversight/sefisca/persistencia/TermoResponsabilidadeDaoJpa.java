package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.TermoResponsabilidade;

@Repository("termoResponsabilidadeDao")
public class TermoResponsabilidadeDaoJpa extends PersistenciaJpa<TermoResponsabilidade>
        implements TermoResponsabilidadeDao {

    private static final long serialVersionUID = 1L;

}
