package br.com.oversight.sefisca.persistencia;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.DadosInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

public interface DadosInstituicaoDao extends Persistencia<DadosInstituicao> {
    DadosInstituicao dadosInstituicaoPorInstituicao(Instituicao instituicao) throws PersistenciaException;
}
