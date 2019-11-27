package br.com.oversight.sefisca.persistencia;

import org.primefaces.model.UploadedFile;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

public interface InstituicaoDao extends Persistencia<Instituicao> {
    int atualizarInstituicaoCsv(UploadedFile file) throws Exception;

    Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws Exception;
}
