package br.com.oversight.sefisca.persistencia;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.Instituicao2;

public interface Instituicao2Dao extends Persistencia<Instituicao2> {
    void atualizarInstituicaoCsv() throws Exception;
}
