package br.com.oversight.sefisca.persistencia;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.ambientinformatica.jpa.persistencia.PersistenciaJpa;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.controle.dto.ProfissionalDTO;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Profissional;
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
	public Instituicao instituicaoPorCnesCpnj(String cnesCnpj, EnumTipoCodigoInstituicao tipoCodigo) throws Exception {
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
	
	@Override
	public Set<Profissional> profissionaisServicePorCnes(String cnes) throws Exception {
		List<ProfissionalDTO> profissionaisDTO = dataSusService.consultarProfissionaisCnes(cnes);
		Set<Profissional> profissionais = new HashSet<Profissional>();
		for(ProfissionalDTO profissialDTO : profissionaisDTO) {
			Profissional profissional = new Profissional(profissialDTO);
			profissionais.add(profissional);
		}
		return profissionais;
	}
}
