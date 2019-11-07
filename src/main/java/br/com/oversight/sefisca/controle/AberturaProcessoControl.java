package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.services.DataSusService;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AberturaProcessoControl")
public class AberturaProcessoControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String cnesCnpj;
	
	@Getter
	@Setter
	private String tipo = "CNES";

	@Getter
	private Usuario usuarioLogado;

	@Getter
	private InstituicaoDTO instituicaoDTO;

	@Getter @Setter
	private Processo processo;
	
	@Getter
	private Instituicao instituicao;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Autowired
	private DataSusService dataSusService;

	@PostConstruct
	public void init() {
		this.usuarioLogado = usuarioLogadoControl.getUsuario();
		this.processo = new Processo();
		this.instituicao = new Instituicao();
	}

	public void consultarEstabelecimento() {
		try {
			if (this.cnesCnpj.isEmpty()) {
				UtilFaces.addMensagemFaces("Codigo obrigatorio CNES", FacesMessage.SEVERITY_ERROR);
				return;
			}

			if(this.tipo.equals("CNES")) {
				this.instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(this.cnesCnpj, null);
			} else {
				this.instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(null, UtilSefisca.limparMascara(this.cnesCnpj));
			}
			
			if (this.instituicaoDTO != null) {
				this.instituicao.setDto(this.instituicaoDTO);
			} else {
				this.instituicao = new Instituicao();
				UtilFaces.addMensagemFaces(this.tipo + " não encontrado.", FacesMessage.SEVERITY_WARN);
				UtilFaces.addMensagemFaces("Caso seja um novo cadastro preencha todos os campos.",
						FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}
	
	public boolean isCnes() {
		return this.tipo.equals("CNES");
	}
	
	
}
