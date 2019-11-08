package br.com.oversight.sefisca.controle;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.ambientinformatica.ambientjsf.util.UtilFaces;
import br.com.oversight.sefisca.controle.dto.InstituicaoDTO;
import br.com.oversight.sefisca.entidade.EnumTipoCodigoInstituicao;
import br.com.oversight.sefisca.entidade.EnumTipoProcesso;
import br.com.oversight.sefisca.entidade.Instituicao;
import br.com.oversight.sefisca.entidade.Processo;
import br.com.oversight.sefisca.entidade.Usuario;
import br.com.oversight.sefisca.persistencia.ProcessoDao;
import br.com.oversight.sefisca.services.DataSusService;
import br.com.oversight.sefisca.util.UtilSefisca;
import lombok.Getter;
import lombok.Setter;

@Scope("conversation")
@Controller("AberturaProcessoControl")
public class AberturaProcessoControl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsuarioLogadoControl usuarioLogadoControl;

	@Autowired
	private DataSusService dataSusService;

	@Autowired
	private ProcessoDao processoDao;

	@Getter
	@Setter
	private String cnesCnpj;

	@Getter
	@Setter
	private EnumTipoCodigoInstituicao tipoCodigo = EnumTipoCodigoInstituicao.CNES;

	@Getter
	private Usuario usuarioLogado;

	@Getter
	private InstituicaoDTO instituicaoDTO;

	@Getter
	@Setter
	private Processo processo;

	@Getter
	private Instituicao instituicao;

	@PostConstruct
	public void init() {
		this.processo = new Processo(usuarioLogadoControl.getUsuario());
	}

	public void consultarEstabelecimento() {
		try {
			if (this.cnesCnpj.isEmpty()) {
				UtilFaces.addMensagemFaces("Codigo obrigatorio CNES", FacesMessage.SEVERITY_ERROR);
				return;
			}

			if (this.tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES)) {
				this.instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(this.cnesCnpj, null);
			} else {
				this.instituicaoDTO = dataSusService.consultarEstabelecimentoSaude(null,
						UtilSefisca.limparMascara(this.cnesCnpj));
			}

			if (this.instituicaoDTO != null) {
				this.processo.getInstituicao().setDto(this.instituicaoDTO);
			} else {
				UtilFaces.addMensagemFaces(this.tipoCodigo + " não encontrado.", FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {
			UtilFaces.addMensagemFaces(e);
		}
	}

	public void confirmar() {
		this.processo = processoDao.alterar(this.processo);
		UtilFaces.addMensagemFaces("Processo salvo com sucesso");
	}

	public boolean isCnes() {
		return this.tipoCodigo.equals(EnumTipoCodigoInstituicao.CNES);
	}

	public List<SelectItem> getTiposProcesso() {
		return UtilFaces.getListEnum(EnumTipoProcesso.values());
	}

	public List<SelectItem> getTiposCodigoInstituicao() {
		return UtilFaces.getListEnum(EnumTipoCodigoInstituicao.values());
	}
}
