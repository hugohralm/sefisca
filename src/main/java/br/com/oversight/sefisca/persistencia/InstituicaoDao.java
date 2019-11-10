package br.com.oversight.sefisca.persistencia;

import java.util.Set;

import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Profissional;

public interface InstituicaoDao extends Persistencia<Instituicao>{
	Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws Exception;
	
	Set<Profissional> profissionaisServicePorCnes(String cnes) throws Exception;
}
