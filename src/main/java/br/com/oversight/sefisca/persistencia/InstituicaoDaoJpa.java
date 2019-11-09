package br.com.oversight.sefisca.persistencia;

import java.io.IOException;

import javax.persistence.TypedQuery;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.exception.PersistenciaException;
import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.services.DataSusService;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;

@Repository("instituicaoDao")
public class InstituicaoDaoJpa extends PersistenciaJpa<Instituicao> implements InstituicaoDao{

    private static final long serialVersionUID = 1L;
    
    @Autowired
	private DataSusService dataSusService;
    
    @Getter
	private InstituicaoDTO instituicaoDTO;

	@Override
	public Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws PersistenciaException, SOAPException, IOException, TransformerFactoryConfigurationError, TransformerException {
		Instituicao response = new Instituicao();
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct i from Instituicao i where 1 = 1");

        if(tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES)){
        	sql.append(" and i.cnes = :cnes");
        }
        
        if(tipoCodigo.equals(EnumTipoCodigoInstituicao.CNPJ)){
        	sql.append(" and i.cnpj = :cnpj");
        }
        
        TypedQuery<Instituicao> query = em.createQuery(sql.toString(), Instituicao.class);
        
        if(tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES)){
        	query.setParameter("cnes", cnesCnpj);
        }
        if(tipoCodigo.equals(EnumTipoCodigoInstituicao.CNPJ)){
        	query.setParameter("cnpj", cnesCnpj);
        }
        
        if(query.getResultList().size() > 0) {
        	return query.getResultList().get(0);
        }
		
		if (tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES)) {
			this.instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(cnesCnpj, null);
		} else {
			this.instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(null, UtilSefisca.limparMascara(cnesCnpj));
		}
		
		
		if (this.instituicaoDTO != null) {
			response.setDto(this.instituicaoDTO);
		}
		
		return response;
	}
}
