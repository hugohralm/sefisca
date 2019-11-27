package br.com.oversight.sefisca.persistencia;

import javax.persistence.PersistenceException;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.DadosProfissionais;
import br.com.oversight.sefisca.entidade.Pessoa;

public interface DadosProfissionaisDao extends Persistencia<DadosProfissionais> {

	DadosProfissionais consultarDadosPorPessoa(Pessoa pessoa) throws PersistenceException;
}
