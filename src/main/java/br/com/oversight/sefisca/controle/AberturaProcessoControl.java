package br.com.oversight.sefisca.controle;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.controle.dto.EstabelecimentoDTO;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.MunicipioDao;
import br.com.oversight.sefisca.persistencia.UsuarioDao;
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
	private EstabelecimentoDTO estabelecimentoDTO;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Autowired
	private DataSusService dataSusService;

	@PostConstruct
	public void init() {
		this.usuarioLogado = usuarioLogadoControl.getUsuario();
		this.estabelecimentoDTO = new EstabelecimentoDTO();
	}

	public void consultarEstabelecimento() {
		try {
			if (this.codigoCNES.isEmpty()) {
				UtilFaces.addMensagemFaces("Codigo obrigatorio CNES", FacesMessage.SEVERITY_ERROR);
				return;
			}

			estabelecimentoDTO = dataSusService.consultarEstabelecimentoSaude(this.codigoCNES);

			if (estabelecimentoDTO != null) {

			} else {
				UtilFaces.addMensagemFaces("CNES não encontrado.", FacesMessage.SEVERITY_WARN);
				UtilFaces.addMensagemFaces("Caso seja uma instituicao nova preencha todos os campos.",
						FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}
}
