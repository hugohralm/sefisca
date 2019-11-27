package br.com.oversight.sefisca.persistencia;

import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.entidade.MotivoDesativacao;

@Repository("motivoDesativacaoDao")
public class MotivoDesativacaoDaoJpa extends PersistenciaJpa<MotivoDesativacao> implements MotivoDesativacaoDao {

    private static final long serialVersionUID = 1L;
}
