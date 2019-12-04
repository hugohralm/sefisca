package br.com.oversight.sefisca.persistencia;

import java.util.Date;

import org.primefaces.model.UploadedFile;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Usuario;

public interface InstituicaoDao extends Persistencia<Instituicao> {
    int atualizarInstituicaoCsv(UploadedFile file, Date ultimaDataAtualizacao, Usuario usuario) throws Exception;
    
    Date ultimaDataAtualizacao();

    Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws Exception;
}
