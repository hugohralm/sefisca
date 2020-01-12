package br.com.oversight.sefisca.persistencia;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnderecoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

public interface EnderecoInstituicaoDao extends Persistencia<EnderecoInstituicao> {
    EnderecoInstituicao enderecoInstituicaoPorInstituicao(Instituicao instituicao) throws PersistenciaException;
}
