package br.com.oversight.sefisca.persistencia;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

public interface InstituicaoDao extends Persistencia<Instituicao> {
    void atualizarInstituicaoCsv() throws Exception;
    
    Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws Exception;
}
