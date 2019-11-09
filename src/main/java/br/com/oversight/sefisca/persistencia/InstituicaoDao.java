package br.com.oversight.sefisca.persistencia;

import java.io.IOException;

import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.Persistencia;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;

public interface InstituicaoDao extends Persistencia<Instituicao>{
	Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws PersistenciaException, SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException;
}
