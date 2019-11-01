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
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AberturaProcessoControl")
public class AberturaProcessoControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String codigoCNES;

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
			if (this.codigoCNES.isEmpty()) {
				UtilFaces.addMensagemFaces("Codigo obrigatorio CNES", FacesMessage.SEVERITY_ERROR);
				return;
			}

			instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(this.codigoCNES);
			if (instituicaoDTO != null) {
				this.instituicao.setDto(this.instituicaoDTO);
			} else {
				this.instituicao = new Instituicao();
				UtilFaces.addMensagemFaces("CNES não encontrado.", FacesMessage.SEVERITY_WARN);
				UtilFaces.addMensagemFaces("Caso seja uma instituicao nova preencha todos os campos.",
						FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}
}
